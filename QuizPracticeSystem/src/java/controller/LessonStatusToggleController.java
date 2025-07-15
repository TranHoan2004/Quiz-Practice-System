/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.LessonDAO;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Lesson;
import utils.Encoder;

/**
 * <h4>LessonStatusToggleController - Bộ điều khiển thay đổi trạng thái bài học</h4>
 * Controller này xử lý yêu cầu POST để chuyển đổi (toggle) trạng thái kích hoạt (status)
 * của một bài học (lesson) cụ thể trong một khóa học của người dùng.
 * Sau khi cập nhật trạng thái, người dùng sẽ được chuyển hướng về trang danh sách bài học.
 * <h4>Chức năng:</h4>
 * - Nhận `lessonId` và `courseId` từ request.
 * - Giải mã `lessonId`, lấy thông tin bài học từ database.
 * - Đảo ngược trạng thái hiện tại của bài học (từ bật sang tắt và ngược lại).
 * - Cập nhật trạng thái mới vào cơ sở dữ liệu.
 * - Chuyển hướng lại về trang danh sách bài học theo `courseId`.
 *
 * @author TuanKD
 */
@WebServlet(name = "LessonStatusToggleController", urlPatterns = {"/user/subject_lesson/status-toggle"})
public class LessonStatusToggleController extends HttpServlet {

    private final LessonDAO lessonDAO = new LessonDAO();

    /**
     * <h4>doPost - Xử lý yêu cầu chuyển đổi trạng thái bài học</h4>
     * Phương thức này xử lý yêu cầu POST gửi từ client để thay đổi trạng thái
     * của một bài học (kích hoạt hoặc vô hiệu hóa).
     * Nếu thao tác thành công, người dùng sẽ được chuyển hướng lại về trang danh sách bài học.
     *
     * @param request  yêu cầu HTTP chứa lessonId (đã mã hóa) và courseId
     * @param response phản hồi HTTP, dùng để điều hướng sau khi xử lý
     * @throws ServletException nếu có lỗi trong quá trình xử lý servlet
     * @throws IOException      nếu có lỗi I/O xảy ra
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String encodedId = request.getParameter("lessonId");
        String encodedCourseId = request.getParameter("courseId");

        try {
            UUID lessonId = UUID.fromString(Encoder.decode(encodedId));
            Lesson lesson = lessonDAO.getById(lessonId.toString());
            if (lesson != null) {
                boolean newStatus = !lesson.isStatus(); // toggle
                lessonDAO.updateLessonStatus(lessonId, newStatus);
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        response.sendRedirect(request.getContextPath() + "/user/subject_lesson?id=" + encodedCourseId);
    }
}
