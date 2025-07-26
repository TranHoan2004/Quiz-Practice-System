package dao;

import model.BlogMedia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlogMediaDAO extends DBContext {
    private final Logger logger;

    public BlogMediaDAO() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public void insertBlogMedia(BlogMedia media) {
        var sql = """
                INSERT INTO `swp391`.blogmedia (
                    id, blog_id, media_type, file_path,
                    caption, display_order
                ) VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, media.getId().toString());
            ps.setString(2, media.getBlogId().toString());
            ps.setString(3, media.getMediaType());
            ps.setString(4, media.getFile_path());
            ps.setString(5, media.getCaption());
            ps.setInt(6, media.getDisplayOrder());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in insertBlogMedia", e);
        }
    }

    public void updateBlogMedia(BlogMedia media) {
        var sql = """
                UPDATE `swp391`.blogmedia
                SET media_type = ?, file_path = ?,
                    caption = ?, display_order = ?
                WHERE id = ?
                """;

        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, media.getMediaType());
            ps.setString(2, media.getFile_path());
            ps.setString(3, media.getCaption());
            ps.setInt(4, media.getDisplayOrder());
            ps.setString(5, media.getId().toString());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in updateBlogMedia", e);
        }
    }

    public void deleteBlogMedia(UUID id) {
        var sql = "DELETE FROM `swp391`.blogmedia WHERE id = ?";

        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, id.toString());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in deleteBlogMedia", e);
        }
    }

    public void deleteBlogMediaByBlogId(UUID blogId) {
        var sql = "DELETE FROM `swp391`.blogmedia WHERE blog_id = ?";
        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, blogId.toString());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in deleteBlogMediaByBlogId", e);
        }
    }

    public List<BlogMedia> getBlogMediaByBlogId(UUID blogId) {
        var sql = "SELECT * FROM `swp391`.blogmedia WHERE blog_id = ? ORDER BY display_order";
        List<BlogMedia> mediaList = new ArrayList<>();

        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, blogId.toString());
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    mediaList.add(mapBlogMediaFromResultSet(rs));
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getBlogMediaByBlogId", e);
        }
        return mediaList;
    }

    public boolean updateDisplayOrder(UUID mediaId, int newOrder) {
        var sql = "UPDATE `swp391`.blogmedia SET display_order = ? WHERE id = ?";

        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newOrder);
            ps.setString(2, mediaId.toString());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in updateDisplayOrder", e);
            return false;
        }
    }

    private BlogMedia mapBlogMediaFromResultSet(ResultSet rs) throws SQLException {
        return BlogMedia.builder()
                .id(UUID.fromString(rs.getString("id")))
                .blogId(UUID.fromString(rs.getString("blog_id")))
                .mediaType(rs.getString("media_type"))
                .file_path(rs.getString("file_path"))
                .caption(rs.getString("caption"))
                .displayOrder(rs.getInt("display_order"))
                .build();
    }
}