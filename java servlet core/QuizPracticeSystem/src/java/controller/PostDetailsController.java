package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.AccountDAO;
import dao.BlogDAO;
import dao.BlogMediaDAO;
import dto.BlogDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Account;
import model.Blog;
import model.BlogMedia;
import utils.PermissionUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <h4>PostDetailsController - Quản lý chi tiết bài viết</h4>
 *
 * <p>
 * Servlet cho phép hiển thị, thêm, cập nhật, xóa bài viết Blog cùng với các
 * file đa phương tiện đính kèm (hình ảnh, video).</p>
 *
 * <p>
 * Servlet hỗ trợ upload file đa phương tiện, validate đầy đủ dữ liệu và chính
 * sửa nội dung bài viết.</p>
 *
 * @author HuongNI
 */
@WebServlet(name = "PostDetailsController", urlPatterns = {"/marketer/post-details"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 30,
        maxRequestSize = 1024 * 1024 * 50
)
public class PostDetailsController extends HttpServlet {

    private static final String IMAGE_DIR = "img/blogs/";
    private static final String VIDEO_DIR = "video/blogs";
    private static final Set<String> IMAGE_EXTS = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private static final Set<String> VIDEO_EXTS = Set.of("mp4", "mov", "avi", "mkv");

    private final AccountDAO accountDAO;
    private final BlogDAO blogDAO;
    private final BlogMediaDAO blogMediaDAO;
    private final Logger logger;

    public PostDetailsController() {
        this.accountDAO = new AccountDAO();
        this.blogDAO = new BlogDAO();
        this.blogMediaDAO = new BlogMediaDAO();
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    /**
     * <h4>doGet - Hiển thị danh sách bài viết với phân trang, redirect to
     * homepage if not marketer or admin</h4>
     * <p>
     * Xử lý truy vấn GET để hiển thị danh sách blog, đánh dấu theo category,
     * keyword, page,...</p>
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check a role to access this page. If not, redirect to the home page.
        if (PermissionUtil.hasRole(request, "User") || request.getSession().getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/unauthorized.jsp");
            return;
        }

        String keyword = request.getParameter("keyword");
        String category = request.getParameter("category");
        String message = request.getParameter("message");
        String type = request.getParameter("type");
        String page = request.getParameter("page");
        String pageSizeStr = request.getParameter("pageSize");

        try {
            int currentPage = page == null ? 1 : Integer.parseInt(page);
            int pageSize = pageSizeStr == null ? 5 : Integer.parseInt(pageSizeStr);
            renderBlogPagination(request, keyword, category, currentPage, pageSize);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            message = e.getMessage();
        }

        List<BlogDTO> categories = getAllCategories("Blog Category");
        request.setAttribute("categories", categories);
        handleRequest(request, response, message, type);
    }

    /**
     * <h4>doPost - Xử lý các thao tác: thêm, xóa, cập nhật bài viết</h4>
     * <p>
     * Dựa vào tham số action trong form để phân quyến đến method tương ứng.</p>
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var action = request.getParameter("action");

        try {
            if (action == null || action.isBlank()) {
                var message = "Invalid action!";
                var type = "error";
                response.sendRedirect(request.getContextPath() + "/marketer/post-details" + "?message=" + message + "&type=" + type);
                return;
            }
            switch (action) {
                case "add-post" ->
                    handleAddNewPost(request, response);
                case "update-post" ->
                    handleUpdatePost(request, response);
                default ->
                    request.getRequestDispatcher("/jsp/marketing-features/post-details.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * <h4>Xử lý yêu cầu HTTP DELETE để xóa blog theo ID</h4>
     *
     * <p>
     * Phương thức này thực hiện các bước sau:</p>
     * <ol>
     * <li>Đọc JSON từ body request.</li>
     * <li>Trích xuất giá trị `id` (mã hóa).</li>
     * <li>Giải mã ID và tiến hành xóa blog & các media liên quan.</li>
     * <li>Trả về mã HTTP tương ứng với kết quả xử lý.</li>
     * </ol>
     *
     * <p>
     * <b>Trường hợp thành công:</b> trả về mã <code>200 OK</code> kèm thông
     * điệp "Delete successfully!".</p>
     * <p>
     * <b>Trường hợp thất bại:</b></p>
     * <ul>
     * <li>Nếu thiếu `id` → <code>400 Bad Request</code></li>
     * <li>Nếu không có blog tương ứng → <code>404 Not Found</code></li>
     * <li>Nếu có lỗi khi xử lý → <code>400 Bad Request</code> với log chi
     * tiết.</li>
     * </ul>
     *
     * <p>
     * Ghi chú: ID là UUID được encode nên cần decode trước khi xử lý.</p>
     *
     * @param request đối tượng {@link HttpServletRequest} chứa body JSON với
     * key <code>id</code>.
     * @param response đối tượng {@link HttpServletResponse} để ghi kết quả trả
     * về.
     * @throws IOException nếu có lỗi khi đọc request hoặc ghi response.
     * @author HuongNI
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        var mapper = new ObjectMapper();
        var jsonNode = mapper.readTree(request.getReader());

        var id = jsonNode.has("id") ? jsonNode.get("id").asText() : null;

        logger.info(">>>>>>> id: " + id);

        if (id == null || id.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Missing blog ID.");
            return;
        }

        try {
            String decodedId = utils.Encoder.decode(id);
            boolean blogRowAffected = blogDAO.deleteBlogById(decodedId);
            blogMediaDAO.deleteBlogMediaByBlogId(UUID.fromString(decodedId));

            if (blogRowAffected) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("Delete successfully!");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println("No matching record found.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to delete blog with id = " + id, e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Bad request");
        }
    }

    /**
     * <h4>handleAddNewPost - Thêm bài viết mới</h4>
     * <p>
     * Kiểm tra đầy đủ trường nhập, upload file, và thên bài với metadata cần
     * thiết.</p>
     */
    private void handleUpdatePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String message = "";
        String type = "";

        try {
            String title = request.getParameter("title");
            String briefInfo = request.getParameter("briefInfo");
            String content = request.getParameter("content");
            String category = request.getParameter("category");
            String status = request.getParameter("status");
            String featured = request.getParameter("featured");

            if (title == null || briefInfo == null || content == null || category == null) {
                message = "Please fill all fields!";
                type = "error";
                response.sendRedirect(request.getContextPath() + "/marketer/post-details" + "?message=" + message + "&type=" + type);
            }

            boolean featuredBool = featured != null;
            boolean statusBool = status != null;

            HttpSession session = request.getSession();
            Account currentUser = (Account) session.getAttribute("currentUser");

            List<BlogMedia> mediaList = handleUploadFile(request, UUID.fromString(utils.Encoder.decode(id)));
            Blog blog = blogDAO.getBlogById(utils.Encoder.decode(id));

            blog.setTitle(title);
            blog.setBriefInfo(briefInfo);
            blog.setContent(content);
            blog.setCategory(UUID.fromString(utils.Encoder.decode(category)));
            blog.setStatus(statusBool);
            blog.setFlagFeature(featuredBool);
            blog.setUpdatedDate(LocalDate.now());

            boolean isSuccess = blogDAO.updateBlog(blog);

            if (isSuccess) {
                for (BlogMedia media : mediaList) {
                    blogMediaDAO.insertBlogMedia(media);
                }
                message = "Update successfully!";
                type = "success";
            } else {
                message = "Update failed!";
                type = "error";
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in handleUpdatePost", e);
        }
        response.sendRedirect(request.getContextPath() + "/marketer/post-details" + "?message=" + message + "&type=" + type);
    }

    /**
     * <h4>handleUpdatePost - Cập nhật nội dung bài viết</h4>
     * <p>
     * Lấy thông tin cập nhật từ form và update vào DB theo ID.</p>
     */
    private void handleAddNewPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String message = "";
        String type = "";
        try {
            String title = request.getParameter("title");
            String briefInfo = request.getParameter("briefInfo");
            String content = request.getParameter("content");
            String category = request.getParameter("category");
            String status = request.getParameter("status");
            String featured = request.getParameter("featured");

            if (title == null || briefInfo == null || content == null || category == null) {
                message = "Please fill all fields!";
                type = "error";
                response.sendRedirect(request.getContextPath() + "/marketer/post-details" + "?message=" + message + "&type=" + type);
                return;
            }

            boolean featuredBool = featured != null;
            boolean statusBool = status != null;

            HttpSession session = request.getSession();
            Account currentUser = (Account) session.getAttribute("currentUser");

            UUID blogId = UUID.randomUUID();
            List<BlogMedia> mediaList = handleUploadFile(request, blogId);

            Blog newBlog = Blog.builder()
                    .id(blogId)
                    .updatedDate(null)
                    .flagFeature(featuredBool)
                    .views(0)
                    .status(statusBool)
                    .briefInfo(briefInfo)
                    .category(UUID.fromString(utils.Encoder.decode(category)))
                    .accountId(currentUser.getId().toString())
                    .content(content)
                    .title(title)
                    .createdDate(LocalDate.now())
                    .build();

            boolean isSuccess = blogDAO.insertBlog(newBlog);

            if (isSuccess) {
                for (BlogMedia media : mediaList) {
                    blogMediaDAO.insertBlogMedia(media);
                }
                message = "Add successfully!";
                type = "success";
            } else {
                message = "Add failed!";
                type = "error";
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in handleAddNewPost", e);
        }
        response.sendRedirect(request.getContextPath() + "/marketer/post-details" + "?message=" + message + "&type=" + type);
    }

    /**
     * <h4>Xử lý upload file media</h4>
     * <p>
     * Lọc ra danh sách các phần tử media hợp lệ từ form submit, xử lý từng file
     * và tạo danh sách các đối tượng BlogMedia.
     * </p>
     *
     * @param request yêu cầu HTTP chứa các file media
     * @param blogId ID của bài viết cần gắn media
     * @return danh sách BlogMedia đã xử lý
     * @throws ServletException nếu có lỗi Servlet
     * @throws IOException nếu có lỗi IO khi xử lý file
     * @author HuongNI
     */
    private List<BlogMedia> handleUploadFile(HttpServletRequest request, UUID blogId) throws ServletException, IOException {
        List<Part> mediaParts = request.getParts()
                .stream()
                .filter(part -> "media".equals(part.getName()) && isValidPart(part))
                .toList();

        if (mediaParts.isEmpty()) {
            logger.warning("No valid media files found in request");
            return new ArrayList<>();
        }

        Map<String, String> captions = extractCaptions(request);
        List<BlogMedia> blogMediaList = new ArrayList<>();

        for (int i = 0; i < mediaParts.size(); i++) {
            Part mediaPart = mediaParts.get(i);
            try {
                BlogMedia media = processMediaPartAndPost(mediaPart, blogId, i, captions);
                if (media != null) {
                    blogMediaList.add(media);
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error processing media part: " + mediaPart.getSubmittedFileName(), e);
            }
        }

        return blogMediaList;
    }

    /**
     * <h4>Xử lý từng file media</h4>
     * <p>
     * Lưu file, xác định loại file (image/video), caption và vị trí hiển thị để
     * tạo đối tượng BlogMedia.
     * </p>
     *
     * @param mediaPart file media từ form
     * @param blogId ID của bài viết
     * @param displayOrder thứ tự hiển thị của file
     * @param captions map chứa các caption theo thứ tự
     * @return đối tượng BlogMedia nếu thành công, null nếu không hợp lệ
     * @throws IOException nếu xảy ra lỗi IO khi lưu file
     * @author HuongNI
     */
    private BlogMedia processMediaPartAndPost(Part mediaPart, UUID blogId, int displayOrder, Map<String, String> captions) throws IOException {
        String originalName = Paths.get(mediaPart.getSubmittedFileName()).getFileName().toString();
        String extension = getExtension(originalName);
        String mediaType = getFileType(extension);
        String targetFolder = getTargetFolder(extension);

        if (mediaType == null || targetFolder == null) {
            logger.warning("Unsupported file type: " + originalName);
            return null;
        }

        String savedFilePath = saveFile(mediaPart, targetFolder, originalName);
        String caption = captions.getOrDefault("caption_" + displayOrder, "");

        return BlogMedia.builder()
                .id(UUID.randomUUID())
                .blogId(blogId)
                .mediaType(mediaType)
                .file_path(savedFilePath)
                .caption(caption)
                .displayOrder(displayOrder)
                .build();
    }

    /**
     * <h4>Lưu file vào hệ thống</h4>
     * <p>
     * Tạo tên file an toàn và lưu vào thư mục tương ứng. Trả về đường dẫn tương
     * đối đến file.
     * </p>
     *
     * @param part phần dữ liệu file
     * @param folder thư mục lưu trữ (ví dụ: "img/", "video/")
     * @param fileName tên gốc của file
     * @return đường dẫn tương đối tới file đã lưu
     * @throws IOException nếu xảy ra lỗi ghi file
     * @author HuongNI
     */
    private String saveFile(Part part, String folder, String fileName) throws IOException {
        String uniqueFileName = getSafeFileName(fileName);
        String relativePath = folder + uniqueFileName;
        String absolutePath = getServletContext().getRealPath(folder);
        absolutePath = absolutePath == null ? "" : absolutePath.replace("\\build", "");
        logger.info("Saving file: " + relativePath + " to " + absolutePath);

        File dir = new File(absolutePath);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to create directory: " + absolutePath);
        }

        String fullPath = Paths.get(absolutePath, uniqueFileName).toString();

        try {
            part.write(fullPath);
        } catch (IOException e) {
            logger.severe("Error saving file: " + e.getMessage());
        }

        return relativePath;
    }

    /**
     * <h4>Kiểm tra tính hợp lệ của file</h4>
     * <p>
     * Đảm bảo file có tên và dung lượng hợp lệ, và có phần mở rộng nằm trong
     * danh sách được hỗ trợ.
     * </p>
     *
     * @param part phần dữ liệu file
     * @return true nếu hợp lệ, ngược lại false
     * @author HuongNI
     */
    private boolean isValidPart(Part part) {
        if (part == null || part.getSubmittedFileName() == null || part.getSize() <= 0) {
            return false;
        }

        String extension = getExtension(part.getSubmittedFileName());
        return IMAGE_EXTS.contains(extension) || VIDEO_EXTS.contains(extension);
    }

    /**
     * <h4>Xác định loại file</h4>
     * <p>
     * Trả về "image" hoặc "video" dựa trên phần mở rộng của file.
     * </p>
     *
     * @param extension phần mở rộng (ví dụ: "jpg", "mp4")
     * @return loại file tương ứng hoặc null nếu không hợp lệ
     * @author HuongNI
     */
    private String getFileType(String extension) {
        if (IMAGE_EXTS.contains(extension)) {
            return "image";
        }
        if (VIDEO_EXTS.contains(extension)) {
            return "video";
        }
        return null;
    }

    /**
     * <h4>Trích xuất caption từ request</h4>
     * <p>
     * Tìm các tham số có dạng "caption_X" và lưu vào map.
     * </p>
     *
     * @param request yêu cầu HTTP chứa dữ liệu form
     * @return map các caption theo thứ tự hiển thị
     * @author HuongNI
     */
    private Map<String, String> extractCaptions(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> {
            if (key.startsWith("caption_") && value.length > 0) {
                map.put(key, value[0]);
            }
        });
        return map;
    }

    /**
     * <h4>Lấy phần mở rộng của file</h4>
     * <p>
     * Dựa trên dấu chấm cuối trong tên file.
     * </p>
     *
     * @param filename tên file
     * @return phần mở rộng (ví dụ: "png"), hoặc chuỗi rỗng nếu không có
     * @author HuongNI
     */
    private String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return (dot >= 0) ? filename.substring(dot + 1).toLowerCase() : "";
    }

    /**
     * <h4>Tạo tên file an toàn và không trùng lặp</h4>
     * <p>
     * timestamp và UUID để tránh trùng tên.
     * </p>
     *
     * @param originalFileName tên file ban đầu
     * @return tên file đã xử lý an toàn
     * @author HuongNI
     */
    public static String getSafeFileName(String originalFileName) {
        int dotIndex = originalFileName.lastIndexOf(".");
        String extension = (dotIndex != -1) ? originalFileName.substring(dotIndex) : "";

        return System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;
    }

    /**
     * <h4>Xác định thư mục lưu trữ theo phần mở rộng</h4>
     * <p>
     * Trả về đường dẫn thư mục tương ứng ("img/" hoặc "video/") dựa trên phần
     * mở rộng của file.
     * </p>
     *
     * @param extension phần mở rộng của file (vd: "jpg", "mp4")
     * @return đường dẫn thư mục hoặc null nếu không xác định được
     * @author TuanKD
     */
    private String getTargetFolder(String extension) {
        if (IMAGE_EXTS.contains(extension)) {
            return IMAGE_DIR;
        }
        if (VIDEO_EXTS.contains(extension)) {
            return VIDEO_DIR;
        }
        return null;
    }

    /**
     * <h4>Xử lý và forward thông báo tới trang JSP</h4>
     * <p>
     * Thiết lập các thuộc tính thông báo và chuyển hướng đến trang
     * post-details.
     * </p>
     *
     * @param request yêu cầu từ client
     * @param response phản hồi về client
     * @param message nội dung thông báo
     * @param type loại thông báo (vd: "success", "error")
     * @throws ServletException nếu xảy ra lỗi servlet
     * @throws IOException nếu xảy ra lỗi IO khi forward
     * @author HuongNI
     */
    private void handleRequest(HttpServletRequest request, HttpServletResponse response, String message, String type) throws ServletException, IOException {
        request.setAttribute("type", type);
        request.setAttribute("message", message);
        request.getRequestDispatcher("/jsp/marketing-features/post-details.jsp").forward(request, response);
    }

    /**
     * <h4>Lấy danh sách danh mục bài viết</h4>
     * <p>
     * Gọi DAO để lấy tất cả danh mục thuộc nhóm "Blog Category".
     * </p>
     *
     * @param category tên nhóm danh mục
     * @return danh sách BlogDTO chứa thông tin danh mục
     * @author HuongNI
     */
    private List<BlogDTO> getAllCategories(String category) {
        try {
            return blogDAO.getCategories(category);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * <h4>Chuyển danh sách Blog sang BlogDTO</h4>
     * <p>
     * Gắn thêm thông tin người dùng, danh mục, media, và encode các ID cho từng
     * blog.
     * </p>
     *
     * @param blogs danh sách blog
     * @return danh sách BlogDTO đã chuẩn hóa và có đầy đủ thông tin hiển thị
     * @author HuongNI
     */
    private List<BlogDTO> getBlogDTO(List<Blog> blogs) {
        List<BlogDTO> list = new ArrayList<>();
        try {
            for (Blog blog : blogs) {
                Account acc = accountDAO.getAccountById(blog.getAccountId());
                if (acc == null) {
                    continue;
                }

                String categoryName = blogDAO.getCategoryNameById(blog.getCategory());

                List<BlogMedia> blogMediaList = blogMediaDAO.getBlogMediaByBlogId(blog.getId());

                ObjectMapper mapper = new ObjectMapper();
                String blogMediaJson = mapper.writeValueAsString(blogMediaList);

                BlogDTO blogDTO = BlogDTO.builder()
                        .id(utils.Encoder.encode(blog.getId().toString()))
                        .accountId(acc.getId().toString())
                        .avatarUrl(acc.getImageUrl())
                        .accountName(acc.getFullName())
                        .briefInfo(blog.getBriefInfo())
                        .title(blog.getTitle())
                        .content(blog.getContent()
                                .replace("&", "&amp;")
                                .replace("\"", "&quot;")
                                .replace("<", "&lt;")
                                .replace(">", "&gt;")
                                .replace("'", "&#39;"))
                        .category(categoryName)
                        .categoryId(utils.Encoder.encode(blog.getCategory().toString()))
                        .createdDate(blog.getCreatedDate())
                        .views(blog.getViews())
                        .status(blog.isStatus())
                        .flagFeature(blog.isFlagFeature())
                        .blogMediaJson(blogMediaJson)
                        .blogMediaList(blogMediaList)
                        .build();

                list.add(blogDTO);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return list;
    }

    /**
     * <h4>Xử lý phân trang blog và đổ dữ liệu cho view</h4>
     * <p>
     * Lấy danh sách blog theo từ khóa, danh mục và số trang. Tính toán tổng số
     * trang, sau đó thiết lập dữ liệu hiển thị.
     * </p>
     *
     * @param request yêu cầu từ client
     * @param keyword từ khóa tìm kiếm (có thể null)
     * @param categoryId ID danh mục (có thể null, sẽ được decode)
     * @param page trang hiện tại
     * @param pageSize số blog mỗi trang
     * @author HuongNI
     */
    private void renderBlogPagination(HttpServletRequest request, String keyword, String categoryId, int page, int pageSize) {
        // Decode and normalize categoryId
        if (categoryId != null) {
            categoryId = utils.Encoder.decode(categoryId).trim();
            if (categoryId.isEmpty()) {
                categoryId = null;
            }
        }

        // Fetch blogs based on filters
        List<Blog> blogs;
        if (keyword == null && categoryId == null) {
            blogs = blogDAO.getBlogs(null, null, page, pageSize, null);
        } else {
            blogs = blogDAO.getBlogs(keyword, categoryId, page, pageSize, null);
        }

        // Convert blogs to DTOs
        List<BlogDTO> blogDTOs = getBlogDTO(blogs);

        // Pagination info
        int totalBlogs = blogDAO.getTotalBlogs(keyword, categoryId, null);
        int totalPages = (int) Math.ceil((double) totalBlogs / pageSize);

        // Set attributes for the view
        request.setAttribute("blogs", blogDTOs);
        request.setAttribute("currentIndex", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalElements", totalBlogs);
    }
}
