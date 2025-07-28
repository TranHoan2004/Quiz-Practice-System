/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.QuizLevel;

/**
 * @author Admin
 */
public class QuizLevelDAO extends DBContext {
    public List<QuizLevel> getAllQuizLevel() throws Exception {
        List<QuizLevel> quizLevelList = new ArrayList<>();
        var sql = "SELECT * FROM `swp391`.quizlevel";
        try (var conn = getConnection();
             var pre = conn.prepareStatement(sql);
             var rs = pre.executeQuery()) {
            while (rs.next()) {
                quizLevelList.add(getQuizLevel(rs));
            }
        }
        return quizLevelList;
    }

    public String getNameByLevelId(String levelId) throws Exception {
        var sql = "SELECT * FROM `swp391`.quizlevel WHERE id = ?";
        try (var conn = getConnection();
             var pre = conn.prepareStatement(sql);) {
            pre.setString(1, levelId);
            var rs = pre.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        }
        return null;
    }

    private QuizLevel getQuizLevel(ResultSet rs) throws Exception {
        return QuizLevel.builder()
                .id(UUID.fromString(rs.getString("id")))
                .name(rs.getString("name"))
                .build();
    }
}
