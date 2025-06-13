/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.*;
import dto.BlogDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.Blog;

/**
 *
 * @author Huong
 */
@WebServlet(name="BlogListController", urlPatterns={"/blog-list"})
public class BlogListController extends HttpServlet {

    private final BlogDAO blogDAO;
    private final AccountDAO accountDAO;
    private final Logger logger;

    public BlogListController() {
        this.blogDAO = new BlogDAO();
        this.accountDAO = new AccountDAO();
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String message = "";
        String keyword = request.getParameter("keyword");
        String category = request.getParameter("category");
        String page = request.getParameter("page");

        List<BlogDTO> categories = getAllCategories();
        List<BlogDTO> latestBlogs = getLatestBlogs();

        try {
            int currentPage = page == null ? 1 : Integer.parseInt(page);
            int pageSize = 5;

            if (keyword == null && category == null) {
                renderBlogPagination(request, null, null, currentPage, pageSize );
            } else {
                renderBlogPagination(request, keyword, category, currentPage, pageSize);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            message = e.getMessage();
        }

        request.setAttribute("latestBlogs", latestBlogs);
        request.setAttribute("categories", categories);
        handleRequest(request, response, message);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        request.setAttribute("message", message);
        request.getRequestDispatcher("/jsp/common-features/blog-list.jsp").forward(request, response);
    }

    private List<BlogDTO> getAllCategories() {
        try {
            return blogDAO.getCategories();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private List<BlogDTO> getLatestBlogs() {
        return getBlogDTO(blogDAO.getLatestBlogs(5));
    }

    private List<BlogDTO> getBlogDTO(List<Blog> blogs) {
        List<BlogDTO> list = new ArrayList<>();
        try {
            for (Blog blog : blogs) {
                Account acc = accountDAO.getAccountById(blog.getAccountId());
                if (acc == null) continue;

                String categoryName = blogDAO.getCategoryNameById(blog.getCategory());

                BlogDTO blogDTO = BlogDTO.builder()
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
                        .build();

                list.add(blogDTO);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return list;
    }

    // Pagination
    private void renderBlogPagination(HttpServletRequest request, String keyword, String categoryId, int page, int pageSize) {
        // Decode and normalize categoryId
        if (categoryId != null) {
            categoryId = utils.Encoder.decode(categoryId).trim();
            if (categoryId.isEmpty()) {
                categoryId = null;
            }
        }

        // Fetch blogs based on filters
        List<Blog> blogs;
        if (keyword == null && categoryId == null) {
            blogs = blogDAO.getBlogs(page, pageSize, 1);
        } else {
            blogs = blogDAO.getBlogs(keyword, categoryId, page, pageSize, 1);
        }

        // Convert blogs to DTOs
        List<BlogDTO> blogDTOs = getBlogDTO(blogs);

        // Pagination info
        int totalBlogs = blogDAO.getTotalBlogs(keyword, categoryId);
        int totalPages = (int) Math.ceil((double) totalBlogs / pageSize);

        // Set attributes for the view
        request.setAttribute("blogs", blogDTOs);
        request.setAttribute("currentIndex", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalElements", totalBlogs);
    }

}
