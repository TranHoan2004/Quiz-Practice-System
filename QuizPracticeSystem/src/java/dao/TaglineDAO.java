package dao;

import model.Tagline;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;
import java.util.logging.Logger;

public class TaglineDAO extends DBContext {
    private final Logger logger;

    public TaglineDAO() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public Tagline getTaglieBySubjectId(String subjectId) {
        logger.info("getTaglieBySubjectId");
        String sql = "SELECT id, name FROM `swp391`.tagline t" +
                " JOIN `swp391`.subject_tagline s ON t.id = s.tagline_id" +
                " WHERE subject_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Tagline.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .name(rs.getString("name"))
                            .build();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
