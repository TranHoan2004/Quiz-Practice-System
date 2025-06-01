package dao;

import model.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.util.logging.*;

/**
 * @author TranHoan
 */
public class SubjectDAO extends DBContext {

    private final Logger logger;

    public SubjectDAO() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public List<Subject> getAllSubjects() throws Exception {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM `swp391`.subject";
        try (Connection connection = getConnection(); PreparedStatement pre = connection.prepareStatement(sql); ResultSet rs = pre.executeQuery()) {
            while (rs.next()) {
                list.add(getEntity(rs));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return list;
    }

    public Subject getById(String id) throws Exception {
        Subject s = Subject.builder().build();
        String sql = "SELECT * FROM `swp391`.subject s WHERE s.id = ?";
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setString(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                while (rs.next()) {
                    s = getEntity(rs);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return s;
    }

    public void create(Subject s) throws Exception {
        String sql = """
                INSERT INTO `swp391`.subject (id, name)
                VALUES (?, ?)
                """;
        try (Connection connection = getConnection(); PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setString(1, s.getId().toString());
            pre.setString(2, s.getName());
            pre.executeQuery();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    public void deleteById(String id) throws Exception {
        String sql = "DELETE FROM `swp391`.subject s WHERE s.id = ?";
        try (Connection connection = getConnection(); PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setString(1, id);
            pre.executeQuery();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }
    
    

    public List<Subject> getTopSubjectsFlag(int top) throws SQLException {
        List<Subject> list = new ArrayList<>();

        String sql = "SELECT * FROM `swp391`.subject WHERE feature_flag = ? ORDER BY RAND() LIMIT ?";

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, 1); // feature_flag = 1
            ps.setInt(2, top); // select top

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(getEntity(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public String getCategoryBySubjectId(String subjectId) throws Exception {
        String sql = """ 
                     SELECT s.value
                FROM `swp391`.setting_subject ss
                JOIN `swp391`.setting s ON ss.setting_id = s.id
                JOIN `swp391`.settingtype stt ON s.setting_type_id = stt.id
                WHERE ss.subject_id = ? AND stt.name = 'Subject Category'
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("value");
                }
            }
        }
        return null;
    }

    private Subject getEntity(ResultSet rs) throws Exception {
        return Subject.builder()
                .id(UUID.fromString(rs.getString("id")))
                .name(rs.getString("name"))
                .thumbnailURL(rs.getString("thumbnail_url"))
                .featureFlag(rs.getBoolean("feature_flag"))
                .build();
    }
}
