/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.utils.HandleRequestBody;
import dao.PersonalSubjectDAO;
import enumerate.SubjectStatus;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.PersonalSubject;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author TranHoan
 */
@WebServlet(urlPatterns = {"/payment"})
public class PaymentController extends HttpServlet {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private static final String BANK_CODE = Dotenv.load().get("BANK_CODE");
    private static final String ACCOUNT_NUMBER = Dotenv.load().get("ACCOUNT_NUMBER");
    private final PersonalSubjectDAO psDAO;

    public PaymentController() {
        psDAO = new PersonalSubjectDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Payment Controller");
        var price = req.getParameter("price");
        var qr = "https://img.vietqr.io/image/" + BANK_CODE + "-" +
                ACCOUNT_NUMBER + "-TEMPLATE.png" +
                "?amount=" + price;
        req.setAttribute("qr", qr);
        req.getRequestDispatcher("/jsp/public-features/payment_qr.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = req.getSession();
        var body = new HandleRequestBody();
        Map<String, Object> map = body.getDataFromRequest(req);
        var status = map.get("status");
        var header = req.getHeader("X-Source");
        var subjectId = session.getAttribute("subjectId");
        var account = (Account) session.getAttribute("currentUser");

        if (header != null && header.equals("subject_register") && Boolean.parseBoolean(status.toString())) {
            psDAO.updateStatus(SubjectStatus.PAID.name(), subjectId.toString(), account.getId().toString());
        }

        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
