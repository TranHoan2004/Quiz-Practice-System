package dao;


import model.Slider;
import utils.Encoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SliderDAO extends DBContext {
    private final Logger logger;

    public SliderDAO() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public List<Slider> getTopSliderActive(int limit) {
        // Tránh dùng LIMIT ? — nối chuỗi biến limit vào SQL
        String sql = "SELECT * FROM `swp391`.slider s WHERE s.status = ? ORDER BY RAND() LIMIT ?";
        List<Slider> list = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, 1); // status = 1 (active)
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(getEntity(rs));
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return list;
    }

    public List<Slider> getAllSliders() {
        String sql = "SELECT * FROM `swp391`.slider";
        List<Slider> list = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(getEntity(rs));
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return list;
    }

    public List<Slider> search(String keyword) {
        String sql = "SELECT * FROM `swp391`.slider WHERE title LIKE ?";
        List<Slider> list = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(getEntity(rs));
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return list;
    }

    public List<Slider> filterByStatus(String status) {
        String sql = "SELECT * FROM `swp391`.slider WHERE status = ?";
        List<Slider> list = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status.equals("active") ? "1" : "0");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(getEntity(rs));
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return list;
    }

    public Slider getSliderById(UUID id) {
        String sql = "SELECT * FROM `swp391`.slider WHERE id = ?";
        Slider slider = null;
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    slider = getEntity(rs);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return slider;
    }

    public void updateSliderStatus(UUID id, boolean status) {
        String sql = "UPDATE `swp391`.slider SET status = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, status);
            ps.setString(2, id.toString());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    private Slider getEntity(ResultSet rs) throws SQLException {
        return Slider.builder()
                .id(UUID.fromString(rs.getString("id")))
                .title(rs.getString("title"))
                .accountId(rs.getString("account_id"))
                .backlinkUrl(rs.getString("backlink_url"))
                .imageUrl(rs.getString("image_url"))
                .status(rs.getBoolean("status"))
                .note(rs.getString("note"))
                .build();
    }
}
