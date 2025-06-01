/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.*;
import dto.ContactInfo;
import dto.CourseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import utils.Encoder;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "SubjectsListController", urlPatterns = {"/user/subjectslistcontroller"})
public class SubjectsListController extends HttpServlet {

    // Khai báo các DAO để thao tác dữ liệu
    private final SubjectDAO subjectDAO;
    private final CourseDAO courseDAO;
    private final LessonDAO lessonDAO;
    private final AccountDAO accountDAO;
    private final ContactDAO contactDAO;
    private final TopicDAO topicDAO;
    private final Logger logger;

    // Khởi tạo các DAO
    public SubjectsListController() {
        this.subjectDAO = new SubjectDAO();
        this.courseDAO = new CourseDAO();
        this.lessonDAO = new LessonDAO();
        this.accountDAO = new AccountDAO();
        this.contactDAO = new ContactDAO();
        this.topicDAO = new TopicDAO();
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    // Xử lý yêu cầu GET từ client (trình duyệt)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy các tham số tìm kiếm từ URL
        String keyword = request.getParameter("keyword");
        String category = request.getParameter("category");
        String status = request.getParameter("status");
        String orgEncoded = request.getParameter("org");
        String message = "";

        try {
            // Lấy tất cả CourseDTO (danh sách môn học đầy đủ thông tin)
            List<CourseDTO> allCourses = getAllCourseDTOs();

            // Nếu có điều kiện lọc → thực hiện lọc
            if (keyword != null || category != null || status != null) {
                allCourses = filterCourses(allCourses, keyword, category, status);
            }

            // Nếu có tham số mã hóa tổ chức → giải mã và lấy thông tin
            if (orgEncoded != null) {
                handleContactFilter(request, orgEncoded);
            }

            // Lấy danh sách subject (chưa dùng rõ ràng trong JSP)
            request.setAttribute("subjects", subjectDAO.getAllSubjects());

            // Chia trang và truyền danh sách đã phân trang sang JSP
            renderPagination(request, allCourses);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            message = e.getMessage(); // Gán lỗi để hiển thị nếu có
        }

        // Truyền message nếu có lỗi và forward sang JSP hiển thị
        request.setAttribute("message", message);
        request.getRequestDispatcher("/jsp/course-features/subjects_list.jsp").forward(request, response);
    }

    // Hàm lấy tất cả CourseDTO từ nhiều bảng liên kết
    private List<CourseDTO> getAllCourseDTOs() throws Exception {
        List<CourseDTO> result = new ArrayList<>();
        List<Course> courses = courseDAO.getAllCourses();

        for (Course c : courses) {
            // Lấy Topic → từ Topic lấy Subject
            Topic t = topicDAO.getTopicById(c.getTopicId());
            Subject s = subjectDAO.getById(t.getSubjectId());

            // Đếm số lượng bài học thuộc Subject
            int lessonCount = lessonDAO.countBySubjectId(s.getId().toString());

            // Lấy thông tin chuyên gia (owner)
            Account owner = accountDAO.getAccountById(c.getExpertId());
            
            String category = subjectDAO.getCategoryBySubjectId(s.getId().toString());
            // Xây dựng đối tượng CourseDTO
            CourseDTO dto = CourseDTO.builder()
                    .id(c.getId().hashCode())            // dùng hashCode làm ID tạm
                    .name(s.getName())                   // tên môn học
                    .category(category)               // tạm dùng tên subject làm category
                    .numberOfLessons(lessonCount)
                    .owner(owner.getFullName())
                    .published(c.isStatus())             // trạng thái đã xuất bản hay chưa
                    .build();

            result.add(dto);  
        }

        return result;
    }

    // Hàm lọc danh sách CourseDTO theo keyword, category, status
    private List<CourseDTO> filterCourses(List<CourseDTO> courses, String keyword, String category, String status) {
        List<CourseDTO> filtered = new ArrayList<>();

        for (CourseDTO course : courses) {
            boolean match = true;

            // Lọc theo keyword nếu có
            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = keyword.trim().toLowerCase();
                match &= course.getName().toLowerCase().contains(kw)
                        || course.getOwner().toLowerCase().contains(kw)
                        || course.getCategory().toLowerCase().contains(kw);
            }

            // Lọc theo category nếu có
            if (category != null && !category.equalsIgnoreCase("all")) {
                match &= course.getCategory().equalsIgnoreCase(category);
            }

            // Lọc theo status nếu có
            if (status != null && !status.equalsIgnoreCase("all")) {
                boolean expectedStatus = status.equalsIgnoreCase("published");
                match &= course.isPublished() == expectedStatus;
            }

            if (match) {
                filtered.add(course);
            }
        }

        return filtered;
    }

    // Giải mã mã tổ chức và gán thông tin contact vào request
    private void handleContactFilter(HttpServletRequest request, String orgEncoded) throws Exception {
        String decodedId = Encoder.decode(orgEncoded);
        Contact contact = contactDAO.getById(decodedId);
        request.setAttribute("contact", contact);
    }

    // Hàm chia trang danh sách CourseDTO
    private void renderPagination(HttpServletRequest request, List<CourseDTO> list) {
        int pageSize = 10; // mỗi trang 10 phần tử
        int currentPage = 1;
        String pageParam = request.getParameter("page");

        if (pageParam != null && pageParam.matches("\\d+")) {
            currentPage = Integer.parseInt(pageParam);
        }

        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(list.size(), startIndex + pageSize);

        List<CourseDTO> paginated = list.subList(startIndex, endIndex);

        // Gán các biến phân trang sang JSP
        request.setAttribute("courses", paginated);
        request.setAttribute("currentIndex", currentPage);
        request.setAttribute("totalPages", list.isEmpty() ? 0 : (int) Math.ceil((double) list.size() / pageSize));
        request.setAttribute("totalElements", list.size());
    }
}
