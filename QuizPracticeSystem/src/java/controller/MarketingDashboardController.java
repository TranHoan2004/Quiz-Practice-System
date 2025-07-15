package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.AccountDAO;
import dao.PersonalCourseDAO;
import dao.SubjectDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.PermissionUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * <h4>MarketingDashboardController - Bộ điều khiển bảng điều khiển marketing</h4>
 *
 * <p>Servlet này chịu trách nhiệm hiển thị bảng điều khiển (dashboard) cho vai trò Marketing,
 * cho phép người dùng lọc theo khoảng thời gian, thống kê về doanh thu, đơn hàng, khóa học, môn học và tài khoản.</p>
 *
 * <p>Các số liệu thống kê được lấy từ cơ sở dữ liệu, so sánh với khoảng thời gian trước đó và hiển thị trên giao diện
 * theo dạng biểu đồ hoặc bảng số liệu.</p>
 *
 * @author HuongNI
 */
@WebServlet(name = "MarketingDashboardController", urlPatterns = {"/marketing/dashboard"})
public class MarketingDashboardController extends HttpServlet {

    private final PersonalCourseDAO personalCourseDAO = new PersonalCourseDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final AccountDAO accountDAO = new AccountDAO();

    /**
     * <h4>doGet - Xử lý yêu cầu hiển thị dashboard, redirect to homepage if not admin or marketer</h4>
     *
     * <p>Phân tích `startDate` và `endDate` từ request, mặc định là 7 ngày gần nhất nếu không có.
     * Sau đó gọi các hàm phụ để chuẩn bị dữ liệu thống kê và forward sang giao diện JSP.</p>
     *
     * @param request  yêu cầu HTTP
     * @param response phản hồi HTTP
     * @throws ServletException lỗi servlet
     * @throws IOException      lỗi vào/ra
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Check a role to access this page. If not, redirect to the home page.
//        String redirectUrl = request.getContextPath() + "/home";
//        if (PermissionUtil.redirectIfNotRole(request, response, "MARKETER" ,redirectUrl)
//                && PermissionUtil.redirectIfNotRole(request, response, "ADMIN",redirectUrl)) return;

        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        LocalDate startDate;
        LocalDate endDate;

        try {
            endDate = (endDateStr == null || endDateStr.isEmpty()) ? LocalDate.now() : LocalDate.parse(endDateStr);
            startDate = (startDateStr == null || startDateStr.isEmpty()) ? endDate.minus(7, ChronoUnit.DAYS) : LocalDate.parse(startDateStr);

            if (startDate.isAfter(endDate)) {
                sendError(request, response, "Start date must be before or equal to end date.", startDateStr, endDateStr);
                return;
            }

            fillDashboardData(request, startDate, endDate);
        } catch (DateTimeParseException e) {
            sendError(request, response, "Invalid date format.", startDateStr, endDateStr);
            return;
        } catch (Exception e) {
            sendError(request, response, "Unexpected error: " + e.getMessage(), startDateStr, endDateStr);
            return;
        }

        request.setAttribute("startDate", startDate.toString());
        request.setAttribute("endDate", endDate.toString());
        request.getRequestDispatcher("/jsp/marketing-features/dashboard.jsp").forward(request, response);
    }

    /**
     * <h4>sendError - Gửi thông báo lỗi về dashboard</h4>
     *
     * <p>Được sử dụng khi xảy ra lỗi định dạng ngày hoặc lỗi logic (ví dụ: startDate > endDate),
     * đồng thời set thuộc tính để hiển thị thông báo lỗi trên giao diện.</p>
     *
     * @param request  yêu cầu HTTP
     * @param response phản hồi HTTP
     * @param message  thông báo lỗi
     * @param start    giá trị startDate gốc
     * @param end      giá trị endDate gốc
     */
    private void sendError(HttpServletRequest request, HttpServletResponse response, String message, String start, String end) throws ServletException, IOException {
        request.setAttribute("message", message);
        request.setAttribute("type", "error");
        request.setAttribute("startDate", start);
        request.setAttribute("endDate", end);
        request.getRequestDispatcher("/jsp/marketing-features/dashboard.jsp").forward(request, response);
    }

    /**
     * <h4>fillDashboardData - Đổ dữ liệu cho dashboard</h4>
     *
     * <p>Phân tích khoảng thời gian hiện tại, lấy các dữ liệu thống kê tương ứng từ DAO,
     * bao gồm môn học, khóa học, tài khoản, doanh thu, và xu hướng đơn hàng.</p>
     *
     * @param request   yêu cầu HTTP
     * @param startDate ngày bắt đầu
     * @param endDate   ngày kết thúc
     * @throws Exception nếu có lỗi khi truy vấn
     */

    private void fillDashboardData(HttpServletRequest request, LocalDate startDate, LocalDate endDate) throws Exception {
        String start = startDate.toString();
        String end = endDate.toString();
        ObjectMapper mapper = new ObjectMapper();

        long daysBetween = ChronoUnit.DAYS.between(LocalDate.parse(start), LocalDate.parse(end)) + 1;

        setSubjectStats(request, start, end, daysBetween);
        setCourseStats(request, start, end, daysBetween);
        setAccountStats(request, start, end, daysBetween);
        setRevenueStats(request, start, end, mapper, daysBetween);
        setOrderTrends(request, startDate, endDate, mapper);
    }

    /**
     * <h4>calculatePercentChange - Tính phần trăm thay đổi</h4>
     *
     * <p>Tính phần trăm thay đổi giữa giá trị trước và hiện tại.
     * Trả về 100 nếu giá trị cũ bằng 0 và giá trị mới khác 0.</p>
     *
     * @param prev    giá trị cũ
     * @param current giá trị mới
     * @return phần trăm thay đổi làm tròn đến 2 chữ số
     */
    private double calculatePercentChange(double prev, double current) {
        if (prev == 0) return current == 0 ? 0 : 100;
        return Math.round(((current - prev) * 100.0 / prev) * 100.0) / 100.0;
    }

    /**
     * <h4>setSubjectStats - Thống kê môn học</h4>
     *
     * <p>Lấy số lượng môn học mới và tổng số môn học trong thời gian hiện tại,
     * so sánh với khoảng thời gian trước đó để tính phần trăm thay đổi.</p>
     */
    private void setSubjectStats(HttpServletRequest request, String start, String end, long daysBetween) {
        int currentNew = subjectDAO.getCountSubjectsByDate(start, end);
        int currentAll = subjectDAO.getCountAllSubjects();

        LocalDate prevStart = LocalDate.parse(start).minusDays(daysBetween);
        LocalDate prevEnd = LocalDate.parse(start).minusDays(1);

        int prevNew = subjectDAO.getCountSubjectsByDate(prevStart.toString(), prevEnd.toString());

        request.setAttribute("numberOfNewSubjects", currentNew);
        request.setAttribute("numberOfAllSubjects", currentAll);
        request.setAttribute("changePercentNewSubjects", calculatePercentChange(prevNew, currentNew));
    }

    /**
     * <h4>setCourseStats - Thống kê khóa học đã đăng ký</h4>
     *
     * <p>Phân tích số lượng đơn hàng có trạng thái: PAID, DECLINED, SENT
     * trong thời gian hiện tại và trước đó, tính phần trăm thay đổi.</p>
     */
    private void setCourseStats(HttpServletRequest request, String start, String end, long daysBetween) {
        int currentSuccess = personalCourseDAO.getCountPersonalCourseByStatus(start, end, "PAID");
        int currentCancel = personalCourseDAO.getCountPersonalCourseByStatus(start, end, "DECLINED");
        int currentSent = personalCourseDAO.getCountPersonalCourseByStatus(start, end, "SENT");

        LocalDate startDate = LocalDate.parse(start);
        String prevStart = startDate.minusDays(daysBetween).toString();
        String prevEnd = startDate.minusDays(1).toString();

        int prevSuccess = personalCourseDAO.getCountPersonalCourseByStatus(prevStart, prevEnd, "PAID");
        int prevCancel = personalCourseDAO.getCountPersonalCourseByStatus(prevStart, prevEnd, "DECLINED");
        int prevSent = personalCourseDAO.getCountPersonalCourseByStatus(prevStart, prevEnd, "SENT");

        double successChange = calculatePercentChange(prevSuccess, currentSuccess);
        double cancelChange = calculatePercentChange(prevCancel, currentCancel);
        double sentChange = calculatePercentChange(prevSent, currentSent);

        request.setAttribute("numberOfCoursesSuccess", currentSuccess);
        request.setAttribute("numberOfCoursesCancel", currentCancel);
        request.setAttribute("numberOfCoursesSummited", currentSent);

        request.setAttribute("changePercentCoursesSuccess", successChange);
        request.setAttribute("changePercentCoursesCancel", cancelChange);
        request.setAttribute("changePercentCoursesSummited", sentChange);
    }

    /**
     * <h4>setAccountStats - Thống kê tài khoản mới và người mua mới</h4>
     *
     * <p>Đếm số lượng tài khoản mới được tạo và số người mới có đơn hàng trong thời gian chỉ định,
     * tính phần trăm thay đổi so với giai đoạn trước.</p>
     */
    private void setAccountStats(HttpServletRequest request, String start, String end, long daysBetween) {
        int currentNew = accountDAO.getCountNewAccountByDate(start, end);
        int currentBought = personalCourseDAO.getCountNewPersonalCourseByNewAccount(start, end);

        LocalDate prevStart = LocalDate.parse(start).minusDays(daysBetween);
        LocalDate prevEnd = LocalDate.parse(start).minusDays(1);

        int prevNew = accountDAO.getCountNewAccountByDate(prevStart.toString(), prevEnd.toString());
        int prevBought = personalCourseDAO.getCountNewPersonalCourseByNewAccount(prevStart.toString(), prevEnd.toString());

        request.setAttribute("numberOfNewAccount", currentNew);
        request.setAttribute("numberOfNewBought", currentBought);

        request.setAttribute("changePercentNewAccounts", calculatePercentChange(prevNew, currentNew));
        request.setAttribute("changePercentNewBought", calculatePercentChange(prevBought, currentBought));
    }

    /**
     * <h4>setRevenueStats - Thống kê doanh thu theo danh mục môn học</h4>
     *
     * <p>Tổng hợp doanh thu theo danh mục môn học trong khoảng thời gian hiện tại,
     * tính tổng doanh thu và phần trăm thay đổi so với kỳ trước.</p>
     *
     * @throws Exception nếu xảy ra lỗi khi truy vấn
     */
    private void setRevenueStats(HttpServletRequest request, String start, String end, ObjectMapper mapper, long daysBetween) throws Exception {
        Map<String, Double> revenueMap = subjectDAO.getRevenueBySubjectCategory(start, end);
        double totalRevenue = revenueMap.values().stream().mapToDouble(Double::doubleValue).sum();

        request.setAttribute("revenueByCategory", mapper.writeValueAsString(revenueMap));
        request.setAttribute("totalRevenue", totalRevenue);

        LocalDate startDate = LocalDate.parse(start);
        String prevStart = startDate.minusDays(daysBetween).toString();
        String prevEnd = startDate.minusDays(1).toString();

        Map<String, Double> prevRevenueMap = subjectDAO.getRevenueBySubjectCategory(prevStart, prevEnd);
        double prevTotalRevenue = prevRevenueMap.values().stream().mapToDouble(Double::doubleValue).sum();

        request.setAttribute("changePercentRevenue", calculatePercentChange(prevTotalRevenue, totalRevenue));
    }

    /**
     * <h4>setOrderTrends - Thiết lập dữ liệu xu hướng đơn hàng</h4>
     *
     * <p>Dựa vào độ dài khoảng thời gian, phân tích theo ngày, tuần hoặc tháng.
     * Kết quả bao gồm danh sách số lượng đơn hàng đã thanh toán và tổng đơn hàng từng mốc thời gian.</p>
     */
    private void setOrderTrends(HttpServletRequest request, LocalDate start, LocalDate end, ObjectMapper mapper) throws Exception {
        List<Integer> success = new ArrayList<>();
        List<Integer> all = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        long days = ChronoUnit.DAYS.between(start, end);

        if (days <= 31) {
            fillDailyTrend(success, all, labels, start, end);
        } else if (days <= 180) {
            fillWeeklyTrend(success, all, labels, start, end);
        } else {
            fillMonthlyTrend(success, all, labels, start, end);
        }

        request.setAttribute("ordersCountTrendSuccess", success);
        request.setAttribute("ordersCountTrendAll", all);
        request.setAttribute("ordersCountTrendDate", mapper.writeValueAsString(labels));
    }

    /**
     * <h4>fillDailyTrend - Thống kê đơn hàng theo từng ngày</h4>
     *
     * <p>Thêm dữ liệu từng ngày từ `start` đến `end` vào danh sách thống kê.</p>
     */
    private void fillDailyTrend(List<Integer> success, List<Integer> all, List<String> labels, LocalDate start, LocalDate end) {
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            String s = d.toString();
            success.add(personalCourseDAO.getCountPersonalCourseByStatus(s, s, "PAID"));
            all.add(personalCourseDAO.getCountPersonalCourse(s, s));
            labels.add(s);
        }
    }

    /**
     * <h4>fillWeeklyTrend - Thống kê đơn hàng theo từng tuần</h4>
     *
     * <p>Gộp dữ liệu thành các tuần 7 ngày để hiển thị xu hướng.</p>
     */
    private void fillWeeklyTrend(List<Integer> success, List<Integer> all, List<String> labels, LocalDate start, LocalDate end) {
        for (LocalDate d = start; !d.isAfter(end); d = d.plusWeeks(1)) {
            LocalDate to = d.plusDays(6).isAfter(end) ? end : d.plusDays(6);
            String fromStr = d.toString();
            String toStr = to.toString();
            success.add(personalCourseDAO.getCountPersonalCourseByStatus(fromStr, toStr, "PAID"));
            all.add(personalCourseDAO.getCountPersonalCourse(fromStr, toStr));
            labels.add(fromStr + " - " + toStr);
        }
    }

    /**
     * <h4>fillMonthlyTrend - Thống kê đơn hàng theo từng tháng</h4>
     *
     * <p>Lấy dữ liệu theo tháng (từ ngày 1 đến cuối tháng) và tính tổng số đơn hàng.</p>
     */
    private void fillMonthlyTrend(List<Integer> success, List<Integer> all, List<String> labels, LocalDate start, LocalDate end) {
        for (LocalDate d = start.withDayOfMonth(1); !d.isAfter(end); d = d.plusMonths(1)) {
            LocalDate to = d.withDayOfMonth(d.lengthOfMonth());
            if (to.isAfter(end)) to = end;
            String fromStr = d.toString();
            String toStr = to.toString();
            success.add(personalCourseDAO.getCountPersonalCourseByStatus(fromStr, toStr, "PAID"));
            all.add(personalCourseDAO.getCountPersonalCourse(fromStr, toStr));
            labels.add(fromStr + " - " + toStr);
        }
    }
}
