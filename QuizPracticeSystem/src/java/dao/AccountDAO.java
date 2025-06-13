package dao;

import java.sql.Connection;

import model.Account;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountDAO extends DBContext {

    private final Logger logger;

    public AccountDAO() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public boolean createAccount(Account account) {
        logger.info("createAccount");
        if (isEmailExist(account.getEmail())) {
            return false;
        }

        // Validate role_id exists
        if (account.getRoleId() == null) {
            logger.log(Level.SEVERE, "Invalid role_id provided");
            return false;
        }

        String sql = "INSERT INTO `swp391`.account (id, email, full_name, password,"
                + " dob, gender, created_date, status, phone, image_url, role_id)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
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
        String sql = "SELECT * FROM `swp391`.account WHERE email = ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return false;
    }

    // Kiểm tra xem số điện thoại đã tồn tại trong cơ sở dữ liệu hay chưa.
    public boolean isPhoneNumberExist(String phoneNumber) {
        String sql = "SELECT * FROM `swp391`.account WHERE phone = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, phoneNumber);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return false;
    }

    // Cập nhật thông tin tài khoản người dùng trong cơ sở dữ liệu.
    public void updateAccount(Account account) {
        String sql = "UPDATE `swp391`.`account` " +
                "SET `full_name` = ?, " +
                "    `dob` = ?, " +
                "    `gender` = ?, " +
                "    `phone` = ?, " +
                "    `image_url` = ? " +
                "WHERE `id` = ?";

        logger.log(Level.INFO, "Giá trị image_url trước khi cập nhật: {0}", account.getImageUrl());

        try (PreparedStatement ptm = getConnection().prepareStatement(sql)) {
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
        logger.info("getById " + id);
        Account account = Account.builder().build();

        String sql = "SELECT * FROM `swp391`.account WHERE id = ?";

        return query(id, account, sql);
    }

    public Account findUserByEmailAndPassword(String email, String password) throws Exception {
        logger.info("Finding account by email and password: " + email);
        Account acc = Account.builder().build();
        String sql = " SELECT * FROM `swp391`.account WHERE email = ? AND password = ? ";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                acc = getAccount(rs);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return acc;
    }

    public Account getAccountByEmail(String email) {
        logger.info("getByEmail " + email);
        Account account = Account.builder().build();
        String sql = " SELECT * FROM `swp391`.account WHERE email = ? ";
        return query(email, account, sql);
    }

    public String getRoleIdByRoleName(String role) {
        String sql = "SELECT id FROM `swp391`.setting WHERE value = ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, role);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("id");
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return null;
    }

    public void updatePasswordByEmail(String password, String email) {
        String sql = """
                UPDATE `swp391`.account a
                SET a.password = ?
                WHERE a.email = ?
                """;
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, password);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
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
}
