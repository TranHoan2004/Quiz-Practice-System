package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SettingDAO extends DBContext {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public List<String> getDimensionBySubject(String id) throws Exception {
        String firstQuery = "SELECT setting_id FROM `swp391`.setting_subject WHERE subject_id = ?";
        List<String> settingId = new ArrayList<>();
        List<String> values = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pre = conn.prepareStatement(firstQuery)) {
            pre.setString(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                while (rs.next()) {
                    settingId.add(rs.getString("setting_id"));
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }

        String secondQuery = "SELECT value FROM `swp391`.setting WHERE id = ?";
        if (!settingId.isEmpty()) {
            try (Connection conn = getConnection();
                 PreparedStatement pre = conn.prepareStatement(secondQuery)) {
                for (String str : settingId) {
                    pre.setString(1, str);
                    try (ResultSet rs = pre.executeQuery()) {
                        while (rs.next()) {
                            values.add(rs.getString("value"));
                        }
                    }
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
                throw e;
            }
        }
        return values;
    }
}
