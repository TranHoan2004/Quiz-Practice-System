package controller;

import dao.QuizDAO;
import dao.QuizLevelDAO;
import dao.QuizTypeDAO;
import dao.SubjectDAO;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.logging.*;

import model.*;

/**
 * <h4>Servlet AddQuizController dùng để xử lý việc thêm mới một bài quiz</h4>
 * - Cung cấp giao diện nhập thông tin quiz thông qua phương thức GET.<br>
 * - Xử lý validate dữ liệu và thêm mới vào cơ sở dữ liệu bằng phương thức POST.<br>
 *
 * @author ThuanND
 */
@WebServlet(name = "AddQuizController", urlPatterns = {"/addquiz"})
public class AddQuizController extends HttpServlet {

    /**
     * <h4>Xử lý HTTP GET: hiển thị form thêm mới quiz</h4>
     * - Truy vấn danh sách Subject, QuizType, QuizLevel từ cơ sở dữ liệu.<br>
     * - Truyền dữ liệu xuống `add_quiz.jsp`.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @throws ServletException nếu có lỗi trong quá trình dispatch
     * @throws IOException      nếu xảy ra lỗi khi gửi phản hồi
     * @author ThuanND
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SubjectDAO subjectDao = new SubjectDAO();
        QuizTypeDAO quizTypeDao = new QuizTypeDAO();
        QuizLevelDAO quizLevelDao = new QuizLevelDAO();

        try {
            List<Subject> subjectList = subjectDao.getAllSubjects();
            List<QuizType> quizTypeList = quizTypeDao.getAllQuizType();
            List<QuizLevel> levelList = quizLevelDao.getAllQuizLevel();

            request.setAttribute("subjectList", subjectList);
            request.setAttribute("quizTypeList", quizTypeList);
            request.setAttribute("levelList", levelList);


            request.getRequestDispatcher("jsp/test-content-features/add_quiz.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error retrieving data: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    /**
     * <h4>Xử lý HTTP POST: thêm mới một bài quiz</h4>
     * - Lấy dữ liệu từ form, kiểm tra hợp lệ (validate).<br>
     * - Nếu có lỗi, hiển thị lại form với thông báo lỗi.<br>
     * - Nếu hợp lệ, lưu quiz mới vào cơ sở dữ liệu và chuyển hướng sang danh sách quiz.
     *
     * @param request  HTTP request chứa dữ liệu form
     * @param response HTTP response
     * @throws ServletException nếu có lỗi trong quá trình xử lý
     * @throws IOException      nếu xảy ra lỗi khi gửi phản hồi
     * @author ThuanND
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String type = request.getParameter("type");
        String level = request.getParameter("level");
        String subjectId = request.getParameter("subject");
        int duration = 0;
        float passRate = 0;
        int numberOfQuestions = 0;

        boolean hasError = false;

        if (title == null || title.trim().isEmpty()) {
            request.setAttribute("titleError", "Title is required.");
            hasError = true;
        }
        if (description == null || description.trim().isEmpty()) {
            request.setAttribute("descriptionError", "Description is required.");
            hasError = true;
        }
        if (type == null || type.trim().isEmpty()) {
            request.setAttribute("typeError", "Type is required.");
            hasError = true;
        }
        if (level == null || level.trim().isEmpty()) {
            request.setAttribute("levelError", "Level is required.");
            hasError = true;
        }

        try {
            duration = Integer.parseInt(request.getParameter("duration"));
            if (duration <= 0) {
                request.setAttribute("durationError", "Duration must be greater than 0.");
                hasError = true;
            }
        } catch (Exception e) {
            request.setAttribute("durationError", "Duration must be a number.");
            hasError = true;
        }

        try {
            passRate = Float.parseFloat(request.getParameter("passRate"));
            if (passRate <= 0 || passRate > 100) {
                request.setAttribute("passRateError", "Pass rate must be between 1 and 100.");
                hasError = true;
            }
        } catch (Exception e) {
            request.setAttribute("passRateError", "Pass rate must be a number.");
            hasError = true;
        }

        try {
            numberOfQuestions = Integer.parseInt(request.getParameter("numberOfQuestions"));
            if (numberOfQuestions < 0) {
                request.setAttribute("numberOfQuestionsError", "Number of questions cannot be negative.");
                hasError = true;
            }
        } catch (Exception e) {
            request.setAttribute("numberOfQuestionsError", "Number of questions must be a number.");
            hasError = true;
        }

        if (hasError) {
            request.setAttribute("title", title);
            request.setAttribute("description", description);
            request.setAttribute("type", type);
            request.setAttribute("level", level);
            request.setAttribute("duration", request.getParameter("duration"));
            request.setAttribute("passRate", request.getParameter("passRate"));
            request.setAttribute("numberOfQuestions", request.getParameter("numberOfQuestions"));

            doGet(request, response);
            return;
        }

        Quiz quiz = new Quiz();
        quiz.setId(UUID.randomUUID());
        quiz.setTitle(title);
        quiz.setDescription(description);
        quiz.setType(type);
        quiz.setLevel(level);
        quiz.setDuration(duration);
        quiz.setPassRate(passRate);
        quiz.setStatus(false);
        quiz.setNumberOfQuestions(numberOfQuestions);
        quiz.setSubjectId(subjectId);


        QuizDAO quizDao = new QuizDAO();
        try {
            quizDao.insertNewQuiz(quiz);
        } catch (Exception ex) {
            Logger.getLogger(AddQuizController.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.sendRedirect("quizzeslist");
    }
}
