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

/**
 * @author HoanTX
 */
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

    /**
     * <h4>Xử lý yêu cầu PUT để hủy đăng ký một khóa học</h4>
     * Nhận ID khóa học (được mã hóa) từ phần thân yêu cầu, giải mã, và xóa khóa
     * học tương ứng với người dùng hiện tại.
     *
     * @param req Đối tượng HttpServletRequest chứa phần thân yêu cầu dưới dạng
     * JSON với key "id"
     * @param resp Đối tượng HttpServletResponse trả về kết quả xử lý
     * @throws IOException nếu xảy ra lỗi đọc/ghi dữ liệu
     * @author HoanTX
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> params = hrb.getDataFromRequest(req);
        String id = Encoder.decode((String) params.get("id"));
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

    /**
     * <h4>Lấy ID của người dùng đang đăng nhập</h4>
     *
     * @param request Đối tượng HttpServletRequest có chứa thông tin session
     * @return ID của người dùng hiện tại (dưới dạng chuỗi UUID), hoặc ID mặc
     * định nếu chưa đăng nhập
     * @author HoanTX
     */
    private String getRecentUser(HttpServletRequest request) {
        Account account = (Account) request.getSession().getAttribute("currentUser");
        return account.getId().toString();
    }
}
