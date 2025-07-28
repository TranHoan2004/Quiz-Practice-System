/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import controller.assistant.HandleOllamaAssistant;
import controller.utils.HandleRequestBody;
import controller.web_socket.SubjectsListSocket;
import dao.*;
import dto.*;
import enumerate.SubjectStatus;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.*;
import utils.Encoder;
import utils.MailUtil;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.*;
import utils.PermissionUtil;

@WebServlet(name = "SubjectsListController", urlPatterns = {"/subject-list"})
public class SubjectsListController extends HttpServlet {

    // <editor-fold> desc="Khai báo các DAO để thao tác dữ liệu"
    private final SubjectDAO sDAO;
    private final CourseDAO cDAO;
    private final LessonDAO lDAO;
    private final AccountDAO aDAO;
    private final ContactDAO ctDAO;
    private final TopicDAO tDAO;
    private final TaglineDAO tlDAO;
    private final Logger logger;
    private final HandleOllamaAssistant assistant;
    private final PersonalSubjectDAO pDAO;
    private final HandleRequestBody hrb;
    private static final String GENERATE_COLOR_AND_ICON = """
                ##You are a helpful UI/UX assistant.
            
                ##Below is a list of academic or job-related topics. For each topic, assign:
            
                1. A suitable Font Awesome icon in HTML format. Example: <i class=\\"fas fa-book\\"></i>
                2. A color code based on these rules:
                   - Data-related fields (data, statistics, AI): "#ffb300"
                   - Business/Management topics: "#8e24aa"
                   - Technology/Engineering fields: "#1976d2"
                   - Others: "#50e384"
            
                ##Notes:
                   -  Do not repeat any topic. Use appropriate and diverse icons.
                   -  Do not change any information about topics that i provide you. Only add color and icon attributes
                   -  Size of topics list must be the same as the input
            
                ##Respond with a valid JSON array only, without any markdown formatting, explanation, or code block. Just the array.
            
                ##Example:
            
                [
                  {
                    "id": "YTBlZWJjOTktOWMwYi00ZWY4LWJiNmQtNmJiOWJkMzgwYTgx",
                    "name": "Data Analyst",
                    "icon": "<i class=\\"fas fa-chart-line\\"></i>",
                    "color": "#ffb300"
                  },
                  {
                    "id": "YTBlZWJjOTktOWMwYi00ZWY4LWJiNmQtNmJiOWJkMzgwYyrt",
                    "name": "Project Manager",
                    "icon": "<i class=\\"fas fa-briefcase\\"></i>",
                    "color": "#8e24aa"
                  }
                ]
            
                Here are the topics: %s
            """;
    private static final String ERROR_PROMPT = """
            You are Miss, a friendly virtual tutor. The user's question does not match any known category. Please write a short, friendly paragraph that:
            
            - Has 3 to 4 sentences and is around 100 words.
            - The **first sentence must clearly say**: the category does not exist.
            - Then, encourage the user to rephrase their question based on the available categories.
            - Remind them to use keywords that are related to those categories.
            - Help them feel supported and guide them patiently to try again.
            
            Instructions:
            - Do NOT mention the word “signal”.
            - Write in clean, plain text (no markdown, no `*` or **).
            - Use emoji like ❌, 💡, ✅ to make the response warm and helpful.
            
            Here are the available categories:
            %s
            """;
    private static final String CATEGORY_MATCHING_PROMPT = """
            You will be given a list of subject categories and a user input written in natural language.
            
            Your task is:
            1. If the user input is meaningless (e.g. random characters, gibberish like "asd", "lll", "123") or completely unrelated to learning topics, return: none
            2. If the user is asking to list all courses or categories, or about the number of courses, return: all
            3. If the input is clearly related and matches exactly or closely to one of the categories, return that category name
            4. If the input is ambiguous or you're unsure, return: none
            
            Only return **one** of the following exact values (case-sensitive): \s
            **all, none, %s**
            
            Do **not** explain your reasoning. Only return one word.
            
            List of categories:
            %s
            
            User input:
            "%s"
            """;
    // </editor-fold>

    public SubjectsListController() {
        this.sDAO = new SubjectDAO();
        this.cDAO = new CourseDAO();
        this.lDAO = new LessonDAO();
        this.aDAO = new AccountDAO();
        this.ctDAO = new ContactDAO();
        this.tDAO = new TopicDAO();
        this.tlDAO = new TaglineDAO();
        this.hrb = new HandleRequestBody();
        this.pDAO = new PersonalSubjectDAO();
        this.assistant = new HandleOllamaAssistant();
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    /**
     * <h4>Xử lý các yêu cầu GET cho màn hình danh sách môn học</h4>
     * Phương thức này tiếp nhận và phân nhánh xử lý các loại yêu cầu khác nhau
     * dựa vào giá trị của header `X-Source`:
     * <ul>
     * <li><b>"pagination"</b>: Thực hiện phân trang danh sách môn học</li>
     * <li><b>"topic"</b>: Kiểm tra trạng thái cá nhân hóa từ cookie, nếu có thì
     * phân trang, nếu không thì hiển thị chủ đề</li>
     * <li><b>khác hoặc null</b>: Xử lý tìm kiếm môn học hoặc danh sách cho
     * admin</li>
     * </ul>
     * Mọi kết quả sẽ được đóng gói trong `model` và trả về dưới dạng JSON.
     *
     * @param request Yêu cầu HTTP từ client, có thể chứa header, session,
     * cookie hoặc tham số
     * @param response Phản hồi HTTP để gửi kết quả JSON
     * @throws ServletException Nếu có lỗi liên quan đến servlet
     * @throws IOException Nếu có lỗi I/O xảy ra khi gửi dữ liệu phản hồi
     * @author HoanTX
     * @author TuanKD
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var header = request.getHeader("X-Source");
        Map<String, Object> model = new HashMap<>();
        System.out.println("Header: " + request.getHeader("X-Source"));
        try {
            if (header != null) {
                switch (header) {
                    case "pagination" -> {
                        pagination(request, model);
                        handleFeaturedSubjects(model);
                        sendData(response, HttpServletResponse.SC_OK, model);
                    }
                    case "topic" -> {
                        var cookie = request.getCookies();
                        var session = request.getSession();
                        @SuppressWarnings("unchecked")
                        List<Subject> subjects = (List<Subject>) session.getAttribute("subjects");
                        boolean status = false;
                        if (cookie != null) {
                            for (var c : cookie) {
                                if (c.getName().equals("customized") && c.getValue().equals("true")) {
                                    status = true;
                                    break;
                                }
                            }
                        }
                        if (status && subjects != null) {
                            model.put("signal", true);
                            request.setAttribute("page", 1);
                            request.setAttribute("size", 6);
                            pagination(request, model);
                            handleFeaturedSubjects(model);
                        } else {
                            model.put("signal", false);
                            model.put("items", "Subjects List");
                            model.put("topicsUI", generateColorAndIconForTopic(response, getAllTopicResp()));
                        }
                        model.put("topics", getAllTopicResp());
                        model.put("main_title", "Subjects List");
                        sendData(response, HttpServletResponse.SC_OK, model);
                    }
                    default ->
                        handleSearchEvent(request, response);
                }
            } else {
                subjectsListForAdminController(request, response);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * <h4>Xử lý yêu cầu POST cho cá nhân hóa và phân loại môn học</h4>
     * Tiếp nhận dữ liệu từ client và thực hiện các hành động khác nhau tùy theo
     * giá trị của header <b>X-Source</b>:
     * <ul>
     * <li>Nếu không có header: gửi prompt cho AI để xác định danh mục môn học
     * phù hợp.</li>
     * <li>Nếu header là <b>"customized_topic"</b>: gửi toàn bộ lựa chọn của
     * người dùng cho AI để đề xuất danh sách môn học phù hợp.</li>
     * <li>Nếu header là <b>"register"</b>: xử lý đăng ký môn học.</li>
     * </ul>
     * Kết quả trả về dưới dạng JSON, hoặc mã lỗi HTTP 400 nếu xảy ra vấn đề.
     *
     * @param req Đối tượng yêu cầu HTTP chứa dữ liệu từ phía client (body và
     * header)
     * @param resp Đối tượng phản hồi HTTP để trả kết quả JSON cho client
     * @throws ServletException Nếu có lỗi liên quan đến servlet xử lý
     * @throws IOException Nếu xảy ra lỗi khi đọc yêu cầu hoặc ghi phản hồi
     * @author HoanTX
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var map = hrb.getDataFromRequest(req);
            var header = req.getHeader("X-Source");
            logger.log(Level.INFO, "Header X-Source detected: {0}", header);

            if (header == null) {
                getSubjectsByPrompt(req, resp, map);
            } else {
                switch (header) {
                    case "register" ->
                        handleRegisterSubject(req, map);
                    case "customized_topic" -> {
                        customizingUsersSelection(req, resp, map);
                        resp.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception: " + e.getMessage(), e);
            var status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            var message = e instanceof IOException || e instanceof URISyntaxException ? "There are some errors happening" : e.getMessage();
            sendData(resp, status, message);
        }
    }

    // <editor-fold> desc="Subjects list for admin"
    /**
     * <h4>Controller xử lý danh sách môn học cho Admin</h4>
     * Hiển thị danh sách môn học (CourseDTO) cho quản trị viên với chức năng
     * tìm kiếm, lọc theo danh mục, trạng thái và tổ chức, đồng thời hỗ trợ phân
     * trang.
     *
     * @param request Yêu cầu HTTP từ phía client
     * @param response Phản hồi HTTP gửi về client
     * @throws ServletException Nếu xảy ra lỗi khi forward request
     * @throws IOException Nếu có lỗi I/O xảy ra trong quá trình xử lý
     * @author TuanKD
     */
    private void subjectsListForAdminController(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String category = request.getParameter("category");
        String status = request.getParameter("status");
        String orgEncoded = request.getParameter("org");
        String message = "";

        Account currentUser = (Account) request.getSession().getAttribute("currentUser");

        try {
            // Lấy dữ liệu tỪ database
            List<CourseDTO> allCourses;

            if (PermissionUtil.hasRole(request, "Admin")) {
                allCourses = getAllCourseDTOs(); // Admin thấy tất cả
            } else if (PermissionUtil.hasRole(request, "Expert")) {
                allCourses = getCoursesByExpertId(currentUser.getId().toString()); // Expert chỉ thấy của họ
            } else {
                response.sendRedirect(request.getContextPath() + "/jsp/unauthorized.jsp");
                return;
            }

            // Lọc theo keyword, category, status nếu có
            if (keyword != null || category != null || status != null) {
                allCourses = filterCourses(allCourses, keyword, category, status);
            }

            // Lọc theo contact (nếu có)
            if (orgEncoded != null) {
                handleContactFilter(request, orgEncoded);
            }

            // Lưu danh sách môn học và category lên request
            request.setAttribute("subjects", sDAO.getAllSubjects());

            // Chia trang và truyền danh sách đã phân trang sang JSP
            renderPagination(request, allCourses);
            String pram = "Blog Category";
            request.setAttribute("categories", sDAO.getAllCategories(pram));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            message = e.getMessage();
        }

        request.setAttribute("message", message);
        request.getRequestDispatcher("/jsp/course-features/subjects_list.jsp").forward(request, response);
    }

    /**
     * <h4>Lấy danh sách đầy đủ CourseDTO</h4>
     * Thu thập dữ liệu từ nhiều bảng liên kết như Course, Topic, Subject,
     * Lesson, Account để tổng hợp thông tin thành các đối tượng CourseDTO.
     *
     * @return Danh sách CourseDTO chứa đầy đủ thông tin để hiển thị
     * @throws Exception Nếu xảy ra lỗi trong quá trình truy vấn hoặc ánh xạ dữ
     * liệu từ database
     * @author TuanKD
     */
    private List<CourseDTO> getAllCourseDTOs() throws Exception {
        List<CourseDTO> result = new ArrayList<>();
        List<Course> courses = cDAO.getAllCourses();

        for (Course c : courses) {
            Topic t = tDAO.getTopicById(c.getTopicId());
            Subject s = sDAO.getById(t.getSubjectId());
            int lessonCount = lDAO.countByCourseId(c.getId().toString());
            Account owner = aDAO.getAccountById(c.getExpertId());

            // Lấy category thông qua subject -> setting_subject -> setting
            String category = sDAO.getCategoryBySubjectId(s.getId().toString());

            CourseDTO dto = CourseDTO.builder()
                    .id(Encoder.encode(c.getId().toString()))
                    .title(c.getTitle())
                    .category(category)
                    .numberOfLessons(lessonCount)
                    .owner(owner != null ? owner.getFullName() : "Unknown")
                    .published(c.isStatus())
                    .subjectId(Encoder.encode(s.getId().toString()))
                    .build();

            result.add(dto);
        }

        return result;
    }

    private List<CourseDTO> getCoursesByExpertId(String expertId) throws Exception {
        List<CourseDTO> result = new ArrayList<>();

        // Giả sử cDAO có phương thức getCoursesByExpertId, nếu chưa có thì bạn cần viết
        List<Course> courses = cDAO.getCoursesByExpertId(expertId);

        for (Course c : courses) {
            Topic t = tDAO.getTopicById(c.getTopicId());
            Subject s = sDAO.getById(t.getSubjectId());
            int lessonCount = lDAO.countByCourseId(c.getId().toString());
            Account owner = aDAO.getAccountById(c.getExpertId());

            String category = sDAO.getCategoryBySubjectId(s.getId().toString());

            CourseDTO dto = CourseDTO.builder()
                    .id(Encoder.encode(c.getId().toString()))
                    .title(c.getTitle())
                    .category(category)
                    .numberOfLessons(lessonCount)
                    .owner(owner != null ? owner.getFullName() : "Unknown")
                    .published(c.isStatus())
                    .subjectId(Encoder.encode(s.getId().toString()))
                    .build();

            result.add(dto);
        }

        return result;
    }

    /**
     * <h4>Lọc danh sách CourseDTO</h4>
     * Lọc danh sách theo từ khóa tìm kiếm, danh mục (category) và trạng thái
     * (status).
     *
     * @param courses Danh sách gốc các CourseDTO
     * @param keyword Từ khóa tìm kiếm (theo tên môn học, tên chủ sở hữu hoặc
     * danh mục)
     * @param category Danh mục lọc (nếu có)
     * @param status Trạng thái xuất bản: all | published | unpublished
     * @return Danh sách CourseDTO đã được lọc theo các tiêu chí đầu vào
     * @author TuanKD
     */
    private List<CourseDTO> filterCourses(List<CourseDTO> courses, String keyword, String category, String status) {
        List<CourseDTO> filtered = new ArrayList<>();

        for (CourseDTO course : courses) {
            boolean match = true;

            // Lọc theo keyword nếu có
            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = keyword.trim().toLowerCase();
                match &= course.getTitle().toLowerCase().contains(kw) || course.getOwner().toLowerCase().contains(kw) || course.getCategory().toLowerCase().contains(kw);
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

    /**
     * <h4>Xử lý bộ lọc theo tổ chức (Contact)</h4>
     * Giải mã mã tổ chức được truyền vào và thêm thông tin tổ chức vào request.
     *
     * @param request Đối tượng HttpServletRequest
     * @param orgEncoded Mã tổ chức đã được mã hóa từ client
     * @throws Exception Nếu tổ chức không tồn tại hoặc giải mã thất bại
     * @author TuanKD
     */
    private void handleContactFilter(HttpServletRequest request, String orgEncoded) throws Exception {
        String decodedId = Encoder.decode(orgEncoded);
        Contact contact = ctDAO.getById(decodedId);
        request.setAttribute("contact", contact);
    }

    /**
     * <h4>Phân trang danh sách CourseDTO</h4>
     * Chia danh sách CourseDTO thành từng trang nhỏ, mỗi trang chứa tối đa 10
     * phần tử.
     *
     * @param request Yêu cầu HTTP chứa thông tin về trang hiện tại
     * @param list Danh sách CourseDTO cần phân trang
     * @author TuanKD
     */
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
    // </editor-fold>

    // <editor-fold> desc="Solve the public subjects list screen">
    /**
     * <h4>Xử lý giao diện danh sách môn học công khai</h4>
     * Phương thức này chuẩn bị dữ liệu danh sách môn học dựa trên prompt (danh
     * mục) được cung cấp.
     *
     * @param request Yêu cầu HTTP chứa thông tin phiên và tham số
     * @param response Phản hồi HTTP sẽ gửi cho client
     * @param prompt Tên danh mục môn học hoặc từ khóa tìm kiếm tương ứng
     * @throws Exception Nếu xảy ra lỗi truy xuất hoặc xử lý dữ liệu
     * @author HoanTX
     */
    private void controlPublicSubjectsListScreen(HttpServletRequest request, HttpServletResponse response, String prompt) throws Exception {
        Map<String, Object> model = new HashMap<>();
        handlePublicSubjectsList(request, model, prompt);
        handleFeaturedSubjects(model);
        sendData(response, HttpServletResponse.SC_OK, model);
    }

    /**
     * <h4>Lấy và chuẩn bị danh sách môn học theo danh mục</h4>
     * Truy xuất danh sách môn học theo danh mục và lưu vào session. Sau đó thực
     * hiện phân trang và thêm vào model.
     *
     * @param req Yêu cầu HTTP
     * @param model Map dùng để lưu dữ liệu phản hồi
     * @param category Tên danh mục ("all", "none", hoặc tên danh mục cụ thể)
     * @throws Exception Nếu xảy ra lỗi truy vấn cơ sở dữ liệu hoặc xử lý dữ
     * liệu khác
     */
    private void handlePublicSubjectsList(HttpServletRequest req, Map<String, Object> model, String category) throws Exception {
        logger.log(Level.INFO, "Handling public subjects list request with category: {0}", category);
        var session = req.getSession();
        List<Subject> subjects = switch (category) {
            case "all" ->
                sDAO.getAllSubjects();
            case "none" ->
                null;
            default ->
                sDAO.getAllSubjectsByCategory(category);
        };
        session.setAttribute("subjects", subjects);
        session.setAttribute("subjectsBackup", subjects);
        pagination(req, model);
    }

    /**
     * <h4>Phân trang danh sách môn học cho màn hình danh sách môn học công
     * khai</h4>
     * Phương thức này phân trang danh sách môn học được lưu trong phiên làm
     * việc và đưa kết quả phân trang vào bản đồ mô hình. Số lượng mục trên mỗi
     * trang và trang hiện tại được xác định bởi các tham số yêu cầu "size" và
     * "page". Danh sách đã được phân trang sẽ được chuyển thành danh sách các
     * SubjectsListDTO để trả về phản hồi.
     *
     * @param req Yêu cầu HTTP chứa các tham số phân trang và thông tin phiên
     * @param model Bản đồ mô hình để lưu dữ liệu phân trang cho phản hồi
     * @author HoanTX
     */
    private void pagination(HttpServletRequest req, Map<String, Object> model) {
        List<SubjectsListDTO> list;
        var session = req.getSession();
        var pageRaw = req.getParameter("page");
        var sizeRaw = req.getParameter("size");
        @SuppressWarnings("unchecked")
        List<Subject> subjects = (List<Subject>) session.getAttribute("subjects");
        var currentPage = pageRaw != null ? Integer.parseInt(pageRaw) : 1;
        var numberItemsPerPage = sizeRaw != null ? Integer.parseInt(sizeRaw) : 6;
        var startIndex = (currentPage - 1) * numberItemsPerPage;

        if (subjects != null) {
            var endIndex = Math.min(subjects.size(), startIndex + numberItemsPerPage);
            var paginatedSubjects = subjects.subList(startIndex, endIndex);

            list = getSubjectsListDTOs(paginatedSubjects);

            model.put("subjects", list);
            model.put("numberOfPages", Math.ceil(subjects.size() / (double) numberItemsPerPage));
            model.put("numberOfItems", subjects.size());
        } else {
            model.put("subjects", null);
        }
    }

    /**
     * <h4>Chuyển đổi danh sách các đối tượng môn học thành các
     * SubjectsListDTO</h4>
     * Ánh xạ từng đối tượng môn học thành một SubjectsListDTO, bao gồm giá
     * tiền, khẩu hiệu, thông tin liên hệ và các gói giá.
     *
     * @param subjects Danh sách các đối tượng môn học cần chuyển đổi
     * @return Danh sách các đối tượng SubjectsListDTO để trả về phản hồi
     * @author HoanTX
     */
    private List<SubjectsListDTO> getSubjectsListDTOs(List<Subject> subjects) {
        return subjects.stream().map(s -> {
            Map<String, String> map = sDAO.getLowestPriceAndSalePriceBySubjectId(s.getId().toString());
            var lowestPrice = Integer.parseInt(map.get("lowest_price") != null ? map.get("lowest_price") : "1");
            var salePrice = Integer.parseInt(map.get("sale_price") != null ? map.get("sale_price") : "1");
            try {
                return SubjectsListDTO.builder()
                        .id(Encoder.encode(s.getId().toString()))
                        .name(s.getName())
                        .tagline(getTaglineNames(s.getId().toString()))
                        .thumbnailURL(s.getThumbnailURL())
                        .lowestPrice(String.valueOf(lowestPrice))
                        .salePrice(String.valueOf(lowestPrice * (100 - salePrice) / 100))
                        .updatedDate(s.getUpdatedDate() == null ? null : s.getUpdatedDate().toString())
                        .contactInfo(getContactInfo(s.getAuthorId()))
                        .pricePackage(calculatePricePackage(s.getId().toString()))
                        .build();
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                return null;
            }
        }).toList();
    }

    /**
     * <h4>Tính toán các gói giá cho một môn học</h4>
     * Xác định các gói giá Đồng, Bạc và Vàng dựa trên số lượng khóa học trong
     * môn học.
     *
     * @param subjectId ID của môn học
     * @return Bản đồ với tên gói làm khóa và giá tiền làm giá trị
     * @author HoanTX
     */
    private Map<String, Integer> calculatePricePackage(String subjectId) {
        Map<String, Integer> map = new HashMap<>();
        var total = sDAO.getNumberOfCoursesPerSubject(subjectId);
        if (total > 0) {
            var cheapestPackage = 0;
            var mediumPackage = 0;
            var maxPackage = 0;
            if (total > 3 && total <= 5) {
                cheapestPackage = sDAO.getTotalPriceOfTopNCourse(subjectId, 3);
                mediumPackage = sDAO.getTotalPriceOfTopNCourse(subjectId, 5);
            } else if (total > 5) {
                maxPackage = sDAO.getTotalPriceOfTopNCourse(subjectId, Math.min(7, total));
            } else {
                cheapestPackage = sDAO.getTotalPriceOfTopNCourse(subjectId, total);
            }
            map.put("Bronze", cheapestPackage);
            map.put("Silver", mediumPackage);
            map.put("Gold", maxPackage);
        }
        return map;
    }

    /**
     * <h4>Lấy thông tin liên hệ của tác giả môn học</h4>
     * Truy xuất thông tin liên hệ dựa trên ID của tác giả và xây dựng một đối
     * tượng ContactInfo.
     *
     * @param id ID của tác giả
     * @return Đối tượng ContactInfo chứa thông tin liên hệ của tác giả
     * @throws Exception nếu không tìm thấy thông tin liên hệ
     * @author HoanTX
     */
    private ContactInfo getContactInfo(String id) throws Exception {
        Contact contact = ctDAO.getById(id);
        return ContactInfo.builder()
                .id(Encoder.encode(contact.getId().toString()))
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .address(contact.getAddress())
                .name(contact.getName())
                .link(contact.getLink())
                .build();
    }

    /**
     * <h4>Gửi dữ liệu JSON phản hồi cho client</h4>
     * Dùng Gson để chuyển đổi dữ liệu thành JSON và gửi về client với mã trạng
     * thái HTTP.
     *
     * @param res Đối tượng phản hồi HTTP
     * @param status Mã trạng thái HTTP
     * @param obj Các đối tượng cần serialize và gửi dưới dạng JSON
     * @throws IOException Nếu xảy ra lỗi khi ghi dữ liệu ra response stream
     */
    private void sendData(HttpServletResponse res, int status, Object... obj) throws IOException {
        res.setContentType("application/json");
        try (PrintWriter out = res.getWriter()) {
            var gson = new Gson();
            res.setStatus(status);
            out.println(gson.toJson(obj));
        }
    }

    /**
     * <h4>Lấy tên khẩu hiệu cho một môn học</h4>
     * Truy xuất danh sách các tên khẩu hiệu gắn với ID môn học đã cho.
     *
     * @param id ID của môn học
     * @return Danh sách các tên khẩu hiệu
     * @author HoanTX
     */
    private List<String> getTaglineNames(String id) {
        return tlDAO.getTaglinesBySubjectId(id).stream()
                .map(Tagline::getName).toList();
    }

    /**
     * <h4>Xử lý các chủ đề nổi bật</h4>
     * Truy xuất 3 chủ đề nổi bật nhất và đưa chúng vào bản đồ mô hình.
     *
     * @param model Bản đồ mô hình để lưu trữ các chủ đề nổi bật
     * @throws SQLException Nếu xảy ra lỗi truy cập cơ sở dữ liệu
     * @author HoanTX
     */
    private void handleFeaturedSubjects(Map<String, Object> model) throws SQLException, ClassNotFoundException {
        var subjects = sDAO.getTopSubjectsFlag(3);
        List<FeaturedSubjects> featuredSubjects = subjects.stream().map(s -> FeaturedSubjects.builder()
                .id(Encoder.encode(s.getId().toString()))
                .name(s.getName())
                .thumbnailURL(s.getThumbnailURL())
                .build()).toList();
        model.put("featured_subjects", featuredSubjects);
    }

    /**
     * <h4>Xử lý tìm kiếm môn học theo tên</h4>
     * Lọc danh sách môn học trong session dựa trên chuỗi truy vấn người dùng
     * nhập.
     *
     * @param req Yêu cầu HTTP chứa tham số tìm kiếm
     * @param resp Phản hồi HTTP để gửi kết quả
     * @throws Exception Nếu xảy ra lỗi trong quá trình xử lý tìm kiếm hoặc truy
     * cập session
     */
    @SuppressWarnings("unchecked")
    private void handleSearchEvent(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        var session = req.getSession();
        var param = req.getParameter("query").trim();

        Map<String, Object> model = new HashMap<>();
        if (session.getAttribute("subjects") != null) {
            List<Subject> subjects = (List<Subject>) session.getAttribute("subjectsBackup");
            List<Subject> result = param.isEmpty() ? subjects
                    : subjects.stream().filter(s -> s.getName().toLowerCase().contains(param.toLowerCase())).toList();
            session.setAttribute("subjects", result);
        }
        pagination(req, model);
        sendData(resp, HttpServletResponse.SC_OK, model);
    }

    /**
     * <h4>Lấy danh sách tất cả TopicResp</h4>
     * Phương thức này truy xuất toàn bộ chủ đề từ cơ sở dữ liệu và chuyển đổi
     * sang danh sách TopicResp.
     *
     * @return Danh sách các đối tượng TopicResp
     * @throws Exception Nếu xảy ra lỗi khi truy xuất dữ liệu
     * @author HoanTX
     */
    private List<TopicResp> getAllTopicResp() throws Exception {
        List<Topic> topics = tDAO.getAllTopic();
        List<TopicResp> topicResp = new ArrayList<>();
        topics.forEach(topic -> topicResp.add(new TopicResp(Encoder.encode(topic.getId().toString()), topic.getName(), "", "")));
        return topicResp;
    }

    /**
     * <h4>Gán màu sắc và icon cho danh sách chủ đề</h4>
     * Phương thức này chia nhỏ danh sách chủ đề thành từng nhóm, gửi prompt tới
     * AI để nhận về icon và màu sắc phù hợp cho từng chủ đề.
     *
     * @param resp Đối tượng HttpServletResponse để truyền vào hàm gọi AI
     * @param list Danh sách các TopicResp cần gán icon và màu sắc
     * @return Danh sách TopicResp đã được cập nhật icon và màu sắc
     * @throws JsonProcessingException Nếu xảy ra lỗi khi xử lý JSON
     * @author HoanTX
     */
    private List<TopicResp> generateColorAndIconForTopic(HttpServletResponse resp, List<TopicResp> list) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        var step = (int) Math.ceil((double) list.size() / 5);
        List<TopicResp> result = new ArrayList<>();

        for (int i = 0; i < step; i++) {
            List<TopicResp> subList = list.subList(i * 5, Math.min((i + 1) * 5, list.size()));
            var json = mapper.writeValueAsString(subList);
            var prompt = GENERATE_COLOR_AND_ICON.formatted(json);

            logger.info("Prompt: {}" + prompt);
            try {
                result.addAll(mapper.readValue(getAnswerFromPrompt(resp, prompt), new TypeReference<>() {
                }));
            } catch (JsonProcessingException ex) {
                logger.log(Level.SEVERE, ex.getMessage(), ex);
            } catch (IOException | URISyntaxException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
        return result;
    }

    /**
     * <h4>Gửi prompt tới trợ lý và lấy phản hồi dạng JSON</h4>
     * Gửi prompt tới AI assistant và xử lý chuỗi kết quả để loại bỏ các đoạn dư
     * thừa như ```json.
     *
     * @param resp Đối tượng phản hồi HTTP
     * @param prompt Nội dung prompt gửi đến AI
     * @return Chuỗi JSON đã làm sạch
     * @throws IOException, URISyntaxException Nếu có lỗi gửi hoặc định dạng URI
     * không hợp lệ
     */
    private String getAnswerFromPrompt(HttpServletResponse resp, String prompt) throws IOException, URISyntaxException {
        var body = assistant.preparePrompt(prompt);
        var answer = assistant.getAnswer(resp, body).trim();
        if (answer.startsWith("```json")) {
            answer = answer.replaceFirst("^```json\\s*", "").replaceFirst("\\s*```$", "");
        }
        return answer;
    }

    /**
     * <h4>Lấy danh sách môn học theo prompt AI</h4>
     * Phương thức này gửi prompt chứa danh sách các danh mục môn học và truy
     * vấn người dùng đến AI để xác định danh mục phù hợp. Nếu AI trả về một
     * danh mục hợp lệ, sẽ hiển thị danh sách môn học tương ứng. Nếu không, gửi
     * thông báo lỗi thân thiện cho client.
     *
     * @param req Yêu cầu HTTP từ client
     * @param resp Phản hồi HTTP gửi về client
     * @param map Dữ liệu đầu vào từ client (chứa prompt)
     * @throws Exception Nếu có lỗi trong quá trình xử lý hoặc truy vấn AI
     * @author HoanTX
     */
    private void getSubjectsByPrompt(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> map) throws Exception {
        String param = "'Domain', 'Group'" ;
        var catesList = sDAO.getAllCategories(param);
        var catesListToString = String.join(", ", catesList);
        var prompt = CATEGORY_MATCHING_PROMPT.formatted(catesListToString, catesListToString, map.get("prompt"));

        var body = assistant.preparePrompt(prompt);
        var answer = assistant.getAnswer(resp, body).trim();

        if (!answer.equals("none")) {
            controlPublicSubjectsListScreen(req, resp, answer);
        } else {
            var errorPrompt = assistant.preparePrompt(ERROR_PROMPT.formatted(String.join(", ", catesList)));
            SubjectsListSocket.notifyClient("currentEmail", assistant.getAnswer(resp, errorPrompt));
            sendData(resp, HttpServletResponse.SC_ACCEPTED, new HashMap<>());
        }
    }

    /**
     * <h4>Cá nhân hóa lựa chọn môn học theo hồ sơ người dùng</h4>
     * Phương thức này gửi thông tin cá nhân hóa của người dùng và danh sách môn
     * học đến AI để nhận về danh sách môn học phù hợp nhất. Kết quả sẽ được lưu
     * vào session và thiết lập cookie đánh dấu trạng thái cá nhân hóa.
     *
     * @param req Yêu cầu HTTP từ client
     * @param resp Phản hồi HTTP gửi về client
     * @param map Dữ liệu đầu vào từ client (chứa thông tin cá nhân hóa)
     * @author HoanTX
     */
    private void customizingUsersSelection(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> map) {
        var obj = new ObjectMapper().convertValue(map, CustomizedLearningTargetReq.class);
        var numberOfSubjects = sDAO.count();
        List<Subject> subjects = new ArrayList<>();
        var step = (int) Math.ceil((double) numberOfSubjects / 5);
        var session = req.getSession();
        var promptTemplate = """
                You are an intelligent educational assistant. Your task is to analyze the user's selections and recommend the most suitable list of s based on their learning profile and the available subject information.
                
                ### User Profile:
                - Learning Target: %s
                - Identified Role: %s
                - Education Level: %s
                - Selected Topics: %s
                
                ### Available Subject Data:
                %s
                
                ### Your Response:
                Return a list of the most relevant **full subject objects** in valid JSON format. Each object should include all fields such as id, name, thumbnailURL, featureFlag, authorId, updatedDate.
                """;

        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            for (int i = 0; i < step; i++) {
                List<Subject> subList = sDAO.getSubjectsByPagination(5, i);
                if (!subList.isEmpty()) {
                    var json = mapper.writeValueAsString(subList);
                    var prompt = promptTemplate.formatted(obj.learningTarget(), obj.identified(), obj.educationLevel(), obj.selectedTopics(), json);
                    subjects.addAll(mapper.readValue(getAnswerFromPrompt(resp, prompt), new TypeReference<>() {
                    }));
                }
            }
        } catch (JsonProcessingException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (IOException | URISyntaxException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        logger.log(Level.INFO, "List: {0}", subjects);

        session.setAttribute("subjects", subjects);
        session.setAttribute("subjectsBackup", subjects);

        if (session.getAttribute("currentUser") != null) {
            var cookie = new Cookie("customized", "true");
            cookie.setMaxAge(7 * 24 * 60 * 60);
            cookie.setPath(req.getContextPath());
            resp.addCookie(cookie);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }
    // </editor-fold>

    // <editor-fold> desc="Solve the subject register screen"
    private boolean isNewAccount = false;

    /**
     * <h4>Đăng ký môn học</h4>
     * Xử lý quy trình đăng ký môn học bao gồm xác thực tài khoản người dùng,
     * tạo tài khoản mới nếu cần, lưu thông tin đăng ký vào cơ sở dữ liệu và gửi
     * email hướng dẫn thanh toán.
     *
     * @param req Yêu cầu HTTP chứa thông tin phiên làm việc và dữ liệu người
     * dùng
     * @param map Bản đồ chứa dữ liệu đầu vào như ID môn học, gói giá, thông tin
     * cá nhân
     * @throws Exception nếu không tìm thấy môn học hoặc gặp lỗi trong quá trình
     * xử lý
     * @author HoanTX
     */
    private void handleRegisterSubject(HttpServletRequest req, Map<String, Object> map) throws Exception {
        var session = req.getSession();
        var account = (Account) session.getAttribute("currentUser");
        var id = Encoder.decode(map.get("id").toString());
        String accountId;
        var pricePackage = map.get("pricePackage");
        var packageName = map.get("pricePackageName");

        if (account == null) {
            isNewAccount = true;
            var email = map.get("email");
            var fullName = map.get("fullName");
            var phoneNumber = map.get("phoneNumber");
            var gender = Integer.parseInt(map.get("gender").toString());
            logger.log(Level.INFO, "price package: {0}", packageName);
            createNewAccount(email.toString(), fullName.toString(), phoneNumber.toString(), gender);
            account = aDAO.getAccountByEmail(email.toString());
            accountId = account.getId().toString();
        } else {
            if (!sDAO.existsById(id)) {
                throw new Exception("Subject not found");
            }
            accountId = account.getId().toString();
        }
        if (pDAO.existBySubjectIdAndAccountId(accountId, id)) {
            throw new Exception("Subject already exists");
        }
        pDAO.insert(PersonalSubject.builder()
                .accountId(accountId)
                .subjectId(id)
                .status(SubjectStatus.SENT.name().toLowerCase())
                .price(Float.parseFloat(pricePackage.toString()))
                .packageName(packageName.toString())
                .registrationTime(LocalDate.now())
                .validFrom(LocalDate.now())
                .validTo(LocalDate.now().plusDays(31 * 12)) // 1 year
                .build());
        sendEmail(aDAO.getAccountByEmail(account.getEmail()), sDAO.getById(id), pricePackage.toString());

        session.setAttribute("subjectId", id);
    }

    /**
     * <h4>Tạo tài khoản mới</h4>
     * Kiểm tra sự tồn tại của email và số điện thoại, sau đó tạo tài khoản
     * người dùng với thông tin được cung cấp.
     *
     * @param email Email đăng ký
     * @param fullName Họ tên đầy đủ
     * @param phoneNumber Số điện thoại liên hệ
     * @param gender Giới tính (mã số nguyên đại diện)
     * @throws IllegalArgumentException nếu email hoặc số điện thoại đã tồn tại
     * @author HoanTX
     */
    private void createNewAccount(String email, String fullName, String phoneNumber, int gender) throws Exception {
        System.out.println("Email: " + email);
        if (aDAO.isEmailExist(email)) {
            System.out.println("Email Existed");
            throw new Exception("Email already exists");
        }
        if (aDAO.isPhoneNumberExist(phoneNumber)) {
            System.out.println("Phone Existed");
            throw new Exception("Phone number already exists");
        }
        aDAO.createAccount(Account.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDate.now())
                .password("MyPassword123.")
                .email(email)
                .fullName(fullName)
                .phoneNumber(phoneNumber)
                .gender(gender)
                .roleId("b1b69765-397a-11f0-84a1-088fc33f56c7")
                .build());
    }

    /**
     * <h4>Gửi email xác nhận và hướng dẫn thanh toán</h4>
     * Gửi email đến người dùng chứa thông tin đăng ký khóa học, tài khoản truy
     * cập, hướng dẫn chuyển khoản và liên kết thanh toán.
     *
     * @param account Tài khoản người dùng
     * @param subject Môn học đã đăng ký
     * @param pricePackage Giá tiền gói học
     * @author HoanTX
     */
    private void sendEmail(Account account, Subject subject, String pricePackage) {
        logger.log(Level.INFO, "Sending mail to {0}", account.getEmail());
        var mailSubject = "Register Order";
        var link = "http://localhost:8080/qps/payment";
        var accountInformationForNewUser = """
                    <div class="section">
                      <h3>👤 Student Account</h3>
                      <ul>
                        <li><strong>Login Email:</strong> %s</li>
                        <li><strong>Password:</strong> %s</li>
                      </ul>
                    </div>
                """.formatted(account.getEmail(), account.getPassword());
        var accountNumber = Dotenv.load().get("ACCOUNT_NUMBER");
        var template = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                  <meta charset="UTF-8">
                  <title>Course Registration & Payment Instructions</title>
                  <style>
                    body {
                      font-family: Arial, sans-serif;
                      color: #333;
                      line-height: 1.6;
                    }
                    .container {
                      max-width: 600px;
                      margin: auto;
                      padding: 20px;
                      border: 1px solid #ddd;
                      border-radius: 8px;
                      background-color: #f9f9f9;
                    }
                    h2 {
                      color: #007bff;
                    }
                    .section {
                      margin-bottom: 20px;
                    }
                    .qr {
                      text-align: center;
                    }
                    .qr img {
                      max-width: 200px;
                    }
                    .footer {
                      font-size: 0.9em;
                      color: #777;
                      text-align: center;
                      margin-top: 30px;
                    }
                  </style>
                </head>
                <body>
                  <div class="container">
                    <h2>Course Registration & Payment Instructions</h2>
                
                    <div class="section">
                      <p>Dear <strong>%s</strong>,</p>
                      <p>Thank you for registering for a course with <strong>Quezee</strong>. Below are the details of your registration:</p>
                    </div>
                
                    <div class="section">
                      <h3>📚 Course Information</h3>
                      <ul>
                        <li><strong>Course Name:</strong> %s</li>
                        <li><strong>Tuition Fee:</strong> %s</li>
                      </ul>
                    </div>
                
                    %s
                
                    <div class="section">
                      <h3>💳 Payment Instructions</h3>
                      <ul>
                        <li><strong>Bank:</strong> Vietcombank</li>
                        <li><strong>Account Number:</strong> %s</li>
                        <li><strong>Account Holder:</strong> Quezee Education Co., Ltd.</li>
                        <li><strong>Transfer Note:</strong> HOAN_BIOLOGY_GOLD</li>
                      </ul>
                    </div>
                
                    <div class="qr">
                      <p>Please click the following link to make your payment:</p>
                      %s
                    </div>
                
                    <div class="footer">
                      <p>Your course will be activated within 24 hours after payment confirmation.</p>
                      <p>If you have any questions, please contact us at <a href="mailto:plan@eventplanners.com">plan@eventplanners.com</a> or call 0990001112.</p>
                    </div>
                  </div>
                </body>
                </html>
                """
                .formatted(
                        account.getFullName(), subject.getName(), pricePackage,
                        isNewAccount ? accountInformationForNewUser : "", accountNumber, link
                );
        MailUtil.sendMail(account.getEmail(), mailSubject, template);
    }
    // </editor-fold>
}
