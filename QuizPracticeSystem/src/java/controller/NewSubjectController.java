package controller;

import dao.AccountDAO;
import dao.ContactDAO;
import dao.CourseDAO;
import dao.SettingDAO;
import dao.TopicDAO;
import dto.NewSubjectDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.UUID;
import model.Course;

@WebServlet(name = "NewCourseController", urlPatterns = {"/user/new_subject"})
public class NewSubjectController extends HttpServlet {

    private final CourseDAO courseDAO = new CourseDAO();
    private final TopicDAO topicDAO = new TopicDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final ContactDAO contactDAO = new ContactDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

        String name = request.getParameter("name"); // OK
        String thumbnail = request.getParameter("thumbnailURL"); // Sửa từ "thumbnail_url" thành "thumbnailURL"
        String topicId = request.getParameter("topic_id"); // OK
        String expertId = request.getParameter("expert_id"); // OK
        String statusStr = request.getParameter("status"); // Sửa từ "status_id" thành "status"
        String featureFlag = request.getParameter("featureFlag"); // Sửa từ "feature_flag" thành "featureFlag"
        String desc = request.getParameter("description");
        String contactId = request.getParameter("contact_id");// OK

        NewSubjectDTO dto = NewSubjectDTO.builder()
                .title(name)
                .thumbnailURL(thumbnail)
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

            response.sendRedirect(request.getContextPath() + "/user/subject_list");

        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Lỗi khi tạo course: " + e.getMessage();
            response.sendRedirect(request.getContextPath() + "/user/new_subject?error="
                    + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
        }
    }

    @Override
    public String getServletInfo() {
        return "New Course Controller - Handles creation of new courses.";
    }
}
