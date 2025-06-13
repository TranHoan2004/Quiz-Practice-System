package controller;

import dao.QuizDAO;
import dao.QuizQuestionSourceConfigDAO;
import dao.SettingDAO;
import dao.TopicDAO;
import dto.SourceItemDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Quiz;
import model.QuizQuestionSourceConfig;

@WebServlet(name = "QuizDetailsSettingController", urlPatterns = {"/setting"})
public class QuizDetailsSettingController extends HttpServlet {

    // ... doGet method giữ nguyên như phiên bản trước ...
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String quizId = request.getParameter("quizId");
        String sourceItem = request.getParameter("sourceItem");
        if (sourceItem == null) {
            sourceItem = "topic"; // Mặc định là "topic"
        }

        QuizDAO quizDao = new QuizDAO();
        QuizQuestionSourceConfigDAO quizQuestionSourceDao = new QuizQuestionSourceConfigDAO();
        SettingDAO domainOrGroupDao = new SettingDAO();
        TopicDAO topicDao = new TopicDAO();

        try {
            Quiz quiz = quizDao.getById(quizId);

            HttpSession session = request.getSession();

            // Chỉ lấy từ DB nếu session chưa có hoặc là lần đầu vào trang
            if (session.getAttribute("QuizQuestionSourceConfigList") == null) {
                List<QuizQuestionSourceConfig> quizQuestionSourceConfigList = quizQuestionSourceDao.getAllQuizQuestionSourceConfigByQuizId(quizId);
                session.setAttribute("QuizQuestionSourceConfigList", quizQuestionSourceConfigList);
            }

            // Tải các loại source item
//            List<SourceItemDTO> sourceItemDomainList = domainOrGroupDao.getListSourceItemSetting(domainOrGroupDao.getListDomainOrGroupBySubjectId(quiz.getSubjectId(), "Domain"), "domain");
//            List<SourceItemDTO> sourceItemGroupList = domainOrGroupDao.getListSourceItemSetting(domainOrGroupDao.getListDomainOrGroupBySubjectId(quiz.getSubjectId(), "Group"), "group");
            List<SourceItemDTO> sourceItemTopicList = topicDao.getListSourceItemTopic(topicDao.getTopicsBySubjectId(quiz.getSubjectId()));

            List<SourceItemDTO> sourceItemList = new ArrayList<>();
//            sourceItemList.addAll(sourceItemDomainList);
//            sourceItemList.addAll(sourceItemGroupList);
            sourceItemList.addAll(sourceItemTopicList);

            session.setAttribute("quiz", quiz);
            session.setAttribute("sourceItem", sourceItem);
            session.setAttribute("sourceItemList", sourceItemList);

            request.getRequestDispatcher("/jsp/course-features/quiz_detail_setting.jsp").forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(QuizDetailsSettingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        // === GOM NHÓM CÁC ACTION REDIRECT ĐỂ XỬ LÝ TRƯỚC ===
        if ("back".equals(action) || "overview".equals(action)) {
            Quiz quiz = (Quiz) session.getAttribute("quiz");
            String quizId = (quiz != null) ? quiz.getId().toString() : "";

            // Xóa các thuộc tính khỏi session
            session.removeAttribute("quiz");
            session.removeAttribute("sourceItem");
            session.removeAttribute("QuizQuestionSourceConfigList");
            session.removeAttribute("sourceItemList");

            if ("back".equals(action)) {
                response.sendRedirect("quizzeslist");
            } else { // action là "overview"
                response.sendRedirect("overview?id=" + quizId);
            }
            return; // Rất quan trọng: kết thúc xử lý sau khi redirect
        }

        // Các xử lý còn lại cho save, add, delete, changeSource
        Quiz quiz = (Quiz) session.getAttribute("quiz");
        String previousSourceItem = (String) session.getAttribute("sourceItem");

        List<QuizQuestionSourceConfig> masterConfigList = (List<QuizQuestionSourceConfig>) session.getAttribute("QuizQuestionSourceConfigList");
        if (masterConfigList == null) {
            masterConfigList = new ArrayList<>();
        }

        int totalQuestions = Integer.parseInt(request.getParameter("totalQuestions"));
        quiz.setNumberOfQuestions(totalQuestions);
        session.setAttribute("quiz", quiz);

        masterConfigList.removeIf(config -> config.getSourceType().equals(previousSourceItem));

// 1. Lấy tất cả các mảng tham số ra trước
        String[] configIds = request.getParameterValues("configId[]");
        String[] sourceIds = request.getParameterValues("sourceId[]");
        String[] numberOfQuestionsValues = request.getParameterValues("numberOfQuestions[]");

// 2. KIỂM TRA AN TOÀN: Chỉ xử lý nếu có dữ liệu dòng được gửi lên.
// Nếu configIds là null, nghĩa là không có dòng phân bổ nào trên giao diện, ta bỏ qua khối này.
        if (configIds != null) {
            // 3. KIỂM TRA TÍNH NHẤT QUÁN: Dữ liệu của một dòng phải đầy đủ.
            // Nếu sourceIds hoặc numberOfQuestionsValues là null, nghĩa là dữ liệu gửi lên bị lỗi/thiếu.
            if (sourceIds != null && numberOfQuestionsValues != null
                    && configIds.length == sourceIds.length
                    && configIds.length == numberOfQuestionsValues.length) {

                // Dữ liệu hợp lệ, tiến hành xử lý như logic cũ
                for (int i = 0; i < configIds.length; i++) {
                    if (configIds[i] != null && !configIds[i].isEmpty()) {
                        QuizQuestionSourceConfig config = new QuizQuestionSourceConfig();
                        config.setId(java.util.UUID.fromString(configIds[i]));
                        config.setQuizId(quiz.getId().toString());
                        config.setSourceType(previousSourceItem);
                        config.setSourceId(sourceIds[i]);
                        config.setNumberOfQuestions(Integer.parseInt(numberOfQuestionsValues[i]));
                        masterConfigList.add(config);
                    }
                }
            } else {
                // Ghi log lỗi cho lập trình viên và thông báo cho người dùng
                Logger.getLogger(QuizDetailsSettingController.class.getName()).log(Level.WARNING,
                        "Form data is inconsistent. configIds, sourceIds, or numberOfQuestionsValues have null value or mismatching lengths.");
                request.setAttribute("message", "Lỗi: Dữ liệu gửi đi không hợp lệ, vui lòng kiểm tra lại.");
            }
        }

        switch (action) {
            case "changeSource":
                session.setAttribute("sourceItem", request.getParameter("sourceItem"));
                break;
            case "add":
                String newSourceId = request.getParameter("newSourceId");
                if (newSourceId != null && !newSourceId.isEmpty()) {
                    QuizQuestionSourceConfig newConfig = new QuizQuestionSourceConfig();
                    // *** ĐÃ CẬP NHẬT THEO YÊU CẦU CỦA BẠN ***
                    newConfig.setId(java.util.UUID.randomUUID());

                    newConfig.setQuizId(quiz.getId().toString());
                    newConfig.setSourceType(previousSourceItem);
                    newConfig.setSourceId(newSourceId);
                    newConfig.setNumberOfQuestions(Integer.parseInt(request.getParameter("newNumberOfQuestions")));
                    masterConfigList.add(newConfig);
                }
                break;
            // case "delete" và "save" giữ nguyên như phiên bản trước
            case "delete":
                String idToDelete = request.getParameter("idToDelete");
                if (idToDelete != null) {
                    masterConfigList.removeIf(config -> config.getId().toString().equals(idToDelete));
                }
                break;
            case "save":
                int calculatedTotal = masterConfigList.stream().mapToInt(QuizQuestionSourceConfig::getNumberOfQuestions).sum();
                if (calculatedTotal != totalQuestions) {
                    request.setAttribute("message", "Lỗi: Tổng số câu hỏi (" + totalQuestions + ") không khớp với tổng số câu hỏi đã phân bổ (" + calculatedTotal + ")!");
                } else {
                    try {
                        // *** BỔ SUNG THEO YÊU CẦU ***
                        // 1. Cập nhật ngày sửa đổi cho đối tượng quiz trong session
                        quiz.setUpdatedDate(LocalDate.now());

                        // 2. Khởi tạo QuizDAO và gọi hàm mới để cập nhật quiz
                        QuizDAO quizDao = new QuizDAO();
                        quizDao.updateNumberOfQuestion(quiz);

                        // 3. Các xử lý còn lại cho việc lưu cấu hình câu hỏi giữ nguyên
                        QuizQuestionSourceConfigDAO configDao = new QuizQuestionSourceConfigDAO();
                        configDao.deleteAllQuizQuestionSourceConfigByQuizId(quiz.getId().toString());
                        if (!masterConfigList.isEmpty()) {
                            configDao.insertListQuizQuestionSourceConfig(masterConfigList);
                        }

                        request.setAttribute("message", "Cài đặt đã được lưu thành công!");

                    } catch (Exception e) {
                        request.setAttribute("message", "Đã xảy ra lỗi khi lưu cài đặt!");
                        Logger.getLogger(QuizDetailsSettingController.class.getName()).log(Level.SEVERE, "Save error", e);
                    }
                }
                break;
        }

        session.setAttribute("QuizQuestionSourceConfigList", masterConfigList);
        request.getRequestDispatcher("/jsp/course-features/quiz_detail_setting.jsp").forward(request, response);
    }
}
