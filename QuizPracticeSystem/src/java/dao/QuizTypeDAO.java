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

import model.QuizType;

/**
 * @author Admin
 */
public class QuizTypeDAO extends DBContext {
    public List<QuizType> getAllQuizType() throws Exception {
        List<QuizType> quizTypeList = new ArrayList<>();
        var sql = "SELECT * FROM `swp391`.quiztype";
        try (var conn = getConnection();
             var pre = conn.prepareStatement(sql);
             var rs = pre.executeQuery()) {
            while (rs.next()) {
                quizTypeList.add(getQuizType(rs));
            }
        }
        return quizTypeList;
    }

    private QuizType getQuizType(ResultSet rs) throws Exception {
        return QuizType.builder()
                .id(UUID.fromString(rs.getString("id")))
                .name(rs.getString("name"))
                .build();
    }

}
