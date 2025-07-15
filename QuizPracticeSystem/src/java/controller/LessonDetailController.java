package controller;

import dao.*;
import dto.LessonDetailDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;
import utils.Encoder;

@WebServlet(name = "LessonDetailController", urlPatterns = {"/user/subject_lesson/lesson_detail"})
public class LessonDetailController extends HttpServlet {

    TopicDAO topicDAO = new TopicDAO();
    LessonDAO lessonDAO = new LessonDAO();
    LearningMaterialDAO materialDAO = new LearningMaterialDAO();
    SettingDAO settingDAO = new SettingDAO();
    CourseDAO courseDAO = new CourseDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String encodedLessonId = request.getParameter("lessonId");
        String lessonId = null;
        LessonDetailDTO dto = new LessonDetailDTO();

        if (encodedLessonId != null && !encodedLessonId.isEmpty()) {
            lessonId = Encoder.decode(encodedLessonId);
        }

        String courseId = request.getParameter("courseId");

        try {
            if (lessonId != null && !lessonId.isEmpty()) {
                // === EDIT MODE ===
                Lesson lesson = lessonDAO.getById(lessonId);
                Course course = courseDAO.getById(lesson.getCourseId());
                String topicId = course.getTopicId();
                Topic topic = topicDAO.getTopicById(topicId);
                String lessonTypeName = settingDAO.getSettingNameById(lesson.getLessonTypeId());
                LearningMaterial material = materialDAO.getByLessonId(lessonId);

                dto = LessonDetailDTO.builder()
                        .lessonId(lessonId)
                        .name(lesson.getName())
                        .type(lessonTypeName)
                        .topicId(topic.getId().toString())
                        .videoLink(material != null ? material.getVideoContentUrl() : null)
                        .htmlContent(material != null ? material.getHtmlContent() : null)
                        .order(lessonDAO.getOrderOfLesson(lessonId))
                        .active(lesson.isStatus())
                        .build();

                courseId = lesson.getCourseId();
                request.setAttribute("mode", "edit");

            } else {
                // === ADD MODE ===
                int nextOrder = lessonDAO.countLessonsByCourse(courseId) + 1;
                dto.setOrder(nextOrder);
                dto.setActive(true);

                Course course = courseDAO.getById(courseId);
                dto.setTopicId(course.getTopicId());

                request.setAttribute("mode", "add");
            }

            request.setAttribute("lesson", dto);
            request.setAttribute("topics", topicDAO.getAllTopic());
            request.setAttribute("courseId", courseId);

        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("message", "Lỗi khi tải dữ liệu: " + ex.getMessage());
        }

        request.getRequestDispatcher("/jsp/course-features/lesson_detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String lessonId = request.getParameter("lessonId");
        String courseId = request.getParameter("courseId");
        String name = request.getParameter("title");
        String lessonTypeName = request.getParameter("type");
        String topicId = request.getParameter("topicId");
        String videoLink = request.getParameter("videoLink");
        String htmlContent = request.getParameter("htmlContent");

        LessonDAO lessonDAO = new LessonDAO();
        CourseDAO courseDAO = new CourseDAO();
        SettingDAO settingDAO = new SettingDAO();
        LearningMaterialDAO materialDAO = new LearningMaterialDAO();

        try {
            String lessonTypeId = settingDAO.getSettingIdByName(lessonTypeName, "Lesson Type");

            if (lessonId == null || lessonId.isEmpty()) {
                // === ADD MODE ===
                UUID newId = UUID.randomUUID();
                Lesson lesson = new Lesson();
                lesson.setId(newId);
                lesson.setName(name);
                lesson.setCourseId(courseId);
                lesson.setLessonTypeId(lessonTypeId);
                lesson.setStatus(true);

                lessonDAO.insertLesson(lesson);

                LearningMaterial material = new LearningMaterial();
                material.setId(newId);
                material.setTitle(name);
                material.setUpdatedDate(LocalDate.now());
                material.setDuration(0);
                material.setVideoContentUrl(videoLink);
                material.setHtmlContent(htmlContent);
                materialDAO.insert(material);

                courseDAO.updateTopic(courseId, topicId);

            } else {
                // === EDIT MODE ===
                Lesson lesson = lessonDAO.getById(lessonId);
                lesson.setName(name);
                lesson.setLessonTypeId(lessonTypeId);
                lesson.setStatus(true);
                lessonDAO.updateLesson(lesson);

                courseDAO.updateTopic(courseId, topicId);

                LearningMaterial material = materialDAO.getByLessonId(lessonId);
                if (material != null) {
                    material.setTitle(name);
                    material.setVideoContentUrl(videoLink);
                    material.setHtmlContent(htmlContent);
                    material.setUpdatedDate(LocalDate.now());
                    materialDAO.update(material);
                }
            }

            response.sendRedirect(request.getContextPath() + "/user/subject_lesson?id=" + courseId);

        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Đã xảy ra lỗi khi xử lý bài học. Vui lòng thử lại.";

            if (lessonId == null || lessonId.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/user/subject_lesson/lesson_detail?courseId="
                        + courseId + "&error=" + java.net.URLEncoder.encode(errorMessage, "UTF-8"));
            } else {
                String encodedLessonId = Encoder.encode(lessonId);
                response.sendRedirect(request.getContextPath() + "/user/subject_lesson/lesson_detail?lessonId="
                        + encodedLessonId + "&error=" + java.net.URLEncoder.encode(errorMessage, "UTF-8"));
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Lesson Detail Add/Edit Controller";
    }
}
