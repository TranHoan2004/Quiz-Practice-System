package controller;

import dao.*;
import model.Option;
import model.Question;
import model.QuestionMedia;
import model.Setting;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet(name = "QuestionDetailsController", urlPatterns = {"/question-details"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 30, // 30MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class QuestionDetailsController extends HttpServlet {

    private final Logger logger = Logger.getLogger(QuestionDetailsController.class.getName());
    private final QuestionDAO questionDAO = new QuestionDAO();
    private final LessonDAO lessonDAO = new LessonDAO();
    private final SettingDAO settingDAO = new SettingDAO();
    private final QuestionMediaDAO questionMediaDAO = new QuestionMediaDAO();
    private final OptionDAO optionDAO = new OptionDAO();
    private final SettingQuestionDAO settingQuestionDAO = new SettingQuestionDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();

    private static final String IMAGE_DIR = "img/questions/";
    private static final String VIDEO_DIR = "video/questions/";
    private static final Set<String> IMAGE_EXTS = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private static final Set<String> VIDEO_EXTS = Set.of("mp4", "mov", "avi", "mkv");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String questionId = request.getParameter("id");
        try {
            Question question = questionDAO.getQuestionById(questionId);
            if (question == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Question not found with ID: " + questionId);
                return;
            }

            loadMediaAndOptions(request, questionId);

            request.setAttribute("allSubjects", subjectDAO.getAllSubjects());
            Setting dimensionCurrent = settingQuestionDAO.getAssignedDimensionByQuestionId(questionId);
            if (dimensionCurrent != null) {
                request.setAttribute("dimensionCurent", dimensionCurrent);
            }
            request.setAttribute("question", question);
            request.setAttribute("dimensions", settingDAO.getAllDimensionsByDomainOrGroup());
            request.setAttribute("lessonsList", lessonDAO.getAllLesson());
            request.getRequestDispatcher("/jsp/test-content-features/new_question_details.jsp").forward(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading question details GET", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading question details.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, List<String>> formFields = new HashMap<>();
        List<Part> fileParts = new ArrayList<>();

        try {
            for (Part part : request.getParts()) {
                if (part.getSubmittedFileName() == null || part.getSubmittedFileName().isEmpty()) {
                    try (InputStream inputStream = part.getInputStream()) {
                        formFields.computeIfAbsent(part.getName(), k -> new ArrayList<>())
                                .add(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
                    }
                } else if (part.getSize() > 0) {
                    fileParts.add(part);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error parsing multipart request", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error processing form data.");
            return;
        }

        String questionId = getSingleValue(formFields, "questionId");
        String redirectUrl = request.getContextPath() + "/question-details?id=" + questionId;

        List<QuestionMedia> mediaFilesToSave = null;
        try {
            mediaFilesToSave = saveMediaFilesToDisk(fileParts, formFields, questionId, request);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed saving media files to disk", e);
            response.sendRedirect(redirectUrl + "&message=Upload failed&type=error");
            return;
        }

        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);

            updateQuestionCoreInfo(conn, formFields, questionId);
            updateQuestionDimension(conn, formFields, questionId);
            processOptions(conn, formFields, questionId);
            updateMediaInfo(conn, formFields, questionId); // chỉ update media cũ
            for (QuestionMedia media : mediaFilesToSave) {
                questionMediaDAO.addQuestionMedia(media, conn);
            }

            conn.commit();
            response.sendRedirect(redirectUrl + "&message=Update successfully&type=success");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Database connection or transaction error.", ex);
            response.sendRedirect(redirectUrl + "&message=Database error.&type=error");
        }
    }

    private void updateMediaInfo(Connection conn, Map<String, List<String>> fields, String questionId) throws SQLException {
        for (String paramName : fields.keySet()) {
            if (paramName.startsWith("mediaUrl_")) {
                String mediaId = paramName.substring("mediaUrl_".length());
                QuestionMedia media = questionMediaDAO.getMediaById(conn, mediaId);
                if (media != null) {
                    media.setFilePath(getSingleValue(fields, "mediaUrl_" + mediaId));
                    media.setCaption(getSingleValue(fields, "mediaCaption_" + mediaId));
                    questionMediaDAO.updateQuestionMedia(conn, media);
                }
            }
        }
    }

    // Helper để lấy giá trị đơn từ map
    private String getSingleValue(Map<String, List<String>> map, String key) {
        return map.getOrDefault(key, Collections.emptyList()).stream().findFirst().orElse(null);
    }

    private void updateQuestionCoreInfo(Connection conn, Map<String, List<String>> fields, String questionId) throws Exception {
        Question question = questionDAO.getQuestionById(conn, questionId);
        if (question == null) {
            throw new Exception("Question not found!");
        }

        question.setSubjectId(getSingleValue(fields, "subjectId"));
        question.setLessonId(getSingleValue(fields, "lessonId"));
        question.setStatus(Boolean.parseBoolean(getSingleValue(fields, "status")));
        question.setContent(getSingleValue(fields, "questionContent"));
        question.setExplanation(getSingleValue(fields, "explanation"));

        questionDAO.updateQuestion(conn, question);
    }

    private void updateQuestionDimension(Connection conn, Map<String, List<String>> fields, String questionId) throws Exception {
        String dimensionId = getSingleValue(fields, "dimensionId");
        Setting currentDimension = settingQuestionDAO.getAssignedDimensionByQuestionId(questionId);
        if ((currentDimension == null && dimensionId != null && !dimensionId.isEmpty())
                || (currentDimension != null && !currentDimension.getId().equals(dimensionId))) {

            settingQuestionDAO.removeDimensionsByQuestionId(questionId, conn);

            if (dimensionId != null && !dimensionId.isEmpty()) {
                settingQuestionDAO.addSettingToQuestion(dimensionId, questionId, conn);

            }
        }
    }

    private void processOptions(Connection conn, Map<String, List<String>> fields, String questionId) throws Exception {
        String deletedIdsParam = getSingleValue(fields, "deletedOptionIds");
        if (deletedIdsParam != null && !deletedIdsParam.isBlank()) {
            for (String id : deletedIdsParam.split(",")) {
                if (!id.isBlank()) {
                    optionDAO.deleteOption(conn, id.trim());
                }
            }
        }

        Set<String> processedOptionIds = new HashSet<>();
        for (String paramName : fields.keySet()) {
            if (paramName.startsWith("optionContent_")) {
                String optionId = paramName.substring("optionContent_".length());
                if (processedOptionIds.contains(optionId)) {
                    continue;
                }

                Option option = optionDAO.getOptionById(conn, optionId);
                if (option != null) {
                    option.setContent(getSingleValue(fields, "optionContent_" + optionId));
                    option.setExplanation(getSingleValue(fields, "optionExplanation_" + optionId));
                    option.setTrue(fields.containsKey("optionIsTrue_" + optionId)); // Checkbox được chọn nếu key tồn tại
                    optionDAO.updateOption(conn, option);
                    processedOptionIds.add(optionId);
                }
            } else if (paramName.startsWith("newOptionContent_")) {
                String tempId = paramName.substring("newOptionContent_".length());
                if (processedOptionIds.contains(tempId)) {
                    continue;
                }

                Option newOption = Option.builder()
                        .id(UUID.randomUUID())
                        .questionId(questionId)
                        .content(getSingleValue(fields, "newOptionContent_" + tempId))
                        .explanation(getSingleValue(fields, "newOptionExplanation_" + tempId))
                        .isTrue(fields.containsKey("newOptionIsCorrect_" + tempId))
                        .build();
                optionDAO.addOption(conn, newOption);
                processedOptionIds.add(tempId);
            }
        }
    }

    private void loadMediaAndOptions(HttpServletRequest request, String questionId) throws Exception {
        request.setAttribute("questionMediaList", questionMediaDAO.getMediaByQuestionId(questionId));
        request.setAttribute("options", optionDAO.getOptionsByQuestionId(questionId));
    }

    private String getExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int dot = filename.lastIndexOf('.');
        return (dot >= 0) ? filename.substring(dot + 1).toLowerCase() : "";
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

    private String getTargetFolder(String extension) {
        if (IMAGE_EXTS.contains(extension)) {
            return IMAGE_DIR;
        }
        if (VIDEO_EXTS.contains(extension)) {
            return VIDEO_DIR;
        }
        return null;
    }

    private String getSafeFileName(String originalFileName) {
        String name = Normalizer.normalize(originalFileName, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^a-zA-Z0-9._-]", "_");
        int dotIndex = name.lastIndexOf(".");
        String baseName = (dotIndex != -1) ? name.substring(0, dotIndex) : name;
        String extension = (dotIndex != -1) ? name.substring(dotIndex) : "";
        return UUID.randomUUID() + "_" + baseName + extension;
    }

    private List<QuestionMedia> saveMediaFilesToDisk(
            List<Part> fileParts, Map<String, List<String>> fields,
            String questionId, HttpServletRequest request) throws IOException, Exception {

        List<QuestionMedia> result = new ArrayList<>();
        int displayOrder = questionMediaDAO.getMaxDisplayOrder(questionId) + 1;

        // Đường dẫn gốc của thư mục Web Pages (khi deploy)
        String realWebPath = request.getServletContext().getRealPath("/");

        for (Part part : fileParts) {
            if (!part.getName().startsWith("newMediaFile_")) {
                continue;
            }

            String tempId = part.getName().substring("newMediaFile_".length());
            String originalName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
            String ext = getExtension(originalName);
            String type = getFileType(ext);
            if (type == null) {
                continue;
            }

            // Phân loại thư mục tương đối trong Web Pages
            String targetFolder = getTargetFolder(ext); // ví dụ: img/questions/ hoặc video/questions/
            String uniqueName = getSafeFileName(originalName);
            String relativePath = targetFolder + uniqueName;

            // Đường dẫn tuyệt đối trên máy (tới thư mục real của webapp)
            String absolutePath = realWebPath + targetFolder;
            absolutePath = absolutePath == null ? "" : absolutePath.replace("\\build", "");
            File uploadDir = new File(absolutePath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fullPath = absolutePath + File.separator + uniqueName;
            part.write(fullPath);

            logger.info("Saved file to: " + fullPath);

            QuestionMedia media = QuestionMedia.builder()
                    .id(UUID.randomUUID())
                    .questionId(questionId)
                    .filePath(relativePath.replace("\\", "/")) // ví dụ: img/questions/abc.jpg
                    .mediaType(type)
                    .caption(getSingleValue(fields, "newMediaCaption_" + tempId))
                    .displayOrder(displayOrder++)
                    .build();

            result.add(media);
        }

        return result;
    }

}
