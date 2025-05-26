package controller;

import dao.*;
import dto.ContactInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dto.RegistrationCourse;

import java.io.IOException;

import model.*;
import utils.Encoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "MyRegistrationController", urlPatterns = {"/user/registration"})
public class MyRegistrationController extends HttpServlet {

    private final PricePackageDAO ppDAO;
    private final PersonalCourseDAO pcDAO;
    private final ContactDAO ccDAO;
    private final SubjectDAO sDAO;
    private final CourseDAO cDAO;
    private final TopicDAO tDAO;
    private final Logger logger;

    public MyRegistrationController() {
        this.logger = Logger.getLogger(this.getClass().getName());
        this.pcDAO = new PersonalCourseDAO();
        this.ppDAO = new PricePackageDAO();
        this.ccDAO = new ContactDAO();
        this.sDAO = new SubjectDAO();
        this.cDAO = new CourseDAO();
        this.tDAO = new TopicDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String filter = request.getParameter("filter");
        String contact = request.getParameter("org");
        String message = "";
        try {
            List<RegistrationCourse> registrationCourses = getRegistrationCourses(request);
            if (keyword == null && filter == null) {
                renderCoursePagination(request, registrationCourses);
            } else {
                handleSearchAndFilter(request, keyword, filter, registrationCourses);
            }
            handleOrgFilter(request, contact, registrationCourses);
            convertContactInformation(request, ccDAO.getAllContacts());
            request.setAttribute("subjects", sDAO.getAllSubjects());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            message = e.getMessage();
        }
        handleRequest(request, response, message);
    }

    // Chuyển hướng sang trang view my_registration.jsp
    private void handleRequest(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        request.setAttribute("message", message);
        request.getRequestDispatcher("/jsp/customer-features/my_registration.jsp").forward(request, response);
    }

    // Lấy ra các khóa học của người dùng đăng nhập đã đăng ký
    private List<PersonalCourse> getPersonalCoursesByRecentUser(HttpServletRequest request) throws Exception {
        Account account = (Account) request.getSession().getAttribute("account");
        String id = account != null ? account.getId().toString() : "b283bfb8-397a-11f0-84a1-088fc33f56c7";
        return pcDAO.getAllByAccount(id);
//        return pcDAO.getAll();
    }

    // Truy vấn database để lấy ra dữ liệu cần thiết rồi đóng gói vào đối tượng RegistrationCourse
    private List<RegistrationCourse> getRegistrationCourses(HttpServletRequest request) throws Exception {
        List<RegistrationCourse> registrationCourses = new ArrayList<>();
        for (PersonalCourse course : getPersonalCoursesByRecentUser(request)) {
            PricePackage pp = ppDAO.getByCourse(course.getCourseId());
            Course c = cDAO.getById(course.getCourseId());
            Topic t = tDAO.getTopicById(c.getTopicId());
            Subject s = sDAO.getById(t.getSubjectId());
            registrationCourses.add(RegistrationCourse.builder()
                    .courseId(Encoder.encode(course.getCourseId()))
                    .subject(s.getName())
                    .registrationTime(course.getEnrollDate())
                    .packageName(pp.getTitle())
                    .totalCost(pp.getPrice())
                    .status(course.getStatus() == null ? null : course.getStatus().name().toLowerCase())
                    .validFrom(course.getEnrollDate())
                    .validTo(course.getEnrollDate().plusDays(pp.getAccessDuration()))
                    .build());
        }
        return registrationCourses;
    }

    // Xử lý phần tìm kiếm và bộ lọc
    private void handleSearchAndFilter(HttpServletRequest request, String keyword, String filter, List<RegistrationCourse> registrationCourses) {
        List<RegistrationCourse> results = new ArrayList<>();
        if (keyword != null) {
            logger.info("Searching for " + keyword);
            for (RegistrationCourse registrationCourse : registrationCourses) {
                if ((registrationCourse.getStatus() != null && registrationCourse.getStatus().toLowerCase().contains(keyword.toLowerCase()))
                        || (registrationCourse.getPackageName() != null && registrationCourse.getPackageName().toLowerCase().contains(keyword.toLowerCase()))
                        || registrationCourse.getSubject().toLowerCase().contains(keyword.toLowerCase())) {
                    System.out.println(registrationCourse.getSubject().toLowerCase() + " " + keyword.toLowerCase());
                    results.add(registrationCourse);
                }
            }
        }
        if (filter != null) {
            logger.info("Filter for " + filter);
            for (RegistrationCourse registrationCourse : registrationCourses) {
                if (registrationCourse.getSubject().toLowerCase().contains(filter.toLowerCase())) {
                    results.add(registrationCourse);
                }
            }
        }
        renderCoursePagination(request, results);
    }

    private void handleOrgFilter(HttpServletRequest request, String keyword, List<RegistrationCourse> course) throws Exception {
        if (keyword != null) {
            keyword = Encoder.decode(keyword);
            logger.info("Filter for " + keyword);
            Course c = cDAO.getById(keyword);
            Contact contact = ccDAO.getById(c.getContact());
            request.setAttribute("contact", contact);
            renderCoursePagination(request, course);
        }
    }

    private void convertContactInformation(HttpServletRequest request, List<Contact> contacts) {
        List<ContactInfo> contactInfos = new ArrayList<>();
        for (Contact contact : contacts) {
            contactInfos.add(ContactInfo.builder()
                    .id(Encoder.encode(contact.getId().toString()))
                    .name(contact.getName())
                    .link(Map.copyOf(contact.getLink()))
                    .email(contact.getEmail())
                    .phone(contact.getPhone())
                    .address(contact.getAddress())
                    .build());
        }
        request.setAttribute("contacts", contactInfos);
    }

    // Phân trang
    private void renderCoursePagination(HttpServletRequest request, List<RegistrationCourse> course) {
        String page = request.getParameter("page");
        int currentPage = (page == null ? 1 : Integer.parseInt(page));

        int startIndex = (currentPage - 1) * 10;
        int endIndex = Math.min(course.size(), startIndex + 10);

        request.setAttribute("courses", course.subList(startIndex, endIndex)); // Dữ liệu chính
        request.setAttribute("currentIndex", currentPage); // Trang hiện tại
        request.setAttribute("totalPages", course.isEmpty() ? 0 : Math.ceil(course.size() / (double) 10)); // Tính tổng số trang
        request.setAttribute("totalElements", course.size()); // Tính tổng số bản ghi được ném ra view
    }
}
