package controller;

import com.google.gson.Gson;
import controller.utils.HandleRequestBody;
import dao.AccountDAO;
import dao.SliderDAO;
import dto.SliderResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.Slider;
import utils.Encoder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@WebServlet(name = "SliderController", urlPatterns = {"/slider"})
public class SliderController extends HttpServlet {
    private final SliderDAO sDao;
    private final AccountDAO aDao;
    private final HandleRequestBody hrb;
    private final Logger logger = Logger.getLogger(SliderController.class.getName());

    public SliderController() {
        this.sDao = new SliderDAO();
        this.aDao = new AccountDAO();
        this.hrb = new HandleRequestBody();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String header = req.getHeader("Type");
        String id = req.getParameter("id");
        if (id != null) {
            UUID uuid = UUID.fromString(Encoder.decode(id));
            logger.info("slider id: " + uuid);
            Slider slider = sDao.getSliderById(uuid);
            if (slider == null) {
                req.setAttribute("error", "Slider not found");
            } else {
                req.setAttribute("slider", convertToResponse(slider));
            }
            req.getRequestDispatcher("jsp/marketing-features/slider_details.jsp").forward(req, resp);
        }
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();

            List<SliderResponse> responses = new ArrayList<>();
            if (header == null) {
                List<Slider> s = sDao.getAllSliders();
                responses = getResponseData(s);
            } else {
                switch (header) {
                    case "keyword" -> responses = getResponseData(searchByKeyword(req));
                    case "filter" -> responses = getResponseData(filter(req));
                }
            }
            logger.info("Responses: " + responses);

            String json = gson.toJson(responses);
            logger.info("json: " + json);

            out.println(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var body = hrb.getDataFromRequest(req);
        var rawId = body.get("id");
        var status = body.get("status");
        var id = UUID.fromString(Encoder.decode(rawId));
        sDao.updateSliderStatus(id, Boolean.parseBoolean(status));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private List<Slider> filter(HttpServletRequest req) {
        String status = req.getParameter("status");
        logger.info("Filtering by status: " + status);
        return sDao.filterByStatus(status);
    }

    private List<Slider> searchByKeyword(HttpServletRequest req) {
        String keyword = req.getParameter("keyword");
        logger.info("Searching for keyword: " + keyword);
        return sDao.search(keyword);
    }

    private List<SliderResponse> getResponseData(List<Slider> s) {
        List<SliderResponse> responses = new ArrayList<>();
        s.forEach(slider -> responses.add(convertToResponse(slider)));
        return responses;
    }

    private SliderResponse convertToResponse(Slider slider) {
        Account a = aDao.getAccountById(slider.getAccountId());
        return SliderResponse.builder()
                .id(Encoder.encode(slider.getId().toString()))
                .title(slider.getTitle())
                .imageUrl(slider.getImageUrl())
                .backlinkUrl(slider.getBacklinkUrl())
                .status(slider.isStatus())
                .author(a.getFullName())
                .note(slider.getNote())
                .build();
    }
}
