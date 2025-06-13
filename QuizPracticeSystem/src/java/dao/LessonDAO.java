package dao;

import model.Lesson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LessonDAO extends DBContext {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public Lesson getById(String id) throws Exception {
        Lesson lesson = Lesson.builder().build();
        String sql = "SELECT * FROM `swp391`.lesson WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    lesson = Lesson.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .status(rs.getBoolean("status"))
                            .name(rs.getString("name"))
                            .courseId(rs.getString("course_id"))
                            .lessonTypeId(rs.getString("lesson_type_id"))
                            .build();
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return lesson;
    }

    public int countBySubjectId(String subjectId) throws Exception {
        String sql = """
                SELECT COUNT(*)
                FROM `swp391`.lesson
                WHERE course_id IN (
                    SELECT id
                    FROM `swp391`.course
                    WHERE topic_id IN (
                        SELECT id
                        FROM `swp391`.topic
                        WHERE subject_id = ?
                    )
                )
                """;
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public List<Lesson> getLessonByCourseId(String id) throws Exception {
        List<Lesson> lessons = new ArrayList<>();
        String sql = "SELECT * FROM `swp391`.lesson WHERE course_id = ?";
        try (Connection conn = getConnection(); PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                while (rs.next()) {
                    Lesson lesson = Lesson.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .status(rs.getBoolean("status"))
                            .name(rs.getString("name"))
                            .courseId(rs.getString("course_id"))
                            .lessonTypeId(rs.getString("lesson_type_id"))
                            .build();
                    lessons.add(lesson);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return lessons;
    }

    public void updateLessonStatus(UUID id, boolean newStatus) throws Exception {
        String sql = "UPDATE `swp391`.lesson SET status = ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, newStatus);
            ps.setString(2, id.toString());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
