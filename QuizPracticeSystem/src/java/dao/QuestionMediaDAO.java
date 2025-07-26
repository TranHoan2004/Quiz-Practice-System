package dao;

import model.QuestionMedia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lớp DAO (Data Access Object) để thực hiện các thao tác CRUD với bảng
 * `question_media`.
 * <p>
 * Lớp này cũng được thiết kế để hỗ trợ transaction bằng cách cung cấp các phiên
 * bản phương thức nhận một đối tượng {@link Connection}.
 */
public class QuestionMediaDAO extends DBContext {

    private final Logger logger = Logger.getLogger(QuestionMediaDAO.class.getName());

    /**
     * Phương thức helper để ánh xạ một hàng từ ResultSet thành đối tượng
     * QuestionMedia.
     *
     * @param rs ResultSet đang trỏ đến hàng dữ liệu.
     * @return Đối tượng QuestionMedia.
     * @throws SQLException nếu có lỗi SQL.
     */
    private QuestionMedia mapRowToQuestionMedia(ResultSet rs) throws SQLException {
        return QuestionMedia.builder()
                .id(UUID.fromString(rs.getString("id")))
                .questionId(rs.getString("question_id"))
                .mediaType(rs.getString("media_type"))
                .filePath(rs.getString("file_path"))
                .caption(rs.getString("caption"))
                .displayOrder(rs.getInt("display_order"))
                .build();
    }

    // =========================================================================
    // PHIÊN BẢN TỰ QUẢN LÝ CONNECTION
    // =========================================================================
    /**
     * Lấy danh sách media của một câu hỏi. (Tự quản lý Connection)
     *
     * @param questionId ID của câu hỏi.
     * @return Danh sách QuestionMedia.
     * @throws Exception nếu có lỗi.
     */
    public List<QuestionMedia> getMediaByQuestionId(String questionId) throws Exception {
        try (var conn = getConnection()) {
            return getMediaByQuestionId(conn, questionId);
        }
    }

    /**
     * Lấy thứ tự hiển thị lớn nhất của media trong một câu hỏi. (Tự quản lý
     * Connection)
     *
     * @param questionId ID của câu hỏi.
     * @return Thứ tự hiển thị lớn nhất, hoặc 0 nếu chưa có media.
     * @throws Exception nếu có lỗi.
     */
    public int getMaxDisplayOrder(String questionId) throws Exception {
        try (var conn = getConnection()) {
            return getMaxDisplayOrder(conn, questionId);
        }
    }

    // =========================================================================
    // PHIÊN BẢN DÙNG CONNECTION TỪ BÊN NGOÀI (CHO TRANSACTION)
    // =========================================================================
    /**
     * Lấy danh sách media của một câu hỏi. (Dùng cho Transaction)
     *
     * @param conn Connection cho transaction.
     * @param questionId ID của câu hỏi.
     * @return Danh sách QuestionMedia.
     * @throws SQLException nếu có lỗi SQL.
     */
    public List<QuestionMedia> getMediaByQuestionId(Connection conn, String questionId) throws SQLException {
        List<QuestionMedia> mediaList = new ArrayList<>();
        var sql = "SELECT * FROM `question_media` WHERE question_id = ? ORDER BY display_order ASC";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, questionId);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    mediaList.add(mapRowToQuestionMedia(rs));
                }
            }
        }
        return mediaList;
    }

    /**
     * Lấy một media cụ thể bằng ID. (Dùng cho Transaction)
     *
     * @param conn Connection cho transaction.
     * @param mediaId ID của media.
     * @return Đối tượng QuestionMedia hoặc null nếu không tìm thấy.
     * @throws SQLException nếu có lỗi SQL.
     */
    public QuestionMedia getMediaById(Connection conn, String mediaId) throws SQLException {
        var sql = "SELECT * FROM `question_media` WHERE id = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, mediaId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToQuestionMedia(rs);
                }
            }
        }
        return null;
    }

    public void addQuestionMedia(QuestionMedia media, Connection conn) throws SQLException {
        String sql = "INSERT INTO `question_media` (id, question_id, media_type, file_path, caption, display_order) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, media.getId().toString());
            ps.setString(2, media.getQuestionId());
            ps.setString(3, media.getMediaType());
            ps.setString(4, media.getFilePath());
            ps.setString(5, media.getCaption());
            ps.setInt(6, media.getDisplayOrder());

            ps.executeUpdate();
        }
    }

    /**
     * Cập nhật thông tin một media đã có. (Dùng cho Transaction)
     *
     * @param conn Connection cho transaction.
     * @param media Đối tượng QuestionMedia cần cập nhật.
     * @throws SQLException nếu có lỗi SQL.
     */
    public void updateQuestionMedia(Connection conn, QuestionMedia media) throws SQLException {
        var sql = "UPDATE `question_media` SET file_path = ?, caption = ?, display_order = ? WHERE id = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, media.getFilePath());
            ps.setString(2, media.getCaption());
            ps.setInt(3, media.getDisplayOrder());
            ps.setString(4, media.getId().toString());
            ps.executeUpdate();
        }
    }

    /**
     * Xóa một media khỏi CSDL. (Dùng cho Transaction)
     *
     * @param conn Connection cho transaction.
     * @param mediaId ID của media cần xóa.
     * @throws SQLException nếu có lỗi SQL.
     */
    public void deleteQuestionMedia(Connection conn, String mediaId) throws SQLException {
        var sql = "DELETE FROM `question_media` WHERE id = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, mediaId);
            ps.executeUpdate();
        }
    }

    /**
     * Lấy thứ tự hiển thị lớn nhất của media trong một câu hỏi. (Dùng cho
     * Transaction)
     *
     * @param conn Connection cho transaction.
     * @param questionId ID của câu hỏi.
     * @return Thứ tự hiển thị lớn nhất, hoặc 0 nếu chưa có media.
     * @throws SQLException nếu có lỗi SQL.
     */
    public int getMaxDisplayOrder(Connection conn, String questionId) throws SQLException {
        var sql = "SELECT MAX(display_order) FROM `question_media` WHERE question_id = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, questionId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    // rs.getInt(1) sẽ trả về 0 nếu kết quả là NULL
                    return rs.getInt(1);
                }
            }
        }
        return 0; // Trả về 0 nếu không có bản ghi nào
    }
}
