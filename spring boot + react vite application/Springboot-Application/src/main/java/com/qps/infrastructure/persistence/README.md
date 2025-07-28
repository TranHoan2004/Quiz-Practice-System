## Lưu ý trước khi code

### Cách tổ chức thư mục

Package được tổ chức theo bảng trong cơ sở dữ liệu.
Cụ thể, repository được chia thành các package tương ứng với các bảng (trừ các bảng trung gian xử lý quan hệ M-M):

- account
- blog (gồm blogmedia và blog)
- contact
- ...

### Vị trí 1 số phương thức truy vấn

Một số phương thức mà truy vấn liên quan đến setting và setting type sẽ được chuyển sang package setting

### Chỉnh sửa logic một số phương thức khác

- Trước đây ae hay có thói quen lấy hết các bản ghi để xử lý, nhưng giờ không dùng kiểu đó nữa. Thay vào đó sử dụng các
  phương thức của PaginationAndSortingRepository để phân trang luôn
- Nếu tạo mới thêm phương thức repo, chú ý đến quy tắc đặt tên để tận dụng tối đa khả năng truy vấn tự động của Spring
  Data JPA:
    + <'Dữ liệu trả về'> findBy<Trường 1><'Từ khóa'><Trường 2>... (Tham số)
    + Tên các trường phải ánh xạ chính xác tên thuộc tính của đối tượng Java sở hữu nó
    + Chú ý các từ khóa được phép sử dụng và các toán tử nối như AND, OR, hoặc sắp xếp như Asc, Desc,...

### Các toán tử hỗ trợ trong việc đặt tên phương thức truy vấn:

| JPQL                                                                              | SQL                            | Ex                                                                   |
|-----------------------------------------------------------------------------------|--------------------------------|----------------------------------------------------------------------|
| Not                                                                               | !=, <>                         | findByStatusNot(Status status)                                       |
| Is, Equals                                                                        | = (bỏ qua cũng được)           |                                                                      |
| Between(String X, String Y)                                                       | BETWEEN                        | findByAgeBetween(int min, int max)                                   |
| LessThan, LessThanEqual                                                           | <, <=                          | findByAgeLessThan(int age), findByAgeLessThanEqual(int age)          |
| IsNull, IsNotNull, NotNull                                                        | IS NULL, IS NOT NULL, NOT NULL | findByLastLoginIsNull()                                              |
| Like, StartingWith, EndingWith, Containing                                        | LIKE                           | findByEmailContaining(String keyword)                                |
| In                                                                                | IN (...)                       | findByDepartmentNotIn(List<String> depts)                            |
| True, False                                                                       | TRUE, FALSE                    | findByActiveTrue()                                                   |
| OrderBy                                                                           | ORDER BY                       | findByStatusOrderByCreatedAtDesc(Status status)                      |
| Top, First                                                                        | LIMIT n                        | findTop3ByStatus(Status s)                                           |
| Dùng tên thuộc tính liên kết, ngăn cách bằng gạch dưới <br/>VD: Address chứa city | JOIN                           | findByAddress_City(String city), JOIN address a WHERE a.city = :city |