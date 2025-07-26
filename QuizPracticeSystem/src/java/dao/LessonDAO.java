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
        }
        return lesson;
    }

    public int countByCourseId(String courseId) throws Exception {
        var sql = "SELECT COUNT(*) FROM lesson WHERE course_id = ?";
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
        var sql = """
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
        }
    }

    /**
     * Lấy danh sách các bài học liên quan đến một ID môn học cụ thể. Phương
     * thức này thực hiện join các bảng lesson, course, topic và subject để lọc
     * các bài học dựa trên ID môn học được cung cấp.
     *
     * @param subjectId ID của môn học để lọc bài học.
     * @return Một danh sách các đối tượng Lesson.
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy cập cơ sở dữ
     *                   liệu.
     */
    public List<Lesson> getLessonsBySubjectId(String subjectId) throws Exception {
        List<Lesson> lessons = new ArrayList<>();
        // Truy vấn SQL để join các bảng lesson, course, topic và subject
        // để truy xuất các bài học liên quan đến subjectId đã cho.
        var sql = """
                SELECT
                l.id,
                l.status,
                l.name,
                l.course_id,
                l.lesson_type_id
                FROM `swp391`.subject s
                JOIN
                    `swp391`.topic t ON t.subject_id = s.id
                JOIN
                    `swp391`.course c ON c.topic_id = t.id
                JOIN
                    `swp391`.lesson l ON l.course_id = c.id
                WHERE
                    t.subject_id = ?
                ORDER BY
                    l.name ASC;
                """;
        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, subjectId); // Đặt tham số subjectId
            try (var rs = pre.executeQuery()) {
                while (rs.next()) {
                    // Xây dựng đối tượng Lesson từ ResultSet
                    lessons.add(Lesson.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .status(rs.getBoolean("status"))
                            .name(rs.getString("name"))
                            .courseId(rs.getString("course_id"))
                            .lessonTypeId(rs.getString("lesson_type_id"))
                            .build()); // Thêm vào danh sách
                }
            }
        }
        return lessons; // Trả về danh sách các bài học
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
        }
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

    public String getNameByLessonId(String lessonId) throws Exception {
        var sql = "SELECT name FROM `swp391`.lesson WHERE id = ?";
        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, lessonId);
            var rs = pre.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        }
        return null;
    }

    /**
     * Lấy tất cả các Lesson từ cơ sở dữ liệu.
     *
     * @return Danh sách các đối tượng Lesson.
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ
     *                   liệu.
     */
    public List<Lesson> getAllLesson() throws Exception {
        List<Lesson> lessons = new ArrayList<>(); // Khởi tạo một danh sách rỗng để chứa các Lesson
        var sql = "SELECT id, status, name, course_id, lesson_type_id FROM `swp391`.lesson"; // Lấy tất cả các cột cần thiết
        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            // Không cần setString cho tham số vì không có WHERE clause
            try (var rs = pre.executeQuery()) {
                while (rs.next()) { // Duyệt qua TẤT CẢ các bản ghi trả về
                    lessons.add(Lesson.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .status(rs.getBoolean("status"))
                            .name(rs.getString("name"))
                            .courseId(rs.getString("course_id"))
                            .lessonTypeId(rs.getString("lesson_type_id"))
                            .build()); // Thêm Lesson vào danh sách
                }
            }
        }
        return lessons; // Trả về danh sách các Lesson
    }

}
