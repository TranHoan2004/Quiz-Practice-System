/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.AccountDAO;
import dao.BlogDAO;
import dao.BlogMediaDAO;
import dto.BlogDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.Blog;
import model.BlogMedia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Huong
 */
@WebServlet(name = "BlogDetailsController", urlPatterns = {"/blog-details"})
public class BlogDetailsController extends HttpServlet {
    private final BlogDAO blogDAO;
    private final AccountDAO accountDAO;
    private final BlogMediaDAO blogMediaDAO;
    private final Logger logger;
    private static final int BLOG_LATEST_LIMIT = 5;

    public BlogDetailsController() {
        this.blogDAO = new BlogDAO();
        this.accountDAO = new AccountDAO();
        this.blogMediaDAO = new BlogMediaDAO();
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    /**
     * <h4>Handle GET request to display blog details page</h4>
     * This method handles fetching a specific blog by ID, retrieving categories and latest blogs.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @throws ServletException if request dispatch fails
     * @throws IOException      if I/O error occurs
     * @author HuongNI
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String encodedId = request.getParameter("id");
        if (encodedId == null || encodedId.isEmpty()) {
            handleRequest(request, response, "Invalid blog ID");
            return;
        }
        try {
            BlogDTO blogDetails = getBlogById(encodedId);
            if (blogDetails == null) {
                handleRequest(request, response, "Blog not found");
                return;
            }

            request.setAttribute("blogDetails", blogDetails);
            request.setAttribute("latestBlogs", getLatestBlogs(BLOG_LATEST_LIMIT));
            request.setAttribute("categories", getAllCategories("Blog Category"));
            request.getRequestDispatcher("/jsp/common-features/blog-details.jsp").forward(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading blog details", e);
            handleRequest(request, response, "Something went wrong");
        }
    }

    /**
     * <h4>Forward to the blog details JSP with a message</h4>
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @param message  message to pass to the view
     * @throws ServletException if dispatch fails
     * @throws IOException      if I/O error occurs
     * @author HuongNI
     */
    private void handleRequest(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        request.setAttribute("message", message);
        request.getRequestDispatcher("/jsp/common-features/blog-details.jsp").forward(request, response);
    }

    /**
     * <h4>Get blog details by encoded ID</h4>
     * This method decodes the blog ID, retrieves the blog from the database,
     * increases view count, and constructs a BlogDTO.
     *
     * @param encodedId encoded blog ID
     * @return BlogDTO or null if not found
     * @author HuongNI
     */
    private BlogDTO getBlogById(String encodedId) {
        try {
            String blogId = utils.Encoder.decode(encodedId);
            Blog blog = blogDAO.getBlogById(blogId);

            if (blog == null) return null;

            // Increase view count
            blogDAO.updateBlogViews(blogId);

            Account acc = accountDAO.getAccountById(blog.getAccountId());
            if (acc == null) return null;

            String categoryName = blogDAO.getCategoryNameById(blog.getCategory());

            List<BlogMedia> blogMediaList = blogMediaDAO.getBlogMediaByBlogId(blog.getId());

            return buildBlogDTO(blog, acc, categoryName, blogMediaList);

        } catch (Exception e) {
            logger.log(Level.WARNING, "Error fetching blog by ID", e);
            return null;
        }
    }

    /**
     * <h4>Retrieve all blog categories</h4>
     *
     * @param category the category group name
     * @return list of BlogDTO representing categories
     * @author HuongNI
     */
    private List<BlogDTO> getAllCategories(String category) {
        try {
            return blogDAO.getCategories(category);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * <h4>Retrieve latest blogs</h4>
     *
     * @param limit number of blogs to retrieve
     * @return list of BlogDTO
     * @author HuongNI
     */
    private List<BlogDTO> getLatestBlogs(int limit) {
        return convertToDTOs(blogDAO.getLatestBlogs(limit));
    }

    /**
     * <h4>Convert Blog entities to BlogDTO list</h4>
     *
     * @param blogs list of Blog entities
     * @return list of BlogDTOs
     * @author HuongNI
     */
    private List<BlogDTO> convertToDTOs(List<Blog> blogs) {
        List<BlogDTO> dtoList = new ArrayList<>();
        for (Blog blog : blogs) {
            try {
                Account acc = accountDAO.getAccountById(blog.getAccountId());
                if (acc == null) continue;

                String categoryName = blogDAO.getCategoryNameById(blog.getCategory());
                List<BlogMedia> mediaList = blogMediaDAO.getBlogMediaByBlogId(blog.getId());

                dtoList.add(buildBlogDTO(blog, acc, categoryName, mediaList));
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error converting Blog to DTO", e);
            }
        }
        return dtoList;
    }

    /**
     * <h4>Build BlogDTO from related entities</h4>
     *
     * @param blog          Blog entity
     * @param acc           Account entity (author)
     * @param categoryName  blog's category name
     * @param mediaList media list associated with blog
     * @return BlogDTO instance
     * @author HuongNI
     */
    private BlogDTO buildBlogDTO(Blog blog, Account acc, String categoryName, List<BlogMedia> mediaList) {
        return BlogDTO.builder()
                .id(utils.Encoder.encode(blog.getId().toString()))
                .accountId(acc.getId().toString())
                .avatarUrl(acc.getImageUrl())
                .accountName(acc.getFullName())
                .briefInfo(blog.getBriefInfo())
                .title(blog.getTitle())
                .content(blog.getContent())
                .category(categoryName)
                .createdDate(blog.getCreatedDate())
                .views(blog.getViews())
                .flagFeature(blog.isFlagFeature())
                .blogMediaList(mediaList)
                .build();
    }
}
