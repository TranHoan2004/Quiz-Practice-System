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
import jakarta.servlet.http.HttpSession;
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accessToken = req.getParameter("token");
        boolean status = accessToken.equals(token);
        MagicLinkSocket.notifyClient(email, status);
        req.getRequestDispatcher("/jsp/common-features/verify_token_successfully.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String contentHeader = request.getHeader("Content");
        try {
            if (contentHeader != null) {
                logger.log(Level.INFO, "Header: {0}", contentHeader);
                switch (contentHeader) {
                    case "google_auth" -> authenticateOtp(request, response);
                    case "email" -> getEmail(request, response);
                    case "sent_otp" -> sendSixDigitsCode(response);
                    case "qr" -> handleGoogleAuthenticator(request, response);
                    case "magic_link" -> handleMagicLink(response);
                    default -> validateCode(request, response);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        logger.info("Received PUT request");
        try {
            Map<String, String> params = hrb.getDataFromRequest(req);
            String password = params.get("password");
            aDAO.updatePasswordByEmail(password, email);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("OK");
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    // <editor-fold> desc="Handle 6-digits method"
    private void sendSixDigitsCode(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        createCode();
        sendEmail(email, acc.getFullName(), "Đây là <strong>mã xác thực 6 số</strong> của bạn. Vui lòng nhập mã này để hoàn tất quá trình đăng ký:");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println("OK");
    }

    private void createCode() {
        Random random = new Random();
        int randomInt = 100000 + random.nextInt(900000);
        code = String.valueOf(randomInt);
        logger.info(code);
    }

    private void sendEmail(String email, String name, String text) {
        String content = """
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

    private void validateCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String> params = hrb.getDataFromRequest(req);
        String receivedCode = params.get("code");
        logger.info("Received code: " + receivedCode);
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
    private void handleGoogleAuthenticator(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String qr = createQR(email, req);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println(qr);
        } catch (WriterException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Error creating QR code: " + e.getMessage());
        }
    }

    private String createQR(String email, HttpServletRequest req) throws IOException, WriterException {
        HttpSession session = req.getSession();
        String secret = AuthUtils.generateSecretKey();
        String qrCode = AuthUtils.generateQRCodeBase64(email, secret);
        session.setAttribute("secret", secret);
        return qrCode;
    }

    private void authenticateOtp(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        Map<String, String> params = hrb.getDataFromRequest(req);
        String otpStr = params.get("otp");
        logger.info("Received OTP: " + otpStr);
        String firstPart = otpStr.substring(0, 3);
        String secondPart = otpStr.substring(3);

        String newOtp = String.join(firstPart, " ", secondPart);
        int otp = Integer.parseInt(newOtp);

        String secret = session.getAttribute("secret").toString();

        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        boolean isCodeValid = gAuth.authorize(secret, otp);

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
    private void handleMagicLink(HttpServletResponse resp) throws IOException {
        TokenUtils.setToken(UUID.randomUUID().toString(), 5 * 60 * 1000); // 5 minutes expiration
        token = TokenUtils.getToken();
        logger.info("Received Magic Link: " + token);
        sendEmail(email, acc.getFullName(), "Đây là đường dẫn xác thực. Hãy nhấn vào <a href='" + token + "'>đường dẫn</a>  để xác minh tài khoản của bạn");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println("Magic link sent to your email.");
    }

    private void getEmail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String> params = hrb.getDataFromRequest(req);
        email = params.get("email");
        if (!aDAO.isEmailExist(email)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("This email does not exist in the system.");
        }
        acc = aDAO.getAccountByEmail(email);
        logger.info("Email: " + email);
    }
}
