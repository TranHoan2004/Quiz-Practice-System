/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
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

@WebServlet(name = "SubjectDetailController", urlPatterns = {"/user/subject_detail"})
public class SubjectDetailController extends HttpServlet {

    private final SubjectDAO subjectDAO;
    private final TopicDAO topicDAO;
    private final CourseDAO courseDAO;
    private final ContactDAO contactDAO;
    private final AccountDAO accountDAO;
    private final SettingDAO settingDAO;
    private final PricePackageDAO pricePackageDAO;
    private final Logger logger;

    public SubjectDetailController() {
        this.subjectDAO = new SubjectDAO();
        this.topicDAO = new TopicDAO();
        this.courseDAO = new CourseDAO();
        this.contactDAO = new ContactDAO();
        this.accountDAO = new AccountDAO();
        this.settingDAO = new SettingDAO();
        this.pricePackageDAO = new PricePackageDAO();
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String encodedId = request.getParameter("id");  // subjectId (encoded)
        String message = "";

        try {

            // Trường hợp không xoá → Load chi tiết subject
            SubjectDetailDTO dto = buildSubjectDetailDTO(encodedId);
            request.setAttribute("subjectDetail", dto);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            message = e.getMessage();
        }

        request.setAttribute("message", message);
        request.getRequestDispatcher("/jsp/course-features/subject_details.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String deleteType = request.getParameter("deleteType");
        String deleteId = request.getParameter("deleteId");
        String encodedId = request.getParameter("subjectId");

        String action = request.getParameter("action"); // để phân biệt chức năng

        try {
            // ====== THÊM PRICE PACKAGE ======
            if ("addPricePackage".equals(action)) {
                String courseId = request.getParameter("courseId");
                String name = request.getParameter("packageName");
                int duration = Integer.parseInt(request.getParameter("packageDuration"));
                int listPrice = Integer.parseInt(request.getParameter("packagePrice"));
                int salePrice = Integer.parseInt(request.getParameter("packageSalePrice"));
                boolean status = Boolean.parseBoolean(request.getParameter("packageStatus"));

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

                String encodedRedirectId = request.getParameter("id");
                response.sendRedirect(request.getContextPath() + "/user/subject_detail?id=" + encodedRedirectId);
                return;
            }

            if ("addDimension".equals(action)) {
                String name = request.getParameter("dimensionName");         // from input[name=dimensionName]
                String description = request.getParameter("dimensionDescription");
                String encodedSubjectId = request.getParameter("subjectId");
                String subjectId = Encoder.decode(encodedSubjectId);

                Setting setting = Setting.builder()
                        .id(UUID.randomUUID())
                        .value(name)
                        .description(description)
                        .status(true) // mặc định true
                        .build();

                settingDAO.createSettingAndAttachToSubject(setting, subjectId, "Dimension of subject");

                response.sendRedirect(request.getContextPath() + "/user/subject_detail?id=" + encodedSubjectId);
                return;
            }

            if ("editPricePackage".equals(action)) {
                UUID id = UUID.fromString(request.getParameter("packageId"));
                String courseId = request.getParameter("courseId");
                String title = request.getParameter("packageName");
                int duration = Integer.parseInt(request.getParameter("packageDuration"));
                int price = Integer.parseInt(request.getParameter("packagePrice"));
                int salePrice = Integer.parseInt(request.getParameter("packageSalePrice"));
                boolean status = Boolean.parseBoolean(request.getParameter("packageStatus"));

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

                pricePackageDAO.update(pkg); // Bạn cần tạo hàm update() trong DAO nếu chưa có

                response.sendRedirect(request.getContextPath() + "/user/subject_detail?id=" + request.getParameter("id"));
                return;
            }

            // ====== XOÁ DIMENSION / PRICE PACKAGE ======
            if (deleteType != null && deleteId != null && encodedId != null) {
                String subjectId = Encoder.decode(encodedId);

                switch (deleteType) {
                    case "dimension" ->
                        settingDAO.deleteSubjectDimension(deleteId, subjectId, "Dimension of subject");
                    case "pricePackage" ->
                        pricePackageDAO.deleteById(deleteId);
                }

                response.sendRedirect(request.getContextPath() + "/user/subject_detail?id=" + encodedId);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Action failed: " + e.getMessage());
        }

        // fallback
        String fallbackId = request.getParameter("id") != null
                ? request.getParameter("id")
                : request.getParameter("subjectId");

        response.sendRedirect(request.getContextPath() + "/user/subject_detail?id=" + fallbackId);

    }

    private SubjectDetailDTO buildSubjectDetailDTO(String encodedId) throws Exception {
        String subjectId = Encoder.decode(encodedId);
        Subject subject = subjectDAO.getById(subjectId);
        List<Topic> topics = topicDAO.getTopicsBySubjectId(subjectId);
        Course course = null;
        if (!topics.isEmpty()) {
            course = courseDAO.getByTopic(topics.get(0).getId().toString());
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
        List<SubjectDimensionDTO> dimensions = settingDAO.getDimensionsBySubjectIdAndDescription(subjectId, "Dimension of subject");

        List<PricePackage> packages = course != null
                ? pricePackageDAO.getByCourseId(course.getId().toString())
                : new ArrayList<>();

        String courseId = course != null ? course.getId().toString() : null;

        SubjectDetailDTO detail = SubjectDetailDTO.builder()
                .id(Encoder.encode(subject.getId().toString()))
                .name(subject.getName())
                .thumbnailUrl(subject.getThumbnailURL())
                .featured(subject.isFeatureFlag())
                .category(category)
                .description(course != null ? course.getDescription() : "")
                .published(course != null && course.isStatus())
                .owner(ownerName)
                .dimensions(dimensions)
                .pricePackages(packages)
                .courseId(courseId)
                .build();

        return detail;

    }
}
