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
import java.util.*;
import java.util.logging.Level;
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

    /**
     * <h4>Xử lý GET request cho slider</h4>
     * <p>
     * Phân nhánh xử lý dựa trên tham số `id` và header:
     * </p>
     * <ul>
     *   <li>Nếu có <code>id</code>:
     *     <ul>
     *       <li>Giải mã và tìm slider theo UUID.</li>
     *       <li>Gửi dữ liệu chi tiết slider để hiển thị ở <code>slider_details.jsp</code>.</li>
     *       <li>Nếu không tìm thấy, gán thuộc tính <code>error</code> cho request.</li>
     *     </ul>
     *   </li>
     *   <li>Nếu không có <code>id</code>, nhưng có <code>Type</code> header:
     *     <ul>
     *       <li><b>Type = "keyword"</b>: tìm slider theo từ khóa query param.</li>
     *       <li><b>Type = "filter"</b>: lọc slider theo trạng thái query param.</li>
     *     </ul>
     *   </li>
     *   <li>Nếu không có header <code>Type</code>: trả về toàn bộ danh sách slider.</li>
     *   <li>Nếu có header <code>X-Source</code>: trả về tiêu đề và breadcrumb dạng JSON.</li>
     * </ul>
     *
     * @param req  HTTP request, có thể chứa `id` (để lấy chi tiết slider)
     *             hoặc header `Type` (keyword/filter) hoặc `X-Source` (tiêu đề UI)
     * @param resp HTTP response trả về HTML (forward JSP) hoặc JSON (API)
     * @throws ServletException nếu có lỗi servlet
     * @throws IOException      nếu có lỗi đọc/ghi I/O
     * @author HoanTX
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        var typeHeader = req.getHeader("Type");
        var titleHeader = req.getHeader("X-Source");
        if (titleHeader == null) {
            var id = req.getParameter("id");
            if (id != null) {
                var uuid = UUID.fromString(Encoder.decode(id));
                logger.info("slider id: " + uuid);
                var slider = sDao.getSliderById(uuid);
                if (slider == null) {
                    req.setAttribute("error", "Slider not found");
                } else {
                    req.setAttribute("slider", convertToResponse(slider));
                }
                req.getRequestDispatcher("jsp/marketing-features/slider_details.jsp").forward(req, resp);
            }
            try (var out = resp.getWriter()) {
                var gson = new Gson();

                List<SliderResponse> responses = new ArrayList<>();
                if (typeHeader == null) {
                    List<Slider> s = sDao.getAllSliders();
                    responses = getResponseData(s);
                } else {
                    switch (typeHeader) {
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
        } else {
            logger.log(Level.INFO, "header: {0}", titleHeader);
            Map<String, String> mapper = new HashMap<>();
            mapper.put("main_title", "Slider");
            mapper.put("items", "Sliders List");
            sendData(resp, mapper);
        }
    }

    /**
     * <h4>Xử lý PUT request để cập nhật trạng thái slider</h4>
     * Cập nhật trạng thái `status` cho slider có `id` được truyền vào trong
     * phần thân yêu cầu.
     *
     * @param req  HTTP request chứa id (encoded) và trạng thái mới
     * @param resp HTTP response trả về mã trạng thái thành công
     * @throws IOException nếu có lỗi khi đọc dữ liệu yêu cầu
     * @author HoanTX
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var body = hrb.getDataFromRequest(req);
        var rawId = (String) body.get("id");
        var status = (String) body.get("status");
        var id = UUID.fromString(Encoder.decode(rawId));
        sDao.updateSliderStatus(id, Boolean.parseBoolean(status));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * <h4>Lọc slider theo trạng thái</h4>
     * Trích xuất `status` từ request và gọi DAO để truy xuất slider phù hợp.
     *
     * @param req HTTP request chứa tham số `status`
     * @return danh sách slider có trạng thái tương ứng
     * @author HoanTX
     */
    private List<Slider> filter(HttpServletRequest req) {
        String status = req.getParameter("status");
        logger.info("Filtering by status: " + status);
        return sDao.filterByStatus(status);
    }

    /**
     * <h4>Tìm slider theo từ khóa</h4>
     * Trích xuất `keyword` từ request và gọi DAO để tìm kiếm slider.
     *
     * @param req HTTP request chứa tham số `keyword`
     * @return danh sách slider phù hợp với từ khóa
     * @author HoanTX
     */
    private List<Slider> searchByKeyword(HttpServletRequest req) {
        String keyword = req.getParameter("keyword");
        logger.info("Searching for keyword: " + keyword);
        return sDao.search(keyword);
    }

    /**
     * <h4>Chuyển danh sách Slider sang danh sách SliderResponse</h4>
     * Sử dụng phương thức `convertToResponse` để chuyển đổi từng phần tử.
     *
     * @param s danh sách slider gốc
     * @return danh sách SliderResponse dùng cho frontend/API
     * @author HoanTX
     */
    private List<SliderResponse> getResponseData(List<Slider> s) {
        List<SliderResponse> responses = new ArrayList<>();
        s.forEach(slider -> responses.add(convertToResponse(slider)));
        return responses;
    }

    /**
     * <h4>Chuyển đổi từ Slider sang SliderResponse</h4>
     * Lấy thông tin tác giả từ `AccountDAO` để đưa vào response.
     *
     * @param slider đối tượng slider cần chuyển đổi
     * @return đối tượng SliderResponse đã sẵn sàng gửi ra client
     * @author HoanTX
     */
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

    /**
     * <h4>Gửi dữ liệu JSON phản hồi cho client</h4>
     * Dùng Gson để chuyển đổi dữ liệu thành JSON và gửi về client với mã trạng thái HTTP.
     *
     * @param res Đối tượng phản hồi HTTP
     * @param obj Các đối tượng cần serialize và gửi dưới dạng JSON
     * @throws IOException Nếu xảy ra lỗi khi ghi dữ liệu ra response stream
     */
    private void sendData(HttpServletResponse res, Object... obj) throws IOException {
        res.setContentType("application/json");
        try (PrintWriter out = res.getWriter()) {
            var gson = new Gson();
            res.setStatus(HttpServletResponse.SC_OK);
            out.println(gson.toJson(obj));
        }
    }
}
