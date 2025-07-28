/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.QuestionDTO;
import model.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuestionDAO extends DBContext {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private List<Object> createObject(
            String subjectId,
            String lessonId,
            String dimensionId,
            String level,
            Boolean status, // Sử dụng Boolean để có thể null
            String content,
            StringBuilder sql) {

        List<Object> params = new ArrayList<>();

        // Điều kiện status cần được xử lý cẩn thận hơn để chấp nhận null cho "All"
        if (status != null) {
            sql.append(" AND q.status = ?");
            params.add(status);
        }

        if (subjectId != null && !subjectId.isBlank()) {
            sql.append(" AND q.subject_id = ?");
            params.add(subjectId);
        }

        if (lessonId != null && !lessonId.isBlank()) {
            sql.append(" AND q.lesson_id = ?");
            params.add(lessonId);
        }

        if (dimensionId != null && !dimensionId.isBlank()) {
            sql.append(" AND sq.setting_id = ?"); // Giả định sq là alias cho setting_question
            params.add(dimensionId);
        }

        if (level != null && !level.isBlank()) {
            sql.append(" AND q.level = ?");
            params.add(level);
        }

        if (content != null && !content.isBlank()) {
            sql.append(" AND q.content LIKE ?");
            params.add("%" + content + "%");
        }

        return params;
    }

    public List<QuestionDTO> pagingQuestion(
            String subjectId,
            String lessonId,
            String dimensionId,
            String level,
            Boolean status,
            String content,
            int page,
            int size
    ) throws Exception {

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        // THAY ĐỔI SQL Ở ĐÂY
        var sql = new StringBuilder(
                """
                        SELECT
                            q.id,
                            q.content,
                            q.level,
                            q.subject_id,
                            sub.name AS subject_name,
                            q.lesson_id,
                            les.name AS lesson_name,
                            q.status,
                            q.explanation,
                            s.id AS dimension_id,      -- Sẽ là NULL nếu không có dimension phù hợp
                            s.value AS dimension_name  -- Sẽ là NULL nếu không có dimension phù hợp
                        FROM question q
                        JOIN subject sub ON q.subject_id = sub.id
                        LEFT JOIN lesson les ON q.lesson_id = les.id
                        LEFT JOIN setting_question sq ON q.id = sq.question_id
                        -- Di chuyển điều kiện vào mệnh đề ON của LEFT JOIN
                        -- Bằng cách này, chúng ta chỉ join với setting và settingtype nếu type là 'Group' hoặc 'Domain'
                        -- Nếu không, question vẫn được giữ lại nhưng các cột của s và st sẽ là NULL.
                        LEFT JOIN setting s ON sq.setting_id = s.id
                        LEFT JOIN settingtype st ON s.setting_type_id = st.id AND st.name IN ('Group', 'Domain')
                        WHERE 1=1
                        """
                // Lưu ý: Bây giờ chúng ta cần kiểm tra xem dimension_id có được lọc không.
                // Nếu có, chúng ta cần đảm bảo rằng chúng ta đang lọc trên các kết quả đã được join.
                // Đoạn code dưới đây giả định rằng createObject() sẽ thêm "AND s.id = ?" nếu dimensionId được cung cấp.
        );


        // Gọi hàm tạo điều kiện WHERE và danh sách tham số
        List<Object> params = createObject(subjectId, lessonId, dimensionId, level, status, content, sql);

        // Thêm phân trang
        sql.append(" ORDER BY q.id LIMIT ? OFFSET ?");
        params.add(size);
        params.add((page - 1) * size);

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pre.setObject(i + 1, params.get(i));
            }

            try (var rs = pre.executeQuery()) {
                // Map kết quả sang DTO
                while (rs.next()) {
                    questionDTOList.add(QuestionDTO.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .content(rs.getString("content"))
                            .level(rs.getString("level"))
                            .subjectId(rs.getString("subject_id"))
                            .subjectName(rs.getString("subject_name"))
                            .lessonId(rs.getString("lesson_id"))
                            .lessonName(rs.getString("lesson_name"))
                            .status(rs.getBoolean("status"))
                            .explanation(rs.getString("explanation"))
                            .dimensionId(rs.getString("dimension_id")) // Có thể là null
                            .dimensionName(rs.getString("dimension_name")) // Có thể là null
                            .build());
                }
            }
        }
        return questionDTOList;
    }

    public int getTotalQuestionDto(
            String subjectId,
            String lessonId,
            String dimensionId,
            String level,
            Boolean status,
            String content) {

        var sql = new StringBuilder(
                """
                        SELECT COUNT(DISTINCT q.id)
                        FROM question q
                        LEFT JOIN setting_question sq ON q.id = sq.question_id
                        LEFT JOIN setting s ON sq.setting_id = s.id -- Cần join với setting để lọc theo dimension_id
                        WHERE 1=1
                        """
        );

        List<Object> params = createObject(subjectId, lessonId, dimensionId, level, status, content, sql);

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pre.setObject(i + 1, params.get(i));
            }

            try (var rs = pre.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return 0;
    }

    // PHƯƠNG THỨC MỚI ĐỂ CẬP NHẬT TRẠNG THÁI CÂU HỎI
    public void updateQuestionStatus(String questionId, boolean newStatus) throws Exception {
        var sql = "UPDATE question SET status = ? WHERE id = ?";
        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setBoolean(1, newStatus);
            pre.setString(2, questionId);
            pre.executeUpdate();
        }
    }

    private Question mapRowToQuestion(ResultSet rs) throws SQLException {
        return Question.builder()
                .id(UUID.fromString(rs.getString("id")))
                .content(rs.getString("content"))
                .topicId(rs.getString("topic_id"))
                .quizId(rs.getString("quiz_id"))
                .level(rs.getString("level"))
                .subjectId(rs.getString("subject_id"))   // Cột mới
                .lessonId(rs.getString("lesson_id"))     // Cột mới
                .status(rs.getBoolean("status"))         // Cột mới
                .explanation(rs.getString("explanation")) // Cột mới
                .build();
    }

    // =========================================================================
    // PHIÊN BẢN TỰ QUẢN LÝ CONNECTION
    // =========================================================================

    /**
     * Lấy một câu hỏi dựa trên ID. (Tự quản lý Connection)
     *
     * @param questionId ID của câu hỏi.
     * @return Đối tượng Question hoặc null nếu không tìm thấy.
     * @throws Exception nếu có lỗi.
     */
    public Question getQuestionById(String questionId) throws Exception {
        try (var conn = getConnection()) {
            return getQuestionById(conn, questionId);
        }
    }

    // =========================================================================
    // PHIÊN BẢN DÙNG CONNECTION TỪ BÊN NGOÀI (CHO TRANSACTION)
    // =========================================================================

    /**
     * Lấy một câu hỏi dựa trên ID. (Dùng cho Transaction)
     *
     * @param conn       Connection cho transaction.
     * @param questionId ID của câu hỏi.
     * @return Đối tượng Question hoặc null nếu không tìm thấy.
     * @throws SQLException nếu có lỗi SQL.
     */
    public Question getQuestionById(Connection conn, String questionId) throws SQLException {
        var sql = "SELECT * FROM `question` WHERE id = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, questionId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToQuestion(rs);
                }
            }
        }
        return null;
    }

    /**
     * Thêm một câu hỏi mới vào CSDL. (Dùng cho Transaction)
     *
     * @param conn     Connection cho transaction.
     * @param question Đối tượng Question cần thêm.
     * @throws SQLException nếu có lỗi SQL.
     */
    public void addQuestion(Connection conn, Question question) throws SQLException {
        var sql = "INSERT INTO `question` (id, content, topic_id, quiz_id, level, subject_id, lesson_id, status, explanation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, question.getId().toString());
            ps.setString(2, question.getContent());
            ps.setString(3, question.getTopicId());
            ps.setString(4, question.getQuizId());
            ps.setString(5, question.getLevel());
            ps.setString(6, question.getSubjectId());
            ps.setString(7, question.getLessonId());
            ps.setBoolean(8, question.isStatus());
            ps.setString(9, question.getExplanation());
            ps.executeUpdate();
        }
    }

    /**
     * Cập nhật một câu hỏi đã có. (Dùng cho Transaction)
     *
     * @param conn     Connection cho transaction.
     * @param question Đối tượng Question cần cập nhật.
     * @throws SQLException nếu có lỗi SQL.
     */
    public void updateQuestion(Connection conn, Question question) throws SQLException {
        var sql = "UPDATE `question` SET content = ?, topic_id = ?, quiz_id = ?, level = ?, subject_id = ?, lesson_id = ?, status = ?, explanation = ? WHERE id = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, question.getContent());
            ps.setString(2, question.getTopicId());
            ps.setString(3, question.getQuizId());
            ps.setString(4, question.getLevel());
            ps.setString(5, question.getSubjectId());
            ps.setString(6, question.getLessonId());
            ps.setBoolean(7, question.isStatus());
            ps.setString(8, question.getExplanation());
            ps.setString(9, question.getId().toString());
            ps.executeUpdate();
        }
    }

    /**
     * Xóa một câu hỏi khỏi CSDL. (Dùng cho Transaction)
     *
     * @param conn       Connection cho transaction.
     * @param questionId ID của câu hỏi cần xóa.
     * @throws SQLException nếu có lỗi SQL.
     */
    public void deleteQuestion(Connection conn, String questionId) throws SQLException {
        var sql = "DELETE FROM `question` WHERE id = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, questionId);
            ps.executeUpdate();
        }
    }
}
