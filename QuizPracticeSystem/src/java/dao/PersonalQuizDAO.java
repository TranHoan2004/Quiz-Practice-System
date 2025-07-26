package dao;

import model.PersonalQuiz;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonalQuizDAO extends DBContext {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    //    kiểm tra xem bài quiz đã có ai làm chưa
    public boolean checkPersonalQuiz(String quizId) {
        var sql = "SELECT 1 FROM `swp391`.personalquiz WHERE quiz_id = ?";
        try (var conn = getConnection();
             var pre = conn.prepareStatement(sql)) {
            pre.setString(1, quizId);
            try (var rs = pre.executeQuery()) {
                return rs.next(); // Nếu có bản ghi thì trả về true
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false; // hoặc throw RuntimeException(e); nếu bạn muốn lan truyền lỗi
        }
    }

    public List<PersonalQuiz> getAllByAccount(String id) throws Exception {
        List<PersonalQuiz> list;
        var sql = "SELECT * FROM `swp391`.personalquiz WHERE account_id = ?";
        try (var conn = getConnection();
             var pre = conn.prepareStatement(sql)) {
            pre.setString(1, id);
            try (var rs = pre.executeQuery()) {
                list = getEntitiesList(rs);
            }
        }
        return list;
    }

    public void deleteById(String id) throws Exception {
        var sql = "DELETE FROM `swp391`.personalquiz WHERE id = ?";
        try (var conn = getConnection();
             var pre = conn.prepareStatement(sql)) {
            pre.setString(1, id);
            pre.executeUpdate();
        }
    }

    public PersonalQuiz getById(String id) throws Exception {
        var query = "SELECT * FROM `swp391`.personalquiz WHERE id = ?";
        try (var conn = getConnection(); var pre = conn.prepareStatement(query)) {
            pre.setString(1, id);
            try (var rs = pre.executeQuery()) {
                if (rs.next()) {
                    return getEntity(rs);
                }
            }
        }
        return null;
    }

    private List<PersonalQuiz> getEntitiesList(ResultSet rs) throws Exception {
        List<PersonalQuiz> list = new ArrayList<>();
        while (rs.next()) {
            list.add(getEntity(rs));
        }
        return list;
    }

    private PersonalQuiz getEntity(ResultSet rs) throws Exception {
        return PersonalQuiz.builder()
                .id(UUID.fromString(rs.getString("id")))
                .accountId(rs.getString("account_id"))
                .quizId(rs.getString("quiz_id"))
                .hasPassed(rs.getBoolean("has_passed"))
                .mark(rs.getInt("mark"))
                .numberOfCorrectQuestion(rs.getInt("number_of_correct_question"))
                .takenDate(rs.getObject("taken_date", LocalDate.class))
                .build();
    }
}
