package dao;

import dto.QuizDTO;
import model.Quiz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuizDAO extends DBContext {

    private final Logger logger;
    private final PersonalQuizDAO personalQuizDAO;
    private final QuizLevelDAO quizLevelDAO;

    public QuizDAO() {
        logger = Logger.getLogger(this.getClass().getName());
        personalQuizDAO = new PersonalQuizDAO();
        quizLevelDAO = new QuizLevelDAO();
    }

    public List<Quiz> getAllQuiz() throws Exception {
        List<Quiz> quizList = new ArrayList<>();
        var sql = "SELECT * FROM `swp391`.quiz";
        try (var conn = getConnection(); var pre = conn.prepareStatement(sql); var rs = pre.executeQuery()) {
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
        var sql = "SELECT * FROM `swp391`.quiz z WHERE z.id=?";
        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, id);
            try (var rs = pre.executeQuery()) {
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

    public QuizDTO getQuizDTOById(String id) throws Exception {
        var sql = """
        SELECT
            q.id, q.duration, q.status, q.pass_rate, q.updated_date,
            q.number_of_question, q.description, q.title, q.subject_id,
            s.name AS subject_name,
            qt.name as type, q.level
        FROM quiz q
        JOIN subject s ON q.subject_id = s.id
        JOIN quiztype qt ON q.type = qt.id
        WHERE q.id = ?
        """;

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, id);

            try (var rs = pre.executeQuery()) {
                if (rs.next()) {
                    return convertToQuizDTO(rs, personalQuizDAO, quizLevelDAO);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }

        return null;
    }


    public int getTotalQuizDto(String subjectId, String type, String title, Boolean status) {
        var sql = new StringBuilder("SELECT COUNT(*) FROM quiz q WHERE 1=1 ");
        List<Object> params = createObject(subjectId, type, title, status, sql);

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

    // Lấy danh sách các quiz theo từng trang (phân trang)
    public List<QuizDTO> pagingQuiz(String subjectId, String type, String title, Boolean status, int page, int size) throws Exception {
        List<QuizDTO> quizDtoList = new ArrayList<>();

        var sql = new StringBuilder(
                """ 
                            SELECT
                            q.id, q.duration, q.status, q.pass_rate, q.updated_date,
                            q.number_of_question, q.description, q.title, q.subject_id,
                            s.name as subject_name,
                            q.type, q.level
                            FROM quiz q
                            JOIN subject s ON q.subject_id = s.id
                            WHERE 1=1
                        """
        );

        List<Object> params = createObject(subjectId, type, title, status, sql);

        sql.append(" ORDER BY q.id LIMIT ? OFFSET ?");
        params.add(size);
        params.add((page - 1) * size);

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pre.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pre.executeQuery()) {
                while (rs.next()) {
                    quizDtoList.add(convertToQuizDTO(rs, personalQuizDAO, quizLevelDAO));
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }
        return quizDtoList;
    }

    public void deleteQuiz(String quizId) throws Exception {
        var sql = "DELETE FROM `swp391`.quiz WHERE id = ?";

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, quizId);
            pre.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting quiz: " + e.getMessage(), e);
            throw e;
        }
    }

    public void insertNewQuiz(Quiz quiz) throws Exception {
        var sql = """
                INSERT INTO `swp391`.quiz (id, duration, status, pass_rate, updated_date, number_of_question,
                description, title, subject_id, type, level) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""";

        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
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

    public void updateBasicInfoOfQuiz(Quiz quiz) throws Exception {
        var sql = """
                UPDATE `swp391`.quiz SET
                title = ?,
                subject_id = ?,
                level = ?,
                duration = ?,
                pass_rate = ?,
                type = ?,
                description = ?,
                updated_date = ?
                WHERE id = ?""";

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
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
        var sql = "UPDATE `swp391`.quiz SET number_of_question = ?, updated_date = ? WHERE id = ?";

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setInt(1, quiz.getNumberOfQuestions());
            pre.setDate(2, java.sql.Date.valueOf(quiz.getUpdatedDate())); // cập nhật ngày sửa đổi
            pre.setString(3, quiz.getId().toString());
            pre.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
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

    private List<Object> createObject(String subjectId, String type, String title, Boolean status, StringBuilder sql) {
        List<Object> params = new ArrayList<>();
        if (status != null) {
            sql.append(" AND q.status = ?");
            params.add(status);
        }
        if (subjectId != null && !subjectId.isBlank()) {
            sql.append(" AND q.subject_id = ?");
            params.add(subjectId);
        }
        if (type != null && !type.isBlank()) {
            sql.append(" AND q.type = ?");
            params.add(type);
        }
        if (title != null && !title.isBlank()) {
            sql.append(" AND q.title LIKE ?");
            params.add("%" + title + "%");
        }
        return params;
    }

    public static QuizDTO convertToQuizDTO(ResultSet rs, PersonalQuizDAO personalQuizDAO, QuizLevelDAO quizLevelDAO)
            throws Exception {
        QuizDTO dto = new QuizDTO();
        dto.setId(UUID.fromString(rs.getString("id")));
        dto.setDuration(rs.getInt("duration"));
        dto.setCheck(personalQuizDAO.checkPersonalQuiz(rs.getString("id")));
        dto.setPassRate(rs.getFloat("pass_rate"));
        dto.setUpdatedDate(rs.getObject("updated_date", LocalDate.class));
        dto.setNumberOfQuestions(rs.getInt("number_of_question"));
        dto.setDescription(rs.getString("description"));
        dto.setTitle(rs.getString("title"));
        dto.setSubjectId(rs.getString("subject_id"));
        dto.setSubjectName(rs.getString("subject_name"));
        dto.setStatus(rs.getBoolean("status"));
        dto.setType(rs.getString("type"));
        dto.setLevel(quizLevelDAO.getNameByLevelId(rs.getString("level")));
        return dto;
    }
}
