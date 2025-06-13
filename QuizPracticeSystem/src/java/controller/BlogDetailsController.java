/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
 *
 * @author Huong
 */
@WebServlet(name="BlogDetailsController", urlPatterns={"/blog-details"})
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String message = "";

        String id = request.getParameter("id");

        BlogDTO blogDetails = getBlogById(id);
        List<BlogDTO> categories = getAllCategories();
        List<BlogDTO> latestBlogs = getLatestBlogs(BLOG_LATEST_LIMIT);

        request.setAttribute("blogDetails", blogDetails);
        request.setAttribute("latestBlogs", latestBlogs);
        request.setAttribute("categories", categories);
        handleRequest(request, response, message);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        request.setAttribute("message", message);
        request.getRequestDispatcher("/jsp/common-features/blog-details.jsp").forward(request, response);
    }

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

            List<BlogMedia> blogMediaList  = blogMediaDAO.getBlogMediaByBlogId(blog.getId());

            return buildBlogDTO(blog, acc, categoryName, blogMediaList);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting blog by ID", e);
            return null;
        }
    }

    private List<BlogDTO> getAllCategories() {
        try {
            return blogDAO.getCategories();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private List<BlogDTO> getLatestBlogs(int limit) {
        return convertToDTOs(blogDAO.getLatestBlogs(limit));
    }

    private List<BlogDTO> convertToDTOs(List<Blog> blogs) {
        List<BlogDTO> blogDTOs = new ArrayList<>();

        for (Blog blog : blogs) {
            try {
                Account acc = accountDAO.getAccountById(blog.getAccountId());
                if (acc == null) continue;

                List<BlogMedia> blogMediaList  = blogMediaDAO.getBlogMediaByBlogId(blog.getId());

                String categoryName = blogDAO.getCategoryNameById(blog.getCategory());
                blogDTOs.add(buildBlogDTO(blog, acc, categoryName, blogMediaList));

            } catch (Exception e) {
                logger.log(Level.WARNING, "Error converting blog to DTO", e);
            }
        }
        return blogDTOs;
    }

    private BlogDTO buildBlogDTO(
            Blog blog, Account acc, String categoryName,
            List<BlogMedia> blogMediaList) {
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
                .thumbnailUrl(blog.getThumbnailUrl())
                .flagFeature(blog.isFlagFeature())
                .blogMediaList(blogMediaList)
                .build();
    }
}
