package controller;

import com.google.gson.Gson;
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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "MyRegistrationController", urlPatterns = {"/user/registration"})
public class MyRegistrationController extends HttpServlet {

    private final PersonalSubjectDAO psDAO;
    private final ContactDAO ccDAO;
    private final SubjectDAO sDAO;
    private final Logger logger;

    public MyRegistrationController() {
        this.logger = Logger.getLogger(this.getClass().getName());
        this.psDAO = new PersonalSubjectDAO();
        this.ccDAO = new ContactDAO();
        this.sDAO = new SubjectDAO();
    }

    /**
     * <h4>Xử lý HTTP GET để hiển thị danh sách đăng ký khóa học của người dùng</h4>
     *
     * <p>Chức năng chính:</p>
     * <ul>
     *   <li>Nếu có header <code>X-Source</code>, trả về tiêu đề trang dưới dạng JSON để sử dụng phía client.</li>
     *   <li>Nếu không, thực hiện các thao tác sau:
     *     <ul>
     *       <li>Lấy danh sách các khóa học mà người dùng đã đăng ký từ cơ sở dữ liệu.</li>
     *       <li>Nếu có tham số <code>keyword</code> hoặc <code>filter</code>, áp dụng tìm kiếm và lọc dữ liệu.</li>
     *       <li>Nếu có tham số <code>org</code>, áp dụng lọc theo tổ chức đào tạo.</li>
     *       <li>Thêm thông tin liên hệ của các tổ chức đào tạo vào request.</li>
     *       <li>Gán danh sách môn học vào request để hiển thị trong phần lọc.</li>
     *       <li>Chuyển tiếp (forward) tới view <code>my_registration.jsp</code>.</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * @param request  yêu cầu HTTP từ client, có thể chứa các tham số: <code>keyword</code>, <code>filter</code>, <code>org</code>
     * @param response phản hồi HTTP trả về cho client hoặc dùng để forward tới view
     * @throws ServletException nếu xảy ra lỗi trong quá trình forward tới JSP
     * @throws IOException      nếu xảy ra lỗi I/O khi xử lý request hoặc response
     * @author HoanTX
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getHeader("X-Source") != null) {
            logger.log(Level.INFO, "header: {0}", request.getHeader("X-Source"));
            Map<String, String> mapper = new HashMap<>();
            mapper.put("main_title", "My Registration");
            mapper.put("items", "My Registration");
            sendData(response, mapper);
        } else {
            String keyword = request.getParameter("keyword");
            String filter = request.getParameter("filter");
            String contact = request.getParameter("org");
            String message = "";
            try {
                List<RegistrationCourse> registrationCourses = getRegisteredSubjects(request);
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
    }

    /**
     * <h4>Gửi dữ liệu JSON phản hồi cho client</h4>
     * Dùng Gson để chuyển đổi dữ liệu thành JSON và gửi về client với mã trạng thái HTTP.
     *
     * @param res Đối tượng phản hồi HTTP
     * @param obj Các đối tượng cần serialize và gửi dưới dạng JSON
     * @throws IOException Nếu xảy ra lỗi khi ghi dữ liệu ra response stream
     */
    private void sendData(HttpServletResponse res, Object... obj) throws IOException {
        res.setContentType("application/json");
        try (PrintWriter out = res.getWriter()) {
            var gson = new Gson();
            res.setStatus(HttpServletResponse.SC_OK);
            out.println(gson.toJson(obj));
        }
    }

    /**
     * <h4>Chuyển hướng sang trang view `my_registration.jsp`</h4>
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @param message  thông báo lỗi nếu có
     * @throws ServletException nếu lỗi forward
     * @throws IOException      nếu lỗi I/O
     * @author HoanTX
     */
    private void handleRequest(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        request.setAttribute("message", message);
        request.getRequestDispatcher("/jsp/customer-features/my_registration.jsp").forward(request, response);
    }

    /**
     * <h4>Truy xuất danh sách môn học đã đăng ký của người dùng</h4>
     * Phương thức lấy thông tin môn học mà người dùng đã đăng ký từ session,
     * sau đó truy vấn các môn học cá nhân tương ứng và xây dựng danh sách các
     * đối tượng {@code RegistrationCourse} để phục vụ cho giao diện lịch sử đăng ký.
     * Nếu không có tài khoản trong phiên làm việc, sẽ sử dụng ID mặc định để xử lý.
     *
     * @param request Đối tượng HTTP chứa thông tin phiên người dùng
     * @return Danh sách các đối tượng {@code RegistrationCourse} đã đăng ký
     * @author HoanTX
     */
    private List<RegistrationCourse> getRegisteredSubjects(HttpServletRequest request) {
        var account = (Account) request.getSession().getAttribute("currentUser");
        var id = account != null ? account.getId().toString() : "b283bfb8-397a-11f0-84a1-088fc33f56c7";
        List<PersonalSubject> subjects = psDAO.getPersonalSubjectsByAccount(id);
        List<RegistrationCourse> registrationCourses = new ArrayList<>();
        subjects.forEach(subject -> {
            try {
                Subject s = sDAO.getById(subject.getSubjectId());
                registrationCourses.add(RegistrationCourse.builder()
                        .courseId(Encoder.encode(s.getId().toString()))
                        .subject(s.getName())
                        .registrationTime(subject.getRegistrationTime())
                        .packageName(subject.getPackageName())
                        .totalCost(subject.getPrice())
                        .status(subject.getStatus())
                        .validFrom(subject.getValidFrom())
                        .validTo(subject.getValidTo())
                        .build());
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        });
        return registrationCourses;
    }

    /**
     * <h4>Tìm kiếm và lọc danh sách RegistrationCourse</h4>
     * - Lọc theo từ khóa (keyword) hoặc tên môn học (filter).<br>
     * - Sau khi lọc, gọi phương thức phân trang để cập nhật dữ liệu view.
     *
     * @param request             HTTP request
     * @param keyword             từ khóa tìm kiếm
     * @param filter              bộ lọc môn học
     * @param registrationCourses danh sách khóa học đăng ký
     * @author HoanTX
     */
    private void handleSearchAndFilter(HttpServletRequest request, String keyword, String filter, List<RegistrationCourse> registrationCourses) {
        List<RegistrationCourse> results = new ArrayList<>();
        if (keyword != null) {
            logger.info("Searching for " + keyword);
            for (RegistrationCourse registrationCourse : registrationCourses) {
                if ((registrationCourse.getStatus() != null && registrationCourse.getStatus().toLowerCase().contains(keyword.toLowerCase()))
                        || (registrationCourse.getPackageName() != null && registrationCourse.getPackageName().toLowerCase().contains(keyword.toLowerCase()))
                        || registrationCourse.getSubject().toLowerCase().contains(keyword.toLowerCase())) {
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

    /**
     * <h4>Lọc theo tổ chức (contact)</h4>
     * - Dùng `org` từ request, decode và tìm khóa học tương ứng để lấy thông
     * tin tổ chức.
     *
     * @param request HTTP request
     * @param keyword courseId đã encode
     * @param course  danh sách các khóa học đăng ký
     * @throws Exception nếu lỗi DB
     * @author HoanTX
     */
    private void handleOrgFilter(HttpServletRequest request, String keyword, List<RegistrationCourse> course) throws Exception {
        if (keyword != null) {
            keyword = Encoder.decode(keyword);
            logger.info("Filter for " + keyword);
            Subject s = sDAO.getById(keyword);
            Contact contact = ccDAO.getById(s.getAuthorId());
            request.setAttribute("contact", contact);
            renderCoursePagination(request, course);
        }
    }

    /**
     * <h4>Chuyển đổi danh sách Contact sang ContactInfo để hiển thị</h4>
     * - Format dữ liệu contact thành object phù hợp với giao diện.
     *
     * @param request  HTTP request
     * @param contacts danh sách contact từ DB
     * @author HoanTX
     */
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

    /**
     * <h4>Phân trang danh sách khóa học</h4>
     * - Hiển thị 10 khóa học mỗi trang và truyền dữ liệu phân trang xuống view.
     *
     * @param request HTTP request có thể chứa tham số `page`
     * @param course  danh sách khóa học đăng ký
     * @author HoanTX
     */
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
