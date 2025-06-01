package controller;

import dao.AccountDAO;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.Account;

@WebServlet("/userProfile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class UserProfileController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String UPLOAD_DIR = "images"; // Thư mục lưu hình ảnh
    private static final Logger logger = Logger.getLogger(UserProfileController.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("currentUser");

        if (account == null) {
            request.setAttribute("error", "Phiên làm việc đã hết hạn. Vui lòng đăng nhập lại.");
            request.getRequestDispatcher("jsp/common-features/login.jsp").forward(request, response);
            return;
        }

        String fullName = request.getParameter("fullName");
        String genderStr = request.getParameter("gender");
        String dobStr = request.getParameter("dob");
        String phoneNumber = request.getParameter("mobile");
        AccountDAO aDao = new AccountDAO();
        boolean hasErrors = false;

        // Xác thực Tên đầy đủ
        if (fullName == null || fullName.trim().isEmpty()) {
            request.setAttribute("fullNameError", "Tên đầy đủ là bắt buộc");
            hasErrors = true;
        } else if (!fullName.matches("^[A-Za-z\\s]+$")) {
            request.setAttribute("fullNameError", "Tên đầy đủ không được chứa ký tự đặc biệt");
            hasErrors = true;
        } else {
            String[] nameWords = fullName.trim().split("\\s+");
            if (nameWords.length < 2) {
                request.setAttribute("fullNameError", "Tên đầy đủ phải chứa ít nhất 2 từ");
                hasErrors = true;
            } else if (Arrays.stream(nameWords).anyMatch(word -> word.length() < 2)) {
                request.setAttribute("fullNameError", "Mỗi từ trong Tên đầy đủ phải có ít nhất 2 ký tự");
                hasErrors = true;
            }
        }

        // Xác thực Giới tính
        int gender = -1;
        if (genderStr == null || genderStr.trim().isEmpty()) {
            request.setAttribute("genderError", "Giới tính là bắt buộc");
            hasErrors = true;
        } else {
            switch (genderStr) {
                case "Male":
                    gender = 0;
                    break;
                case "Female":
                    gender = 1;
                    break;
                case "Other":
                    gender = 2;
                    break;
                default:
                    request.setAttribute("genderError", "Giá trị giới tính không hợp lệ");
                    hasErrors = true;
            }
        }

        // Xác thực Ngày sinh
        LocalDate dob = null;
        if (dobStr == null || dobStr.trim().isEmpty()) {
            request.setAttribute("dobError", "Ngày sinh là bắt buộc");
            hasErrors = true;
        } else {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                dob = LocalDate.parse(dobStr, formatter);
                if (dob.isAfter(LocalDate.now())) {
                    request.setAttribute("dobError", "Ngày sinh không được sau ngày hiện tại");
                    hasErrors = true;
                }
            } catch (Exception e) {
                request.setAttribute("dobError", "Định dạng Ngày sinh không hợp lệ");
                hasErrors = true;
            }
        }

        // Xác thực Số điện thoại
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            request.setAttribute("mobileError", "Số điện thoại là bắt buộc");
            hasErrors = true;
        } else if (!phoneNumber.matches("^0\\d{9}$")) {
            request.setAttribute("mobileError", "Số điện thoại phải có 10 chữ số và bắt đầu bằng 0");
            hasErrors = true;
        } else if (!phoneNumber.equals(account.getPhoneNumber())) {
            try {
                if (aDao.isPhoneNumberExist(phoneNumber)) {
                    request.setAttribute("mobileError", "Số điện thoại đã tồn tại");
                    hasErrors = true;
                }
            } catch (Exception e) {
                request.setAttribute("mobileError", "Lỗi khi kiểm tra tính khả dụng của số điện thoại");
                hasErrors = true;
            }
        }

        // Xử lý việc tải lên hình ảnh
        String imageUrl = account.getImageUrl(); // Mặc định giữ nguyên giá trị cũ
        try {
            Part filePart = request.getPart("imageInput");
            logger.log(Level.INFO, "filePart: {0}", filePart);
            if (filePart != null && filePart.getSize() > 0) {
                String contentType = filePart.getContentType();
                logger.log(Level.INFO, "Loại tệp: {0}, Kích thước: {1}", new Object[]{contentType, filePart.getSize()});
                if (!contentType.startsWith("image/")) {
                    request.setAttribute("imageError", "Vui lòng tải lên một tệp hình ảnh (ví dụ: JPG, PNG).");
                    hasErrors = true;
                } else {
                    String fileName = account.getId() + "_" + System.currentTimeMillis() + ".jpg";
                    String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        if (!uploadDir.mkdir()) {
                            request.setAttribute("imageError", "Không thể tạo thư mục để lưu hình ảnh.");
                            hasErrors = true;
                        }
                    }
                    if (!hasErrors) {
                        String filePath = uploadPath + File.separator + fileName;
                        logger.log(Level.INFO, "Đường dẫn lưu tệp: {0}", filePath);
                        filePart.write(filePath);
                        imageUrl = UPLOAD_DIR + "/" + fileName;
                        logger.log(Level.INFO, "Đã lưu tệp tại: {0}, imageUrl: {1}", new Object[]{filePath, imageUrl});
                    }
                }
            } else if (filePart == null) {
                logger.log(Level.WARNING, "Không có tệp nào được gửi từ imageInput");
            } else {
                logger.log(Level.WARNING, "Tệp được gửi từ imageInput có kích thước 0");
            }
        } catch (IllegalStateException e) {
            logger.log(Level.SEVERE, "Lỗi tải lên tệp: {0}", e.getMessage());
            request.setAttribute("imageError", "Tải lên tệp thất bại: Dung lượng tệp vượt quá giới hạn hoặc lỗi máy chủ. Chi tiết: " + e.getMessage());
            hasErrors = true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Lỗi xử lý hình ảnh: {0}", e.getMessage());
            request.setAttribute("imageError", "Lỗi khi xử lý tải lên hình ảnh: " + e.getMessage());
            hasErrors = true;
        }
        logger.log(Level.INFO, "imageUrl trước khi cập nhật: {0}", imageUrl);
        if (hasErrors) {
            request.getRequestDispatcher("jsp/common-features/edit_profile.jsp").forward(request, response);
            return;
        }

        // Cập nhật thông tin người dùng
        Account accountUpdate = new Account();
        accountUpdate.setId(account.getId());
        accountUpdate.setEmail(account.getEmail());
        accountUpdate.setFullName(fullName);
        accountUpdate.setGender(gender);
        accountUpdate.setDob(dob);
        accountUpdate.setPhoneNumber(phoneNumber);
        accountUpdate.setImageUrl(imageUrl);

        // Cập nhật vào cơ sở dữ liệu
        aDao.updateAccount(accountUpdate);

        // Lưu thông tin tài khoản đã cập nhật vào session
        session.setAttribute("currentUser", accountUpdate);

        // Đặt thông báo thành công và chuyển hướng đến success.jsp
        request.setAttribute("message", "Cập nhật hồ sơ thành công!");
        request.getRequestDispatcher("jsp/common-features/success.jsp").forward(request, response);
    }
}