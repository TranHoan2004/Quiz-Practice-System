package controller;

import com.google.gson.Gson;
import controller.utils.HandleRequestBody;
import dao.*;
import dto.PracticeExam;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import utils.Encoder;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "PracticeListController", urlPatterns = {"/user/practice"})
public class PracticeListController extends HttpServlet {

    private final Logger logger;
    private final SubjectDAO sDAO;
    private final PersonalQuizDAO pDAO;
    private final QuizDAO qDAO;
    private final SettingDAO stDAO;
    private final HandleRequestBody hrb;

    public PracticeListController() {
        this.logger = Logger.getLogger(this.getClass().getName());
        this.sDAO = new SubjectDAO();
        this.pDAO = new PersonalQuizDAO();
        this.qDAO = new QuizDAO();
        this.stDAO = new SettingDAO();
        this.hrb = new HandleRequestBody();
    }

    /**
     * <h4>Xử lý GET request để hiển thị danh sách bài luyện tập</h4>
     * - Lấy danh sách bài luyện tập của người dùng đang đăng nhập từ
     * PersonalQuiz.<br>
     * - Nếu có `keyword` hoặc `filter`, áp dụng tìm kiếm và lọc trước khi phân
     * trang.<br>
     * - Gửi dữ liệu đến view `practices_list.jsp`.
     *
     * @param req  request từ client, có thể chứa `keyword`, `filter`, `page`
     * @param resp response để forward đến JSP
     * @throws ServletException nếu xảy ra lỗi khi forward
     * @throws IOException      nếu xảy ra lỗi I/O
     * @author HoanTX
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getHeader("X-Source") != null) {
            logger.log(Level.INFO, "header: {0}", req.getHeader("X-Source"));
            Map<String, String> mapper = new HashMap<>();
            mapper.put("main_title", "Practice");
            mapper.put("items", "Practices List");
            sendData(resp, mapper);
        } else {
            String message = "";
            String keyword = req.getParameter("keyword");
            String filter = req.getParameter("filter");
            try {
                Account account = (Account) req.getSession().getAttribute("currentUser");
                List<PersonalQuiz> quizz = pDAO.getAllByAccount(account == null ? "b283bfb8-397a-11f0-84a1-088fc33f56c7" : account.getId().toString());
                List<PracticeExam> exams = getPracticesList(quizz);
                if (filter == null && keyword == null) {
                    renderExamsPagination(req, exams);
                } else {
                    renderExamsPagination(req, handleSearchAndFilter(exams, keyword, filter));
                }
                req.setAttribute("subjects", sDAO.getAllSubjects());
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                message = e.getMessage();
            }
            req.setAttribute("message", message);
            req.getRequestDispatcher("/jsp/customer-features/practices_list.jsp").forward(req, resp);
        }
    }

    /**
     * <h4>Xử lý DELETE request để xóa một bài luyện tập</h4>
     * Xóa bản ghi luyện tập theo ID được truyền trong request body (đã encode).
     *
     * @param req  HTTP request chứa JSON body với field `id`
     * @param resp HTTP response trả về status 200 nếu thành công, 400 nếu thất
     *             bại
     * @throws IOException nếu xảy ra lỗi I/O
     * @author HoanTX
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> response = hrb.getDataFromRequest(req);
        String id = (String) response.get("id");
        try {
            pDAO.deleteById(Encoder.decode(id));
            resp.setStatus(200);
            resp.getWriter().println("OK");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Bad request");
        }
    }

    /**
     * <h4>Phân trang danh sách bài luyện tập</h4>
     * Chia danh sách `exams` thành các trang, mỗi trang chứa tối đa 10 phần tử.
     * Đặt các thuộc tính để hiển thị lên giao diện.
     *
     * @param request HTTP request có thể chứa tham số `page`
     * @param exams   danh sách bài luyện tập cần phân trang
     * @author HoanTX
     */
    private void renderExamsPagination(HttpServletRequest request, List<PracticeExam> exams) {
        String page = request.getParameter("page");
        int currentPage = (page == null ? 1 : Integer.parseInt(page));

        int startIndex = (currentPage - 1) * 10;
        int endIndex = Math.min(exams.size(), startIndex + 10);

        request.setAttribute("exams", exams.subList(startIndex, endIndex)); // Dữ liệu chính
        request.setAttribute("currentIndex", currentPage); // Trang hiện tại
        request.setAttribute("totalPages", exams.isEmpty() ? 0 : Math.ceil(exams.size() / (double) 10)); // Tính tổng số trang
        request.setAttribute("totalElements", exams.size()); // Tính tổng số bản ghi được ném ra view
    }

    /**
     * <h4>Chuyển danh sách PersonalQuiz thành PracticeExam để hiển thị</h4>
     * Duyệt qua từng bài quiz cá nhân, kết hợp với dữ liệu từ bảng Quiz và
     * Subject để tạo thành PracticeExam.
     *
     * @param quizzes danh sách PersonalQuiz từ DB
     * @return danh sách PracticeExam hiển thị cho người dùng
     * @throws Exception nếu có lỗi khi truy vấn dữ liệu
     * @author HoanTX
     */
    private List<PracticeExam> getPracticesList(List<PersonalQuiz> quizzes) throws Exception {
        List<PracticeExam> exams = new ArrayList<>();
        for (PersonalQuiz quiz : quizzes) {
            Quiz q = qDAO.getById(quiz.getQuizId());
            Subject s = sDAO.getById(q.getSubjectId());
            exams.add(PracticeExam.builder()
                    .id(Encoder.encode(quiz.getId().toString()))
                    .subjectName(s.getName())
                    .examName(q.getTitle())
                    .dateTaken(quiz.getTakenDate().toString())
                    .numberOfCorrectQuestions(quiz.getNumberOfCorrectQuestion())
                    .numberOfQuestions(q.getNumberOfQuestions())
                    .duration(getDuration(q.getNumberOfQuestions()))
                    .moreInformation(getDomains(s.getId().toString()))
                    .build());
        }
        return exams;
    }

    /**
     * <h4>Lấy danh sách tên các lĩnh vực (domains) theo subjectId</h4>
     * Gọi DAO để lấy danh sách `dimension` rồi nối thành chuỗi.
     *
     * @param id subject ID
     * @return chuỗi các lĩnh vực cách nhau bởi dấu phẩy
     * @throws Exception nếu truy vấn lỗi
     * @author HoanTX
     */
    private String getDomains(String id) throws Exception {
        List<String> domains = stDAO.getDimensionBySubject(id);
        return String.join(", ", domains);
    }

    /**
     * <h4>Tính toán thời gian làm bài dựa trên số lượng câu hỏi</h4>
     * Mỗi câu hỏi tương ứng với 90 giây. Format kết quả thành HH:mm:ss.
     *
     * @param numberOfQuestions tổng số câu hỏi của bài thi
     * @return thời lượng làm bài ở định dạng HH:mm:ss
     * @author HoanTX
     */
    private String getDuration(int numberOfQuestions) {
        Duration d = Duration.ofSeconds((numberOfQuestions * 90L));
        LocalTime time = LocalTime.MIDNIGHT.plus(d);
        return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    /**
     * <h4>Tìm kiếm và lọc danh sách PracticeExam</h4>
     * - Nếu có `keyword`, tìm trong tất cả thuộc tính như tên đề thi, môn học,
     * ngày làm,...<br>
     * - Nếu có `filter`, lọc theo tên môn học.<br>
     * Trả về danh sách đã qua xử lý để phân trang.
     *
     * @param exams   danh sách bài luyện tập gốc
     * @param keyword chuỗi tìm kiếm (có thể null)
     * @param filter  chuỗi bộ lọc (có thể null)
     * @return danh sách kết quả sau khi tìm và lọc
     * @author HoanTX
     */
    private List<PracticeExam> handleSearchAndFilter(List<PracticeExam> exams, String keyword, String filter) {
        logger.info("handling search and filter: " + keyword + " " + filter);
        List<PracticeExam> results = new ArrayList<>();
        if (keyword != null) {
            for (PracticeExam exam : exams) {
                if ((exam.getExamName() != null && exam.getExamName().toLowerCase().contains(keyword))
                        || (exam.getSubjectName() != null && exam.getSubjectName().toLowerCase().contains(keyword))
                        || (exam.getMoreInformation() != null && exam.getMoreInformation().toLowerCase().contains(keyword))
                        || String.valueOf(exam.getNumberOfCorrectQuestions()).contains(keyword)
                        || String.valueOf(exam.getNumberOfQuestions()).contains(keyword)
                        || (exam.getDateTaken() != null && exam.getDateTaken().contains(keyword))
                        || (exam.getDuration() != null && exam.getDuration().contains(keyword))) {
                    results.add(exam);
                }
            }
        }
        if (filter != null) {
            for (PracticeExam exam : exams) {
                if (exam.getSubjectName().toLowerCase().equals(filter)) {
                    results.add(exam);
                }
            }
        }
        return results;
    }

    /**
     * <h4>Gửi dữ liệu JSON phản hồi cho client</h4>
     * Dùng Gson để chuyển đổi dữ liệu thành JSON và gửi về client với mã trạng thái HTTP.
     *
     * @param res    Đối tượng phản hồi HTTP
     * @param obj    Các đối tượng cần serialize và gửi dưới dạng JSON
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
}
