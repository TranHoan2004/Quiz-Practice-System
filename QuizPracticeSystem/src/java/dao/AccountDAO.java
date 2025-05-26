package dao;

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
        if(isEmailExist(account.getEmail())) {
            return false;
        }

        String sql = "INSERT INTO account (id, email, full_name, password," +
                " dob, gender, created_date, status, phone, image_url, role_id)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
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
            throw new RuntimeException(e);
        }
    }

    public boolean isEmailExist(String email) {
        String sql = "SELECT * FROM account WHERE email = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            try(ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public Account getAccountById(String id) {
        logger.info("getById " + id);
        Account account = Account.builder().build();

        String sql = "SELECT * FROM account WHERE id = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                account = getAccount(rs);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return account;
    }
    
    public Account findUserByEmailAndPassword(String email, String password) throws Exception {
        logger.info("Finding account by email and password: " + email);
        Account acc = Account.builder().build();
        String sql = "SELECT * FROM account WHERE email = ? AND password = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                acc = getAccount(rs);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
        return acc;
    }
    

    public Account getAccountByEmail(String email) {
        logger.info("getByEmail " + email);
        Account account = Account.builder().build();

        String sql = "SELECT * FROM account WHERE email = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            try(ResultSet rs = ps.executeQuery()) {
                account = getAccount(rs);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return account;
    }

    private Account getAccount(ResultSet rs) throws SQLException {
        return Account.builder()
                .id(UUID.fromString(rs.getString("id")))
                .email(rs.getString("email"))
                .fullName(rs.getString("fullname"))
                .password(rs.getString("password"))
                .dob(rs.getObject("dob", LocalDate.class))
                .gender(rs.getInt("gender"))
                .createdDate(rs.getObject("create_date", LocalDate.class))
                .status(rs.getBoolean("status"))
                .phoneNumber(rs.getString("phone"))
                .imageUrl(rs.getString("image_url"))
                .build();
    }
}
