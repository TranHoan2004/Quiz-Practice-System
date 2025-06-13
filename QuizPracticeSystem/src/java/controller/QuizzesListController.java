/*
 * Click nbsp://.netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbsp://.netbeans/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dao.PersonalQuizDAO;
import dao.QuizDAO;
import dao.QuizLevelDAO;
import dao.QuizTypeDAO;
import dao.SubjectDAO;
import dto.QuizDTO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.QuizType;
import model.Subject;

/**
 *
 * @author Admin
 */
@WebServlet(name="QuizzesListController", urlPatterns={"/quizzeslist"})
public class QuizzesListController extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet QuizzesListController</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet QuizzesListController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // Phương thức này lấy dữ liệu quiz và các thông tin cần thiết để hiển thị trên JSP
    private void loadQuizData(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
        QuizTypeDAO typeQuizDao = new QuizTypeDAO();
        SubjectDAO subjectDao = new SubjectDAO();
        QuizDAO quizDao = new QuizDAO();

        try {
            String pageStr = request.getParameter("page");
            int page = (pageStr != null && !pageStr.isEmpty()) ? Integer.parseInt(pageStr) : 1;

            String subjectId = request.getParameter("subjectId");
            String type = request.getParameter("type");
            String title = request.getParameter("title");

            List<QuizType> quizTypeList = typeQuizDao.getAllQuizType();
            List<Subject> subjectList = subjectDao.getAllSubjects();

            List<QuizDTO> quizDtoList = quizDao.pagingQuiz(page, subjectId, type, title);

            int totalQuizzes = quizDao.getTotalQuizDto(subjectId, type, title);
            int pageSize = 5;
            int endPage = totalQuizzes / pageSize;
            if (totalQuizzes % pageSize != 0) {
                endPage++;
            }

            request.setAttribute("quizTypeList", quizTypeList);
            request.setAttribute("subjectList", subjectList);
            request.setAttribute("quizDtoList", quizDtoList);
            request.setAttribute("endPage", endPage);
            request.setAttribute("currentPage", page);
        } catch (Exception e) {
            request.setAttribute("error", "Error retrieving data: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        loadQuizData(request, response);
        request.getRequestDispatcher("/jsp/course-features/quizzes_list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        QuizDAO quizDao = new QuizDAO();

        try {
            String action = request.getParameter("action");

            if ("delete".equals(action)) {
                String quizId = request.getParameter("quizId");
                quizDao.deleteQuiz(quizId);
            }

            // Xử lý form Search
            loadQuizData(request, response);
            request.getRequestDispatcher("/jsp/course-features/quizzes_list.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Error processing request: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles quiz listing, searching, and deleting";
    }
}