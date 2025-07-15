package controller;

import dao.*;
import dto.BlogDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Account;
import model.Blog;
import model.BlogMedia;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "BlogListController", urlPatterns = {"/blog-list"})
public class BlogListController extends HttpServlet {

    private final BlogDAO blogDAO = new BlogDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final BlogMediaDAO blogMediaDAO = new BlogMediaDAO();
    private final Logger logger = Logger.getLogger(BlogListController.class.getName());

    /**
     * <h4>Handle GET request for blog list page</h4>
     * Handles search and filtering based on keyword and category,
     * applies pagination and forwards to JSP view.
     *
     * @param request  the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if forward fails
     * @throws IOException      if I/O error occurs
     * @author HuongNI
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String keyword = request.getParameter("keyword");
        String category = request.getParameter("category");
        int currentPage = parsePage(request.getParameter("page"));
        int pageSize = 5;

        List<BlogDTO> latestBlogs = loadLatestBlogs(5);
        List<BlogDTO> categories = loadCategories("Blog Category");

        try {
            renderBlogPagination(request, keyword, category, currentPage, pageSize);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in blog pagination: " + e.getMessage(), e);
            request.setAttribute("message", "Unable to load blog list.");
        }

        request.setAttribute("latestBlogs", latestBlogs);
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("/jsp/common-features/blog-list.jsp").forward(request, response);
    }


    /**
     * <h4>Parse page string to number</h4>
     *
     * @param pageParam  pageNumber String
     **/
    private int parsePage(String pageParam) {
        try {
            return (pageParam != null) ? Integer.parseInt(pageParam) : 1;
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    /**
     * <h4>Get all categories with the given group name</h4>
     *
     * @param groupName category group name
     * @return list of BlogDTO representing categories
     * @author HuongNI
     */
    private List<BlogDTO> loadCategories(String groupName) {
        try {
            return blogDAO.getCategories(groupName);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load categories: " + e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * <h4>Retrieve latest blogs</h4>
     *
     * @return list of BlogDTO with recent blog posts
     * @author HuongNI
     */
    private List<BlogDTO> loadLatestBlogs(int limit) {
        List<Blog> blogs = blogDAO.getLatestBlogs(limit);
        return convertToDTOs(blogs);
    }

    /**
     * <h4>Render blog pagination with optional filters</h4>
     *
     * @param request    the HttpServletRequest
     * @param keyword    search keyword (nullable)
     * @param categoryEncoded encoded category ID (nullable)
     * @param page       current page number
     * @param pageSize   number of items per page
     * @author HuongNI
     */
    private void renderBlogPagination(HttpServletRequest request, String keyword, String categoryEncoded, int page, int pageSize) {
        String categoryId = (categoryEncoded != null) ? utils.Encoder.decode(categoryEncoded).trim() : null;
        if (categoryId != null && categoryId.isEmpty()) {
            categoryId = null;
        }

        List<Blog> blogs = (keyword == null && categoryId == null)
                ? blogDAO.getBlogs(null, null, page, pageSize, 1)
                : blogDAO.getBlogs(keyword, categoryId, page, pageSize, 1);

        List<BlogDTO> blogDTOs = convertToDTOs(blogs);

        int totalBlogs = blogDAO.getTotalBlogs(keyword, categoryId, null);
        int totalPages = (int) Math.ceil((double) totalBlogs / pageSize);

        request.setAttribute("blogs", blogDTOs);
        request.setAttribute("currentIndex", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalElements", totalBlogs);
    }

    /**
     * <h4>Convert Blog entities to BlogDTO list</h4>
     *
     * @param blogs list of Blog entities
     * @return list of BlogDTOs
     * @author HuongNI
     */
    private List<BlogDTO> convertToDTOs(List<Blog> blogs) {
        List<BlogDTO> list = new ArrayList<>();
        for (Blog blog : blogs) {
            try {
                Account acc = accountDAO.getAccountById(blog.getAccountId());
                if (acc == null) continue;

                String categoryName = blogDAO.getCategoryNameById(blog.getCategory());
                List<BlogMedia> mediaList = blogMediaDAO.getBlogMediaByBlogId(blog.getId());

                BlogDTO dto = BlogDTO.builder()
                        .id(utils.Encoder.encode(blog.getId().toString()))
                        .accountId(acc.getId().toString())
                        .avatarUrl(acc.getImageUrl())
                        .accountName(acc.getFullName())
                        .briefInfo(blog.getBriefInfo())
                        .title(blog.getTitle())
                        .content(blog.getContent())
                        .category(categoryName)
                        .categoryId(utils.Encoder.encode(blog.getCategory().toString()))
                        .createdDate(blog.getCreatedDate())
                        .views(blog.getViews())
                        .status(blog.isStatus())
                        .flagFeature(blog.isFlagFeature())
                        .blogMediaList(mediaList)
                        .build();

                list.add(dto);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Failed to convert Blog to DTO: " + e.getMessage(), e);
            }
        }
        return list;
    }
}
