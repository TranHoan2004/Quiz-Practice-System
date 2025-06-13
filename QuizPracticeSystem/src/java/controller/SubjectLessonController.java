/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.*;
import dto.LessonDTO;
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

@WebServlet(name = "LessonListController", urlPatterns = {"/user/subject_lesson"})
public class SubjectLessonController extends HttpServlet {

    private final LessonDAO lessonDAO;
    private final CourseDAO courseDAO;
    private final SettingDAO settingDAO;
    private final Logger logger;
    private final SubjectDAO subjectDAO;

    public SubjectLessonController() {
        this.subjectDAO = new SubjectDAO();
        this.lessonDAO = new LessonDAO();
        this.courseDAO = new CourseDAO();
        this.settingDAO = new SettingDAO();
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String courseId = request.getParameter("id");
        String keyword = request.getParameter("keyword");
        String lessonType = request.getParameter("lessonType");
        String status = request.getParameter("status");
        String message = "";

        try {

            // Lấy danh sách bài học
            List<LessonDTO> lessonDTOs = getAllLessonDTOs(courseId);
            System.out.println("Lesson count: " + lessonDTOs.size());
            // Lọc theo tham số tìm kiếm
            if (keyword != null || lessonType != null || status != null) {
                lessonDTOs = filterLessons(lessonDTOs, keyword, lessonType, status);
            }

            // Phân trang và gửi dữ liệu sang JSP
            renderPagination(request, lessonDTOs);

            // Gửi danh sách category (lesson types) sang JSP
            request.setAttribute("lessonTypes", settingDAO.getLessonTypes());

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            message = e.getMessage();
        }

        request.setAttribute("message", message);
        request.getRequestDispatcher("/jsp/course-features/subject_lesson.jsp").forward(request, response);
    }
    
    private List<LessonDTO> getAllLessonDTOs(String courseId) throws Exception {
        List<LessonDTO> lessonDTOs = new ArrayList<>();

        // TODO: Lấy courseId động theo subject hoặc package nếu cần
        List<Lesson> lessons = lessonDAO.getLessonByCourseId(courseId);
        Map<String, String> lessonTypeMap = settingDAO.getLessonTypeMap(); // id -> value

        for (Lesson lesson : lessons) {
            String typeValue = lessonTypeMap.getOrDefault(lesson.getLessonTypeId(), "Unknown");
            System.out.println("Found lesson: " + lesson.getName() + ", type = " + typeValue);
            LessonDTO dto = LessonDTO.builder()
                    .id(Encoder.encode(lesson.getId().toString()))
                    .title(lesson.getName())
                    .type(typeValue)
                    .active(lesson.isStatus())
                    .build();
            lessonDTOs.add(dto);

        }

        return lessonDTOs;
    }

    private List<LessonDTO> filterLessons(List<LessonDTO> lessons, String keyword, String lessonType, String status) {
        List<LessonDTO> filtered = new ArrayList<>();

        for (LessonDTO lesson : lessons) {
            boolean match = true;

            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = keyword.trim().toLowerCase();
                match &= lesson.getTitle().toLowerCase().contains(kw)
                        || lesson.getType().toLowerCase().contains(kw);
            }

            if (lessonType != null && !lessonType.equalsIgnoreCase("all")) {

                match &= lesson.getType().trim().equalsIgnoreCase(lessonType.trim());
            }

            if (status != null && !status.equalsIgnoreCase("all")) {
                boolean expected = status.equalsIgnoreCase("active");
                match &= lesson.isActive() == expected;
            }

            if (match) {
                filtered.add(lesson);
            }

        }

        return filtered;
    }

    private void renderPagination(HttpServletRequest request, List<LessonDTO> list) {
        int pageSize = 10;
        String pageSizeParam = request.getParameter("pageSize");
        if (pageSizeParam != null && pageSizeParam.matches("\\d+")) {
            pageSize = Integer.parseInt(pageSizeParam);
        }

        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && pageParam.matches("\\d+")) {
            currentPage = Integer.parseInt(pageParam);
        }

        int totalElements = list.size();
        int totalPages = totalElements == 0 ? 1 : (int) Math.ceil((double) totalElements / pageSize);
        currentPage = Math.min(currentPage, totalPages);

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(totalElements, start + pageSize);

        List<LessonDTO> paginated = (start >= end) ? Collections.emptyList() : list.subList(start, end);

        request.setAttribute("pageSize", pageSize);
        request.setAttribute("lessons", paginated);
        request.setAttribute("currentIndex", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalElements", totalElements);
    }
}
