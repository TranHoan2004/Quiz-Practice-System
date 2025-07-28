package com.qps.infrastructure.service.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * <h4>Tiện ích hỗ trợ xác thực bằng Google Authenticator.</h4>
 *
 * <p>Class này cung cấp các phương thức để:</p>
 * <ul>
 *     <li>Tạo khóa bí mật (secret key) cho người dùng</li>
 *     <li>Tạo URL chứa thông tin mã OTP theo chuẩn TOTP</li>
 *     <li>Tạo hình ảnh mã QR từ URL để quét bằng ứng dụng Google Authenticator</li>
 *     <li>Trả về mã QR dưới dạng Base64 để hiển thị trên giao diện web</li>
 * </ul>
 *
 * @author HoanTX
 */
@Slf4j
public class GoogleAuthenticationQRGeneratorUtil {

    /**
     * <h4>Sinh secret key ngẫu nhiên cho người dùng.</h4>
     *
     * <p>Secret này sẽ được dùng để cấu hình Google Authenticator bên phía client.</p>
     *
     * @return Chuỗi secret key (ví dụ: "KJH234AF...")
     */
    public static String generateSecretKey() {
        log.info("Generating secret key for Google Authenticator");
        var gAuth = new GoogleAuthenticator();
        var key = gAuth.createCredentials();
        return key.getKey();
    }

    /**
     * <h4>Tạo URL chứa thông tin định danh của người dùng và secret key để tích hợp vào mã QR.</h4>
     *
     * <p>URL này có định dạng chuẩn TOTP và được Google Authenticator hỗ trợ.</p>
     *
     * @param email  Email người dùng dùng làm định danh.
     * @param secret Secret key đã sinh cho người dùng.
     * @return Chuỗi URL theo chuẩn otpauth.
     */
    public static String getQRBarcodeURL(String email, String secret) {
        log.info("Generating QR barcode URL for email: {}", email);
        var issuer = "QPS";
        return String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                issuer, email, secret, issuer
        );
    }

    /**
     * <h4>Sinh mã QR dưới dạng ảnh từ email và secret key.</h4>
     *
     * @param email  Email người dùng.
     * @param secret Secret key của người dùng.
     * @return Đối tượng {@link BufferedImage} chứa mã QR.
     * @throws WriterException Nếu có lỗi khi tạo mã QR.
     */
    public static BufferedImage generateQRCodeImage(String email, String secret) throws WriterException {
        log.info("Generating QR code image for email: {}", email);
        var barcodeText = getQRBarcodeURL(email, secret);
        var qrCodeWriter = new QRCodeWriter();
        var bitMatrix = qrCodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * Tạo mã QR từ email và secret key, sau đó chuyển đổi thành chuỗi Base64 để render lên HTML.
     *
     * <p>Chuỗi này có thể được gán vào thuộc tính <code>src</code> của thẻ &lt;img&gt; trên giao diện web.</p>
     *
     * @param email  Email người dùng.
     * @param secret Secret key của người dùng.
     * @return Chuỗi base64 đại diện cho ảnh PNG của mã QR.
     * @throws WriterException Nếu có lỗi khi tạo QR code.
     * @throws IOException     Nếu có lỗi khi ghi ảnh vào bộ nhớ.
     */
    public static String generateQRCodeBase64(String email, String secret) throws WriterException, IOException {
        log.info("Generating QR code Base64 for email: {}", email);
        var qrImage = generateQRCodeImage(email, secret);
        var baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", baos);
        var imageBytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
