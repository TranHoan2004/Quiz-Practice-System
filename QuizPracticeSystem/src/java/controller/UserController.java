package controller;

import controller.utils.HandleRequestBody;
import dao.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

@WebServlet(name = "UserController", urlPatterns = {"/user"})
public class UserController extends HttpServlet {

    private final AccountDAO aDAO;
    private final HandleRequestBody hrb;
    private static String code = "";
    private static String email = "";
    private final Logger logger = Logger.getLogger(UserController.class.getName());

    public UserController() {
        this.aDAO = new AccountDAO();
        this.hrb = new HandleRequestBody();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sendCode(email, request);
        request.getRequestDispatcher("/jsp/common-features/reset_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        email = request.getParameter("email");
        sendCode(email, request);
        validateCode(request);
        request.getRequestDispatcher("/jsp/common-features/reset_password.jsp").forward(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("hehe");
        Map<String, String> params = hrb.getDataFromRequest(req);
        System.out.println(params.get("password"));
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("OK");
    }

    private void sendCode(String email, HttpServletRequest req) {
        if (email != null && aDAO.isEmailExist(email)) {
//        if (email != null) {
            Random random = new Random();
            int randomInt = 100000 + random.nextInt(900000);
            code = String.valueOf(randomInt);
            logger.info(code);
            req.setAttribute("code", code);
        } else {
            req.setAttribute("error1", "Email is not exist");
        }
    }

    private void validateCode(HttpServletRequest req) {
        if (req.getParameter("1") != null &&
                req.getParameter("2") != null
                && req.getParameter("3") != null
                && req.getParameter("4") != null
                && req.getParameter("5") != null
                && req.getParameter("6") != null) {
            char[] arr = new char[]{
                    req.getParameter("1").charAt(0),
                    req.getParameter("2").charAt(0),
                    req.getParameter("3").charAt(0),
                    req.getParameter("4").charAt(0),
                    req.getParameter("5").charAt(0),
                    req.getParameter("6").charAt(0)
            };
            String randomCode = String.valueOf(arr);
            logger.info(randomCode);
            if (randomCode.equals(code)) {
                logger.info("Code is valid");
                req.setAttribute("reset", "true");
            } else {
                req.setAttribute("error2", "Invalid code");
                req.setAttribute("code", "code");
            }
        }
    }
}
