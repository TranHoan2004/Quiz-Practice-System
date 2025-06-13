/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.LessonDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import model.Lesson;
import utils.Encoder;

/**
 *
 * @author Lenovo
 */
@WebServlet(name = "LessonStatusToggleController", urlPatterns = {"/user/subject_lesson/status-toggle"})
public class LessonStatusToggleController extends HttpServlet {

    private final LessonDAO lessonDAO = new LessonDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String encodedId = request.getParameter("lessonId");
        String encodedCourseId = request.getParameter("courseId"); // ✅ lấy courseId để redirect

        try {
            UUID lessonId = UUID.fromString(Encoder.decode(encodedId));
            Lesson lesson = lessonDAO.getById(lessonId.toString());
            if (lesson != null) {
                boolean newStatus = !lesson.isStatus(); // toggle
                lessonDAO.updateLessonStatus(lessonId, newStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect(request.getContextPath() + "/user/subject_lesson?id=" + encodedCourseId);
    }
}
