package dao;

import java.sql.Connection;

import model.Account;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountDAO extends DBContext {

    private final Logger logger;

    public AccountDAO() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public boolean createAccount(Account account) {
        var sql = """
                INSERT INTO `swp391`.account (id, email, full_name, password,
                dob, gender, created_date, status, phone, image_url, role_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (var connection = getConnection(); var ps = connection.prepareStatement(sql)) {
            ps.setString(1, account.getId().toString());
            ps.setString(2, account.getEmail());
            ps.setString(3, account.getFullName());
            ps.setString(4, account.getPassword());
            ps.setObject(5, account.getDob());
            ps.setInt(6, account.getGender());
            ps.setObject(7, account.getCreatedDate());
            ps.setBoolean(8, account.isStatus());
            ps.setString(9, account.getPhoneNumber());
            ps.setString(10, account.getImageUrl());
            ps.setString(11, account.getRoleId());
            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return false;
    }

    public boolean isEmailExist(String email) {
        var sql = "SELECT * FROM `swp391`.account WHERE email = ?";
        try (var connection = getConnection(); var ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (var rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return false;
    }

    // Kiểm tra xem số điện thoại đã tồn tại trong cơ sở dữ liệu hay chưa.
    public boolean isPhoneNumberExist(String phoneNumber) {
        var sql = "SELECT * FROM `swp391`.account WHERE phone = ?";
        try (var ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, phoneNumber);
            try (var rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return false;
    }

    // Cập nhật thông tin tài khoản người dùng trong cơ sở dữ liệu.
    public void updateAccount(Account account) {
        var sql = """
                UPDATE `swp391`.`account`
                SET `full_name` = ?,
                    `dob` = ?,
                    `gender` = ?,
                    `phone` = ?,
                    `image_url` = ?
                WHERE `id` = ?""";
        try (var ptm = getConnection().prepareStatement(sql)) {
            ptm.setString(1, account.getFullName());
            ptm.setDate(2, account.getDob() != null ? java.sql.Date.valueOf(account.getDob()) : null);
            ptm.setInt(3, account.getGender());
            ptm.setString(4, account.getPhoneNumber());
            ptm.setString(5, account.getImageUrl());
            ptm.setString(6, account.getId().toString());
            int rowsAffected = ptm.executeUpdate();
            if (rowsAffected == 0) {
                logger.log(Level.WARNING, "Không có hàng nào được cập nhật cho ID tài khoản: {0}", account.getId());
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Lỗi SQL khi cập nhật tài khoản: " + ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            logger.log(Level.SEVERE, "Lỗi xác thực: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Lỗi không mong muốn khi cập nhật tài khoản: " + ex.getMessage(), ex);
        }
    }

    public Account getAccountById(String id) {
        var account = Account.builder().build();

        var sql = "SELECT * FROM `swp391`.account WHERE id = ?";

        return query(id, account, sql);
    }

    public Account findUserByEmailAndPassword(String email, String password) throws Exception {
        var acc = Account.builder().build();
        var sql = " SELECT * FROM `swp391`.account WHERE email = ? AND password = ? ";
        try (var connection = getConnection(); var ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (var rs = ps.executeQuery()) {
                acc = getAccount(rs);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return acc;
    }

    public Account getAccountByEmail(String email) {
        var account = Account.builder().build();
        var sql = "SELECT * FROM `swp391`.account WHERE email = ? ";
        return query(email, account, sql);
    }

    public String getRoleIdByRoleName(String role) {
        var sql = "SELECT id FROM `swp391`.setting WHERE value = ?";
        try (var connection = getConnection(); var ps = connection.prepareStatement(sql)) {
            ps.setString(1, role);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("id");
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return null;
    }

    public String getRoleNameById(String id) {
        var sql = "SELECT value FROM `swp391`.setting WHERE id = ?";
        try (var connection = getConnection(); var ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("value");
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return null;
    }

    public void updatePasswordByEmail(String password, String email) {
        var sql = """
                UPDATE `swp391`.account a
                SET a.password = ?
                WHERE a.email = ?
                """;
        try (var connection = getConnection(); var ps = connection.prepareStatement(sql)) {
            ps.setString(1, password);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public int getCountNewAccountByDate(String startDate, String endDate) {
        var sql = "SELECT COUNT(*) FROM `swp391`.account WHERE created_date BETWEEN ? AND ?";
        try (var connection = getConnection(); var ps = connection.prepareStatement(sql)) {
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

    public List<Account> getAllExperts() throws ClassNotFoundException {
        List<Account> experts = new ArrayList<>();

        var sql = """
                SELECT * FROM `swp391`.account
                WHERE role_id = (
                    SELECT id FROM `swp391`.setting
                    WHERE value = 'Expert'
                    LIMIT 1
                )
                """;

        try (var connection = getConnection(); var ps = connection.prepareStatement(sql)) {
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    experts.add(Account.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .email(rs.getString("email"))
                            .fullName(rs.getString("full_name"))
                            .password(rs.getString("password"))
                            .dob(rs.getObject("dob", LocalDate.class))
                            .gender(rs.getInt("gender"))
                            .createdDate(rs.getObject("created_date", LocalDate.class))
                            .status(rs.getBoolean("status"))
                            .phoneNumber(rs.getString("phone"))
                            .imageUrl(rs.getString("image_url"))
                            .roleId(rs.getString("role_id"))
                            .build());
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Lỗi khi lấy danh sách Expert: " + e.getMessage(), e);
        }

        return experts;
    }

    private Account query(String id, Account account, String sql) {
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                account = getAccount(rs);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return account;
    }

    private Account getAccount(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return Account.builder()
                    .id(UUID.fromString(rs.getString("id")))
                    .email(rs.getString("email"))
                    .fullName(rs.getString("full_name"))
                    .password(rs.getString("password"))
                    .dob(rs.getObject("dob", LocalDate.class))
                    .gender(rs.getInt("gender"))
                    .createdDate(rs.getObject("created_date", LocalDate.class))
                    .status(rs.getBoolean("status"))
                    .phoneNumber(rs.getString("phone"))
                    .imageUrl(rs.getString("image_url"))
                    .roleId(rs.getString("role_id"))
                    .build();
        }
        return null;
    }
}
