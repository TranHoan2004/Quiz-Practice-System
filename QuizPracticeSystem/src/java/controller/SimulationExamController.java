/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import dao.QuizDAO;
import dao.SubjectDAO;
import dto.QuizDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import model.Subject;

/**
 *
 * @author Huong
 */
@WebServlet(name="SimulationExamController", urlPatterns={"/simulation-exams"})
public class SimulationExamController extends HttpServlet {
    private final Logger logger = Logger.getLogger(RegisterController.class.getName());
    private static final String SIMULATION_EXAMS_JSP = "/jsp/customer-features/simulation_exams.jsp";
    private static final String SUCCESS_TYPE = "success";
    private static final String ERROR_TYPE = "error";

    private final QuizDAO quizDAO;
    private final SubjectDAO subjectDAO;

    public SimulationExamController() {
        this.quizDAO = new QuizDAO();
        this.subjectDAO = new SubjectDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        try {
            if (isAjax) {
                handleAjaxRequest(request, response);
            } else {
                loadSimulationExams(request);
                forwardToJsp(request, response);
            }
        } catch (Exception e) {
            logger.severe("Error loading simulation exams: " + e.getMessage());
            if (isAjax) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"Failed to load simulation exams\"}");
            } else {
                setResponseAttributes(request, "Failed to load simulation exams.", ERROR_TYPE);
                forwardToJsp(request, response);
            }
        }
    }

    private void handleAjaxRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String subjectId = request.getParameter("subject");
        String search = request.getParameter("search");
        int page = parseIntOrDefault(request.getParameter("page"), 1);
        int size = parseIntOrDefault(request.getParameter("size"), 10);

        List<QuizDTO> quizzes = quizDAO.pagingQuiz(subjectId, null, search, true, page, size);
        int total = quizDAO.getTotalQuizDto(subjectId, null, search, true);
        int totalPages = Math.max(1, (int) Math.ceil((double) total / size));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) 
            (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()))
        .create();


        // Gửi cả danh sách và tổng số trang
        var result = new java.util.HashMap<String, Object>();
        result.put("quizzes", quizzes);
        result.put("totalPages", totalPages);

        response.getWriter().write(gson.toJson(result));
    }

    private void loadSimulationExams(HttpServletRequest request) throws Exception {
        List<Subject> subjects = this.subjectDAO.getAllSubjects();
        request.setAttribute("subjects", subjects);
    }

    private void setResponseAttributes(HttpServletRequest request, String message, String type) {
        request.setAttribute("message", message);
        request.setAttribute("type", type);
    }

    private void forwardToJsp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(SIMULATION_EXAMS_JSP).forward(request, response);
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

}
