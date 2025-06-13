package utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;

public class AuthUtils {
    private static Logger logger = Logger.getLogger(AuthUtils.class.getName());

    public static String generateSecretKey() {
        logger.info("Generating secret key for Google Authenticator");
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    public static String getQRBarcodeURL(String email, String secret) {
        logger.info("Generating QR barcode URL for email: " + email);
        String issuer = "QPS";
        return String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                issuer, email, secret, issuer
        );
    }

    public static BufferedImage generateQRCodeImage(String email, String secret) throws WriterException {
        logger.info("Generating QR code image for email: " + email);
        String barcodeText = getQRBarcodeURL(email, secret);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    // Trả về QR dưới dạng Base64 để render <img src="data:image/png;base64,...">
    public static String generateQRCodeBase64(String email, String secret) throws WriterException, IOException {
        logger.info("Generating QR code Base64 for email: " + email);
        BufferedImage qrImage = generateQRCodeImage(email, secret);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
