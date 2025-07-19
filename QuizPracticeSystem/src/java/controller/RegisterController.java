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
@WebServlet(name = "RegisterController", urlPatterns = {"/auth/register"})
public class RegisterController extends HttpServlet {

    private final AccountDAO accountDAO;
    private final Logger logger = Logger.getLogger(RegisterController.class.getName());
    private static final String REGISTER_JSP = "/jsp/common-features/register.jsp";
    private static final String SUCCESS_TYPE = "success";
    private static final String ERROR_TYPE = "error";
    private static final String DEFAULT_ROLE = "User";

    public RegisterController() {
        this.accountDAO = new AccountDAO();
    }
    
    @Override 
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        forwardToJsp(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phoneNumber = request.getParameter("phoneNumber");
            int gender;

            try {
                gender = Integer.parseInt(request.getParameter("gender"));
            } catch (NumberFormatException e) {
                setResponseAttributes(request, "Invalid gender format!", ERROR_TYPE);
                forwardToJsp(request, response);
                return;
            }

            String validationMessage = validateAccount(fullName, email, phoneNumber);
            if (validationMessage != null) {
                setResponseAttributes(request, validationMessage, ERROR_TYPE);
            } else if (handleRegister(fullName, gender, phoneNumber, email)) {
                setResponseAttributes(
                        request,
                        "Registration successful! Please check your email to verify.",
                        SUCCESS_TYPE);
            } else {
                setResponseAttributes(request, "Registration failed! Please try again.", ERROR_TYPE);
            }

            forwardToJsp(request, response);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Registration error: {0}", ex.getMessage());
            setResponseAttributes(request,
                    "An error occurred during registration.",
                    ERROR_TYPE);
            forwardToJsp(request, response);
        }
    }

    public String validateAccount(String fullName, String email, String phoneNumber) {
        if (fullName == null || fullName.isEmpty()) {
            return "Full name is required!";
        }
        if (email == null || email.isEmpty()) {
            return "Email is required!";
        }
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return "Phone number is required!";
        }
        if (!utils.Validation.isValidEmail(email)) {
            return "Email is invalid!";
        }
        if (!utils.Validation.isValidVietnamesePhone(phoneNumber)) {
            return "Phone number is invalid!";
        }
        if (accountDAO.isEmailExist(email)) {
            return "Email is already registered!";
        }
        return null;
    }

    private boolean handleRegister(String fullName, int gender, String phoneNumber, String email) {
        String generatedPassword = utils.PasswordUtils.generatePassword(16);

        String html = buildEmailTemplate(fullName, generatedPassword);

        try {
            MailUtil.sendMail(email, "Welcome to QPS – Set up your account", html);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }

        String roleId = accountDAO.getRoleIdByRoleName(DEFAULT_ROLE);
        if (roleId == null) {
            logger.log(Level.SEVERE, "Invalid role_id for role: {0}", DEFAULT_ROLE);
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
                .roleId(accountDAO.getRoleIdByRoleName("User"))
                .build();

        return this.accountDAO.createAccount(account);
    }

    private String buildEmailTemplate(String fullName, String generatedPassword) {
        return "<html><body style='font-family:sans-serif;'>"
                + "<div style='max-width:600px;margin:0 auto;padding:20px;border:1px solid #eee;border-radius:6px;background-color:#fff;'>"
                + "<h2 style='color:#333;'>Chào mừng bạn đến với QPS</h2>"
                + "<p>Xin chào <strong>" + fullName + "</strong>,</p>"
                + "<p>Cảm ơn bạn đã đăng ký sử dụng nền tảng của chúng tôi.</p>"
                + "<p>Dưới đây là <strong>mã truy cập tạm thời</strong> của bạn:</p>"
                + "<div style='background-color:#f4f4f4;padding:12px 18px;border-radius:6px;font-family:monospace;font-size:16px;border:1px dashed #ccc;color:#333;'>"
                + generatedPassword + "</div>"
                + "<p>Bạn có thể đăng nhập tại: <a href='https://qps.vn/login'>https://qps.vn/login</a></p>"
                + "<p>Nếu bạn không thực hiện hành động này, vui lòng bỏ qua email.</p>"
                + "<hr style='margin-top:30px;'>"
                + "<p style='font-size:12px;color:#888;'>Đây là email tự động. Vui lòng không phản hồi.</p>"
                + "<p style='font-size:12px;color:#888;'>Mọi hỗ trợ xin liên hệ: <a href='mailto:huongnn2201@gmail.com'>huongnn2201@gmail.com</a></p>"
                + "<p style='font-size:12px;color:#888;'>QPS Team, Hanoi, Vietnam</p>"
                + "</div></body></html>";
    }

    private void setResponseAttributes(HttpServletRequest request, String message, String type) {
        request.setAttribute("message", message);
        request.setAttribute("type", type);
    }

    private void forwardToJsp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(REGISTER_JSP).forward(request, response);
    }

}
