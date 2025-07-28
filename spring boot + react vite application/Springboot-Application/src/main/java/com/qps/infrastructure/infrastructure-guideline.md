# 🧱 Tầng `infrastructure` trong Clean Architecture

Tầng `infrastructure` chứa toàn bộ phần **kỹ thuật triển khai** mà hệ thống cần để hoạt động:

- Truy cập database (JPA, JDBC, Mongo, Dgraph…)
- Gửi email, lưu file, xác thực token
- Giao tiếp với dịch vụ ngoài (Google Auth, AWS S3, Zalo...)

Tầng này **thực thi các "port" (interface)** được định nghĩa ở tầng `application/port`.

---

## ✅ Nguyên tắc quan trọng

| Quy tắc                                        | Mô tả                                                   |
|------------------------------------------------|---------------------------------------------------------|
| ❌ Không có nghiệp vụ trong tầng này            | Chỉ kỹ thuật, không xử lý logic business                |
| ✅ Spring Bean được sử dụng tại đây             | Có thể dùng `@Component`, `@Service`, `@Repository`,... |
| 🔁 Không gọi `UseCase` hoặc `Domain` trong đây | Tránh tạo dependency ngược từ outer → inner             |

---

### Triển khai ở infrastructure/service

```java

@Service
public class EmailSenderImpl implements EmailSenderPort {

    private final JavaMailSender mailSender;

    public EmailSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
```

**Lưu ý**
- Service ở tầng này chứa các dịch vụ bên ngoài hệ thống, không liên quan tới nghiệp vụ chính
- Được dùng đầy đủ tính năng được Spring cung cấp