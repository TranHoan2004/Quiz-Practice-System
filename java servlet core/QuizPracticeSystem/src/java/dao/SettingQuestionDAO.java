/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.QuestionDimensionsDTO;
import model.Setting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date; // Import java.sql.Date cho việc chuyển đổi LocalDate
import java.time.LocalDate; // Import java.time.LocalDate
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SettingQuestionDAO extends DBContext {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final SettingDAO settingDAO;

    public SettingQuestionDAO() {
        this.settingDAO = new SettingDAO();
    }

    /**
     * Cập nhật một Dimension (Setting) duy nhất mới được chọn cho một Question.
     * Chỉ cập nhật những Dimension có liên quan đến settingtype 'Domain' và
     * 'Group'. Phương thức này sẽ xóa các Dimension cũ của Question đó thuộc
     * loại 'Domain'/'Group' và sau đó chèn Dimension mới được cung cấp.
     *
     * @param questionIdStr ID của Question cần cập nhật (dưới dạng String).
     * @param newDimensionSettingIdStr ID của Setting (Dimension) mới được chọn
     * (dưới dạng String), hoặc null nếu muốn xóa mà không thêm mới.
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy cập cơ sở dữ
     * liệu.
     * @author ThuanHD
     */
    public void updateDimensionsForQuestion(String questionIdStr, String newDimensionSettingIdStr) throws Exception {
        Connection conn = null;
        PreparedStatement deleteStmt = null;
        PreparedStatement insertStmt = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Begin transaction

            // Get IDs for 'Group' and 'Domain' setting types
            String groupSettingTypeId = settingDAO.getSettingTypeIdByName("Group");
            String domainSettingTypeId = settingDAO.getSettingTypeIdByName("Domain");

            if (groupSettingTypeId == null && domainSettingTypeId == null) {
                throw new Exception("Could not find ID for SettingType 'Group' or 'Domain'. Cannot update Dimension.");
            }

            // 1. Delete old Dimensions for this Question belonging to 'Domain' and 'Group' types
            String deleteSql = """
                    DELETE FROM `swp391`.setting_question
                    WHERE question_id = ?
                    AND setting_id IN (
                        SELECT id FROM `swp391`.setting
                        WHERE setting_type_id IN (?, ?)
                    );
                    """;
            deleteStmt = conn.prepareStatement(deleteSql);
            deleteStmt.setString(1, questionIdStr);
            deleteStmt.setString(2, groupSettingTypeId);
            deleteStmt.setString(3, domainSettingTypeId);
            deleteStmt.executeUpdate();

            // 2. Insert the new selected Dimension (if newDimensionSettingIdStr is not null or empty)
            if (newDimensionSettingIdStr != null && !newDimensionSettingIdStr.isEmpty()) {
                String insertSql = "INSERT INTO `swp391`.setting_question (setting_id, question_id) VALUES (?, ?)";
                insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setString(1, newDimensionSettingIdStr);
                insertStmt.setString(2, questionIdStr);
                insertStmt.executeUpdate();
            }

            conn.commit(); // Commit transaction if everything is successful

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction if an error occurs
                } catch (SQLException rollbackEx) {
                    // Consider re-throwing or wrapping rollbackEx if it's critical,
                    // otherwise, it might be swallowed in a real application.
                    // For this optimization, we're removing explicit logging,
                    // but in a production app, you'd want some form of error reporting.
                }
            }
            // Re-throw the original exception to be handled by the caller
            throw e;
        } finally {
            // Close resources in a try-with-resources like manner, or individually as below
            if (insertStmt != null) {
                try {
                    insertStmt.close();
                } catch (SQLException e) {
                    // Suppress or handle quietly
                }
            }
            if (deleteStmt != null) {
                try {
                    deleteStmt.close();
                } catch (SQLException e) {
                    // Suppress or handle quietly
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit to true
                } catch (SQLException e) {
                    // Suppress or handle quietly
                }
                try {
                    conn.close();
                } catch (SQLException e) {
                    // Suppress or handle quietly
                }
            }
        }
    }

    /**
     * Lấy một Dimension (Setting) duy nhất đã được gán cho một Question cụ thể
     * từ bảng setting_question. Chỉ lấy những Dimension có setting_type_id liên
     * quan đến 'Domain' và 'Group'.
     *
     * @param questionIdStr ID của Question (dưới dạng String).
     * @return Đối tượng Setting (Dimension) đã gán, hoặc null nếu không tìm
     * thấy.
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy cập cơ sở dữ
     * liệu.
     * @author ThuanHD
     */
    public Setting getAssignedDimensionByQuestionId(String questionIdStr) throws Exception { // Đổi tên hàm
        Setting dimension = null;
        var groupSettingTypeId = settingDAO.getSettingTypeIdByName("Group");
        var domainSettingTypeId = settingDAO.getSettingTypeIdByName("Domain");

        if (groupSettingTypeId == null && domainSettingTypeId == null) {
            logger.log(Level.WARNING, "Không tìm thấy ID cho SettingType 'Group' hoặc 'Domain'.");
            return null;
        }

        // Truy vấn SQL để lấy duy nhất một Dimension được gán cho câu hỏi
        var sql = """
                SELECT s.id, s.value, s.status, s.description, s.updated_date, s.setting_type_id
                FROM `swp391`.setting s
                JOIN `swp391`.setting_question sq ON s.id = sq.setting_id
                WHERE sq.question_id = ?
                AND s.setting_type_id IN (?, ?)
                LIMIT 1; -- Giới hạn kết quả chỉ lấy 1 bản ghi
                """;

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, questionIdStr);
            pre.setString(2, groupSettingTypeId);
            pre.setString(3, domainSettingTypeId);

            try (var rs = pre.executeQuery()) {
                if (rs.next()) {
                    LocalDate updatedDate = null;
                    var sqlDate = rs.getDate("updated_date");
                    if (sqlDate != null) {
                        updatedDate = sqlDate.toLocalDate();
                    }

                    dimension = Setting.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .value(rs.getString("value"))
                            .status(rs.getBoolean("status"))
                            .description(rs.getString("description"))
                            .updatedDate(updatedDate)
                            .settingTypeId(rs.getString("setting_type_id"))
                            .build();
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Lỗi khi lấy Dimension được gán theo Question ID: " + e.getMessage());
            throw e;
        }
        return dimension;
    }

    /**
     * Xóa tất cả các dimensions (settings) được gán cho một câu hỏi cụ thể,
     * nhưng chỉ những dimensions có setting_type_id tương ứng với name là
     * 'Domain' hoặc 'Group'.
     *
     * @param questionId ID của câu hỏi mà các dimensions cần được xóa.
     * @throws Exception nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ
     * liệu.
     */
    public void removeDimensionsByQuestionId(String questionId, Connection conn) throws Exception {
        String sql = """
            DELETE sq FROM `swp391`.setting_question sq
            JOIN `swp391`.setting s ON sq.setting_id = s.id
            JOIN `swp391`.settingtype st ON s.setting_type_id = st.id
            WHERE sq.question_id = ?
            AND st.name IN ('Domain', 'Group');
            """;

        try (PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, questionId);
            int rowsAffected = pre.executeUpdate();
            logger.log(Level.INFO, "Đã xóa {0} bản ghi trong setting_question cho questionId {1} (chỉ Domain/Group).",
                    new Object[]{rowsAffected, questionId});
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Lỗi khi xóa dimensions (Domain/Group) cho câu hỏi " + questionId + ": " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Thêm một setting (dimension) vào một câu hỏi. Phương thức này cần được
     * gọi sau khi xóa các dimensions cũ.
     *
     * @param settingId ID của setting (dimension) cần thêm.
     * @param questionId ID của câu hỏi.
     * @throws Exception nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ
     * liệu.
     */
    public void addSettingToQuestion(String settingId, String questionId, Connection conn) throws Exception {
        var sql = "INSERT INTO `swp391`.setting_question (setting_id, question_id) VALUES (?, ?)";
        try (var pre = conn.prepareStatement(sql)) {
            pre.setString(1, settingId);
            pre.setString(2, questionId);

            int rowsAffected = pre.executeUpdate();
            logger.log(Level.INFO, "Đã thêm {0} bản ghi vào setting_question: settingId={1}, questionId={2}",
                    new Object[]{rowsAffected, settingId, questionId});
        }
    }

    public boolean existsBySettingIdAndQuestionId(String questionId, String settingId) throws SQLException, ClassNotFoundException {
        var query = """
                SELECT * FROM setting_question sq WHERE sq.setting_id = ? AND sq.question_id + ?
                """;
        try (var conn = getConnection(); var pre = conn.prepareStatement(query)) {
            pre.setString(1, settingId);
            pre.setString(2, questionId);
            try (var rs = pre.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        }
        return false;
    }

}
