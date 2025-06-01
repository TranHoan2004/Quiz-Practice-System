package controller;

import dao.QuizDAO;
import dao.QuizLevelDAO;
import dao.TopicDAO;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Quiz;
import model.QuizLevel;
import model.Topic;
import model.QuizType;
import model.Subject;

@WebServlet(name = "AddQuizController", urlPatterns = {"/addquiz"})
public class AddQuizController extends HttpServlet {

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

            String subjectId = request.getParameter("subjectId");
            if (subjectId != null && !subjectId.isEmpty()) {
                TopicDAO topicDao = new TopicDAO();
                List<Topic> topicList = topicDao.getTopicsBySubjectId(subjectId);
                request.setAttribute("topicList", topicList);
            }

            request.getRequestDispatcher("/jsp/course-features/add_quiz.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error retrieving data: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String type = request.getParameter("type");
        String level = request.getParameter("level");
        String topicId = request.getParameter("topicId");
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
        quiz.setTopicId(type);
        quiz.setTopicId(topicId);
        

        QuizDAO quizDao = new QuizDAO();
        try {
            quizDao.insertNewQuiz(quiz);
        } catch (Exception ex) {
            Logger.getLogger(AddQuizController.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.sendRedirect("quizzeslist");
    }
}
