// Trong file controller/QuestionsListController.java

package controller;

import dao.LessonDAO;
import dao.QuestionDAO;
import dao.SettingDAO;
import dao.SubjectDAO;
import dto.QuestionDTO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Thêm import này cho Optional
import model.Lesson;
import model.Setting;
import model.Subject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
@WebServlet(name="QuestionsListController", urlPatterns={"/questionsList"})
public class QuestionsListController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(QuestionsListController.class.getName());
    private static final int PAGE_SIZE = 10; // Kích thước trang mặc định

    private void loadQuestionData(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        SubjectDAO subjectDao = new SubjectDAO();
        LessonDAO lessonDao = new LessonDAO();
        SettingDAO settingDao = new SettingDAO();
        QuestionDAO questionDao = new QuestionDAO();

        try {
            String pageStr = request.getParameter("page");
            int page = (pageStr != null && !pageStr.isEmpty()) ? Integer.parseInt(pageStr) : 1;

            String subjectId = request.getParameter("subjectId");
            String lessonId = request.getParameter("lessonId");
            String dimensionId = request.getParameter("dimensionId");
            String level = request.getParameter("level");
            String statusParam = request.getParameter("status");
            String content = request.getParameter("content");

            Boolean statusFilter = null;
            if ("active".equalsIgnoreCase(statusParam)) {
                statusFilter = true;
            } else if ("inactive".equalsIgnoreCase(statusParam)) {
                statusFilter = false;
            }

            List<Subject> subjectList = subjectDao.getAllSubjects();
            List<Lesson> lessonList = lessonDao.getAllLesson();
            List<Setting> groupList = settingDao.getSettingsByType("Group");
            List<Setting> domainList = settingDao.getSettingsByType("Domain");

            List<Setting> dimensionList = new ArrayList<>(); // Sử dụng ArrayList rõ ràng
            dimensionList.addAll(groupList);
            dimensionList.addAll(domainList);
            
            List<Setting> levelList = settingDao.getSettingsByType("Question Level");

            List<QuestionDTO> questionDtoList = questionDao.pagingQuestion(
                    subjectId, lessonId, dimensionId, level, statusFilter, content, page, PAGE_SIZE
            );

            int totalQuestions = questionDao.getTotalQuestionDto(
                    subjectId, lessonId, dimensionId, level, statusFilter, content
            );

            int endPage = (int) Math.ceil((double) totalQuestions / PAGE_SIZE);
            if (endPage == 0 && totalQuestions > 0) {
                 endPage = 1;
            } else if (endPage == 0 && totalQuestions == 0) {
                 endPage = 1;
            }

            request.setAttribute("subjectList", subjectList);
            request.setAttribute("lessonList", lessonList);
            request.setAttribute("dimensionList", dimensionList);
            request.setAttribute("levelList", levelList);
            request.setAttribute("questionDtoList", questionDtoList);
            request.setAttribute("endPage", endPage);
            request.setAttribute("currentPage", page);

            request.setAttribute("paramSubjectId", subjectId);
            request.setAttribute("paramLessonId", lessonId);
            request.setAttribute("paramDimensionId", dimensionId);
            request.setAttribute("paramLevel", level);
            request.setAttribute("paramStatus", statusParam);
            request.setAttribute("paramContent", content);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Lỗi khi lấy dữ liệu cho danh sách câu hỏi: " + e.getMessage(), e);
            request.setAttribute("error", "Lỗi khi truy xuất dữ liệu: " + e.getMessage());
            // KHÔNG forward TẠI ĐÂY. Hãy để doGET/doPOST xử lý việc forward cuối cùng.
            // Ném lại Exception để doGET/doPOST bắt được và forward đến trang lỗi.
            throw new ServletException("Error in loadQuestionData", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            loadQuestionData(request, response);
            request.getRequestDispatcher("/jsp/test-content-features/questions_list.jsp").forward(request, response);
        } catch (ServletException e) {
            // Bắt lỗi từ loadQuestionData và chuyển hướng đến trang lỗi
            logger.log(Level.SEVERE, "Lỗi trong doGet của QuestionsListController: " + e.getMessage(), e);
            // request.setAttribute("error" đã được đặt trong loadQuestionData)
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        QuestionDAO questionDao = new QuestionDAO();

        try {
            String action = request.getParameter("action");
            String questionId = request.getParameter("questionId");

            if ("toggleStatus".equals(action) && questionId != null && !questionId.isEmpty()) {
                
                               
                List<QuestionDTO> questions = questionDao.pagingQuestion(
                    null, null, null, null, null, null, 1, Integer.MAX_VALUE // Lấy tất cả hoặc một số lượng lớn
                );
                Optional<QuestionDTO> currentQuestionOpt = questions.stream()
                                                            .filter(q -> q.getId().toString().equals(questionId))
                                                            .findFirst();

                if (currentQuestionOpt.isPresent()) {
                    QuestionDTO currentQuestion = currentQuestionOpt.get();
                    boolean newStatus = !currentQuestion.isStatus();
                    questionDao.updateQuestionStatus(questionId, newStatus);
                    logger.log(Level.INFO, "Trạng thái câu hỏi ID {0} đã được chuyển đổi thành {1}", new Object[]{questionId, newStatus});
                } else {
                     logger.log(Level.WARNING, "Không tìm thấy câu hỏi với ID {0} để chuyển đổi trạng thái.", questionId);
                }

            } 

            // Sau khi xử lý action, load lại dữ liệu và forward đến trang JSP chính
            loadQuestionData(request, response);
            request.getRequestDispatcher("/jsp/test-content-features/questions_list.jsp").forward(request, response);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Lỗi trong doPost của QuestionsListController: " + e.getMessage(), e);
            request.setAttribute("error", "Lỗi khi xử lý yêu cầu: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Xử lý liệt kê câu hỏi, tìm kiếm, lọc và chuyển đổi trạng thái/xóa";
    }
}