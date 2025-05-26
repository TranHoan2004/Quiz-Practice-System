/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dao.AccountDAO;
import dao.BlogDAO;
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
@WebServlet(name="HomeController", urlPatterns={"/home"})
public class HomeController extends HttpServlet {
    private final BlogDAO blogDAO;
    private final AccountDAO accountDAO;

    public HomeController() {
        this.blogDAO = new BlogDAO();
        this.accountDAO = new AccountDAO();
    }
   
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        List<BlogDTO> hottestBlogs = handleGetTop5HottestBlogs();

        request.setAttribute("hottestBlogs", hottestBlogs);
        request.getRequestDispatcher("/jsp/home-feature/home.jsp").forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
    }

    public List<BlogDTO> handleGetTop5HottestBlogs () {
        List<BlogDTO> hottestBlogs = new ArrayList<>();


        try {
            List<Blog> blogs = blogDAO.getTop5HottestBlogs();
            for (Blog blog : blogs) {
                Account acc = accountDAO.getAccountById(blog.getAccountId());

                BlogDTO blogDTO = BlogDTO.builder()
                        .id(blog.getId().toString())
                        .accountId(acc.getId().toString())
                        .avatarUrl(acc.getImageUrl())
                        .accountName(acc.getFullName())
                        .briefInfo(blog.getBriefInfo())
                        .title(blog.getTitle())
                        .content(blog.getContent())
                        .createdDate(blog.getCreatedDate())
                        .views(blog.getViews())
                        .thumbnailUrl(blog.getThumbnailUrl())
                        .build();

                hottestBlogs.add(blogDTO);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return hottestBlogs;
    }

    public static void main(String[] args) {
        HomeController home = new HomeController();

        List<BlogDTO> hottestBlogs = home.handleGetTop5HottestBlogs();

        for (BlogDTO blog : hottestBlogs) {
            System.out.println(blog.getTitle());
        }

        System.out.println("Done!");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
