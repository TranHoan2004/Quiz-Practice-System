package dao;

import dto.SourceItemDTO;
import dto.SubjectDimensionDTO;

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
        var firstQuery = "SELECT setting_id FROM `swp391`.setting_subject WHERE subject_id = ?";
        List<String> settingId = new ArrayList<>();
        List<String> values = new ArrayList<>();
        try (var conn = getConnection(); var pre = conn.prepareStatement(firstQuery)) {
            pre.setString(1, id);
            try (var rs = pre.executeQuery()) {
                while (rs.next()) {
                    settingId.add(rs.getString("setting_id"));
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }

        var secondQuery = "SELECT value FROM `swp391`.setting WHERE id = ?";
        if (!settingId.isEmpty()) {
            try (var conn = getConnection(); var pre = conn.prepareStatement(secondQuery)) {
                for (String str : settingId) {
                    pre.setString(1, str);
                    try (var rs = pre.executeQuery()) {
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
        var sql = "SELECT value FROM `swp391`.setting WHERE setting_type_id = ?";
        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
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
        var sql = "SELECT id, value FROM `swp391`.setting WHERE setting_type_id = 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13'";
        try (var conn = getConnection(); var ps = conn.prepareStatement(sql); var rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getString("id"), rs.getString("value"));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return map;
    }

    public List<SubjectDimensionDTO> getDimensionsBySubjectId(String subjectId) throws Exception {
        List<SubjectDimensionDTO> dimensions = new ArrayList<>();

        var sql = """
                    SELECT s.id, s.value, s.description
                    FROM `swp391`.setting_subject ss
                    JOIN `swp391`.setting s ON ss.setting_id = s.id
                    WHERE ss.subject_id = ?
                """;

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, subjectId);
            try (var rs = pre.executeQuery()) {
                while (rs.next()) {
                    dimensions.add(SubjectDimensionDTO.builder()
                            .id(rs.getString("id"))
                            .name(rs.getString("value"))
                            .description(rs.getString("description"))
                            .build());
                }
            }
        }
        return dimensions;
    }

    public void deleteSubjectDimension(String settingId, String subjectId) throws Exception {
        var sql = """
                DELETE FROM `swp391`.setting_subject
                WHERE setting_id = ? AND subject_id = ?""";

        try (var conn = getConnection(); var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, settingId);
            stmt.setString(2, subjectId);
            stmt.executeUpdate(); // Nếu cần kiểm tra: stmt.executeUpdate() > 0
        }
    }

    public void createSettingAndAttachToSubject(Setting s, String subjectId) throws Exception {
        var insertSetting = """
                    INSERT INTO `swp391`.setting (id, value, status, description, updated_date, setting_type_id)
                    VALUES (?, ?, ?, ?, ?, ?)
                """;

        String link = """
                    INSERT INTO `swp391`.setting_subject (setting_id, subject_id)
                    VALUES (?, ?)
                """;

        try (var conn = getConnection()) {
            conn.setAutoCommit(false);
            try (var ps = conn.prepareStatement(insertSetting)) {
                ps.setString(1, s.getId().toString());
                ps.setString(2, s.getValue());
                ps.setBoolean(3, s.isStatus());
                ps.setString(4, s.getDescription());
                ps.setDate(5, java.sql.Date.valueOf(LocalDate.now()));
                ps.setString(6, "b1a3f640-397a-11f0-84a1-088fc33f56c7");
                ps.executeUpdate();
            }

            try (var ps2 = conn.prepareStatement(link)) {
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

    public List<Setting> getListDomainOrGroupBySubjectId(String subjectId, String domainOrGroup) throws Exception {
        List<Setting> settingList = new ArrayList<>();
        var sql = """
                SELECT s.*
                FROM `swp391`.setting s
                JOIN `swp391`.settingtype st ON s.setting_type_id = st.id
                JOIN `swp391`.setting_subject ss ON ss.setting_id = s.id
                WHERE ss.subject_id = ? AND st.name = ?""";

        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            ps.setString(2, domainOrGroup); // "Domain" hoặc "Group"
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    settingList.add(getSetting(rs));
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return settingList;
    }

    public List<SourceItemDTO> getListSourceItemSetting(List<Setting> settingList, String domainorGroup) {
        List<SourceItemDTO> sourceItemDTOList = new ArrayList<>();

        if (settingList == null || settingList.isEmpty()) {
            return sourceItemDTOList;
        }

        for (var setting : settingList) {
            if (setting == null) {
                continue; // tránh NullPointerException
            }
            var sourceItem = new SourceItemDTO();
            sourceItem.setId(setting.getId());
            sourceItem.setValue(setting.getValue());
            sourceItem.setSourceType(domainorGroup);
            sourceItemDTOList.add(sourceItem);
        }
        return sourceItemDTOList;
    }

    private Setting getSetting(ResultSet rs) throws Exception {
        return Setting.builder()
                .id(UUID.fromString(rs.getString("id")))
                .value(rs.getString("value"))
                .status(rs.getBoolean("status"))
                .description(rs.getString("description"))
                .updatedDate(rs.getDate("updated_date") != null
                        ? rs.getDate("updated_date").toLocalDate() : null)
                .settingTypeId(rs.getString("setting_type_id"))
                .build();
    }

    public String getSettingIdByName(String value, String settingTypeName) throws Exception {
        var sql = """
                    SELECT s.id
                    FROM `swp391`.Setting s
                    JOIN `swp391`.SettingType st ON s.setting_type_id = st.id
                    WHERE s.value = ? AND st.name = ?
                """;
        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            ps.setString(2, settingTypeName);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("id");
                }
            }
        }
        return null;
    }

    public String getSettingNameById(String id) throws Exception {
        var sql = "SELECT value FROM `swp391`.setting WHERE id = ?";
        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("value");
                }
            }
        }
        return null;
    }

}
