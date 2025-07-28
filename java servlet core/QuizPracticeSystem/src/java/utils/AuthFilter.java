package utils;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import model.Account;

/**
 * <h4>AuthFilter - Bộ lọc xác thực người dùng</h4>
 *
 * <p>
 * Lọc tất cả các request tới các đường dẫn `/admin/*` và `/marketer/*`. Kiểm
 * tra người dùng đã đăng nhập chưa và có quyền truy cập phù hợp không. Nếu
 * không đăng nhập hoặc không có quyền, chuyển hướng tới trang login hoặc
 * unauthorized.
 * </p>
 */
@WebFilter({"/admin/*", "/makerter/*", "/expert/*"}) // Lưu ý: "makerter" có thể là lỗi chính tả
public class AuthFilter implements Filter {

    /**
     * Hàm xử lý chính của bộ lọc. Kiểm tra: - Người dùng đã đăng nhập chưa (dựa
     * vào session và thuộc tính "currentUser"). - Người dùng có vai trò phù hợp
     * với route đang truy cập không.
     *
     * Nếu không đăng nhập → redirect tới trang login. Nếu đăng nhập nhưng sai
     * vai trò → redirect tới trang unauthorized.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();

        // Lấy session hiện tại (nếu có)
        HttpSession session = req.getSession(false);
        Account user = (session != null) ? (Account) session.getAttribute("currentUser") : null;

        // Nếu chưa đăng nhập → chuyển hướng về trang đăng nhập
        if (user == null) {
            res.sendRedirect(contextPath + "/auth/login");
            return;
        }

        // Nếu truy cập vào trang admin mà không phải Admin, Marketer, Sale → chuyển hướng
        if (uri.contains("/admin/")) {
            if (!PermissionUtil.hasRole(req, "Admin")
                    && !PermissionUtil.hasRole(req, "Marketer")
                    && !PermissionUtil.hasRole(req, "Sale")) {
                res.sendRedirect(contextPath + "/jsp/unauthorized.jsp");
                return;
            }
        }

        // Nếu truy cập vào trang marketer mà không phải Marketer → chuyển hướng
        if (uri.contains("/marketer/")) {
            if (!PermissionUtil.hasRole(req, "Marketer") || !PermissionUtil.hasRole(req, "Admin")) {
                res.sendRedirect(contextPath + "/jsp/unauthorized.jsp");
                return;
            }
        }

        if (uri.contains("/sale/")) {
            if (!PermissionUtil.hasRole(req, "Sale") || !PermissionUtil.hasRole(req, "Admin")) {
                res.sendRedirect(contextPath + "/jsp/unauthorized.jsp");
                return;
            }
        }

        if (uri.contains("/expert/")) {
            if (!PermissionUtil.hasRole(req, "Expert") && !PermissionUtil.hasRole(req, "Admin")) {
                res.sendRedirect(contextPath + "/jsp/unauthorized.jsp");
                return;
            }
        }

        // Nếu qua được hết kiểm tra → cho phép request tiếp tục
        chain.doFilter(request, response);
    }
}
