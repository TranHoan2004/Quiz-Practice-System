# 📦 `application/usecase` – Application Layer trong Clean Architecture

Thư mục `application/usecase` là nơi khai báo **các luồng nghiệp vụ (Use Case)** của hệ thống. Mỗi use case đại diện cho
một hành vi ứng dụng rõ ràng như: đăng ký khóa học, nộp bài kiểm tra, xem kết quả,...

## 🎯 Vai trò chính

- **Đóng vai trò trung tâm orchestration** (điều phối luồng nghiệp vụ)
- **Không chứa logic nghiệp vụ phức tạp** (logic này nên để trong `domain/service`)
- **Không phụ thuộc Spring**, framework, JPA, hay các adapter bên ngoài
- Gọi tới các **`port` (interface)** để truy cập database, gửi email, phát event...

---

## 🔁 Cách usecase tương tác với các tầng khác

---

## 📌 Quy tắc tổ chức

- Mỗi use case là **một class độc lập**, ví dụ:
    - `SubmitQuizUseCase.java`
    - `RegisterCourseUseCase.java`
    - `ReviewQuizResultUseCase.java`

- Không dùng `@Service` hay `@Component` trong use case — wiring sẽ do config hoặc adapter đảm nhiệm.

---

## 📦 Ví dụ mẫu: `SubmitQuizUseCase`

```java
public class SubmitQuizUseCase {

    private final QuizRepositoryPort quizRepository;
    private final EmailSenderPort emailSender;

    public SubmitQuizUseCase(QuizRepositoryPort quizRepository, EmailSenderPort emailSender) {
        this.quizRepository = quizRepository;
        this.emailSender = emailSender;
    }

    public void execute(SubmitQuizRequest request) {
        Quiz quiz = quizRepository.findById(request.getQuizId());
        quiz.submitAnswers(request.getAnswers());

        quizRepository.save(quiz);

        emailSender.sendResultNotification(request.getUserEmail(), quiz.getScore());
    }
}
```

---

## 🧪 Unit Test dễ dàng

Do usecase không phụ thuộc Spring hoặc hạ tầng, bạn có thể dễ dàng test nó bằng cách mock các port:

```java

@Test
void shouldSendEmailAfterQuizSubmission() {
    QuizRepositoryPort mockRepo = mock(QuizRepositoryPort.class);
    EmailSenderPort mockEmail = mock(EmailSenderPort.class);

    SubmitQuizUseCase usecase = new SubmitQuizUseCase(mockRepo, mockEmail);
    usecase.execute(new SubmitQuizRequest(...));

    verify(mockEmail).sendResultNotification(...);
}
```

---

## 🔌 Giao tiếp với các tầng khác

| Tầng              | Giao tiếp với?                                                         | Mục đích?                                        |
|-------------------|------------------------------------------------------------------------|--------------------------------------------------|
| adapter/          | Adapter gọi usecase qua constructor hoặc DI                            | Nhận input từ controller/websocket               |
| domain/           | Usecase gọi domain model/service để xử lý dữ liệu thô do repo trả về   | Thực hiện logic nghiệp vụ chính                  |
| dto/              | Dùng làm input/output cho usecase                                      | Chuẩn hóa dữ liệu giữa tầng adapter và usecase   |
| infrastructure/   | Usecase gọi các repo                                                   | Truy cập DB, gửi mail, file, event,...           |

Luồng chính: usecase -> ìnfrastructure/persistence -> dữ liệu thô -> domain/service -> kết quả cuối cùng
---

## 🔌 **Lưu ý khi tạo usecase**

**Không được quản lý usecase như 1 bean (Tức là không dùng annotation để đánh dấu)**
Lý do:

1. UseCase không phải thành phần kỹ thuật, nó chỉ là sự phối hợp giữa các logic
2. Nếu quản l như 1 bean thì sẽ phá vỡ tính độc lập với framework
3. Hỏng DI: Vì theo clean architecture, chỉ có thành phần boundary (giao tiếp với bên ngoài) mới được quản lý bởi
   framework, còn các thành phần core thì không. Có nghĩa là trừ adapter, infrastructure, còn lại không được quản lý như 1 bean