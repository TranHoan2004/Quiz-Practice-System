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
        String sql = "SELECT * FROM subject";
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {
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
        String sql = "SELECT * FROM subject s WHERE s.id = ?";
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
                INSERT INTO subject (id, name)
                VALUES (?, ?)
                """;
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setString(1, s.getId().toString());
            pre.setString(2, s.getName());
            pre.executeQuery();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    public void deleteById(String id) throws Exception {
        String sql = "DELETE FROM subject s WHERE s.id = ?";
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setString(1, id);
            pre.executeQuery();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    private Subject getEntity(ResultSet rs) throws Exception {
        return Subject.builder()
                .id(UUID.fromString(rs.getString("id")))
                .name(rs.getString("name"))
                .thumbnailURL(rs.getString("thumbnail_url"))
                .build();
    }
}
