package controller;

import com.google.zxing.WriterException;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import controller.utils.HandleRequestBody;
import controller.web_socket.MagicLinkSocket;
import dao.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import utils.AuthUtils;
import utils.MailUtil;
import utils.TokenUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "UserController", urlPatterns = {"/user"})
public class UserController extends HttpServlet {

    private final AccountDAO aDAO;
    private final HandleRequestBody hrb;
    private static String code = "";
    private static String email = "";
    private static Account acc;
    private static String token = "";
    private final Logger logger = Logger.getLogger(UserController.class.getName());

    public UserController() {
        this.aDAO = new AccountDAO();
        this.hrb = new HandleRequestBody();
    }

    /**
     * <h4>Xử lý yêu cầu GET từ phía client.</h4>
     * <p>
     * Phương thức này xác thực mã token được gửi qua URL, so sánh với token hệ
     * thống đã lưu, và gửi kết quả về qua WebSocket.</p>
     *
     * @param req Đối tượng {@link HttpServletRequest} chứa thông tin yêu cầu.
     * @param resp Đối tượng {@link HttpServletResponse} dùng để trả về kết quả.
     * @throws ServletException Nếu xảy ra lỗi Servlet.
     * @throws IOException Nếu xảy ra lỗi I/O.
     * @author HoanTX
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var accessToken = req.getParameter("token");
        var status = accessToken.equals(token);
        MagicLinkSocket.notifyClient(email, status);
        req.getRequestDispatcher("/jsp/common-features/verify_token_successfully.html").forward(req, resp);
    }

    /**
     * <h4>Xử lý yêu cầu POST từ phía client với nhiều loại hành vi khác nhau
     * dựa vào header "Content".</h4>
     * <p>
     * Các hành vi bao gồm: xác thực OTP Google, gửi email, tạo mã xác thực 6
     * số, tạo mã QR, gửi Magic Link, và xác minh mã.</p>
     *
     * @param request Đối tượng {@link HttpServletRequest} chứa dữ liệu gửi từ
     * client.
     * @param response Đối tượng {@link HttpServletResponse} dùng để phản hồi.
     * @author HoanTX
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        var contentHeader = request.getHeader("Content");
        try {
            if (contentHeader != null) {
                logger.log(Level.INFO, "Header: {0}", contentHeader);
                switch (contentHeader) {
                    case "google_auth" ->
                        authenticateOtp(request, response);
                    case "email" ->
                        getEmail(request, response);
                    case "sent_otp" ->
                        sendSixDigitsCode(response);
                    case "qr" ->
                        handleGoogleAuthenticator(request, response);
                    case "magic_link" ->
                        handleMagicLink(response);
                    default ->
                        validateCode(request, response);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * <h4>Xử lý yêu cầu PUT dùng để cập nhật mật khẩu của người dùng.</h4>
     * <p>
     * Lấy mật khẩu từ body request, cập nhật vào database dựa theo email đã
     * lưu.</p>
     *
     * @param req Đối tượng {@link HttpServletRequest} chứa dữ liệu yêu cầu.
     * @param resp Đối tượng {@link HttpServletResponse} dùng để trả về trạng
     * thái.
     * @author HoanTX
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        logger.info("Received PUT request");
        try {
            Map<String, Object> params = hrb.getDataFromRequest(req);
            var password = (String) params.get("password");
            aDAO.updatePasswordByEmail(password, email);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("OK");
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    // <editor-fold> desc="Handle 6-digits method"
    /**
     * <h4>Tạo mã xác thực 6 chữ số và gửi đến email người dùng hiện tại.</h4>
     *
     * @param resp Đối tượng {@link HttpServletResponse} dùng để trả trạng thái
     * thành công.
     * @throws IOException Nếu có lỗi khi gửi phản hồi.
     * @author HoanTX
     */
    private void sendSixDigitsCode(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        createCode();
        sendEmail(email, acc.getFullName(), "Đây là <strong>mã xác thực 6 số</strong> của bạn. Vui lòng nhập mã này để hoàn tất quá trình đăng ký:");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println("OK");
    }

    /**
     * <p>
     * Tạo ngẫu nhiên mã xác thực gồm 6 chữ số và lưu vào biến toàn cục.</p>
     *
     * @author HoanTX
     */
    private void createCode() {
        var random = new Random();
        var randomInt = 100000 + random.nextInt(900000);
        code = String.valueOf(randomInt);
        logger.info(code);
    }

    /**
     * <p>
     * Gửi email đến người dùng với nội dung được chỉ định, bao gồm mã xác thực
     * hoặc đường dẫn magic link.</p>
     *
     * @param email Email người nhận.
     * @param name Tên người nhận (hiển thị trong nội dung email).
     * @param text Nội dung mô tả sẽ hiển thị trước mã xác thực hoặc liên kết.
     * @author HoanTX
     */
    private void sendEmail(String email, String name, String text) {
        var content = """
                <html>
                <body style='font-family:sans-serif;'>
                         <div style='max-width:600px;margin:0 auto;padding:20px;border:1px solid #eee;border-radius:6px;background-color:#fff;'>
                             <h2 style='color:#333;'>Xác thực tài khoản QPS</h2>
                             <p>Xin chào <strong>%s</strong>,</p>
                             <p>Cảm ơn bạn đã đăng ký tài khoản trên nền tảng QPS.</p>
                             <p>%s</p>
                             <div style='background-color:#f4f4f4;padding:12px 18px;border-radius:6px;
                                         font-family:monospace;font-size:24px;border:1px dashed #ccc;
                                         color:#333;text-align:center;letter-spacing:4px;'>
                                 %s
                             </div>
                             <p style='margin-top:20px;'>Mã xác thực có hiệu lực trong 5 phút.</p>
                             <p>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email.</p>
                             <hr style='margin-top:30px;'>
                             <p style='font-size:12px;color:#888;'>Đây là email tự động, vui lòng không phản hồi.</p>
                             <p style='font-size:12px;color:#888;'>Mọi hỗ trợ xin liên hệ:\s
                                 <a href='mailto:huongnn2201@gmail.com'>huongnn2201@gmail.com</a>
                             </p>
                             <p style='font-size:12px;color:#888;'>QPS Team, Hanoi, Vietnam</p>
                         </div>
                     </body>
                     </html>
                """.formatted(name, text, code);
        MailUtil.sendMail(email, "[Mã xác thực] Xác thực tài khoản QPS", content);
    }

    /**
     * <p>
     * Nhận mã từ client và xác minh với mã hệ thống đã lưu.</p>
     *
     * @param req Đối tượng {@link HttpServletRequest} chứa mã xác thực.
     * @param resp Đối tượng {@link HttpServletResponse} trả về trạng thái xác
     * thực.
     * @throws IOException Nếu có lỗi khi gửi phản hồi.
     * @author HoanTX
     */
    private void validateCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> params = hrb.getDataFromRequest(req);
        var receivedCode = params.get("code");
        logger.log(Level.INFO, "Received code: {0}", receivedCode);
        if (receivedCode != null) {
            if (receivedCode.equals(code)) {
                logger.info("Code is valid");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getWriter().println("Invalid code");
            }
        }
    }
    // </editor-fold>

    // <editor-fold> desc="Handle Google Authenticator method"
    /**
     * <p>
     * Xử lý yêu cầu tạo mã QR cho Google Authenticator dựa trên email người
     * dùng.</p>
     *
     * @param req Đối tượng {@link HttpServletRequest}.
     * @param resp Đối tượng {@link HttpServletResponse} trả về ảnh QR dạng
     * base64.
     * @throws IOException Nếu có lỗi I/O.
     * @author HoanTX
     */
    private void handleGoogleAuthenticator(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            var qr = createQR(email, req);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println(qr);
        } catch (WriterException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Error creating QR code: " + e.getMessage());
        }
    }

    /**
     * <p>
     * Tạo mã QR Google Authenticator từ email người dùng và lưu secret vào
     * session.</p>
     *
     * @param email Email người dùng.
     * @param req Đối tượng {@link HttpServletRequest} để lấy session.
     * @return Chuỗi base64 ảnh QR.
     * @throws IOException Nếu lỗi xảy ra trong quá trình sinh mã.
     * @throws WriterException Nếu có lỗi khi tạo mã QR.
     * @author HoanTX
     */
    private String createQR(String email, HttpServletRequest req) throws IOException, WriterException {
        var session = req.getSession();
        var secret = session.getAttribute("secret");
        if (secret == null) {
            secret = AuthUtils.generateSecretKey();
            session.setAttribute("secret", secret);
        }
        var qrCode = AuthUtils.generateQRCodeBase64(email, (String) secret);
        session.setAttribute("secret", secret);
        return qrCode;
    }

    /**
     * <p>
     * Xác thực mã OTP Google Authenticator gửi từ phía client.</p>
     *
     * @param req Đối tượng {@link HttpServletRequest} chứa mã OTP.
     * @param resp Đối tượng {@link HttpServletResponse} trả kết quả xác thực.
     * @throws IOException Nếu có lỗi khi gửi phản hồi.
     * @author HoanTX
     */
    private void authenticateOtp(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var session = req.getSession();
        Map<String, Object> params = hrb.getDataFromRequest(req);
        var otpStr = (String) params.get("otp");
        var otp = Integer.parseInt(otpStr.trim());

        var secret = session.getAttribute("secret").toString();

        var gAuth = new GoogleAuthenticator();
        var isCodeValid = gAuth.authorize(secret, otp);

        if (isCodeValid) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("OK");
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().println("Invalid OTP");
        }
    }
    // </editor-fold>

    // Handle magic link method
    /**
     * <p>
     * Tạo Magic Link với token ngẫu nhiên có thời hạn 5 phút và gửi qua
     * email.</p>
     *
     * @param resp Đối tượng {@link HttpServletResponse} phản hồi thành công cho
     * client.
     * @throws IOException Nếu có lỗi khi gửi email hoặc phản hồi.
     * @author HoanTX
     */
    private void handleMagicLink(HttpServletResponse resp) throws IOException {
        TokenUtils.setToken(UUID.randomUUID().toString(), 5 * 60 * 1000); // 5-minute expiration
        token = TokenUtils.getToken();
        logger.info("Received Magic Link: " + token);
//        var link = "http://localhost:8080/qps/user?token=" + token;
//        sendEmail(email, acc.getFullName(), "Đây là đường dẫn xác thực. Hãy nhấn vào <a href='" + link + "'>đường dẫn</a>  để xác minh tài khoản của bạn");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println("Magic link sent to your email.");
    }

    /**
     * <h4>Lấy địa chỉ email từ request body</h4>
     * <p>
     * Kiểm tra email có tồn tại trong hệ thống không.</p>
     * <p>
     * Nếu tồn tại, lưu email và tài khoản vào biến toàn cục để sử dụng cho các
     * bước xác thực tiếp theo.</p>
     *
     * @param req Đối tượng {@link HttpServletRequest} chứa dữ liệu email.
     * @param resp Đối tượng {@link HttpServletResponse} phản hồi nếu email
     * không tồn tại.
     * @throws IOException Nếu có lỗi khi đọc hoặc ghi dữ liệu.
     * @author HoanTX
     */
    private void getEmail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> params = hrb.getDataFromRequest(req);
        email = (String) params.get("email");
        if (!aDAO.isEmailExist(email)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("This email does not exist in the system.");
        }
        acc = aDAO.getAccountByEmail(email);
        logger.log(Level.INFO, "Email: {0}", email);
    }
}
