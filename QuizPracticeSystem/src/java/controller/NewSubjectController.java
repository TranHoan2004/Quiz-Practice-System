package controller;

import dao.AccountDAO;
import dao.BlogDAO;
import dao.BlogMediaDAO;
import dao.ContactDAO;
import dao.CourseDAO;
import dao.SettingDAO;
import dao.TopicDAO;
import dto.NewSubjectDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import model.Course;
import utils.PermissionUtil;

@WebServlet(name = "NewCourseController", urlPatterns = {"/new-subject"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 30,
        maxRequestSize = 1024 * 1024 * 50
)
public class NewSubjectController extends HttpServlet {

    private final CourseDAO courseDAO = new CourseDAO();
    private final TopicDAO topicDAO = new TopicDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final ContactDAO contactDAO = new ContactDAO();
    private final Logger logger;

    public NewSubjectController() {
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    private static final String IMAGE_DIR = "img/subjects/";
    private static final String VIDEO_DIR = "video/subjects";
    private static final Set<String> IMAGE_EXTS = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private static final Set<String> VIDEO_EXTS = Set.of("mp4", "mov", "avi", "mkv");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (PermissionUtil.redirectIfNotRole(request, response, "Admin", request.getContextPath() + "/jsp/unauthorized.jsp")) {
            return;
        }
        try {
            request.setAttribute("topicList", topicDAO.getAllTopic()); // Topic
            request.setAttribute("expertList", accountDAO.getAllExperts());
            request.setAttribute("contactList", contactDAO.getAllContacts());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load data: " + e.getMessage());
        }
        request.getRequestDispatcher("/jsp/course-features/new_subject.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (PermissionUtil.redirectIfNotRole(request, response, "Admin", request.getContextPath() + "/jsp/unauthorized.jsp")) {
            return;
        }

        String name = request.getParameter("name"); // OK

        String topicId = request.getParameter("topic_id"); // OK
        String expertId = request.getParameter("expert_id"); // OK
        String statusStr = request.getParameter("status"); // Sửa từ "status_id" thành "status"
        String featureFlag = request.getParameter("featureFlag"); // Sửa từ "feature_flag" thành "featureFlag"
        String desc = request.getParameter("description");
        String contactId = request.getParameter("contact_id");// OK

        Part thumbnailPart = request.getPart("thumbnailURL");

        String thumbnailPath = null;
        if (isValidPart(thumbnailPart)) {
            String ext = getExtension(thumbnailPart.getSubmittedFileName());
            String folder = getTargetFolder(ext);
            if (folder != null) {
                thumbnailPath = saveFile(thumbnailPart, folder, thumbnailPart.getSubmittedFileName());
            }
        } else {
            String errorMessage = "Invalid file uploaded.";
            response.sendRedirect(request.getContextPath() + "/new-subject?error="
                    + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
            return;
        }

        NewSubjectDTO dto = NewSubjectDTO.builder()
                .title(name)
                .thumbnailURL(thumbnailPath)
                .topicId(topicId)
                .expertId(expertId)
                .contactId(contactId)
                .active("true".equals(statusStr))
                .featured(featureFlag != null)
                .description(desc)
                .build();

        UUID courseId = UUID.randomUUID();

        try {
            Course course = Course.builder()
                    .id(courseId)
                    .title(dto.getTitle())
                    .thumbnailUrl(dto.getThumbnailURL())
                    .topicId(dto.getTopicId())
                    .expertId(dto.getExpertId())
                    .contact(dto.getContactId())
                    .status(dto.isActive())
                    .description(dto.getDescription())
                    .createdDate(LocalDate.now())
                    .updatedDate(LocalDate.now())
                    .numberOfLessons(0) // ban đầu là 0
                    .build();

            courseDAO.insert(course);

            response.sendRedirect(request.getContextPath() + "/subject-list");

        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Lỗi khi tạo course: " + e.getMessage();
            response.sendRedirect(request.getContextPath() + "/new-subject?error="
                    + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
        }
    }

    @Override
    public String getServletInfo() {
        return "New Course Controller - Handles creation of new courses.";
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
}
