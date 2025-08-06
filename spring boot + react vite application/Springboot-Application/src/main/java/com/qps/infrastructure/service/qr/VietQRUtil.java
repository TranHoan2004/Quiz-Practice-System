package com.qps.infrastructure.service.qr;

import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class VietQRUtil {
    @Value("${qr.bank-code}")
    private String BANK_CODE;

    @Value("${qr.account-number}")
    private String ACCOUNT_NUMBER;

    public String getQRImage(double amount) {
        var qr = "https://img.vietqr.io/image/" + BANK_CODE + "-" +
                ACCOUNT_NUMBER + "-TEMPLATE.png" +
                "?amount=" + amount;
        try {
            var url = new URI(qr);
            var qrImage = ImageIO.read(url.toURL());
            var output = new File("qr_code.png");
            ImageIO.write(qrImage, "png", output);
            return output.getAbsoluteFile().getName();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
