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
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.*;
import utils.Encoder;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.*;

@WebServlet(name = "SubjectsListController", urlPatterns = {"/user/subject_list"})
public class SubjectsListController extends HttpServlet implements Runnable {

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
    private HttpServletResponse resp;
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
    private final HandleRequestBody hrb;
    private final static Map<String, String> FIRST_PART_OF_CUSTOMIZATION = Map.of(
            "Working", "Tập trung vào kỹ năng phục vụ công việc hiện tại hoặc tương lai.",
            "Improve my knowledge", "Bồi dưỡng và nâng cao hiểu biết trong lĩnh vực đã học.",
            "Explore new knowledge", "Khám phá những kiến thức hoàn toàn mới với bạn.");
    private final static Map<String, String> SECOND_PART_OF_CUSTOMIZATION = Map.of(
            "Pupil", "Học sinh cấp 1, 2 hoặc 3.",
            "Student", "Sinh viên đang theo học tại đại học hoặc cao đẳng.",
            "Working professional", "Đã đi làm và đang phát triển sự nghiệp.");
    private final static Map<String, String> THIRD_PART_OF_CUSTOMIZATION = Map.of(
            "Secondary education or below", "THPT hoặc thấp hơn.",
            "Undergraduate level", "Đang học hoặc đã tốt nghiệp đại học.",
            "Postgraduate level", "Cao học, thạc sĩ hoặc tiến sĩ."
    );
    private final static String promptTemplate = """
            You are an intelligent classification assistant.
            Below is a list of subjects and three groups of personalization criteria.

            Your task is to:

            1. Read the provided list of subjects.
            2. Based on combinations of the three personalization criteria, classify subjects into learning trails.

            Each trail represents a learning path for a specific combination of personalization:

            FIRST_PART_OF_CUSTOMIZATION: Learning goals (users can select multiple)

            SECOND_PART_OF_CUSTOMIZATION: Learner profile (only one selection allowed)

            THIRD_PART_OF_CUSTOMIZATION: Education level (only one selection allowed)

            ### Personalization Criteria:
            * FIRST_PART_OF_CUSTOMIZATION (Learning goals — multi-select):
            - "Working": Focus on skills that support current or future job roles.
            - "Improve my knowledge": Reinforce and deepen understanding in existing areas of expertise.
            - "Explore new knowledge": Explore knowledge that is completely new to the learner.

            * SECOND_PART_OF_CUSTOMIZATION (Learner profile — single-select):
            - "Pupil": Primary or secondary school students.
            - "Student": University or college students.
            - "Working professional": Individuals currently employed and building their career.

            * THIRD_PART_OF_CUSTOMIZATION (Education level — single-select):
            - "Secondary education or below": High school or lower.
            - "Undergraduate level": Currently pursuing or completed undergraduate degree.
            - "Postgraduate level": Graduate school, master's, or doctoral level.

            ### Expected output:
            - Generate a list of learning trails, where each trail corresponds to one combination of the three personalization criteria.
            - Each trail should contain the list of subjects that are relevant to that combination.
            - A subject may appear in multiple trails if applicable.
            - Your classification must be based on the semantic meaning of both the subject and the associated criteria descriptions.

            ### Preferred output format (JSON-like):
            [
              {
                "firstCriteria": ["Working"],
                "secondCriteria": "Working professional",
                "thirdCriteria": "Undergraduate level",
                "subjects": ["Business Communication", "Project Management"]
              },
              {
                "firstCriteria": ["Explore new knowledge", "Improve my knowledge"],
                "secondCriteria": "Student",
                "thirdCriteria": "Undergraduate level",
                "subjects": ["Artificial Intelligence", "Philosophy", "Design Thinking"]
              }
            ]
            ### Start with the following subject list:
            %s
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
//        Thread thread = new Thread();
//        thread.start();
        try {
            if (header != null) {
                switch (header) {
                    case "pagination" -> {
                        logger.info("Handling pagination request");
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
                            for (Cookie c : cookie) {
                                if (c.getName().equals("customized") && c.getValue().equals("true")) {
                                    status = true;
                                    break;
                                }
                            }
                        }
                        logger.log(Level.INFO, "subjects: {0}", subjects);
                        if (status && subjects != null) {
                            model.put("signal", true);
                            request.setAttribute("page", 1);
                            request.setAttribute("size", 6);
                            pagination(request, model);
                            handleFeaturedSubjects(model);
                        } else {
                            model.put("signal", false);
                            model.put("topics", getAllTopicResp());
                            model.put("main_title", "Subjects List");
                            model.put("items", "Subjects List");
                            model.put("topicsUI", generateColorAndIconForTopic(response, getAllTopicResp()));
                        }
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
     * <h4>Xử lý các yêu cầu POST cho cá nhân hóa và phân loại môn học</h4>
     * Phương thức này tiếp nhận dữ liệu người dùng gửi lên và thực hiện một
     * trong các hành động:
     * <ul>
     * <li>Nếu không có header <b>X-Source</b>: Gửi prompt để AI xác định danh
     * mục môn học phù hợp</li>
     * <li>Nếu header là <b>"customized_topic"</b>: Gửi toàn bộ lựa chọn người
     * dùng đến AI để đề xuất danh sách môn học phù hợp</li>
     * </ul>
     * Mọi kết quả được trả về dưới dạng JSON, hoặc thông báo lỗi với mã trạng
     * thái 400 nếu có vấn đề.
     *
     * @param req Yêu cầu HTTP chứa dữ liệu từ client (body và header)
     * @param resp Phản hồi HTTP để trả kết quả JSON
     * @throws ServletException Nếu có lỗi liên quan đến servlet
     * @throws IOException Nếu có lỗi khi đọc dữ liệu yêu cầu hoặc ghi dữ liệu
     * phản hồi
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
            } else if (header.equals("customized_topic")) {
                customizingUsersSelection(req, resp, map);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception: " + e.getMessage(), e);
            var status = 400;
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
            request.setAttribute("subjects", sDAO.getAllSubjects());

            // Chia trang và truyền danh sách đã phân trang sang JSP
            renderPagination(request, allCourses);
            request.setAttribute("categories", sDAO.getAllCategories());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            message = e.getMessage(); // Gán lỗi để hiển thị nếu có
        }

        // Truyền message nếu có lỗi và forward sang JSP hiển thị
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
            // Lấy Topic → từ Topic lấy Subject
            Topic t = tDAO.getTopicById(c.getTopicId());
            Subject s = sDAO.getById(t.getSubjectId());

            // Đếm số lượng bài học thuộc Subject
            int lessonCount = lDAO.countBySubjectId(s.getId().toString());

            // Lấy thông tin chuyên gia (owner)
            Account owner = aDAO.getAccountById(c.getExpertId());

            String category = sDAO.getCategoryBySubjectId(s.getId().toString());
            // Xây dựng đối tượng CourseDTO
            CourseDTO dto = CourseDTO.builder().id(Encoder.encode(c.getId().toString())) // dùng hashCode làm ID tạm
                    .title(s.getName()) // tên môn học
                    .category(category) // tạm dùng tên subject làm category
                    .numberOfLessons(lessonCount).owner(owner.getFullName()).published(c.isStatus()) // trạng thái đã xuất bản hay chưa
                    .subjectId(Encoder.encode(s.getId().toString())).build();

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
     * <h4>Phân trang danh sách môn học lưu trong session</h4>
     * Cắt danh sách môn học thành các trang nhỏ hơn và thêm vào model cùng với
     * thông tin phân trang.
     *
     * @param req Yêu cầu HTTP chứa tham số trang và kích thước
     * @param model Map chứa danh sách đã phân trang và số liệu liên quan
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
     * <h4>Chuyển đổi danh sách Subject thành DTO có thêm thông tin</h4>
     * Mỗi đối tượng Subject sẽ được ánh xạ thành SubjectsListDTO có thêm giá
     * bán và thông tin liên hệ.
     *
     * @param subjects Danh sách các đối tượng Subject
     * @return Danh sách DTO SubjectsListDTO đã được làm giàu dữ liệu
     */
    private List<SubjectsListDTO> getSubjectsListDTOs(List<Subject> subjects) {
        return subjects.stream().map(s -> {
            Map<String, String> map = sDAO.getLowestPriceAndSalePriceBySubjectId(s.getId().toString());
            try {
                return SubjectsListDTO.builder().id(Encoder.encode(s.getId().toString())).name(s.getName()).tagline(getTaglineNames(s.getId().toString())).thumbnailURL(s.getThumbnailURL()).lowestPrice(map.get("lowest_price")).salePrice(map.get("sale_price")).updatedDate(s.getUpdatedDate() == null ? null : s.getUpdatedDate().toString()).contactInfo(getContactInfo(s.getAuthorId())).build();
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                return null;
            }
        }).toList();
    }

    /**
     * <h4>Lấy thông tin liên hệ tác giả qua ID</h4>
     * Truy vấn thông tin liên hệ dựa trên ID tác giả và chuyển đổi sang
     * ContactInfo DTO.
     *
     * @param id ID của tác giả
     * @return Đối tượng ContactInfo chứa thông tin liên hệ
     * @throws Exception Nếu có lỗi khi truy xuất dữ liệu liên hệ
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
     * <h4>Lấy danh sách tagline theo môn học</h4>
     * Truy xuất danh sách các tagline (nhãn) của môn học thông qua ID.
     *
     * @param id ID của môn học
     * @return Danh sách tên các tagline (dưới dạng chuỗi)
     */
    private List<String> getTaglineNames(String id) {
        return tlDAO.getTaglinesBySubjectId(id).stream()
                .map(Tagline::getName).toList();
    }

    /**
     * <h4>Lấy danh sách môn học nổi bật và thêm vào model</h4>
     * Truy xuất các môn học được đánh dấu nổi bật và chuyển thành DTO đơn giản
     * để hiển thị.
     *
     * @param model Map dữ liệu để lưu danh sách featured_subjects
     * @throws SQLException Nếu xảy ra lỗi truy vấn cơ sở dữ liệu
     */
    private void handleFeaturedSubjects(Map<String, Object> model) throws SQLException {
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
     * <h4>Lấy toàn bộ danh sách chủ đề và chuyển thành DTO</h4>
     * Truy xuất toàn bộ chủ đề và ánh xạ thành các đối tượng TopicResp.
     *
     * @return Danh sách DTO chủ đề (TopicResp)
     * @throws Exception Nếu xảy ra lỗi truy vấn dữ liệu
     */
    private List<TopicResp> getAllTopicResp() throws Exception {
        List<Topic> topics = tDAO.getAllTopic();
        List<TopicResp> topicResp = new ArrayList<>();
        topics.forEach(topic -> topicResp.add(new TopicResp(Encoder.encode(topic.getId().toString()), topic.getName(), "", "")));
        return topicResp;
    }

    /**
     * <h4>Sinh màu sắc và icon cho các chủ đề bằng trợ lý AI</h4>
     * Chia danh sách chủ đề thành nhiều phần và gửi prompt tới AI để sinh thông
     * tin hình ảnh.
     *
     * @param resp Đối tượng phản hồi HTTP để truyền xuống hàm gọi AI
     * @param list Danh sách chủ đề cần sinh màu/icon
     * @return Danh sách chủ đề đã có thêm màu sắc và biểu tượng
     * @throws JsonProcessingException Nếu xảy ra lỗi khi phân tích JSON
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
     * <h4>Trích lọc danh mục từ prompt để hiển thị danh sách môn học phù
     * hợp</h4>
     * Sử dụng AI để phân loại prompt người dùng thành danh mục và gọi hàm hiển
     * thị tương ứng.
     *
     * @param req Yêu cầu HTTP
     * @param resp Phản hồi HTTP
     * @param map Map chứa prompt từ người dùng
     * @throws Exception Nếu xảy ra lỗi trong quá trình phân loại hoặc xử lý
     * prompt
     */
    private void getSubjectsByPrompt(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> map) throws Exception {
        var catesList = sDAO.getAllCategories();
        var catesListToString = String.join(", ", catesList);
        var prompt = CATEGORY_MATCHING_PROMPT.formatted(catesListToString, catesListToString, map.get("prompt"));

        var body = assistant.preparePrompt(prompt);
        var answer = assistant.getAnswer(resp, body).trim();

        if (!answer.equals("none")) {
            controlPublicSubjectsListScreen(req, resp, answer);
        } else {
            var errorPrompt = assistant.preparePrompt(ERROR_PROMPT.formatted(String.join(", ", sDAO.getAllCategories())));
            SubjectsListSocket.notifyClient("currentEmail", assistant.getAnswer(resp, errorPrompt));
            sendData(resp, HttpServletResponse.SC_ACCEPTED, new HashMap<>());
        }
    }

    /**
     * <h4>Cá nhân hóa danh sách môn học dựa trên lựa chọn của người dùng</h4>
     * Xây dựng prompt dựa trên hồ sơ người dùng và nhờ trợ lý AI gợi ý danh
     * sách môn học phù hợp.
     *
     * @param req Yêu cầu HTTP chứa dữ liệu người dùng
     * @param resp Phản hồi HTTP
     * @param map Map chứa các lựa chọn như mục tiêu học tập, vai trò, trình độ
     * học vấn và chủ đề
     * @throws Exception Nếu có lỗi khi gọi AI hoặc xử lý dữ liệu JSON
     */
    private void customizingUsersSelection(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> map) throws Exception {
        var obj = new ObjectMapper().convertValue(map, CustomizedLearningTargetReq.class);
        List<Subject> s = sDAO.getAllSubjects();
        List<Subject> subjects = new ArrayList<>();
        var step = (int) Math.ceil((double) s.size() / 5);
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
                List<Subject> subList = s.subList(i * 5, Math.min((i + 1) * 5, s.size()));
                var json = mapper.writeValueAsString(subList);
                var prompt = promptTemplate.formatted(obj.learningTarget(), obj.identified(), obj.educationLevel(), obj.selectedTopics(), json);
                subjects.addAll(mapper.readValue(getAnswerFromPrompt(resp, prompt), new TypeReference<>() {
                }));
            }
        } catch (JsonProcessingException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (IOException | URISyntaxException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        logger.log(Level.INFO, "List: {0}", subjects);

        session.setAttribute("subjects", subjects);
        session.setAttribute("subjectsBackup", subjects);

        var cookie = new Cookie("customized", "true");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setPath(req.getContextPath());
        resp.addCookie(cookie);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
    // </editor-fold>

    @Override
    public void run() {
//        try {
//            var mapper = new ObjectMapper();
//            List<Subject> s = sDAO.getAllSubjects();
//            List<Subject> result = new ArrayList<>();
//            var step = (int) Math.ceil((double) s.size() / 5);
//            for (int i = 0; i < step; i++) {
//                List<Subject> subList = s.subList(i * 5, Math.min((i + 1) * 5, s.size()));
//                var prompt = promptTemplate.formatted(subList);
//                result.addAll(mapper.readValue(getAnswerFromPrompt(resp, prompt), new TypeReference<>() {
//                }));
//            }
//            result.forEach(r -> {
//                System.out.println(r.toString());
//            });
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, e.getMessage(), e);
//        }
    }
}