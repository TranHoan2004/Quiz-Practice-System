package dao;

import model.Blog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class BlogDAO extends DBContext{
    private final Logger logger;

    public BlogDAO() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public List<Blog> getAllBlogs() {
        logger.info("getAllBlogs");
        String sql = "SELECT * FROM `swp391`.blog";
        List<Blog> list = new ArrayList<>();
        try (Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            query(list, ps);
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, e.getMessage());
        }
        return list;
    }

    public List<Blog> getTop5HottestBlogs() {
        logger.info("getTop5HottestBlogs");
        List<Blog> list = new ArrayList<>();
        String sql = "SELECT * FROM `swp391`.blog ORDER BY views DESC LIMIT 5";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            query(list, ps);
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, e.getMessage());
        }
        return list;
    }

    public List<Blog> getLatestBlogs(int limit) {
        logger.info("getLatestBlogs");
        List<Blog> list = new ArrayList<>();
        String sql = "SELECT * FROM `swp391`.blog ORDER BY created_date DESC LIMIT ?";
        try (Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            query(list, ps);
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, e.getMessage());
        }
        return list;
    }

    public Blog getBlogById(String id) {
        logger.info("getBlogById");
        Blog blog = Blog.builder().build();
        List<Blog> list = new ArrayList<>();
        String sql = "SELECT * FROM `swp391`.blog b WHERE b.id=?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
                query(list, ps);
                blog = list.getFirst();
             }
        catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, e.getMessage());
        }
        return blog;
    }

    public List<Blog> getBlogsByCategoryId(String categoryId) {
        logger.info("getBlogsByCategoryId");
        List<Blog> list = new ArrayList<>();
        String sql = "SELECT * FROM `swp391`.blog b WHERE b.category=?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, categoryId);
            query(list, ps);
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, e.getMessage());
        }

        return list;
    }

    public void updateBlogViews(String id) {
        logger.info("updateBlogViews");
        String sql = "UPDATE `swp391`.blog SET views = views + 1 WHERE id=?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, e.getMessage());
        }
    }

    public void deleteBlogById(String id) {
        logger.info("deleteBlogById");
        String sql = "DELETE FROM `swp391`.blog WHERE id=?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, e.getMessage());
        }
    }

    private void query(List<Blog> list, PreparedStatement ps) throws Exception {
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Blog blog = Blog.builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .accountId(rs.getString("account_id"))
                        .title(rs.getString("title"))
                        .content(rs.getString("content"))
                        .views(rs.getInt("views"))
                        .category(rs.getString("category"))
                        .thumbnailUrl(rs.getString("thumbnail_url"))
                        .briefInfo(rs.getString("brief_info"))
                        .status(rs.getBoolean("status"))
                        .updatedDate(rs.getObject("updated_date", LocalDate.class))
                        .createdDate(rs.getObject("created_date", LocalDate.class))
                        .build();

                list.add(blog);
            }
        }
    }
}
