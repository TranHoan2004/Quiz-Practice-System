package controller;

import dao.SubjectDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "PracticeListController", urlPatterns = {"/user/practice"})
public class PracticeListController extends HttpServlet {
    private final Logger logger;
    private final SubjectDAO sDAO;

    public PracticeListController() {
        this.logger = Logger.getLogger(this.getClass().getName());
        this.sDAO = new SubjectDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String message = "";
        try {
            req.setAttribute("subjects", sDAO.getAllSubjects());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            message = e.getMessage();
        }
        req.setAttribute("message", message);
        req.getRequestDispatcher("/jsp/customer-features/practices_list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
