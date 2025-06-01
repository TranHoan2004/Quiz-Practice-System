package dao;


import model.Slider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SliderDAO extends DBContext{
    private final Logger logger;

    public SliderDAO() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public List<Slider> getTopSliderActive(int limit) throws Exception {
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
            throw e;
        }
        return list;
    }

    public Slider getEntity(ResultSet rs) throws Exception {
        return Slider.builder()
                .id(UUID.fromString(rs.getString("id")))
                .title(rs.getString("title"))
                .accountId(rs.getString("account_id"))
                .backlinkUrl(rs.getString("backlink_url"))
                .imageUrl(rs.getString("image_url"))
                .status(rs.getBoolean("status"))
                .build();
    }
}
