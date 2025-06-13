package dao;

import dto.QuizDTO;
import model.Quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuizDAO extends DBContext {
    private final Logger logger;

    public QuizDAO() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public List<Quiz> getAllQuiz() throws Exception {
        List<Quiz> quizList = new ArrayList<>();
        String sql = "SELECT * FROM `swp391`.quiz";
        try (Connection conn = getConnection();
             PreparedStatement pre = conn.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {
            while (rs.next()) {
                quizList.add(getQuiz(rs));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return quizList;
    }

    public Quiz getById(String id) throws Exception {
        Quiz quiz = Quiz.builder().build();
        String sql = "SELECT * FROM `swp391`.quiz z WHERE z.id=?";
        try (Connection conn = getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    quiz = getQuiz(rs);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return quiz;
    }

    public int getTotalQuizDto(String subjectId, String type, String title) throws Exception {
        int total = 0;

        StringBuilder sql = new StringBuilder(
                "SELECT q.id FROM quiz q WHERE 1=1 "
        );

        List<Object> params = createObject(subjectId, type, title, sql);

        try (Connection conn = getConnection(); PreparedStatement pre = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pre.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pre.executeQuery()) {
                while (rs.next()) {
                    total++;
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }

        return total;
    }

    // Lấy danh sách các quiz theo từng trang (phân trang)
    public List<QuizDTO> pagingQuiz(int index, String subjectId, String type, String title) throws Exception {
        List<QuizDTO> quizDtoList = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT " +
                        "q.id, q.duration, q.status, q.pass_rate, q.updated_date, " +
                        "q.number_of_question, q.description, q.title, q.subject_id, " +
                        "q.type, q.level " +
                        "FROM quiz q " +
                        "WHERE 1=1 "
        );

        List<Object> params = createObject(subjectId, type, title, sql);

        sql.append("ORDER BY q.id LIMIT 5 OFFSET ? ");
        params.add((index - 1) * 5);

        try (Connection conn = getConnection(); PreparedStatement pre = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pre.setObject(i + 1, params.get(i));
            }

            PersonalQuizDAO personalQuizDAO = new PersonalQuizDAO();
            QuizLevelDAO quizLevelDAO = new QuizLevelDAO();

            try (ResultSet rs = pre.executeQuery()) {
                while (rs.next()) {
                    QuizDTO dto = new QuizDTO();
                    dto.setId(UUID.fromString(rs.getString("id")));
                    dto.setDuration(rs.getInt("duration"));
                    dto.setCheck(personalQuizDAO.checkPersonalQuiz(rs.getString("id")));
                    dto.setPassRate(rs.getFloat("pass_rate"));
                    dto.setUpdatedDate(rs.getObject("updated_date", LocalDate.class));
                    dto.setNumberOfQuestions(rs.getInt("number_of_question"));
                    dto.setDescription(rs.getString("description"));
                    dto.setTitle(rs.getString("title"));
                    dto.setSubjectId(rs.getString("subject_id")); // subject_id now comes directly from quiz
                    dto.setType(rs.getString("type"));
                    dto.setLevel(quizLevelDAO.getNameByLevelId(rs.getString("level")));

                    quizDtoList.add(dto);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }

        return quizDtoList;
    }

    public void deleteQuiz(String quizId) throws Exception {
        String sql = "DELETE FROM `swp391`.quiz WHERE id = ?";

        try (Connection conn = getConnection(); PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, quizId);
            pre.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting quiz: " + e.getMessage(), e);
            throw e;
        }
    }

    public void insertNewQuiz(Quiz quiz) throws Exception {
        String sql = "INSERT INTO `swp391`.quiz (id, duration, status, pass_rate, updated_date, number_of_question, description, title, subject_id, type, level) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, quiz.getId().toString());
            ps.setInt(2, quiz.getDuration());
            ps.setBoolean(3, quiz.isStatus());
            ps.setFloat(4, quiz.getPassRate());
            ps.setDate(5, java.sql.Date.valueOf(LocalDate.now())); // updated_date = ngày hiện tại
            ps.setInt(6, quiz.getNumberOfQuestions());
            ps.setString(7, quiz.getDescription());
            ps.setString(8, quiz.getTitle());
            ps.setString(9, quiz.getSubjectId()); // cần có setTopicId trong Quiz
            ps.setString(10, quiz.getType());
            ps.setString(11, quiz.getLevel());

            ps.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    private Quiz getQuiz(ResultSet rs) throws Exception {
        return Quiz.builder()
                .id(UUID.fromString(rs.getString("id")))
                .duration(rs.getInt("duration"))
                .passRate(rs.getFloat("pass_rate"))
                .updatedDate(rs.getObject("updated_date", LocalDate.class))
                .numberOfQuestions(rs.getInt("number_of_question"))
                .description(rs.getString("description"))
                .title(rs.getString("title"))
                .subjectId(rs.getString("subject_id"))
                .type(rs.getString("type"))
                .level(rs.getString("level"))
                .build();
    }

    private List<Object> createObject(String subjectId, String type, String title, StringBuilder sql) {
        List<Object> params = new ArrayList<>();

        if (subjectId != null && !subjectId.isEmpty()) {
            sql.append("AND q.subject_id = ? ");
            params.add(subjectId);
        }
        if (type != null && !type.isEmpty()) {
            sql.append("AND q.type = ? ");
            params.add(type);
        }
        if (title != null && !title.isEmpty()) {
            sql.append("AND q.title LIKE ? ");
            params.add("%" + title + "%");
        }
        return params;
    }
    
     public void updateBasicInfoOfQuiz(Quiz quiz) throws Exception {
    String sql = "UPDATE quiz SET " +
                 "title = ?, " +
                 "subject_id = ?, " +
                 "level = ?, " +
                 "duration = ?, " +
                 "pass_rate = ?, " +
                 "type = ?, " +
                 "description = ?, " +
                 "updated_date = ? " +
                 "WHERE id = ?";

    try (Connection conn = getConnection(); PreparedStatement pre = conn.prepareStatement(sql)) {
        pre.setString(1, quiz.getTitle());
        pre.setString(2, quiz.getSubjectId());
        pre.setString(3, quiz.getLevel());
        pre.setInt(4, quiz.getDuration());
        pre.setFloat(5, quiz.getPassRate());
        pre.setString(6, quiz.getType());
        pre.setString(7, quiz.getDescription());
        pre.setDate(8, java.sql.Date.valueOf(quiz.getUpdatedDate()));
        pre.setString(9, quiz.getId().toString());

        pre.executeUpdate();
    } catch (Exception e) {
        logger.log(Level.SEVERE, e.getMessage(), e);
        throw e;
    }
}



    public void updateNumberOfQuestion(Quiz quiz) throws Exception {
        String sql = "UPDATE quiz SET number_of_question = ?, updated_date = ? WHERE id = ?";

        try (Connection conn = getConnection(); PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, quiz.getNumberOfQuestions());
            pre.setDate(2, java.sql.Date.valueOf(quiz.getUpdatedDate())); // cập nhật ngày sửa đổi
            pre.setString(3, quiz.getId().toString());

            pre.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }
    }

}
