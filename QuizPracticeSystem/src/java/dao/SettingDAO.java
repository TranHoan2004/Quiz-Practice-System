package dao;

import dto.SubjectDimensionDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Setting;

public class SettingDAO extends DBContext {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    public static final String LESSON_TYPE_SETTING_ID = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13";

    public List<String> getDimensionBySubject(String id) throws Exception {
        String firstQuery = "SELECT setting_id FROM `swp391`.setting_subject WHERE subject_id = ?";
        List<String> settingId = new ArrayList<>();
        List<String> values = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement pre = conn.prepareStatement(firstQuery)) {
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
            try (Connection conn = getConnection(); PreparedStatement pre = conn.prepareStatement(secondQuery)) {
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

    public List<String> getLessonTypes() throws Exception {
        List<String> types = new ArrayList<>();
        String sql = "SELECT value FROM `swp391`.setting WHERE setting_type_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, LESSON_TYPE_SETTING_ID); // UUID cố định
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                types.add(rs.getString("value"));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return types;
    }

    public Map<String, String> getLessonTypeMap() throws Exception {
        Map<String, String> map = new HashMap<>();
        String sql = "SELECT id, value FROM `swp391`.setting WHERE setting_type_id = 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getString("id"), rs.getString("value"));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return map;
    }

    public List<SubjectDimensionDTO> getDimensionsBySubjectIdAndDescription(String subjectId, String description) throws Exception {
        List<SubjectDimensionDTO> dimensions = new ArrayList<>();

        String sql = """
                    SELECT s.id, s.value, s.description
                    FROM `swp391`.setting_subject ss
                    JOIN `swp391`.setting s ON ss.setting_id = s.id
                    WHERE ss.subject_id = ? AND s.description = ?
                """;

        try (Connection conn = getConnection(); PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, subjectId);
            pre.setString(2, description);
            try (ResultSet rs = pre.executeQuery()) {
                while (rs.next()) {
                    SubjectDimensionDTO dto = SubjectDimensionDTO.builder()
                            .id(rs.getString("id"))
                            .name(rs.getString("value"))
                            .description(rs.getString("description"))
                            .build();
                    dimensions.add(dto);
                }
            }
        }
        return dimensions;
    }

    public void deleteSubjectDimension(String settingId, String subjectId, String description) throws Exception {
        String sql = "DELETE FROM `swp391`.setting_subject "
                + "WHERE setting_id = ? AND subject_id = ? "
                + "AND setting_id IN (SELECT id FROM `swp391`.setting WHERE description = ?)";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, settingId);
            stmt.setString(2, subjectId);
            stmt.setString(3, description);

            stmt.executeUpdate(); // Nếu cần kiểm tra số dòng bị ảnh hưởng: stmt.executeUpdate() > 0
        }
    }

    public void createSettingAndAttachToSubject(Setting s, String subjectId, String description) throws Exception {
        String insertSetting = """
                    INSERT INTO `swp391`.setting (id, value, status, description, updated_date, setting_type_id)
                    VALUES (?, ?, ?, ?, ?, ?)
                """;

        String link = """
                    INSERT INTO `swp391`.setting_subject (setting_id, subject_id)
                    VALUES (?, ?)
                """;

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(insertSetting)) {
                ps.setString(1, s.getId().toString());
                ps.setString(2, s.getValue());
                ps.setBoolean(3, s.isStatus());
                ps.setString(4, s.getDescription());
                ps.setDate(5, java.sql.Date.valueOf(LocalDate.now()));
                ps.setString(6, "b1a3f640-397a-11f0-84a1-088fc33f56c7");
                ps.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement(link)) {
                ps2.setString(1, s.getId().toString());
                ps2.setString(2, subjectId);
                ps2.executeUpdate();
            }

            conn.commit();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

}
