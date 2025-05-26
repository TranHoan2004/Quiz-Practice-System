package dao;

import model.Blog;

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
        String sql = "SELECT * FROM blog";
        List<Blog> list = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            query(list, ps);
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, e.getMessage());
        }
        return list;
    }

    public List<Blog> getTop5HottestBlogs() throws Exception {
        logger.info("getTop5HottestBlogs");
        List<Blog> list = new ArrayList<>();
        String sql = "SELECT * FROM blog ORDER BY views DESC LIMIT 5";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            query(list, ps);
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, e.getMessage());
        }
        return list;
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

    public static void main(String[] args) {
        BlogDAO dao = new BlogDAO();
        try {
            List<Blog> list = dao.getTop5HottestBlogs();
            for (Blog blog : list) {
                System.out.println(blog);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
