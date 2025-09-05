package com.qps.application.usecase.user;

import com.qps.infrastructure.service.email.EmailService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserEventListener {
    EmailService eSrv;

    @Value("${web-frontend.url}")
    @NonFinal
    String webUrl;

    @Async
    @EventListener
    public void handleNotificationReadEvent(CreateUserEvent event) throws MessagingException {
        var subject = "[QPS] Chào mừng bạn – Thông tin đăng nhập tài khoản";
        webUrl = webUrl.concat("/signin");
        var content = """
                <html>
                <body style='font-family:sans-serif;'>
                    <div style='max-width:600px;margin:0 auto;padding:20px;border:1px solid #eee;border-radius:6px;background-color:#fff;'>
                        <h2 style='color:#333;'>Chào mừng bạn đến với QPS</h2>
                
                        <p>Xin chào <strong>%s</strong>,</p>
                        <p>Cảm ơn bạn đã đăng ký sử dụng nền tảng của chúng tôi.</p>
                        <p>Dưới đây là <strong>mật khẩu truy cập tạm thời</strong> của bạn:</p>
                        <div style='background-color:#f4f4f4;padding:12px 18px;border-radius:6px;font-family:monospace;font-size:16px;border:1px dashed #ccc;color:#333;'>
                            YourPassword123.
                        </div>
                
                        <p>Bạn có thể đăng nhập tại: <a href='%s'>%s</a></p>
                        <p>Nếu bạn không thực hiện hành động này, vui lòng bỏ qua email.</p>
                        <hr style='margin-top:30px;'>
                        <p style='font-size:12px;color:#888;'>Đây là email tự động. Vui lòng không phản hồi.</p>
                        <p style='font-size:12px;color:#888;'>Mọi hỗ trợ xin liên hệ: <a href='mailto:huongnn2201@gmail.com'>huongnn2201@gmail.com</a></p>
                        <p style='font-size:12px;color:#888;'>QPS Team, Hanoi, Vietnam</p>
                    </div>
                </body>
                </html>
                """.formatted(event.fullName(), webUrl, webUrl);
        eSrv.sendNormalEmail(event.email(), subject, content);
    }
}
