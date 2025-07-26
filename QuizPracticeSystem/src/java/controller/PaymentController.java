/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.utils.HandleRequestBody;
import dao.AccountDAO;
import dao.PersonalSubjectDAO;
import dao.SubjectDAO;
import dto.PaymentResp;
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
import java.util.logging.Level;
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
    private final SubjectDAO sDAO;
    private final AccountDAO aDAO;

    public PaymentController() {
        this.psDAO = new PersonalSubjectDAO();
        this.sDAO = new SubjectDAO();
        this.aDAO = new AccountDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Payment Controller");
        var ps = getPersonalSubject(req);
        System.out.println("Registered subject: " + ps);
        if (ps != null) {
            try {
                var qr = "https://img.vietqr.io/image/" + BANK_CODE + "-" +
                        ACCOUNT_NUMBER + "-TEMPLATE.png" +
                        "?amount=" + ps.getPrice();
                var subject = sDAO.getById(ps.getSubjectId());
                var account = aDAO.getAccountById(ps.getAccountId());

                var pr = new PaymentResp(
                        subject.getName(), ps.getPackageName(),
                        String.format("%.0f%n", ps.getPrice()), account.getFullName(),
                        account.getEmail()
                );
                req.setAttribute("pr", pr);
                req.setAttribute("qr", qr);
            } catch (Exception e) {
                log.log(Level.WARNING, e.getMessage(), e);
            }
        } else {
            req.setAttribute("pr", null);
        }
        req.getRequestDispatcher("/jsp/public-features/payment_qr.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var body = new HandleRequestBody();
        Map<String, Object> map = body.getDataFromRequest(req);
        var status = map.get("status");
        var header = req.getHeader("X-Source");
        var ps = getPersonalSubject(req);

        if (header != null && header.equals("subject_register") && Boolean.parseBoolean(status.toString()) && ps != null) {
            psDAO.updateStatus(SubjectStatus.PAID.name().toLowerCase(), ps.getSubjectId(), ps.getAccountId());
        }

        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private PersonalSubject getPersonalSubject(HttpServletRequest req) {
        var session = req.getSession();
        var subjectId = session.getAttribute("subjectId");
        var account = (Account) session.getAttribute("currentUser");
        if (account != null) {
            return psDAO.getPersonalSubjectsByAccountAndSubject(account.getId().toString(), subjectId.toString());
        }
        return null;
    }
}
