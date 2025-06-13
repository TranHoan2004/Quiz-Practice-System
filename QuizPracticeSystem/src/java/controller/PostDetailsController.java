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
import utils.Encoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet(name = "PostDetailsController", urlPatterns = {"/post-details"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 30,
        maxRequestSize = 1024 * 1024 * 50
)

public class PostDetailsController extends HttpServlet {

    private static final String IMAGE_DIR = "img/";
    private static final String VIDEO_DIR = "video/";
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

//        HttpSession session = request.getSession();
//        Account currentUser = (Account) session.getAttribute("currentUser");
//        if (currentUser == null || !currentUser.getRoleId().equals(accountDAO.getRoleIdByRoleName("Admin"))) {
//            response.sendRedirect(request.getContextPath() + "/blog-list");
//        }
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

        List<BlogDTO> categories = getAllCategories();
        request.setAttribute("categories", categories);
        handleRequest(request, response, message, type);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null || action.isBlank()) {
                String message = "Invalid action!";
                String type = "error";
                response.sendRedirect(request.getContextPath() + "/post-details" + "?message=" + message + "&type=" + type);
                return;
            }
            switch (action) {
                case "add-post" ->
                    handleAddNewPost(request, response);
                case "delete-post" ->
                    handleDeletePost(request, response);
                case "update-post" ->
                    handleUpdatePost(request, response);
                default ->
                    request.getRequestDispatcher("/jsp/marketing-features/post-details.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

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
                response.sendRedirect(request.getContextPath() + "/post-details" + "?message=" + message + "&type=" + type);
                return;
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
        response.sendRedirect(request.getContextPath() + "/post-details" + "?message=" + message + "&type=" + type);
    }

    private void handleAddNewPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                response.sendRedirect(request.getContextPath() + "/post-details" + "?message=" + message + "&type=" + type);
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
                    .accountId("0246ca96-45cb-11f0-8270-e1d174d2be43")
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
        response.sendRedirect(request.getContextPath() + "/post-details" + "?message=" + message + "&type=" + type);
    }

    private void handleDeletePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String message = "";
        String type = "";

        boolean blogRowAffected = blogDAO.deleteBlogById(utils.Encoder.decode(id));
        boolean mediaRowAffected = blogMediaDAO.deleteBlogMediaByBlogId(UUID.fromString(utils.Encoder.decode(id)));
        if (!blogRowAffected && !mediaRowAffected) {
            message = "Delete fall";
            type = "error";
        } else {
            message = "Delete successfully!";
            type = "success";
        }
        request.setAttribute("message", message);
        request.setAttribute("type", type);
        response.sendRedirect(request.getContextPath() + "/post-details" + "?message=" + message + "&type=" + type);
    }

    private List<BlogMedia> handleUploadFile(HttpServletRequest request, UUID blogId) throws ServletException, IOException {
        List<Part> mediaParts = request.getParts()
                .stream()
                .filter(part -> "media".equals(part.getName()) && isValidPart(part))
                .collect(Collectors.toList());

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
                .mediaUrl(savedFilePath)
                .caption(caption)
                .displayOrder(displayOrder)
                .build();
    }

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

    private boolean isValidPart(Part part) {
        if (part == null || part.getSubmittedFileName() == null || part.getSize() <= 0) {
            return false;
        }

        String extension = getExtension(part.getSubmittedFileName());
        return IMAGE_EXTS.contains(extension) || VIDEO_EXTS.contains(extension);
    }

    private String getFileType(String extension) {
        if (IMAGE_EXTS.contains(extension)) {
            return "image";
        }
        if (VIDEO_EXTS.contains(extension)) {
            return "video";
        }
        return null;
    }

    private Map<String, String> extractCaptions(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> {
            if (key.startsWith("caption_") && value.length > 0) {
                map.put(key, value[0]);
            }
        });
        return map;
    }

    private String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return (dot >= 0) ? filename.substring(dot + 1).toLowerCase() : "";
    }

    public static String getSafeFileName(String originalFileName) {
        String fileName = originalFileName == null ? "unknown" : originalFileName;
        String name = Normalizer.normalize(originalFileName, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("đ", "d")
                .replaceAll("Đ", "D")
                .toLowerCase();

        name = name.replaceAll("[^a-zA-Z0-9._-]", "_");

        int dotIndex = name.lastIndexOf(".");
        String baseName = (dotIndex != -1) ? name.substring(0, dotIndex) : name;
        String extension = (dotIndex != -1) ? name.substring(dotIndex) : "";

        return UUID.randomUUID().toString() + "_" + baseName + extension;
    }

    private String getTargetFolder(String extension) {
        if (IMAGE_EXTS.contains(extension)) {
            return IMAGE_DIR;
        }
        if (VIDEO_EXTS.contains(extension)) {
            return VIDEO_DIR;
        }
        return null;
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response, String message, String type) throws ServletException, IOException {
        request.setAttribute("type", type);
        request.setAttribute("message", message);
        request.getRequestDispatcher("/jsp/marketing-features/post-details.jsp").forward(request, response);
    }

    private List<BlogDTO> getAllCategories() {
        try {
            return blogDAO.getCategories();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

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
                        .content(blog.getContent())
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

    // Pagination
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
            blogs = blogDAO.getBlogs(page, pageSize, null);
        } else {
            blogs = blogDAO.getBlogs(keyword, categoryId, page, pageSize, null);
        }

        // Convert blogs to DTOs
        List<BlogDTO> blogDTOs = getBlogDTO(blogs);

        // Pagination info
        int totalBlogs = blogDAO.getTotalBlogs(keyword, categoryId);
        int totalPages = (int) Math.ceil((double) totalBlogs / pageSize);

        // Set attributes for the view
        request.setAttribute("blogs", blogDTOs);
        request.setAttribute("currentIndex", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalElements", totalBlogs);
    }

}
