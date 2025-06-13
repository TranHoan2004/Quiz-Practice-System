package controller;

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
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String> response = hrb.getDataFromRequest(req);
        String id = response.get("id");
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

    // Phân trang
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

    private String getDomains(String id) throws Exception {
        List<String> domains = stDAO.getDimensionBySubject(id);
        return String.join(", ", domains);
    }

    private String getDuration(int numberOfQuestions) {
        Duration d = Duration.ofSeconds((numberOfQuestions * 90L));
        LocalTime time = LocalTime.MIDNIGHT.plus(d);
        return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

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
}
