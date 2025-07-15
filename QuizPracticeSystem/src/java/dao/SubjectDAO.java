package dao;

import model.Subject;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

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
        try (var connection = getConnection();
             var pre = connection.prepareStatement(sql);
             var rs = pre.executeQuery()) {
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
        var s = Subject.builder().build();
        var sql = "SELECT * FROM `swp391`.subject s WHERE s.id = ?";
        try (var connection = getConnection();
             var pre = connection.prepareStatement(sql)) {
            pre.setString(1, id);
            try (var rs = pre.executeQuery()) {
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
        var sql = """
                INSERT INTO `swp391`.subject (id, name)
                VALUES (?, ?)
                """;
        try (var connection = getConnection();
             var pre = connection.prepareStatement(sql)) {
            pre.setString(1, s.getId().toString());
            pre.setString(2, s.getName());
            pre.executeQuery();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    public void deleteById(String id) {
        var sql = "DELETE FROM `swp391`.subject s WHERE s.id = ?";
        try (var connection = getConnection();
             var pre = connection.prepareStatement(sql)) {
            pre.setString(1, id);
            pre.executeQuery();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public List<Subject> getTopSubjectsFlag(int top) throws SQLException {
        List<Subject> list = new ArrayList<>();

        var sql = "SELECT * FROM `swp391`.subject WHERE feature_flag = ? ORDER BY RAND() LIMIT ?";

        try (var connection = getConnection();
             var ps = connection.prepareStatement(sql)) {
            ps.setInt(1, 1); // feature_flag = 1
            ps.setInt(2, top); // select top

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(getEntity(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public String getCategoryBySubjectId(String subjectId) throws Exception {
        var sql = """ 
                     SELECT s.value
                FROM `swp391`.setting_subject ss
                JOIN `swp391`.setting s ON ss.setting_id = s.id
                JOIN `swp391`.settingtype stt ON s.setting_type_id = stt.id
                WHERE ss.subject_id = ? AND stt.name = 'Blog Category'
                """;

        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("value");
                }
            }
        }
        return null;
    }

    public List<String> getAllCategories() throws Exception {
        List<String> result = new ArrayList<>();

        var sql = """
                    SELECT DISTINCT s.value
                    FROM `swp391`.setting_subject ss
                    JOIN `swp391`.setting s ON ss.setting_id = s.id
                    JOIN `swp391`.settingtype stt ON s.setting_type_id = stt.id
                    WHERE stt.name IN ('Domain', 'Group')
                """;
        // WHERE stt.name = 'Blog Category'

        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(rs.getString("value"));
            }
        }
        return result;
    }

    public List<Subject> getAllSubjectsByCategory(String category) throws Exception {
        List<Subject> result = new ArrayList<>();
        var sql = """
                    SELECT * FROM `swp391`.subject s
                    JOIN `swp391`.setting_subject ss ON s.id = ss.subject_id
                    JOIN `swp391`.setting st ON ss.setting_id = st.id
                    JOIN `swp391`.settingtype stt ON st.setting_type_id = stt.id
                    WHERE st.value = ?
                """;

        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(getEntity(rs));
                }
            }
        }
        return result;
    }

    public int getCountSubjectsByDate(String startDate, String endDate) {
        var sql = "SELECT COUNT(*) FROM `swp391`.subject WHERE created_date BETWEEN ? AND ?";
        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return 0;
    }

    public int getCountAllSubjects() {
        var sql = "SELECT COUNT(*) FROM `swp391`.subject";
        try (var conn = getConnection(); var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return 0;
    }

    public List<Subject> getAllSubjectsByName(String name) {
        logger.info("getAllSubjectsByName");
        var query = """
                SELECT * FROM `swp391`.subject s
                WHERE s.name LIKE ?
                """;
        List<Subject> list = new ArrayList<>();
        try (var conn = getConnection();
             var pre = conn.prepareStatement(query)) {
            pre.setString(1, "%" + name + "%");
            try (var rs = pre.executeQuery()) {
                while (rs.next()) {
                    list.add(getEntity(rs));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return list;
    }

    public Map<String, Double> getRevenueBySubjectCategory(String startDate, String endDate) {
        var sql = """
                SELECT s.name AS category, SUM(pp.sale_price) AS total_revenue
                FROM `swp391`.subject s
                JOIN `swp391`.topic t ON t.subject_id = s.id
                JOIN `swp391`.course c ON c.topic_id = t.id
                JOIN `swp391`.pricepackage pp ON pp.course_id = c.id
                JOIN `swp391`.personalcourse ps ON ps.course_id = c.id
                WHERE ps.enroll_date BETWEEN ? AND ?
                GROUP BY s.name
                ORDER BY total_revenue DESC;
                """;

        Map<String, Double> result = new HashMap<>();

        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    var subjectName = rs.getString("category");
                    var totalRevenue = rs.getDouble("total_revenue");
                    result.put(subjectName, totalRevenue);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getRevenueBySubjectCategory: " + e.getMessage(), e);
        }
        return result;
    }

    public Map<String, String> getLowestPriceAndSalePriceBySubjectId(String subjectId) {
        var sql = """
                SELECT pp.price AS 'lowest_price', pp.sale_price
                FROM `swp391`.pricepackage pp
                JOIN `swp391`.course c ON c.id = pp.course_id
                JOIN `swp391`.topic t ON t.id = c.topic_id
                JOIN `swp391`.subject s ON s.id = t.subject_id
                WHERE s.id = ?
                  AND pp.price = (
                    SELECT MIN(pp2.price)
                    FROM `swp391`.pricepackage pp2
                             JOIN `swp391`.course c2 ON c2.id = pp2.course_id
                             JOIN `swp391`.topic t2 ON t2.id = c2.topic_id
                    WHERE t2.subject_id = s.id
                )
                """;
        Map<String, String> result = new HashMap<>();
        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    result.put("sale_price", rs.getString("sale_price"));
                    result.put("lowest_price", rs.getString("lowest_price"));
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return result;
    }

    private Subject getEntity(ResultSet rs) throws SQLException {
        return Subject.builder()
                .id(UUID.fromString(rs.getString("id")))
                .name(rs.getString("name"))
                .thumbnailURL(rs.getString("thumbnail_url"))
                .featureFlag(rs.getBoolean("feature_flag"))
                .authorId(rs.getString("author"))
                .updatedDate(rs.getObject("updated_date", LocalDate.class))
                .build();
    }
}
