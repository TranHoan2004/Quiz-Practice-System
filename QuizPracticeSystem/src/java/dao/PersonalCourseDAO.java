package dao;

import dto.StasusPersonalCourseDTO;
import model.PersonalCourse;
import enumerate.PersonalCourseStatus;

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
public class PersonalCourseDAO extends DBContext {
    private final Logger log;

    public PersonalCourseDAO() {
        log = Logger.getLogger(this.getClass().getName());
    }

    public List<PersonalCourse> getAll() throws Exception {
        log.info("getAll");
        List<PersonalCourse> list = new ArrayList<>();
        String sql = "SELECT * FROM `swp391`.personalcourse";
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {
            query(list, rs);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return list;
    }

    public List<PersonalCourse> getAllByAccount(String id) throws Exception {
        log.info("getAllByAccount");
        List<PersonalCourse> list = new ArrayList<>();
        String sql = """
                SELECT * FROM personalcourse p
                WHERE p.account_id=?
                """;
        return getData(id, list, sql);
    }

    public List<PersonalCourse> getAllByCourse(String id) throws Exception {
        log.info("getAllByCourse");
        List<PersonalCourse> list = new ArrayList<>();
        String sql = """
                SELECT * FROM personalcourse p
                WHERE p.course_id=?
                """;
        return getData(id, list, sql);
    }

    public List<PersonalCourse> getTopCoursePurchases(int limit) throws Exception {
        log.info("getTopCoursePurchases");
        List<PersonalCourse> list = new ArrayList<>();
        String sql = """
                SELECT course_id, COUNT(*) AS total_purchases
                FROM swp391.personalcourse
                WHERE status = 'PAID'
                GROUP BY course_id
                ORDER BY total_purchases DESC
                LIMIT ?;
                """;
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setInt(1, limit);
            try (ResultSet rs = pre.executeQuery()) {
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
        log.info("getAllByAccountAndCourse");
        PersonalCourse pc = PersonalCourse.builder().build();
        String sql = """
                SELECT * FROM `swp391`.personalcourse p
                WHERE p.account_id=? AND p.course_id=?
                """;
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, accountId);
            ps.setString(2, courseId);
            try (ResultSet rs = ps.executeQuery()) {
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
        log.info("deleteByCourseAndAccount");
        String sql = """
                DELETE FROM `swp391`.personalcourse p
                WHERE p.course_id=? AND p.account_id=?
                """;
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, courseID);
            ps.setString(2, accountID);
            ps.executeUpdate();
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    public void deleteByAccount(String id) throws Exception {
        log.info("deleteByAccount");
        String sql = """
                DELETE FROM `swp391`.personalcourse p
                WHERE p.account_id=?
                """;
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    public void create(PersonalCourse pc) throws Exception {
        log.info("create");
        String sql = """
                INSERT INTO `swp391`.personalcourse (id, account_id, course_id, expire_date, enroll_date, progress, status)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
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

    private void query(List<PersonalCourse> list, ResultSet rs) throws SQLException {
        while (rs.next()) {
            PersonalCourse c = PersonalCourse.builder()
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
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
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

        String sql = "SELECT DISTINCT status FROM personalcourse WHERE status IS NOT NULL";

        try (Connection conn = getConnection(); PreparedStatement pre = conn.prepareStatement(sql)) {
            try (ResultSet rs = pre.executeQuery()) {
                while (rs.next()) {
                    StasusPersonalCourseDTO dto = StasusPersonalCourseDTO.builder()
                            .status(rs.getString("status"))
                            .build();
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            throw e;
        }

        return list;
    }

}
