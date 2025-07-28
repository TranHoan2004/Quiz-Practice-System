package dao;

import model.PersonalSubject;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonalSubjectDAO extends DBContext {
    private final Logger logger = Logger.getLogger(PersonalSubjectDAO.class.getName());

    public List<PersonalSubject> getPersonalSubjectsByAccount(String id) {
        List<PersonalSubject> personalSubjects = new ArrayList<>();
        var sql = "SELECT * FROM `swp391`.personalsubject WHERE account_id = ?";

        try (var conn = this.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    personalSubjects.add(PersonalSubject.builder()
                            .subjectId(rs.getString("subject_id"))
                            .accountId(rs.getString("account_id"))
                            .packageName(rs.getString("package_name"))
                            .registrationTime(rs.getObject("registration_date", LocalDate.class))
                            .validFrom(rs.getObject("valid_from", LocalDate.class))
                            .validTo(rs.getObject("valid_to", LocalDate.class))
                            .status(rs.getString("status"))
                            .price(rs.getFloat("price"))
                            .build());
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return personalSubjects;
    }

    public PersonalSubject getPersonalSubjectsByAccountAndSubject(String accountId, String subjectId) {
        var personalSubjects = PersonalSubject.builder().build();
        var sql = "SELECT * FROM `swp391`.personalsubject WHERE account_id = ? AND subject_id = ?";

        try (var conn = this.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountId);
            ps.setString(2, subjectId);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    personalSubjects = PersonalSubject.builder()
                            .subjectId(rs.getString("subject_id"))
                            .accountId(rs.getString("account_id"))
                            .packageName(rs.getString("package_name"))
                            .registrationTime(rs.getObject("registration_date", LocalDate.class))
                            .validFrom(rs.getObject("valid_from", LocalDate.class))
                            .validTo(rs.getObject("valid_to", LocalDate.class))
                            .status(rs.getString("status"))
                            .price(rs.getFloat("price"))
                            .build();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return personalSubjects;
    }

    public void updateStatus(String status, String id, String accountId) {
        var query = """
                UPDATE `swp391`.personalsubject SET status = ? WHERE subject_id = ? AND account_id = ?
                """;
        try (var conn = getConnection(); var pre = conn.prepareStatement(query)) {
            pre.setString(1, status);
            pre.setString(2, id);
            pre.setString(3, accountId);
            pre.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void insert(PersonalSubject subject) throws SQLException, ClassNotFoundException {
        var sql = """
                INSERT INTO `personalsubject`
                VALUES(?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (var conn = this.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, subject.getAccountId());
            ps.setString(2, subject.getSubjectId());
            ps.setString(3, subject.getStatus());
            ps.setObject(4, subject.getRegistrationTime());
            ps.setString(5, subject.getPackageName());
            ps.setObject(6, subject.getValidFrom());
            ps.setObject(7, subject.getValidTo());
            ps.setFloat(8, subject.getPrice());
            ps.executeUpdate();
        }
    }

    public boolean existBySubjectIdAndAccountId(String accountId, String subjectId) {
        var sql = "SELECT * FROM `swp391`.personalsubject WHERE account_id = ? AND subject_id = ?";

        try (var conn = this.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountId);
            ps.setString(2, subjectId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return false;
    }

    public void deleteByAccountIdAndSubjectId(String accountId, String subjectId) throws SQLException, ClassNotFoundException {
        var query = "DELETE FROM `swp391`.personalsubject WHERE account_id = ? AND subject_id = ?";
        try (var conn = getConnection(); var pre = conn.prepareStatement(query)) {
            pre.setString(1, accountId);
            pre.setString(2, subjectId);
            pre.executeUpdate();
        }
    }
}
