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
import model.QuizLevel;
/**
 *
 * @author Admin
 */
public class QuizLevelDAO extends DBContext{
    private final Logger logger;

    public QuizLevelDAO() {
        logger = Logger.getLogger(this.getClass().getName());
    }
    
    public List<QuizLevel> getAllQuizLevel() throws Exception {
        List<QuizLevel> quizLevelList = new ArrayList<>();
        String sql = "SELECT * FROM `swp391`.quizlevel";
        try (Connection conn = getConnection();
             PreparedStatement pre = conn.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {
            while (rs.next()) {
                quizLevelList.add(getQuizLevel(rs));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return quizLevelList;
    }
    
    public String getNameByLevelId(String levelId) throws Exception {
        String sql = "SELECT * FROM `swp391`.quizlevel WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pre = conn.prepareStatement(sql);) {
            pre.setString(1, levelId);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
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
