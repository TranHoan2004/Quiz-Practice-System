package dao;

import model.Lesson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LessonDAO extends DBContext {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public Lesson getById(String id) throws Exception {
        var lesson = Lesson.builder().build();
        var sql = "SELECT * FROM `swp391`.lesson WHERE id = ?";
        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, id);
            try (var rs = pre.executeQuery()) {
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

    public int countByCourseId(String courseId) throws Exception {
        String sql = "SELECT COUNT(*) FROM lesson WHERE course_id = ?";
        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public List<Lesson> getLessonByCourseId(String id) {
        List<Lesson> lessons = new ArrayList<>();
        var sql = "SELECT * FROM `swp391`.lesson WHERE course_id = ?";
        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, id);
            try (var rs = pre.executeQuery()) {
                while (rs.next()) {
                    lessons.add(Lesson.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .status(rs.getBoolean("status"))
                            .name(rs.getString("name"))
                            .courseId(rs.getString("course_id"))
                            .lessonTypeId(rs.getString("lesson_type_id"))
                            .build());
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return lessons;
    }

    public void updateLessonStatus(UUID id, boolean newStatus) {
        var sql = "UPDATE `swp391`.lesson SET status = ? WHERE id = ?";
        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, newStatus);
            ps.setString(2, id.toString());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public void insertLesson(Lesson lesson) throws Exception {
        String sql = """
                    INSERT INTO `swp391`.lesson (id, status, name, course_id, lesson_type_id)
                    VALUES (?, ?, ?, ?, ?)
                    """;

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, lesson.getId().toString());
            pre.setBoolean(2, lesson.isStatus());
            pre.setString(3, lesson.getName());
            pre.setString(4, lesson.getCourseId());
            pre.setString(5, lesson.getLessonTypeId());
            pre.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    public void updateLesson(Lesson lesson) throws Exception {
        var sql = """
                UPDATE `swp391`.lesson
                SET status = ?, name = ?, course_id = ?, lesson_type_id = ?
                WHERE id = ?
                """;

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setBoolean(1, lesson.isStatus());
            pre.setString(2, lesson.getName());
            pre.setString(3, lesson.getCourseId());
            pre.setString(4, lesson.getLessonTypeId());
            pre.setString(5, lesson.getId().toString()); // WHERE id = ?
            pre.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    public int countLessonsByCourse(String courseId) throws Exception {
        var sql = "SELECT COUNT(*) FROM `swp391`.lesson WHERE course_id = ?";
        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int getOrderOfLesson(String lessonId) throws Exception {
        var sql = "SELECT course_id FROM `swp391`.lesson WHERE id = ?";
        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, lessonId);
            var rs = ps.executeQuery();
            if (rs.next()) {
                var courseId = rs.getString("course_id");
                List<Lesson> lessons = getLessonByCourseId(courseId);
                for (int i = 0; i < lessons.size(); i++) {
                    if (lessons.get(i).getId().toString().equals(lessonId)) {
                        return i + 1;
                    }
                }
            }
        }
        return 1;
    }

}
