package utils;

import dao.AccountDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;

import java.io.IOException;

/**
 * Utility class for managing and checking user permissions. This class provides
 * methods to verify if the current user has admin privileges and to handle
 * redirects for users without such privileges.
 */
public class PermissionUtil {

    private static final AccountDAO accountDAO = new AccountDAO();

    /**
     * Check if the current user has the given role.
     *
     * @param request
     * @param roleName the name of the required role (e.g., "Admin", "Marketer")
     * @return true if the user has the given role, false otherwise
     */
    public static boolean hasRole(HttpServletRequest request, String roleName) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        String userRole = (String) session.getAttribute("userRole");
        if (userRole == null) {
            return false;
        }

        return userRole.equals(roleName);
    }

    /**
     * Redirect if the user does not have the given role.
     *
     * @param request
     * @param response
     * @param requiredRoleName the name of the required role (e.g., "Admin",
     * "Marketer")
     * @param redirectUrl full redirect path (should include contextPath)
     * @return true if redirected, false if allowed
     */
    public static boolean redirectIfNotRole(HttpServletRequest request, HttpServletResponse response, String requiredRoleName, String redirectUrl) throws IOException {
        if (!hasRole(request, requiredRoleName)) {
            response.sendRedirect(redirectUrl);
            return true;
        }
        return false;
    }

}
