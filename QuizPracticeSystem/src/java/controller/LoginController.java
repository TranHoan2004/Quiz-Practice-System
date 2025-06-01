/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import controller.utils.LoginAttempt;
import dao.AccountDAO;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.logging.Level;
import java.util.logging.Logger;

import model.Account;

/**
 * @author Lenovo
 */
@WebServlet(name = "LoginController", urlPatterns = {"/user/login"})
public class LoginController extends HttpServlet {

    private final AccountDAO ad;

    public LoginController() {
        this.ad = new AccountDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account currentUser = (Account) session.getAttribute("currentUser");
        if (currentUser != null) {
            if (currentUser.getRoleId().equals("3b952226-3886-11f0-98a8-e4a8dfb1bdb7")) {
                response.sendRedirect(""); //Guest
            }
            if (currentUser.getRoleId().equals("3b953cbd-3886-11f0-98a8-e4a8dfb1bdb7")) {
                response.sendRedirect(""); //Customer
            }
            if (currentUser.getRoleId().equals("3b953e41-3886-11f0-98a8-e4a8dfb1bdb7")) {
                response.sendRedirect(""); //Expert
            }
            if (currentUser.getRoleId().equals("3b953efb-3886-11f0-98a8-e4a8dfb1bdb7")) {
                response.sendRedirect(""); //Sale
            }
            if (currentUser.getRoleId().equals("3b953f8d-3886-11f0-98a8-e4a8dfb1bdb7")) {
                response.sendRedirect(""); //Marketing
            }
            if (currentUser.getRoleId().equals("63f7dae0-384c-11f0-9e24-e4a8dfb1bdb7")) {
                response.sendRedirect(request.getContextPath() + "/user/registration"); //Admin
            }
            if (currentUser.getRoleId().equals("c5ae3575-3a5b-11f0-b817-74d713b03787")) {
                response.sendRedirect(request.getContextPath() + "/user/registration"); //Admin
            }
        } else {
            request.getRequestDispatcher("/jsp/common-features/login_account.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String email = request.getParameter("email") != null ? request.getParameter("email").trim() : "";
        String password = request.getParameter("password") != null ? request.getParameter("password").trim() : "";

        HttpSession session = request.getSession();

        // Kiểm tra input rỗng
        if (email.isEmpty() || password.isEmpty()) {
            if (email.isEmpty()) {
                request.setAttribute("emptyEmail", "Email không thể để trống.");
            }
            if (password.isEmpty()) {
                request.setAttribute("emptyPassword", "Mật khẩu không thể để trống.");
            }
            request.getRequestDispatcher("/jsp/common-features/login_account.jsp").forward(request, response);
            return;
        }

        try {
            // Kiểm tra email có đang bị khóa không
            if (LoginAttempt.isBlocked(email)) {
                long remaining = LoginAttempt.getRemainingLockTime(email) / 1000; // giây
                request.setAttribute("error", "Tài khoản đã bị tạm dừng. Vui lòng thử lại sau!");
                request.getRequestDispatcher("/jsp/common-features/login_account.jsp").forward(request, response);
                return;
            }

            // ✅ Kiểm tra email tồn tại
            Account accByEmail = ad.getAccountByEmail(email);
            if (accByEmail == null || accByEmail.getId() == null) {
                request.setAttribute("error", "Tài khoản không tồn tại!");
                request.getRequestDispatcher("/jsp/common-features/login_account.jsp").forward(request, response);
                return;
            }

            //  Kiểm tra email + password
            Account currentUser = ad.findUserByEmailAndPassword(email, password);
            if (currentUser == null || currentUser.getId() == null) {
                //  Sai mật khẩu → tăng số lần sai
                LoginAttempt.loginFailed(email);
                int attempts = LoginAttempt.getAttempts(email);
                String message = "Sai mật khẩu!";
                request.setAttribute("error", message);
                request.getRequestDispatcher("/jsp/common-features/login_account.jsp").forward(request, response);
                return;
            }

            //  Đăng nhập thành công → reset trạng thái login
            LoginAttempt.loginSucceeded(email);
            session.setAttribute("currentUser", currentUser);

            switch (currentUser.getRoleId()) {
                case "c5ae3575-3a5b-11f0-b817-74d713b03787" ->
                        response.sendRedirect(request.getContextPath() + "/user/registration");
                case "3b952226-3886-11f0-98a8-e4a8dfb1bdb7" -> response.sendRedirect("trang-guest");
                case "3b953cbd-3886-11f0-98a8-e4a8dfb1bdb7" -> response.sendRedirect("trang-Customer");
                case "3b953e41-3886-11f0-98a8-e4a8dfb1bdb7" -> response.sendRedirect("trang-Expert");
                case "3b953efb-3886-11f0-98a8-e4a8dfb1bdb7" -> response.sendRedirect("trang-sale");
                case "3b953f8d-3886-11f0-98a8-e4a8dfb1bdb7" -> response.sendRedirect("trang-marketing");
                case "63f7dae0-384c-11f0-9e24-e4a8dfb1bdb7" ->
                        response.sendRedirect(request.getContextPath() + "/user/registration");
                case "b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a21" -> response.sendRedirect("/qps/home");
                default -> response.sendRedirect("error.jsp");
            }

        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("error", "Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/jsp/common-features/login_account.jsp").forward(request, response);
        }
    }
    //Xu ly phan khoa tai khoan

}
