/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import dao.QuizDAO;
import dto.QuizDTO;
import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Quiz;

import java.time.LocalDate;

/**
 *
 * @author Huong
 */
@WebServlet(name = "QuizDetailsController", urlPatterns = {"/quiz-detail"})
public class QuizDetailsController extends HttpServlet {

    private final QuizDAO quizDAO = new QuizDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");

        try {
            QuizDTO quiz = quizDAO.getQuizDTOById(id);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>)
                            (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()))
                    .create();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(quiz));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Failed to load quiz detail\"}");
        }
    }
}
