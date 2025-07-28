package dao;

import model.Option;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Lớp DAO (Data Access Object) để thực hiện các thao tác CRUD (Create, Read,
 * Update, Delete) với bảng `option` trong cơ sở dữ liệu.
 * <p>
 * Mỗi phương thức thao tác dữ liệu (thêm, sửa, xóa) đều có hai phiên bản:
 * <ul>
 * <li>Một phiên bản tự quản lý kết nối (dành cho các thao tác đơn lẻ).</li>
 * <li>Một phiên bản nhận {@link Connection} làm tham số để có thể tham gia vào một transaction.</li>
 * </ul>
 */
public class OptionDAO extends DBContext {

    // =========================================================================
    // PHIÊN BẢN TỰ QUẢN LÝ CONNECTION (DÙNG CHO GET)
    // =========================================================================

    /**
     * Lấy danh sách các đáp án cho một câu hỏi. (Tự quản lý Connection)
     *
     * @param questionId ID của câu hỏi.
     * @return Danh sách các đối tượng Option.
     * @throws Exception nếu có lỗi xảy ra.
     */
    public List<Option> getOptionsByQuestionId(String questionId) throws Exception {
        try (Connection conn = getConnection()) {
            return getOptionsByQuestionId(conn, questionId);
        }
    }

    /**
     * Lấy một đáp án dựa trên ID. (Tự quản lý Connection)
     *
     * @param optionId ID của đáp án.
     * @return Đối tượng Option hoặc null nếu không tìm thấy.
     * @throws Exception nếu có lỗi xảy ra.
     */
    public Option getOptionById(String optionId) throws Exception {
        try (Connection conn = getConnection()) {
            return getOptionById(conn, optionId);
        }
    }

    // =========================================================================
    // PHIÊN BẢN DÙNG CONNECTION TỪ BÊN NGOÀI (DÙNG CHO TRANSACTION)
    // =========================================================================

    /**
     * Lấy danh sách các đáp án cho một câu hỏi. (Dùng cho Transaction)
     *
     * @param conn       Đối tượng Connection để tham gia transaction.
     * @param questionId ID của câu hỏi.
     * @return Danh sách các đối tượng Option.
     * @throws SQLException nếu có lỗi SQL.
     */
    public List<Option> getOptionsByQuestionId(Connection conn, String questionId) throws SQLException {
        List<Option> options = new ArrayList<>();
        var sql = "SELECT id, content, is_true, explanation, question_id FROM `option` WHERE question_id = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, questionId);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    options.add(mapRowToOption(rs));
                }
            }
        }
        return options;
    }

    /**
     * Lấy một đáp án dựa trên ID. (Dùng cho Transaction)
     *
     * @param conn     Đối tượng Connection để tham gia transaction.
     * @param optionId ID của đáp án.
     * @return Đối tượng Option hoặc null nếu không tìm thấy.
     * @throws SQLException nếu có lỗi SQL.
     */
    public Option getOptionById(Connection conn, String optionId) throws SQLException {
        var sql = "SELECT id, content, is_true, explanation, question_id FROM `option` WHERE id = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, optionId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToOption(rs);
                }
            }
        }
        return null;
    }

    /**
     * Thêm một đáp án mới vào CSDL. (Dùng cho Transaction)
     *
     * @param conn   Đối tượng Connection để tham gia transaction.
     * @param option Đối tượng Option cần thêm.
     * @throws SQLException nếu có lỗi SQL.
     */
    public void addOption(Connection conn, Option option) throws SQLException {
        var sql = "INSERT INTO `option` (id, content, is_true, explanation, question_id) VALUES (?, ?, ?, ?, ?)";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, option.getId().toString());
            ps.setString(2, option.getContent());
            ps.setBoolean(3, option.getIsTrue()); // Dùng getIsTrue() để tương thích
            ps.setString(4, option.getExplanation());
            ps.setString(5, option.getQuestionId());
            ps.executeUpdate();
        }
    }

    /**
     * Cập nhật một đáp án đã có. (Dùng cho Transaction)
     *
     * @param conn   Đối tượng Connection để tham gia transaction.
     * @param option Đối tượng Option cần cập nhật.
     * @throws SQLException nếu có lỗi SQL.
     */
    public void updateOption(Connection conn, Option option) throws SQLException {
        var sql = "UPDATE `option` SET content = ?, is_true = ?, explanation = ? WHERE id = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, option.getContent());
            ps.setBoolean(2, option.getIsTrue());
            ps.setString(3, option.getExplanation());
            ps.setString(4, option.getId().toString());
            ps.executeUpdate();
        }
    }

    /**
     * Xóa một đáp án khỏi CSDL. (Dùng cho Transaction)
     *
     * @param conn     Đối tượng Connection để tham gia transaction.
     * @param optionId ID của đáp án cần xóa.
     * @throws SQLException nếu có lỗi SQL.
     */
    public void deleteOption(Connection conn, String optionId) throws SQLException {
        var sql = "DELETE FROM `option` WHERE id = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, optionId);
            ps.executeUpdate();
        }
    }

    /**
     * Phương thức helper private để ánh xạ dữ liệu từ một hàng của ResultSet
     * sang một đối tượng Option.
     *
     * @param rs ResultSet đang trỏ đến hàng dữ liệu hiện tại.
     * @return một đối tượng Option đã được điền đầy đủ thông tin.
     * @throws SQLException nếu có lỗi khi truy cập dữ liệu từ ResultSet.
     */
    private Option mapRowToOption(ResultSet rs) throws SQLException {
        // Lấy ID từ CSDL dưới dạng String và chuyển đổi thành UUID
        UUID idAsUUID = UUID.fromString(rs.getString("id"));

        return Option.builder()
                .id(idAsUUID)
                .content(rs.getString("content"))
                .isTrue(rs.getBoolean("is_true")) // rs.getBoolean() hoạt động tốt với TINYINT(1)
                .explanation(rs.getString("explanation"))
                .questionId(rs.getString("question_id"))
                .build();
    }
}
