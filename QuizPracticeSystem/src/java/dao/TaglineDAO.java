package dao;

import model.Tagline;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaglineDAO extends DBContext {
    private final Logger logger;

    public TaglineDAO() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public Tagline getTaglineBySubjectId(String subjectId) {
        var sql = """
                SELECT id, name FROM `swp391`.tagline t
                JOIN `swp391`.subject_tagline s ON t.id = s.tagline_id
                WHERE subject_id = ?
                """;

        try (var connection = getConnection();
             var ps = connection.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (var rs = ps.executeQuery()) {
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

    public List<Tagline> getTaglinesBySubjectId(String subjectId) {
        var sql = """
                SELECT id, name FROM `swp391`.tagline t
                JOIN `swp391`.subject_tagline s ON t.id = s.tagline_id
                WHERE subject_id = ?
                """;
        List<Tagline> list = new ArrayList<>();
        try (var connection = getConnection();
             var ps = connection.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(Tagline.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .name(rs.getString("name"))
                            .build());
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return list;
    }
}
