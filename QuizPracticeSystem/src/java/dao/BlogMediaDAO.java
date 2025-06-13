package dao;

import model.BlogMedia;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    public boolean insertBlogMedia(BlogMedia media) {
        logger.info("insertBlogMedia");
        String sql = """
                INSERT INTO `swp391`.blogmedia (
                    id, blog_id, media_type, file_path,
                    caption, display_order
                ) VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, media.getId().toString());
            ps.setString(2, media.getBlogId().toString());
            ps.setString(3, media.getMediaType());
            ps.setString(4, media.getMediaUrl());
            ps.setString(5, media.getCaption());
            ps.setInt(6, media.getDisplayOrder());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in insertBlogMedia", e);
            return false;
        }
    }

    public boolean updateBlogMedia(BlogMedia media) {
        logger.info("updateBlogMedia");
        String sql = """
                UPDATE `swp391`.blogmedia 
                SET media_type = ?, media_url = ?,
                    caption = ?, display_order = ?
                WHERE id = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, media.getMediaType());
            ps.setString(2, media.getMediaUrl());
            ps.setString(3, media.getCaption());
            ps.setInt(4, media.getDisplayOrder());
            ps.setString(5, media.getId().toString());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in updateBlogMedia", e);
            return false;
        }
    }

    public boolean deleteBlogMedia(UUID id) {
        logger.info("deleteBlogMedia");
        String sql = "DELETE FROM `swp391`.blogmedia WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id.toString());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in deleteBlogMedia", e);
            return false;
        }
    }

    public boolean deleteBlogMediaByBlogId(UUID blogId) {
        logger.info("deleteBlogMediaByBlogId");
        String sql = "DELETE FROM `swp391`.blogmedia WHERE blog_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, blogId.toString());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in deleteBlogMediaByBlogId", e);
            return false;
        }
    }

    public List<BlogMedia> getBlogMediaByBlogId(UUID blogId) {
        logger.info("getBlogMediaByBlogId");
        String sql = "SELECT * FROM `swp391`.blogmedia WHERE blog_id = ? ORDER BY display_order";
        List<BlogMedia> mediaList = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, blogId.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mediaList.add(mapBlogMediaFromResultSet(rs));
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getBlogMediaByBlogId", e);
        }
        return mediaList;
    }

    private BlogMedia mapBlogMediaFromResultSet(ResultSet rs) throws SQLException {
        return BlogMedia.builder()
                .id(UUID.fromString(rs.getString("id")))
                .blogId( UUID.fromString(rs.getString("blog_id")))
                .mediaType(rs.getString("media_type"))
                .mediaUrl(rs.getString("file_path"))
                .caption(rs.getString("caption"))
                .displayOrder(rs.getInt("display_order"))
                .build();
    }

    public boolean updateDisplayOrder(UUID mediaId, int newOrder) {
        logger.info("updateDisplayOrder");
        String sql = "UPDATE `swp391`.blogmedia SET display_order = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newOrder);
            ps.setString(2, mediaId.toString());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in updateDisplayOrder", e);
            return false;
        }
    }
}