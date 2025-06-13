/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import utils.MailUtil;

/**
 * @author Huong
 */
@WebServlet(name = "RegisterController", urlPatterns = {"/user/register"})
public class RegisterController extends HttpServlet {

    private final AccountDAO accountDAO;
    private final Logger logger = Logger.getLogger(RegisterController.class.getName());

    public RegisterController() {
        this.accountDAO = new AccountDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/jsp/common-features/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phoneNumber = request.getParameter("phoneNumber");
            int gender = Integer.parseInt(request.getParameter("gender"));

            String message = null;

            if (accountDAO.isEmailExist(email)) {
                message = "Email is already exist!";
            } else if (!utils.Validation.isValidEmail(email)) {
                message = "Email is invalid!";
            } else if (!utils.Validation.isValidVietnamesePhone(phoneNumber)) {
                message = "Phone number is invalid!";
            } else if (handleRegister(fullName, gender, phoneNumber, email)) {
                message = "Register successfully! Please check your email to verify.!";
            } else {
                message = "Register failed!. Please try again.";
            }

            request.setAttribute("message", message);
            request.getRequestDispatcher("/jsp/common-features/register.jsp").forward(request, response);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            request.setAttribute("error", "Đã xảy ra lỗi trong quá trình đăng ký.");
            request.getRequestDispatcher("/jsp/common-features/register.jsp").forward(request, response);
        }
    }

    private boolean handleRegister(String fullName, int gender, String phoneNumber, String email) {
        String generatedPassword = utils.PasswordUtils.generatePassword(16);

        String html = "<html><body style='font-family:sans-serif;'>" +
                "<div style='max-width:600px;margin:0 auto;padding:20px;border:1px solid #eee;border-radius:6px;background-color:#fff;'>" +
                "<h2 style='color:#333;'>Chào mừng bạn đến với QPS</h2>" +
                "<p>Xin chào <strong>" + fullName + "</strong>,</p>" +
                "<p>Cảm ơn bạn đã đăng ký sử dụng nền tảng của chúng tôi.</p>" +
                "<p>Dưới đây là <strong>mã truy cập tạm thời</strong> của bạn:</p>" +
                "<div style='background-color:#f4f4f4;padding:12px 18px;border-radius:6px;font-family:monospace;font-size:16px;border:1px dashed #ccc;color:#333;'>" +
                generatedPassword + "</div>" +
                "<p>Bạn có thể đăng nhập tại: <a href='https://qps.vn/login'>https://qps.vn/login</a></p>" +
                "<p>Nếu bạn không thực hiện hành động này, vui lòng bỏ qua email.</p>" +
                "<hr style='margin-top:30px;'>" +
                "<p style='font-size:12px;color:#888;'>Đây là email tự động. Vui lòng không phản hồi.</p>" +
                "<p style='font-size:12px;color:#888;'>Mọi hỗ trợ xin liên hệ: <a href='mailto:huongnn2201@gmail.com'>huongnn2201@gmail.com</a></p>" +
                "<p style='font-size:12px;color:#888;'>QPS Team, Hanoi, Vietnam</p>" +
                "</div></body></html>";

        try {
            MailUtil.sendMail(email, "Welcome to QPS – Set up your account", html);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }

        Account account = Account.builder()
                .id(UUID.randomUUID())
                .fullName(fullName)
                .email(email)
                .password(utils.Encoder.encode(generatedPassword))
                .gender(gender)
                .status(false)
                .phoneNumber(phoneNumber)
                .createdDate(LocalDate.now())
                .roleId(accountDAO.getRoleIdByRoleName("Admin"))
                .build();
        return this.accountDAO.createAccount(account);
    }

}
