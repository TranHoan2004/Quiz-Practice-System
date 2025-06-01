/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.*;
import dto.BlogDTO;
import dto.SubjectDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;

/**
 *
 * @author Huong
 */
@WebServlet(name = "HomeController", urlPatterns = {"/home"})
public class HomeController extends HttpServlet {

    private final BlogDAO blogDAO;
    private final AccountDAO accountDAO;
    private final SubjectDAO subjectDAO;
    private final TaglineDAO taglineDAO;
    private final SliderDAO sliderDAO;

    public HomeController() {
        this.blogDAO = new BlogDAO();
        this.accountDAO = new AccountDAO();
        this.subjectDAO = new SubjectDAO();
        this.taglineDAO = new TaglineDAO();
        this.sliderDAO = new SliderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<BlogDTO> hottestBlogs = handleGetTop5HottestBlogs();
        List<BlogDTO> latestBlogs = handleGetLatestBlogs();
        List<SubjectDTO> featureSubject = getTopFeatureSubjectDTO();

        request.setAttribute("sliderActive", getTopSliderActive());
        request.setAttribute("featureSubject", featureSubject);
        request.setAttribute("hottestBlogs", hottestBlogs);
        request.setAttribute("latestBlogs", latestBlogs);
        request.getRequestDispatcher("/jsp/home-feature/home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }

    private List<BlogDTO> handleGetTop5HottestBlogs() {
        try {
            return getBlogDTO(blogDAO.getTop5HottestBlogs());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<BlogDTO> handleGetLatestBlogs() {
        try {
            return getBlogDTO(blogDAO.getLatestBlogs(3));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<BlogDTO> getBlogDTO(List<Blog> blogs) {
        List<BlogDTO> list = new ArrayList<>();

        try {
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

                list.add(blogDTO);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        return list;
    }

    private List<SubjectDTO> getTopFeatureSubjectDTO() {
        List<SubjectDTO> list = new ArrayList<>();
        try {
            List<Subject> subjects = subjectDAO.getTopSubjectsFlag(4);
            for (Subject subject : subjects) {
                Tagline tagline = taglineDAO.getTaglieBySubjectId(subject.getId().toString());
                list.add(SubjectDTO.builder()
                                .subjectName(subject.getName())
                                .thumbnailUrl(subject.getThumbnailURL())
                                .tagline(tagline.getName())
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    private List<Slider> getTopSliderActive() {
        try {
            return sliderDAO.getTopSliderActive(5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
