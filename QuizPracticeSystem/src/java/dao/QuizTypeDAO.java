/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
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
    private final Logger logger;

    public QuizTypeDAO() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public List<QuizType> getAllQuizType() throws Exception {
        List<QuizType> quizTypeList = new ArrayList<>();
        String sql = "SELECT * FROM `swp391`.quiztype";
        try (Connection conn = getConnection();
             PreparedStatement pre = conn.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {
            while (rs.next()) {
                quizTypeList.add(getQuizType(rs));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
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
