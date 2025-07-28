# Hướng Dẫn Viết Controller Trong Ứng Dụng Spring Boot

## 🎯 Mục tiêu

- Hướng dẫn tổ chức controller theo kiến trúc **feature-based package**
- Định nghĩa rõ vai trò, giới hạn và nguyên tắc khi viết controller
- Áp dụng **Clean Code** và các best practices của Spring

---

## 📁 1. Tổ Chức Package Theo Tính Năng (Feature-based)

Thay vì tổ chức theo tầng (layer-based), ứng dụng nên tổ chức controller theo **feature** để dễ mở rộng và bảo trì.

📌 Mỗi **feature** sẽ là một module độc lập logic. Các controller **chỉ xử lý request liên quan đến feature đó**.

Ví dụ: tổ chức thành các feature như public features, admin-features, sale-features,...

---

## 📌 2. Vai Trò Của Controller

- **Chỉ tiếp nhận request và trả response** (không chứa logic nghiệp vụ).
- Giao tiếp với `usecase` để xử lý nghiệp vụ.
- Thực hiện validate request (dùng annotation hoặc validator riêng).
- Trả kết quả (dùng `ResponseEntity`, DTO, hoặc `@RestControllerAdvice` để xử lý lỗi chung).

---

## ✅ 3. Nguyên Tắc Code Controller Cơ Bản

### ✅ 3.1. Sử dụng `@RestController` thay cho `@Controller`

```java

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
}
```

### ✅ 3.2. Sử dụng các annotation của swagger để tài liệu hóa endpoint

```java

@Tag(name = "User Controller", description = "Describe")
public class AccountController {
    @GetMapping("/")
    @Operation(
            summary = "Create Attachment",
            description = "Upload File To Server and Create Attachment",
            requestBody =
            @RequestBody(
                    required = true,
                    content =
                    @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "object", implementation = MultipartFile.class))))
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    public ResponseEntity<Account> getUser() {
        return null;
    }

    // Operation định nghĩa 1 phương thức xử lý endpoint, mô tả body truyền về
    // ApiResponse mô tả luồng dữ liệu api trả ra
}
```

### ✅ 3.3. Sử dụng đúng giao thức HTTP cho từng loại yêu cầu:

| Giao thức | Mô tả                    |
|-----------|--------------------------|
| HEAD      | Lấy header               |
| GET       | Lấy dữ liệu              |
| POST      | Tạo mới dữ liệu          |
| PUT       | Cập nhật toàn bộ dữ liệu |
| PATCH     | Cập nhật 1 phần dữ liệu  |
| DELETE    | Xóa dữ liệu              |

### ✅ 3.4. Quy tắc đặt tên endpoint:

1. Dùng danh từ số nhiều để đại diện cho resource (GET /users/{id})
2. Dùng cấu trúc phân cấp URL để thể hiện mối quan hệ (Prefix là tên actor luôn)
   VD:
    ```text
    GET /users/{userId}/orders            # đơn hàng của 1 user
    GET /courses/{courseId}/lessons       # danh sách bài học trong 1 khóa học
    POST /projects/{projectId}/members    # thêm thành viên vào project
    ```
3. Tránh sử dụng từ khóa hành động trong URL (getUser, addUser, deleteUser)
4. Dùng camelCase cho field name, kebab-case cho query param nếu cần
   - Path và query param nên dùng kebab-case (từ viết thường, nối với nhau bằng dấu -, không phải _ hay viết hoa)
   - Field trong JSON nên dùng camelCase:
   ```json
   {
    "fullName": "Nguyễn Văn A",
     "email": "a@example.com"
   }
   ```
5. Sử dụng danh từ đơn giản và dễ hiểu
