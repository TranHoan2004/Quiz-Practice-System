package dao;

import dto.StasusPersonalCourseDTO;
import model.PersonalCourse;
import enumerate.PersonalCourseStatus;

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
public class PersonalCourseDAO extends DBContext {
    private final Logger log;

    public PersonalCourseDAO() {
        log = Logger.getLogger(this.getClass().getName());
    }

    public List<PersonalCourse> getAll() throws Exception {
        List<PersonalCourse> list = new ArrayList<>();
        var sql = "SELECT * FROM `swp391`.personalcourse";
        try (var connection = getConnection();
             var pre = connection.prepareStatement(sql);
             var rs = pre.executeQuery()) {
            query(list, rs);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return list;
    }

    public List<PersonalCourse> getAllByAccount(String id) throws Exception {
        List<PersonalCourse> list = new ArrayList<>();
        var sql = """
                SELECT * FROM personalcourse p
                WHERE p.account_id=?
                """;
        return getData(id, list, sql);
    }

    public List<PersonalCourse> getAllByCourse(String id) throws Exception {
        List<PersonalCourse> list = new ArrayList<>();
        var sql = """
                SELECT * FROM personalcourse p
                WHERE p.course_id=?
                """;
        return getData(id, list, sql);
    }

    public List<PersonalCourse> getTopCoursePurchases(int limit) throws Exception {
        List<PersonalCourse> list = new ArrayList<>();
        var sql = """
                SELECT course_id, COUNT(*) AS total_purchases
                FROM swp391.personalcourse
                WHERE status = 'PAID'
                GROUP BY course_id
                ORDER BY total_purchases DESC
                LIMIT ?;
                """;
        try (var connection = getConnection();
             var pre = connection.prepareStatement(sql)) {
            pre.setInt(1, limit);
            try (var rs = pre.executeQuery()) {
                while (rs.next()) {
                    list.add(PersonalCourse.builder()
                            .courseId(rs.getString("course_id"))
                            .build());
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        return list;
    }

    public PersonalCourse getAllByAccountAndCourse(String accountId, String courseId) throws Exception {
        var pc = PersonalCourse.builder().build();
        var sql = """
                SELECT * FROM `swp391`.personalcourse p
                WHERE p.account_id=? AND p.course_id=?
                """;
        try (var connection = getConnection();
             var ps = connection.prepareStatement(sql)) {
            ps.setString(1, accountId);
            ps.setString(2, courseId);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    pc = PersonalCourse.builder()
                            .accountId(rs.getString("account_id"))
                            .courseId(rs.getString("course_id"))
                            .expireDate(rs.getObject("expire_date", LocalDate.class))
                            .enrollDate(rs.getObject("enroll_date", LocalDate.class))
                            .progress(rs.getInt("progress"))
                            .status(PersonalCourseStatus.valueOf(rs.getString("status")))
                            .build();
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return pc;
    }

    public void deleteByCourseAndAccount(String accountID, String courseID) throws Exception {
        var sql = """
                DELETE FROM `swp391`.personalcourse p
                WHERE p.course_id=? AND p.account_id=?
                """;
        try (var connection = getConnection();
             var ps = connection.prepareStatement(sql)) {
            ps.setString(1, courseID);
            ps.setString(2, accountID);
            ps.executeUpdate();
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    public void deleteByAccount(String id) throws Exception {
        var sql = """
                DELETE FROM `swp391`.personalcourse p
                WHERE p.account_id=?
                """;
        try (var connection = getConnection();
             var ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    public void create(PersonalCourse pc) throws Exception {
        var sql = """
                INSERT INTO `swp391`.personalcourse (id, account_id, course_id, expire_date, enroll_date, progress, status)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (var connection = getConnection();
             var ps = connection.prepareStatement(sql)) {
            ps.setString(1, pc.getId().toString());
            ps.setString(2, pc.getAccountId());
            ps.setString(3, pc.getCourseId());
            ps.setObject(4, pc.getExpireDate());
            ps.setObject(5, pc.getEnrollDate());
            ps.setInt(6, pc.getProgress());
            ps.setString(7, pc.getStatus().toString());
            ps.executeUpdate();
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    public int getCountPersonalCourseByStatus(String startDate, String endDate, String status) {
        var sql = "SELECT COUNT(*) FROM `swp391`.personalcourse WHERE status = ? AND enroll_date BETWEEN ? AND ?";
        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.trim().toUpperCase());
            ps.setString(2, startDate);
            ps.setString(3, endDate);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        return 0;
    }

    public int getCountPersonalCourse(String startDate, String endDate) {
        String sql = "SELECT COUNT(*) FROM `swp391`.personalcourse WHERE enroll_date BETWEEN ? AND ?";
        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        return 0;
    }

    public int getCountNewPersonalCourseByNewAccount(String startDate, String endDate) {
        var sql = """
                    SELECT COUNT(*) FROM `swp391`.personalcourse ps
                    JOIN `swp391`.account a ON ps.account_id = a.id
                    WHERE ps.enroll_date BETWEEN ? AND ?
                    AND a.created_date BETWEEN ? AND ?
                """;
        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ps.setString(3, startDate);
            ps.setString(4, endDate);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error in getCountNewPersonalCourseByNewAccount: " + e.getMessage(), e);
        }
        return 0;
    }

    private void query(List<PersonalCourse> list, ResultSet rs) throws SQLException {
        while (rs.next()) {
            var c = PersonalCourse.builder()
                    .accountId(rs.getString("account_id"))
                    .courseId(rs.getString("course_id"))
                    .expireDate(rs.getObject("expire_date", LocalDate.class))
                    .enrollDate(rs.getObject("expire_date", LocalDate.class))
                    .progress(rs.getInt("progress"))
                    .id(UUID.fromString(rs.getString("id")))
                    .build();
            if (rs.getString("status") != null) {
                c.setStatus(PersonalCourseStatus.valueOf(rs.getString("status")));
            }
            list.add(c);
        }
    }

    private List<PersonalCourse> getData(String id, List<PersonalCourse> list, String sql) throws Exception {
        try (var connection = getConnection();
             var ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (var rs = ps.executeQuery()) {
                query(list, rs);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return list;
    }

    public List<StasusPersonalCourseDTO> getStatus() throws Exception {
        List<StasusPersonalCourseDTO> list = new ArrayList<>();

        var sql = "SELECT DISTINCT status FROM `swp391`.personalcourse WHERE status IS NOT NULL";

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            try (var rs = pre.executeQuery()) {
                while (rs.next()) {
                    list.add(StasusPersonalCourseDTO.builder()
                            .status(rs.getString("status"))
                            .build());
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            throw e;
        }

        return list;
    }

}
