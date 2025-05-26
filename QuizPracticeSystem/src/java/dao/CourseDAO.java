package dao;

import model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author TranHoan
 */
public class CourseDAO extends DBContext {

    private final Logger logger;

    public CourseDAO() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public List<Course> getAllCourses() throws Exception {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM course";
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {
            while (rs.next()) {
                list.add(buildCourse(rs));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return list;
    }

    public Course getById(String id) throws Exception {
        Course s = Course.builder().build();
        String sql = "SELECT * FROM course s WHERE s.id = ?";
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setString(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                s = getEntityFromResultSet(s, rs);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return s;
    }

    public Course getByTopic(String topicId) throws Exception {
        logger.info("getByTopic " + topicId);
        Course s = Course.builder().build();
        String sql = "SELECT * FROM course s WHERE s.topic_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setString(1, topicId);
            try (ResultSet rs = pre.executeQuery()) {
                s = getEntityFromResultSet(s, rs);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
                throw e;
            }
            return s;
        }
    }

    public void deleteById(String id) throws Exception {
        logger.info("Deleting course with id " + id);
        String sql = "DELETE FROM course s WHERE s.id = ?";
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setString(1, id);
            pre.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    public void create(Course c) throws Exception {
        String sql = """
                INSERT INTO course (id, title, duration, status, description, created_date, updated_date, thumbnail_url, number_of_lessons, topic_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setString(1, c.getId().toString());
            pre.setString(2, c.getTitle());
            pre.setFloat(3, c.getDuration());
            pre.setBoolean(4, c.isStatus());
            pre.setString(5, c.getDescription());
            pre.setString(6, c.getDescription());
            pre.setObject(7, c.getCreatedDate());
            pre.setObject(8, c.getUpdatedDate());
            pre.setString(9, c.getThumbnailUrl());
            pre.setInt(10, c.getNumberOfLessons());
            pre.setString(11, c.getTopicId());
            pre.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    private Course getEntityFromResultSet(Course s, ResultSet rs) throws SQLException {
        while (rs.next()) {
            s = buildCourse(rs);
        }
        return s;
    }

    private Course buildCourse(ResultSet rs) throws SQLException {
        return Course.builder()
                .id(UUID.fromString(rs.getString("id")))
                .title(rs.getString("title"))
                .duration(rs.getFloat("duration"))
                .description(rs.getString("description"))
                .createdDate(rs.getObject("created_date", LocalDate.class))
                .updatedDate(rs.getObject("updated_date", LocalDate.class))
                .thumbnailUrl(rs.getString("thumbnail_url"))
                .numberOfLessons(rs.getInt("number_of_lessons"))
                .topicId(rs.getString("topic_id"))
                .contact(rs.getString("contact"))
                .build();
    }
}
