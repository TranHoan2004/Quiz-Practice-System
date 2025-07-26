package controller;

import dao.*;
import dto.SubjectDetailDTO;
import dto.SubjectDimensionDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.Encoder;
import utils.Validation;

@WebServlet(name = "SubjectDetailController", urlPatterns = {"/subject-detail"})
public class SubjectDetailController extends HttpServlet {

    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final TopicDAO topicDAO = new TopicDAO();
    private final CourseDAO courseDAO = new CourseDAO();
    private final ContactDAO contactDAO = new ContactDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final SettingDAO settingDAO = new SettingDAO();
    private final PricePackageDAO pricePackageDAO = new PricePackageDAO();
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String encodedCourseId = request.getParameter("id");
        String message = (String) request.getAttribute("message");
        if (message == null) {
            message = "";
        }

        if (encodedCourseId == null || encodedCourseId.isBlank()) {
            request.setAttribute("message", "Không tìm thấy thông tin khóa học (id rỗng hoặc null)");
            request.getRequestDispatcher("/jsp/course-features/subject_details.jsp").forward(request, response);
            return;
        }

        try {
            String courseId = Encoder.decode(encodedCourseId);
            String subjectId = subjectDAO.getSubjectIdByCourseId(courseId);
            if (subjectId == null) {
                throw new Exception("Không tìm thấy Subject với courseId: " + courseId);
            }

            String encodedSubjectId = Encoder.encode(subjectId);
            SubjectDetailDTO dto = buildSubjectDetailDTO(encodedSubjectId, courseId);

            request.setAttribute("subjectDetail", dto);
            request.setAttribute("subjectId", encodedSubjectId);
            request.setAttribute("courseId", courseId);
            request.setAttribute("userRole", request.getSession().getAttribute("userRole"));

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            message = "Lỗi khi tải dữ liệu: " + e.getMessage();
        }

        request.setAttribute("message", message);
        request.getRequestDispatcher("/jsp/course-features/subject_details.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String encodedSubjectId = request.getParameter("subjectId");
        String courseId = request.getParameter("courseId");
        String userRole = (String) request.getSession().getAttribute("userRole");

        try {
            if ("addPricePackage".equals(action)) {
                if (!"Admin".equals(userRole)) {
                    response.sendRedirect(request.getContextPath() + "/unauthorized.jsp");
                    return;
                }

                String name = request.getParameter("packageName");
                int duration = Integer.parseInt(request.getParameter("packageDuration"));
                int listPrice = Integer.parseInt(request.getParameter("packagePrice"));
                int salePrice = Integer.parseInt(request.getParameter("packageSalePrice"));
                boolean status = Boolean.parseBoolean(request.getParameter("packageStatus"));

                String checkPrice = Validation.validatePricePackage(listPrice, salePrice);
                if (checkPrice != null) {
                    request.setAttribute("message", checkPrice);
                    request.setAttribute("showModal", "add"); 

                    // Tải lại subjectDetail để render lại giao diện
                    SubjectDetailDTO dto = buildSubjectDetailDTO(encodedSubjectId, courseId);
                    request.setAttribute("subjectDetail", dto);
                    request.setAttribute("subjectId", encodedSubjectId);
                    request.setAttribute("courseId", courseId);
                    request.setAttribute("userRole", request.getSession().getAttribute("userRole"));

                    request.getRequestDispatcher("/jsp/course-features/subject_details.jsp").forward(request, response);
                    return;

                }

                PricePackage pkg = PricePackage.builder()
                        .id(UUID.randomUUID())
                        .courseId(courseId)
                        .title(name)
                        .price(listPrice)
                        .salePrice(salePrice)
                        .accessDuration(duration)
                        .status(status)
                        .description(null)
                        .build();

                pricePackageDAO.create(pkg);
            } else if ("addDimension".equals(action)) {
                if (encodedSubjectId != null) {
                    String subjectId = Encoder.decode(encodedSubjectId);
                    String name = request.getParameter("dimensionName");
                    String description = request.getParameter("dimensionDescription");

                    Setting setting = Setting.builder()
                            .id(UUID.randomUUID())
                            .value(name)
                            .description(description)
                            .status(true)
                            .build();

                    settingDAO.createSettingAndAttachToSubject(setting, subjectId);
                }
            } else if ("editPricePackage".equals(action)) {
                if (!"Admin".equals(userRole)) {
                    response.sendRedirect(request.getContextPath() + "/unauthorized.jsp");
                    return;
                }

                UUID id = UUID.fromString(request.getParameter("packageId"));
                String title = request.getParameter("packageName");
                int duration = Integer.parseInt(request.getParameter("packageDuration"));
                int price = Integer.parseInt(request.getParameter("packagePrice"));
                int salePrice = Integer.parseInt(request.getParameter("packageSalePrice"));
                boolean status = Boolean.parseBoolean(request.getParameter("packageStatus"));

                String checkPrice = Validation.validatePricePackage(price, salePrice);
                if (checkPrice != null) {
                    request.setAttribute("message", checkPrice);
                    request.setAttribute("showModal", "edit");
                }

                PricePackage pkg = PricePackage.builder()
                        .id(id)
                        .courseId(courseId)
                        .title(title)
                        .accessDuration(duration)
                        .price(price)
                        .salePrice(salePrice)
                        .status(status)
                        .description(null)
                        .build();

                pricePackageDAO.update(pkg);
            } else if ("updateOverview".equals(action)) {
                if (encodedSubjectId != null) {
                    String description = request.getParameter("description");
                    boolean published = Boolean.parseBoolean(request.getParameter("subjectStatus"));
                    String courseId1 = request.getParameter("courseId"); // Đã là UUID thật
                    courseDAO.updateCourseOverview(courseId1, description, published);
                }
            }

            // Delete logic - không phụ thuộc action nữa
            String deleteType = request.getParameter("deleteType");
            String deleteId = request.getParameter("deleteId");
            if (deleteType != null && deleteId != null && encodedSubjectId != null && "Admin".equals(userRole)) {
                String subjectId = Encoder.decode(encodedSubjectId);
                switch (deleteType) {
                    case "dimension" ->
                        settingDAO.deleteSubjectDimension(deleteId, subjectId);
                    case "pricePackage" ->
                        pricePackageDAO.deleteById(deleteId);
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            request.setAttribute("message", "Action failed: " + e.getMessage());
        }

        // fallback redirect an toàn
        if (courseId != null) {
            response.sendRedirect(request.getContextPath() + "/subject-detail?id=" + Encoder.encode(courseId));
        } else {
            response.sendRedirect(request.getContextPath() + "/subject-detail");
        }
    }

    private SubjectDetailDTO buildSubjectDetailDTO(String encodedSubjectId, String courseIdParam) throws Exception {
        String subjectId = Encoder.decode(encodedSubjectId);
        Subject subject = subjectDAO.getById(subjectId);
        if (subject == null) {
            throw new Exception("Subject không tồn tại.");
        }

        Course course = null;
        if (courseIdParam != null && !courseIdParam.isBlank()) {
            course = courseDAO.getById(courseIdParam);
        } else {
            List<Topic> topics = topicDAO.getTopicsBySubjectId(subjectId);
            course = !topics.isEmpty()
                    ? courseDAO.getByTopic(topics.getFirst().getId().toString())
                    : courseDAO.getById(subjectDAO.getCourseIdBySubjectId(subjectId));
        }

        String ownerName = "Unknown";
        if (course != null) {
            Contact contact = contactDAO.getById(course.getContact());
            if (contact != null) {
                Account expert = accountDAO.getAccountByEmail(contact.getEmail());
                if (expert != null) {
                    ownerName = expert.getFullName();
                }
            }
        }

        String category = subjectDAO.getCategoryBySubjectId(subjectId);
        List<SubjectDimensionDTO> dimensions = settingDAO.getDimensionsBySubjectId(subjectId);
        List<PricePackage> packages = course != null
                ? pricePackageDAO.getByCourseId(course.getId().toString())
                : new ArrayList<>();

        for (PricePackage pkg : packages) {
            int discount = pkg.getSalePrice();
            int original = pkg.getPrice();
            int finalPrice = original - (original * discount / 100);
            pkg.setSalePrice(finalPrice); // override for display
        }

        return SubjectDetailDTO.builder()
                .id(encodedSubjectId)
                .name(subject.getName())
                .thumbnailUrl(course.getThumbnailUrl())
                .featured(subject.isFeatureFlag())
                .category(category)
                .description(course != null ? course.getDescription() : "")
                .published(course != null && course.isStatus())
                .owner(ownerName)
                .dimensions(dimensions)
                .pricePackages(packages)
                .courseId(course != null ? course.getId().toString() : null)
                .build();
    }
}
