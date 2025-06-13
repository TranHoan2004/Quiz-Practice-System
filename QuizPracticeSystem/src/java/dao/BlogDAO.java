package dao;

import dto.BlogDTO;
import model.Blog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class BlogDAO extends DBContext {
    private final Logger logger;

    public BlogDAO() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public List<Blog> getBlogs(String keyword, String category, int page, int pageSize, Integer status) {
        logger.info("getBlogs with search and filter");
        StringBuilder sql = new StringBuilder("SELECT * FROM `swp391`.blog WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (status != null) {
            sql.append(" AND status = ?");
            params.add(status);
        }

        // Filter by keyword if the keyword is not empty
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (title LIKE ? OR brief_info LIKE ? OR content LIKE ?)");
            String pattern = "%" + keyword.trim() + "%";
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
        }

        // Filter by category if the category is not empty
        if (category != null) {
            sql.append(" AND category = ?");
            params.add(category);
        }

        // Add pagination condition to the SQL statement
        sql.append(" ORDER BY created_date DESC LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        List<Blog> blogs = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    blogs.add(mapBlogFromResultSet(rs));
                }
            }
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error in getBlogs", e);
        }
        return blogs;
    }

    public List<Blog> getBlogs(int page, int pageSize, Integer status) {
        return getBlogs(null, null, page, pageSize, status);
    }

    // Get total blogs pagination from DB
    public int getTotalBlogs(String keyword, String category, Integer status) {
        logger.info("getTotalBlogs for pagination");
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM `swp391`.blog WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (status != null) {
            sql.append(" AND status = ?");
            params.add(status);
        }

        // Search by keyword if the keyword is not empty
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (title LIKE ? OR brief_info LIKE ? OR content LIKE ?)");
            String pattern = "%" + keyword.trim() + "%";
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
        }

        // Filter by category if the category is not empty
        if (category != null && !category.trim().isEmpty()) {
            sql.append(" AND category = ?");
            params.add(category);
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // Set parameters to the prepared statement
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error in getTotalBlogs", e);
        }
        return 0;
    }

    public int getTotalBlogs(String keyword, String category) {
        return getTotalBlogs(keyword, category, null);
    }

    public boolean insertBlog(Blog blog) {
        logger.info("insertBlog");
        String sql = """
                INSERT INTO `swp391`.blog (
                    id, account_id, title, content, views, 
                    category, thumbnail_url, brief_info, status,
                    updated_date, created_date, flag_feature
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, blog.getId().toString());
            ps.setString(2, blog.getAccountId());
            ps.setString(3, blog.getTitle());
            ps.setString(4, blog.getContent());
            ps.setInt(5, blog.getViews());
            ps.setString(6, blog.getCategory().toString());
            ps.setString(7, blog.getThumbnailUrl());
            ps.setString(8, blog.getBriefInfo());
            ps.setBoolean(9, blog.isStatus());
            ps.setObject(10, blog.getUpdatedDate());
            ps.setObject(11, blog.getCreatedDate());
            ps.setBoolean(12, blog.isFlagFeature());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error in insertBlog", e);
            return false;
        }
    }

    public boolean updateBlog(Blog blog) {
        logger.info("updateBlog");
        String sql = """
                UPDATE `swp391`.blog SET 
                    title = ?, content = ?, views = ?, 
                    category = ?, thumbnail_url = ?, brief_info = ?, status = ?,
                    updated_date = ?, flag_feature = ?
                WHERE id = ?
        """;
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, blog.getTitle());
            ps.setString(2, blog.getContent());
            ps.setInt(3, blog.getViews());
            ps.setString(4, blog.getCategory().toString());
            ps.setString(5, blog.getThumbnailUrl());
            ps.setString(6, blog.getBriefInfo());
            ps.setBoolean(7, blog.isStatus());
            ps.setObject(8, blog.getUpdatedDate());
            ps.setBoolean(9, blog.isFlagFeature());
            ps.setString(10, blog.getId().toString());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error in updateBlog", e);
            return false;
        }

    }

    public List<Blog> getHottestBlogs(int limit) {
        logger.info("getHottestBlogs");
        String sql = "SELECT * FROM `swp391`.blog ORDER BY views DESC LIMIT ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                return mapBlogsFromResultSet(rs);
            }
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error in getHottestBlogs", e);
        }
        return new ArrayList<>();
    }

    // Get latest blogs from DB
    public List<Blog> getLatestBlogs(int limit) {
        logger.info("getLatestBlogs");
        String sql = "SELECT * FROM `swp391`.blog ORDER BY created_date DESC LIMIT ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                return mapBlogsFromResultSet(rs);
            }
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error in getLatestBlogs", e);
        }
        return new ArrayList<>();
    }

    // Get list category from DB
    public List<BlogDTO> getCategories() throws Exception {
        String sql = """
                SELECT DISTINCT s.id, s.value
                FROM `swp391`.settingtype st
                JOIN `swp391`.setting s ON st.id = s.setting_type_id
                JOIN `swp391`.blog b ON s.id = b.category
                WHERE st.name = ?
                """;

        List<BlogDTO> list = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Blog Category");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(BlogDTO.builder()
                            .categoryId(utils.Encoder.encode(rs.getString("id")))
                            .category(rs.getString("value"))
                            .build());
                }
            }
        }
        return list;
    }

    // Lấy tên category theo id
    public String getCategoryNameById(UUID id) throws Exception {
        String sql = """
                SELECT s.value
                FROM `swp391`.settingtype st
                JOIN `swp391`.setting s ON st.id = s.setting_type_id
                JOIN `swp391`.blog b ON s.id = b.category
                WHERE st.name = ? AND b.category = ?
                """;
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Blog Category");
            ps.setString(2, id.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("value");
                }
            }
        }
        return "";
    }

    public Blog getBlogById(String id) {
        String sql = "SELECT * FROM `swp391`.blog WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapBlogFromResultSet(rs);
                }
            }
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error in getBlogById", e);
        }
        return null;
    }

    public void updateBlogViews(String id) {
        logger.info("updateBlogViews");
        String sql = "UPDATE `swp391`.blog SET views = views + 1 WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error in updateBlogViews", e);
        }
    }

    public boolean deleteBlogById(String id) {
        logger.info("deleteBlogById");
        String sql = "DELETE FROM `swp391`.blog WHERE id = ?";
        int rowAffected = 0;
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            rowAffected = ps.executeUpdate();
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error in deleteBlogById", e);
        }
        return rowAffected > 0;
    }

    // Helper: map ResultSet to List<Blog>
    private List<Blog> mapBlogsFromResultSet(ResultSet rs) throws SQLException {
        List<Blog> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapBlogFromResultSet(rs));
        }
        return list;
    }

    // Helper: map ResultSet to blog
    private Blog mapBlogFromResultSet(ResultSet rs) throws SQLException {
        return Blog.builder()
                .id(UUID.fromString(rs.getString("id")))
                .accountId(rs.getString("account_id"))
                .title(rs.getString("title"))
                .content(rs.getString("content"))
                .views(rs.getInt("views"))
                .category(UUID.fromString(rs.getString("category")))
                .thumbnailUrl(rs.getString("thumbnail_url"))
                .briefInfo(rs.getString("brief_info"))
                .status(rs.getBoolean("status"))
                .updatedDate(rs.getObject("updated_date", LocalDate.class))
                .createdDate(rs.getObject("created_date", LocalDate.class))
                .flagFeature(rs.getBoolean("flag_feature"))
                .build();
    }


}