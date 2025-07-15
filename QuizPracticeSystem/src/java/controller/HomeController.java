package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.*;
import dto.BlogDTO;
import dto.CourseDTO;
import dto.SubjectDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;

@WebServlet(name = "HomeController", urlPatterns = {"/home"})
public class HomeController extends HttpServlet {

    private final BlogDAO blogDAO;
    private final AccountDAO accountDAO;
    private final SubjectDAO subjectDAO;
    private final TaglineDAO taglineDAO;
    private final SliderDAO sliderDAO;
    private final CourseDAO courseDAO;
    private final PersonalCourseDAO personalCourseDAO;
    private final PricePackageDAO pricePackageDAO;
    private final BlogMediaDAO blogMediaDAO;
    private final Logger logger;

    public HomeController() {
        this.blogDAO = new BlogDAO();
        this.accountDAO = new AccountDAO();
        this.subjectDAO = new SubjectDAO();
        this.taglineDAO = new TaglineDAO();
        this.sliderDAO = new SliderDAO();
        this.courseDAO = new CourseDAO();
        this.personalCourseDAO = new PersonalCourseDAO();
        this.pricePackageDAO = new PricePackageDAO();
        this.blogMediaDAO = new BlogMediaDAO();
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int blogLimit = 5;
        int featureSubjectLimit = 5;
        int courseLimit = 10;

        try {
            List<BlogDTO> hottestBlogs = getHottestBlogs(blogLimit);
            List<BlogDTO> latestBlogs = getLatestBlogs(blogLimit);
            List<SubjectDTO> featureSubject = getFeatureSubjectDTO(featureSubjectLimit);
            List<CourseDTO> courses = getFeatureCourse(courseLimit);

            request.setAttribute("sliderActive", getTopSliderActive());
            request.setAttribute("courses", courses);
            request.setAttribute("featureSubject", featureSubject);
            request.setAttribute("hottestBlogs", hottestBlogs);
            request.setAttribute("latestBlogs", latestBlogs);

            request.getRequestDispatcher("/jsp/public-features/home.jsp").forward(request, response);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            request.setAttribute("message", "Something went wrong");
            request.getRequestDispatcher("/jsp/public-features/home.jsp").forward(request, response);
        }
    }

    /**
     * <h4>Handle GET request to display home page</h4>
     * This method handles fetching featured courses.
     *
     * @param limit number of courses to retrieve
     * @return List<CourseDTO> course list or empty list if error occurs.
     * @author HuongNI
     *
     */
    private List<CourseDTO> getFeatureCourse(int limit) {
        List<CourseDTO> courses = new ArrayList<>();
        try {
            List<PersonalCourse> personalCourses = personalCourseDAO.getTopCoursePurchases(limit);
            for (PersonalCourse personalCourse : personalCourses) {
                Course course = courseDAO.getById(personalCourse.getCourseId());
                PricePackage pricePackage = pricePackageDAO.getByCourse(course.getId().toString());
                if (course == null || pricePackage == null) {
                    continue;
                }

                CourseDTO courseDTO = CourseDTO.builder()
                        .price(pricePackage.getPrice())
                        .salePrice(pricePackage.getSalePrice())
                        .id(utils.Encoder.encode(course.getId().toString()))
                        .image(course.getThumbnailUrl())
                        .description(course.getDescription())
                        .title(course.getTitle())
                        .build();

                courses.add(courseDTO);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return courses;
    }

    /**
     * <h4>Handle GET request to display home page</h4>
     * This method handles fetching the hottest blogs.
     *
     * @param limit number of blogs to retrieve
     * @return List<BlogDTO> blog list or empty list if error occurs.
     * @author HuongNI
     *
     */
    private List<BlogDTO> getHottestBlogs(int limit) {
        try {
            return getBlogDTO(blogDAO.getHottestBlogs(limit));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    /**
     * <h4>Handle GET request to display home page</h4>
     * This method handles fetching the latest blogs and hottest blogs.
     *
     * @param limit number of blogs to retrieve
     * @return List<BlogDTO> blog list or empty list if error occurs.
     * @author HuongNI
     *
     */
    private List<BlogDTO> getLatestBlogs(int limit) {
        try {
            return getBlogDTO(blogDAO.getLatestBlogs(limit));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    /**
     * <h4>Convert Blog entities to BlogDTO list</h4>
     *
     * @param blogs Blog entities
     * @return list of BlogDTOs
     * @author HuongNI
     */
    private List<BlogDTO> getBlogDTO(List<Blog> blogs) {
        if (blogs == null || blogs.isEmpty()) {
            return Collections.emptyList();
        }

        List<BlogDTO> list = new ArrayList<>();

        for (Blog blog : blogs) {
            try {
                Account acc = accountDAO.getAccountById(blog.getAccountId());
                if (acc == null) {
                    continue;
                }

                List<BlogMedia> blogMediaList = blogMediaDAO.getBlogMediaByBlogId(blog.getId());
                String categoryName = blogDAO.getCategoryNameById(blog.getCategory());

                list.add(convertToBlogDTO(blog, acc, categoryName, blogMediaList));

            } catch (Exception e) {
                logger.log(Level.WARNING, "Failed to map Blog to DTO: " + blog.getId(), e);
            }
        }
        return list;
    }

    /**
     * <h4>Convert Blog entities to BlogDTO list</h4>
     *
     * @param blog Blog entities
     * @param acc Account entities
     * @param mediaList media list
     * @param categoryName category name
     *
     * @return list of BlogDTOs
     * @author HuongNI
     */
    private BlogDTO convertToBlogDTO(Blog blog, Account acc, String categoryName, List<BlogMedia> mediaList) {
        return BlogDTO.builder()
                .id(utils.Encoder.encode(blog.getId().toString()))
                .accountId(acc.getId().toString())
                .avatarUrl(acc.getImageUrl())
                .accountName(acc.getFullName())
                .briefInfo(blog.getBriefInfo())
                .title(blog.getTitle())
                .content(blog.getContent())
                .category(categoryName)
                .status(blog.isStatus())
                .createdDate(blog.getCreatedDate())
                .flagFeature(blog.isFlagFeature())
                .views(blog.getViews())
                .blogMediaList(mediaList)
                .build();
    }

    /**
     * <h4>Get a top feature subject</h4>
     *
     * @return List<SubjectDTO> subject list or empty list if error occurs.
     */
    private List<SubjectDTO> getFeatureSubjectDTO(int limit) {
        List<SubjectDTO> list = new ArrayList<>();
        try {
            List<Subject> subjects = subjectDAO.getTopSubjectsFlag(limit);
            for (Subject subject : subjects) {
                Tagline tagline = taglineDAO.getTaglineBySubjectId(subject.getId().toString());
                list.add(SubjectDTO.builder()
                        .subjectName(subject.getName())
                        .thumbnailUrl(subject.getThumbnailURL())
                        .tagline(tagline.getName())
                        .build());
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return list;
    }

    /**
     * <h4>Get top slider active</h4>
     *
     * @return List<Slider> slider active or empty list if error occurs.
     */
    private List<Slider> getTopSliderActive() {
        try {
            return sliderDAO.getTopSliderActive(5);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}
