package controller;

import controller.utils.HandleRequestBody;
import dao.PersonalCourseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import utils.Encoder;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

// HoanTX
@WebServlet(name = "CoursesController", urlPatterns = {"/user/course"})
public class CoursesController extends HttpServlet {
    private final HandleRequestBody hrb;
    private final PersonalCourseDAO pcDAO;
    private final Logger logger;

    public CoursesController() {
        hrb = new HandleRequestBody();
        pcDAO = new PersonalCourseDAO();
        logger = Logger.getLogger(this.getClass().getName());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String> params = hrb.getDataFromRequest(req);
        String id = Encoder.decode(params.get("id"));
        try {
            pcDAO.deleteByCourseAndAccount(getRecentUser(req), id);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("OK");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    // Lấy ra các khóa học của người dùng đăng nhập đã đăng ký
    private String getRecentUser(HttpServletRequest request) {
        Account account = (Account) request.getSession().getAttribute("account");
        return account != null ? account.getId().toString() : "b283bfb8-397a-11f0-84a1-088fc33f56c7";
//        return pcDAO.getAll();
    }
}
