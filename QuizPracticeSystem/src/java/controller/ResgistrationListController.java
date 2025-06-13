package controller;

import dao.CourseDAO;
import dao.PersonalCourseDAO;
import dao.SubjectDAO;
import dto.RegistrationDTO;
import dto.StasusPersonalCourseDTO;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Subject;

@WebServlet(name = "ResgistrationListController", urlPatterns = {"/resgistrationList"})
public class ResgistrationListController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        loadResgistrationDtoDataAndCheck(request, response);
        request.getRequestDispatcher("/jsp/sale-features/resgistration_list.jsp").forward(request, response);
    }

    private void loadResgistrationDtoDataAndCheck(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CourseDAO courseDao = new CourseDAO();
        SubjectDAO subjectDao = new SubjectDAO();
        PersonalCourseDAO personalCourseDao = new PersonalCourseDAO();

        try {
            String pageStr = request.getParameter("page");
            int page = (pageStr != null && !pageStr.isEmpty()) ? Integer.parseInt(pageStr) : 1;

            String subject = request.getParameter("subjectId");
            String email = request.getParameter("email");
            String status = request.getParameter("status");
            String validFromStr = request.getParameter("validFrom");
            String validToStr = request.getParameter("validTo");
            String numberOfLine_raw = request.getParameter("numberOfLine");
            String[] selectedColumns = request.getParameterValues("columns");

            LocalDate validFrom = null;
            LocalDate validTo = null;
            try {
                if (validFromStr != null && !validFromStr.isEmpty()) {
                    validFrom = LocalDate.parse(validFromStr);
                }
                if (validToStr != null && !validToStr.isEmpty()) {
                    validTo = LocalDate.parse(validToStr);
                }
                if (validFrom != null && validTo != null && !validFrom.isBefore(validTo)) {
                    request.setAttribute("validateError", "Ngày bắt đầu phải nhỏ hơn ngày kết thúc.");
                }
            } catch (Exception e) {
                request.setAttribute("validateError", "Định dạng ngày tháng không hợp lệ.");
            }

            int totalRegistrationDto = courseDao.getTotalRegistrationDto(subject, status, validFrom, validTo, email);

            // ================= LOGIC ĐÃ SỬA =================
            int numberOfLine = 5; // Giá trị mặc định
            if (numberOfLine_raw != null && !numberOfLine_raw.isEmpty()) {
                try {
                    numberOfLine = Integer.parseInt(numberOfLine_raw);
                    if (numberOfLine <= 0) {
                        request.setAttribute("validateError", "Số lượng bản ghi mỗi trang phải lớn hơn 0.");
                        numberOfLine = 5; // Nếu nhập số âm hoặc 0, quay về mặc định
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("validateError", "Định dạng số lượng bản ghi không hợp lệ.");
                    numberOfLine = 5; // Nếu nhập không phải là số, quay về mặc định
                }
            }

            // Tính toán endPage chính xác
            int endPage = (totalRegistrationDto > 0) ? (totalRegistrationDto / numberOfLine + (totalRegistrationDto % numberOfLine == 0 ? 0 : 1)) : 1;
            // ================= KẾT THÚC SỬA =================

            // Ràng buộc trang hợp lệ
            if (page < 1) page = 1;
            if (page > endPage) page = endPage;

            List<Subject> subjectList = subjectDao.getAllSubjects();
            List<RegistrationDTO> registrationDtoList = courseDao.pagingRegistrationDto(page, numberOfLine, subject, status, validFrom, validTo, email);
            List<StasusPersonalCourseDTO> statusList = personalCourseDao.getStatus();

            boolean[] columnVisibility = new boolean[9];
            if (selectedColumns != null) {
                for (String col : selectedColumns) {
                    switch (col) {
                        case "ID": columnVisibility[0] = true; break;
                        case "Email": columnVisibility[1] = true; break;
                        case "Registration time": columnVisibility[2] = true; break;
                        case "Subject": columnVisibility[3] = true; break;
                        case "Package": columnVisibility[4] = true; break;
                        case "Total cost": columnVisibility[5] = true; break;
                        case "Status": columnVisibility[6] = true; break;
                        case "Valid from": columnVisibility[7] = true; break;
                        case "Valid to": columnVisibility[8] = true; break;
                        case "All":
                            for (int i = 0; i < columnVisibility.length; i++) columnVisibility[i] = true;
                            break;
                    }
                }
            } else {
                for (int i = 0; i < columnVisibility.length; i++) columnVisibility[i] = true;
            }

            request.setAttribute("columnVisibility", columnVisibility);
            request.setAttribute("statusList", statusList);
            request.setAttribute("subjectList", subjectList);
            request.setAttribute("RegistrationDtoList", registrationDtoList);
            request.setAttribute("endPage", endPage);
            request.setAttribute("currentPage", page);
            request.setAttribute("subjectId", subject); // Sửa lại để khớp với tên param
            request.setAttribute("email", email);
            request.setAttribute("status", status);
            request.setAttribute("validFrom", validFromStr);
            request.setAttribute("validTo", validToStr);
            request.setAttribute("numberOfLine", numberOfLine);
            request.setAttribute("selectedColumns", selectedColumns); // Đổi tên để rõ ràng hơn

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi lấy dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/jsp/sale-features/resgistration_list.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Resgistration List Controller";
    }
}