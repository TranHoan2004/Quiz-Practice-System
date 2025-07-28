package com.qps.infrastructure.service.qr;

import io.github.cdimascio.dotenv.Dotenv;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class VietQRUtil {
    private static final String BANK_CODE = Dotenv.load().get("BANK_CODE");
    private static final String ACCOUNT_NUMBER = Dotenv.load().get("ACCOUNT_NUMBER");

    public static String getQRImage(double amount) {
        String qr = "https://img.vietqr.io/image/" + BANK_CODE + "-" +
                ACCOUNT_NUMBER + "-TEMPLATE.png" +
                "?amount=" + amount;
        try {
            URI url = new URI(qr);
            BufferedImage qrImage = ImageIO.read(url.toURL());
            File output = new File("qr_code.png");
            ImageIO.write(qrImage, "png", output);
            return output.getAbsoluteFile().getName();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
