package controller;

import dao.QuizDAO;
import dao.QuizLevelDAO;
import dao.QuizTypeDAO;
import dao.SubjectDAO;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Quiz;
import model.QuizLevel;
import model.QuizType;
import model.Subject;

@WebServlet(name = "QuizDetailsOverview", urlPatterns = {"/overview"})
public class QuizDetailsOverview extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String quizId = request.getParameter("id");
        QuizDAO quizDao = new QuizDAO();
        QuizTypeDAO typeQuizDao = new QuizTypeDAO();
        SubjectDAO subjectDao = new SubjectDAO();
        QuizLevelDAO quizLevelDao = new QuizLevelDAO();

        try {
            Quiz quiz = quizDao.getById(quizId);
            List<QuizType> quizTypeList = typeQuizDao.getAllQuizType();
            List<Subject> subjectList = subjectDao.getAllSubjects();
            List<QuizLevel> quizLevelList = quizLevelDao.getAllQuizLevel();

            request.setAttribute("quiz", quiz);
            request.setAttribute("quizTypeList", quizTypeList);
            request.setAttribute("subjectList", subjectList);
            request.setAttribute("quizLevelList", quizLevelList);
            request.getRequestDispatcher("/jsp/course-features/quiz_detail_overview.jsp").forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(QuizDetailsOverview.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        String title = request.getParameter("title");
        String subjectId = request.getParameter("subjectId");
        String level = request.getParameter("level");
        String durationStr = request.getParameter("duration");
        String passRateStr = request.getParameter("passRate");
        String type = request.getParameter("type");
        String description = request.getParameter("description");

        QuizDAO quizDao = new QuizDAO();
        QuizTypeDAO typeQuizDao = new QuizTypeDAO();
        SubjectDAO subjectDao = new SubjectDAO();
        QuizLevelDAO quizLevelDao = new QuizLevelDAO();

        Map<String, String> errors = new HashMap<>();
        int duration = 0;
        float passRate = 0;

        // Kiểm tra các trường không được để trống
        if (title == null || title.trim().isEmpty()) {
            errors.put("title", "Tên không được để trống");
        }
        if (subjectId == null || subjectId.trim().isEmpty()) {
            errors.put("subjectId", "Vui lòng chọn môn học");
        }
        if (level == null || level.trim().isEmpty()) {
            errors.put("level", "Vui lòng chọn cấp độ");
        }
        if (type == null || type.trim().isEmpty()) {
            errors.put("type", "Vui lòng chọn loại bài kiểm tra");
        }
        if (description == null || description.trim().isEmpty()) {
            errors.put("description", "Mô tả không được để trống");
        }

        // Kiểm tra Duration
        try {
            duration = Integer.parseInt(durationStr);
            if (duration <= 0) {
                errors.put("duration", "Thời gian phải lớn hơn 0");
            }
        } catch (NumberFormatException e) {
            errors.put("duration", "Thời gian phải là số hợp lệ");
        }

        // Kiểm tra Pass Rate
        try {
            passRate = Float.parseFloat(passRateStr);
            if (passRate <= 0 || passRate > 100) {
                errors.put("passRate", "Tỷ lệ qua phải từ 1 đến 100");
            }
        } catch (NumberFormatException e) {
            errors.put("passRate", "Tỷ lệ qua phải là số hợp lệ");
        }

        try {
            Quiz quiz = quizDao.getById(idStr);
            List<QuizType> quizTypeList = typeQuizDao.getAllQuizType();
            List<Subject> subjectList = subjectDao.getAllSubjects();
            List<QuizLevel> quizLevelList = quizLevelDao.getAllQuizLevel();

            request.setAttribute("quiz", quiz);
            request.setAttribute("quizTypeList", quizTypeList);
            request.setAttribute("subjectList", subjectList);
            request.setAttribute("quizLevelList", quizLevelList);

            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/jsp/course-features/quiz_detail_overview.jsp").forward(request, response);
                return;
            }

            // Cập nhật dữ liệu nếu hợp lệ
            quiz.setTitle(title);
            quiz.setSubjectId(subjectId);
            quiz.setLevel(level);
            quiz.setDuration(duration);
            quiz.setPassRate(passRate);
            quiz.setType(type);
            quiz.setDescription(description);
            quiz.setUpdatedDate(java.time.LocalDate.now());

            quizDao.updateBasicInfoOfQuiz(quiz);

            request.setAttribute("success", "Cập nhập thành công");
            request.getRequestDispatcher("/jsp/course-features/quiz_detail_overview.jsp").forward(request, response);

        } catch (Exception ex) {
            Logger.getLogger(QuizDetailsOverview.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/jsp/course-features/quiz_detail_overview.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles quiz details overview and updates";
    }
}