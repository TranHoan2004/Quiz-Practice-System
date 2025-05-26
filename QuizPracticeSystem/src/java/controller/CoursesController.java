package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CourseDAO;
import dao.PersonalCourseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.PersonalCourse;
import utils.Encoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "CoursesController", urlPatterns = {"/user/course"})
public class CoursesController extends HttpServlet {
    private final CourseDAO cDAO;
    private final PersonalCourseDAO pcDAO;
    private final Logger logger;

    public CoursesController() {
        cDAO = new CourseDAO();
        pcDAO = new PersonalCourseDAO();
        logger = Logger.getLogger(this.getClass().getName());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> params = getDataFromRequest(req);
        String id = Encoder.decode(params.get("id"));
        System.out.println(id);
        try {
//            pcDAO.deleteByCourseAndAccount(getRecentUser(req), id);
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
    private String getRecentUser(HttpServletRequest request) throws Exception {
        Account account = (Account) request.getSession().getAttribute("account");
        return account != null ? account.getId().toString() : "b283bfb8-397a-11f0-84a1-088fc33f56c7";
//        return pcDAO.getAll();
    }

    private Map<String, String> getDataFromRequest(HttpServletRequest req) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String json = sb.toString();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<>() {
        });
    }
}
