/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Account;

/**
 *
 * @author Lenovo
 */
public class LoginAccount extends HttpServlet {

    AccountDAO ad = new AccountDAO();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginAccount</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginAccount at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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
        } else {
            request.getRequestDispatcher("jsp/common-features/loginAccount.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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
            request.getRequestDispatcher("jsp/common-features/loginAccount.jsp").forward(request, response);
            return;
        }

        try {
            // Kiểm tra email có đang bị khóa không
            if (LoginAttemptService.isBlocked(email)) {
                long remaining = LoginAttemptService.getRemainingLockTime(email) / 1000; // giây
                request.setAttribute("error", "Tài khoản đang bị khóa. Vui lòng thử lại sau!");
                request.getRequestDispatcher("jsp/common-features/loginAccount.jsp").forward(request, response);
                return;
            }

            // ✅ Kiểm tra email tồn tại
            Account accByEmail = ad.getAccountByEmail(email);
            if (accByEmail == null || accByEmail.getId() == null) {
                request.setAttribute("error", "Tài khoản không tồn tại!");
                request.getRequestDispatcher("jsp/common-features/loginAccount.jsp").forward(request, response);
                return;
            }

            //  Kiểm tra email + password
            Account currentUser = ad.findUserByEmailAndPassword(email, password);
            if (currentUser == null || currentUser.getId() == null) {
                //  Sai mật khẩu → tăng số lần sai
                LoginAttemptService.loginFailed(email);
                int attempts = LoginAttemptService.getAttempts(email);
                String message = "Sai mật khẩu!";
                request.setAttribute("error", message);
                request.getRequestDispatcher("jsp/common-features/loginAccount.jsp").forward(request, response);
                return;
            }

            //  Đăng nhập thành công → reset trạng thái login
            LoginAttemptService.loginSucceeded(email);
            session.setAttribute("currentUser", currentUser);

            switch (currentUser.getRoleId()) {
                case "3b952226-3886-11f0-98a8-e4a8dfb1bdb7" ->
                    response.sendRedirect("trang-guest");
                case "3b953cbd-3886-11f0-98a8-e4a8dfb1bdb7" ->
                    response.sendRedirect("trang-Customer");
                case "3b953e41-3886-11f0-98a8-e4a8dfb1bdb7" ->
                    response.sendRedirect("trang-Expert");
                case "3b953efb-3886-11f0-98a8-e4a8dfb1bdb7" ->
                    response.sendRedirect("trang-sale");
                case "3b953f8d-3886-11f0-98a8-e4a8dfb1bdb7" ->
                    response.sendRedirect("trang-marketing");
                case "63f7dae0-384c-11f0-9e24-e4a8dfb1bdb7" ->
                    response.sendRedirect(request.getContextPath() + "/user/registration");
                default ->
                    response.sendRedirect("error.jsp");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LoginAccount.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("error", "Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau.");
            request.getRequestDispatcher("jsp/common-features/loginAccount.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    //Xu ly phan khoa tai khoan
    public class LoginAttemptService {

        //so lan nhap tai khoan sai 
        private static final int MAX_ATTEMPT_FIRST = 5;
        private static final int MAX_ATTEMPT_SECOND = 10;
        //thoi gian bi khoa
        private static final long FIRST_LOCK_DURATION = 30_000; //30 giay
        private static final long SECOND_LOCK_DURATION = 30 * 60 * 1000; // 30 phut
        //Map de luu tru thoi gian va tai khoan bi khoa
        private static final Map<String, Integer> attempts = new HashMap<>(); //dem so lan dang nhap sai
        private static final Map<String, Long> lockTime = new HashMap<>(); //luu tru thoi diem bat dau khoa hien tai
        private static final Map<String, Long> lockDuration = new HashMap<>(); //so thoi gian bi khoa
        //check dang nhap sai 
        public static void loginFailed(String email) {
            int currentAttempts = attempts.getOrDefault(email, 0) + 1;
            attempts.put(email, currentAttempts);
            if (currentAttempts == MAX_ATTEMPT_FIRST) {
                lockTime.put(email, System.currentTimeMillis());
                lockDuration.put(email, FIRST_LOCK_DURATION);
            } else if (currentAttempts == MAX_ATTEMPT_SECOND) {
                lockTime.put(email, System.currentTimeMillis());
                lockDuration.put(email, SECOND_LOCK_DURATION);
            }
        }
        //Kiem tra tai khoan co dang bi khoa khong
        public static boolean isBlocked(String email) {
            if (!lockTime.containsKey(email)) {
                return false;
            }

            long elapsed = System.currentTimeMillis() - lockTime.get(email);
            long duration = lockDuration.getOrDefault(email, 0L);

            if (elapsed > duration) {
                lockTime.remove(email);
                lockDuration.remove(email);
                // Không reset số lần sai → tiếp tục tăng cho đến khi người đó đăng nhập đúng
                return false;
            }
            return true;
        }
        //Tinh Thoi Gian Con Lai 
        public static long getRemainingLockTime(String email) {
            if (!lockTime.containsKey(email)) {
                return 0;
            }
            long elapsed = System.currentTimeMillis() - lockTime.get(email);
            long duration = lockDuration.getOrDefault(email, 0L);
            long remaining = duration - elapsed;
            return remaining > 0 ? remaining : 0;
        }
        //Neu nguoi dung dang nhap thanh cong thi xoa tai khoan khoi hashmap
        public static void loginSucceeded(String email) {
            attempts.remove(email);
            lockTime.remove(email);
            lockDuration.remove(email);
        }

        public static int getAttempts(String email) {
            return attempts.getOrDefault(email, 0);
        }
    }

}
