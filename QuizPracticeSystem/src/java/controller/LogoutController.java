package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * <h4>LogoutController - Đăng xuất người dùng</h4>
 * <p>Servlet này chịu trách nhiệm hủy session đăng nhập hiện tại của người dùng và chuyển hướng về trang đăng nhập.</p>
 */
@WebServlet(name = "LogoutController", urlPatterns = {"/auth/logout"})
public class LogoutController extends HttpServlet {

    /**
     * <h4>doGet - Thực hiện logout</h4>
     *
     * <p>Hủy session và chuyển về trang login.</p>
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Lấy session hiện tại, nếu có

        if (session != null) {
            session.invalidate(); // Xóa toàn bộ session
        }

        // Chuyển hướng về trang login
        response.sendRedirect(request.getContextPath() + "/auth/login");
    }

    /**
     * Chuyển hướng POST sang GET (nếu có ai cố gửi POST tới logout)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
