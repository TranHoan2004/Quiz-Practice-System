package dao;

import dto.SourceItemDTO;
import dto.SubjectDimensionDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.sql.Date;
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
            }
        }
        return values;
    }

    public List<String> getLessonTypes() throws Exception {
        List<String> types = new ArrayList<>();
        var sql = "SELECT value FROM `swp391`.setting WHERE setting_type_id = ?";
        try (var conn = getConnection(); var ps = conn.prepareStatement(sql); var rs = ps.executeQuery()) {
            ps.setString(1, LESSON_TYPE_SETTING_ID); // UUID cố định
            while (rs.next()) {
                types.add(rs.getString("value"));
            }
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

        var link = """
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
        }
        return settingList;
    }

    public List<SourceItemDTO> getListSourceItemSetting(List<Setting> settingList, String domainGroup) {
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
            sourceItem.setSourceType(domainGroup);
            sourceItemDTOList.add(sourceItem);
        }
        return sourceItemDTOList;
    }

    /**
     * Lấy danh sách các Dimension (Setting) liên quan đến một Subject cụ thể.
     * Các Dimension này được định nghĩa là những Setting có setting_type_id
     * liên quan đến 'Group' hoặc 'Domain' và được gán cho Subject đó
     * thông qua bảng setting_subject.
     *
     * @param subjectId ID của Subject.
     * @return Danh sách các đối tượng Setting (Dimension).
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy cập cơ sở dữ liệu.
     */
    public List<Setting> getDimensionsBySubject(String subjectId) throws Exception {
        List<Setting> dimensions = new ArrayList<>();

        var sql = """
                SELECT s.id, s.value, s.status, s.description, s.updated_date, s.setting_type_id
                FROM `swp391`.setting s
                JOIN `swp391`.setting_subject ss ON s.id = ss.setting_id
                WHERE ss.subject_id = ?
                AND s.setting_type_id IN (?, ?);
                """;

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, subjectId);
            try (var rs = pre.executeQuery()) {
                while (rs.next()) {
                    // Xử lý LocalDate
                    LocalDate updatedDate = null;
                    var sqlDate = rs.getDate("updated_date");
                    if (sqlDate != null) {
                        updatedDate = sqlDate.toLocalDate(); // Chuyển đổi java.sql.Date sang java.time.LocalDate
                    }
                    dimensions.add(Setting.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .value(rs.getString("value"))
                            .status(rs.getBoolean("status"))
                            .description(rs.getString("description"))
                            .updatedDate(updatedDate) // Sử dụng LocalDate
                            .settingTypeId(rs.getString("setting_type_id"))
                            .build());
                }
            }
        }
        return dimensions;
    }

    /**
     * Lấy ID của một SettingType dựa trên tên của nó.
     *
     * @param typeName Tên của SettingType (ví dụ: "Group", "Domain").
     * @return ID của SettingType dưới dạng String, hoặc null nếu không tìm thấy.
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy cập cơ sở dữ liệu.
     */
    public String getSettingTypeIdByName(String typeName) throws Exception {
        String settingTypeId = null;
        var sql = "SELECT id FROM `swp391`.settingtype WHERE name = ?";
        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, typeName);
            try (var rs = pre.executeQuery()) {
                if (rs.next()) {
                    settingTypeId = rs.getString("id");
                }
            }
        }
        return settingTypeId;
    }

    public Setting getSettingById(String id) throws Exception {
        Setting setting = null;
        var sql = "SELECT id, value, status, description, updated_date, setting_type_id FROM `swp391`.setting WHERE id = ?";
        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, id);
            try (var rs = pre.executeQuery()) {
                if (rs.next()) {
                    setting = getSetting(rs);
                }
            }
        }
        return setting;
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

    public List<Setting> getSettingsByType(String typeName) throws Exception {
        List<Setting> settings = new ArrayList<>();
        var sql = """
                    SELECT s.*
                    FROM setting s
                    JOIN settingtype st ON s.setting_type_id = st.id
                    WHERE st.name = ?
                """;

        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, typeName);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    settings.add(getSetting(rs));
                }
            }
        }
        return settings;
    }

    public void insertSettingSubjectLink(String settingId, String subjectId) throws Exception {
        var sql = "INSERT INTO `swp391`.setting_subject (setting_id, subject_id) VALUES (?, ?)";
        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, settingId);
            ps.setString(2, subjectId);
            ps.executeUpdate();
        }
    }

    public void insert(Setting setting) throws Exception {
        var sql = """
                    INSERT INTO `swp391`.setting (id, value, status, description, updated_date, setting_type_id)
                    VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, setting.getId().toString());
            ps.setString(2, setting.getValue());
            ps.setBoolean(3, setting.isStatus());
            ps.setString(4, setting.getDescription());
            ps.setDate(5, java.sql.Date.valueOf(setting.getUpdatedDate()));
            ps.setString(6, setting.getSettingTypeId());

            ps.executeUpdate();
        }
    }

    public String getCategoryIdBySubjectId(String subjectId) throws Exception {
        var sql = """
                    SELECT s.id
                    FROM setting_subject ss
                    JOIN setting s ON ss.setting_id = s.id
                    JOIN settingtype st ON s.setting_type_id = st.id
                    WHERE ss.subject_id = ? AND st.name = 'Blog Category'
                    LIMIT 1
                """;

        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("id");
                }
            }
        }
        return null;
    }

    public List<Setting> getAllDimensionsByDomainOrGroup() {
        List<Setting> settings = new ArrayList<>();
        var sql = """
                SELECT *
                FROM `swp391`.setting s
                JOIN `swp391`.settingtype st ON s.setting_type_id = st.id
                WHERE st.name = 'Domain' or st.name = 'Group'
                """;
        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    Setting setting = Setting.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .value(rs.getString("value"))
                            .build();
                    settings.add(setting);
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return settings;
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

}
