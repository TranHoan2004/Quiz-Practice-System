/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.QuizQuestionSourceConfig;

/**
 * @author Admin
 */
public class QuizQuestionSourceConfigDAO extends DBContext {

    public List<QuizQuestionSourceConfig> getAllQuizQuestionSourceConfigByQuizId(String quizId) throws Exception {
        List<QuizQuestionSourceConfig> configList = new ArrayList<>();
        var sql = "SELECT * FROM `swp391`.quiz_question_source_config WHERE quiz_id = ?";

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, quizId);
            try (var rs = pre.executeQuery()) {
                while (rs.next()) {
                    configList.add(getQuizQuestionSourceConfig(rs));
                }
            }
        }
        return configList;
    }

    public List<QuizQuestionSourceConfig> getAllQuizQuestionSourceConfigById(String quizId, String sourceType) throws Exception {
        List<QuizQuestionSourceConfig> configList = new ArrayList<>();
        var sql = new StringBuilder("SELECT * FROM quiz_question_source_config WHERE 1=1 ");

        if (quizId != null && !quizId.isEmpty()) {
            sql.append("AND quiz_id = ? ");
        }
        if (sourceType != null && !sourceType.isEmpty()) {
            sql.append("AND source_type = ? ");
        }

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (quizId != null && !quizId.isEmpty()) {
                pre.setString(index++, quizId);
            }

            if (sourceType != null && !sourceType.isEmpty()) {
                pre.setString(index++, sourceType.toLowerCase());
            }

            try (var rs = pre.executeQuery()) {
                while (rs.next()) {
                    configList.add(getQuizQuestionSourceConfig(rs));
                }
            }
        }
        return configList;
    }

    public String getSourceType(String quizId) throws Exception {
        var sql = "SELECT source_type FROM `swp391`.quiz_question_source_config WHERE quiz_id = ?";

        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, quizId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Trả về giá trị source_type đã lưu, ví dụ: "group", "domain", hoặc "topic"
                    return rs.getString("source_type");
                }
            }
        }
        return null; // Không tìm thấy
    }

    public void deleteAllQuizQuestionSourceConfigByQuizId(String quizId) throws Exception {
        var sql = "DELETE FROM `swp391`.quiz_question_source_config WHERE quiz_id = ?";

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, quizId);
            pre.executeUpdate();
        }
    }

    public void insertListQuizQuestionSourceConfig(List<QuizQuestionSourceConfig> configList) throws Exception {
        var sql = "INSERT INTO `swp391`.quiz_question_source_config (id, quiz_id, source_type, source_id, number_of_questions) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            for (QuizQuestionSourceConfig config : configList) {
                // Tạo UUID nếu chưa có
                if (config.getId() == null) {
                    config.setId(UUID.randomUUID());
                }

                pre.setString(1, config.getId().toString());
                pre.setString(2, config.getQuizId());
                pre.setString(3, config.getSourceType());
                pre.setString(4, config.getSourceId());
                pre.setInt(5, config.getNumberOfQuestions());
                pre.addBatch(); // Thêm vào batch
            }
            pre.executeBatch(); // Thực thi batch insert
        }
    }

    private QuizQuestionSourceConfig getQuizQuestionSourceConfig(ResultSet rs) throws Exception {
        return QuizQuestionSourceConfig.builder()
                .id(UUID.fromString(rs.getString("id")))
                .quizId(rs.getString("quiz_id"))
                .sourceType(rs.getString("source_type"))
                .sourceId(rs.getString("source_id"))
                .numberOfQuestions(rs.getInt("number_of_questions"))
                .build();
    }
}
