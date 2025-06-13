package dao;

import dto.RegistrationDTO;
import model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author TranHoan
 */
public class CourseDAO extends DBContext {

    private final Logger logger;

    public CourseDAO() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public List<Course> getAllCourses() throws Exception {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM `swp391`.course";
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {
            while (rs.next()) {
                list.add(buildCourse(rs));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return list;
    }

    public Course getById(String id) throws Exception {
        Course s = Course.builder().build();
        String sql = "SELECT * FROM `swp391`.course s WHERE s.id = ?";
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setString(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                s = getEntityFromResultSet(s, rs);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());

        }
        return s;
    }

    public Course getByTopic(String topicId) throws Exception {
        logger.info("getByTopic " + topicId);
        Course s = Course.builder().build();
        String sql = "SELECT * FROM `swp391`.course s WHERE s.topic_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setString(1, topicId);
            try (ResultSet rs = pre.executeQuery()) {
                s = getEntityFromResultSet(s, rs);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
            return s;
        }
    }

    public void deleteById(String id) throws Exception {
        logger.info("Deleting course with id " + id);
        String sql = "DELETE FROM `swp391`.course s WHERE s.id = ?";
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setString(1, id);
            pre.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    public void create(Course c) throws Exception {
        String sql = """
                INSERT INTO `swp391`.course (id, title, duration, description, created_date, updated_date, thumbnail_url, number_of_lessons, topic_id, contact, expert_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setString(1, c.getId().toString());
            pre.setString(2, c.getTitle());
            pre.setFloat(3, c.getDuration());
            pre.setBoolean(4, c.isStatus());
            pre.setString(5, c.getDescription());
            pre.setString(6, c.getDescription());
            pre.setObject(7, c.getCreatedDate());
            pre.setObject(8, c.getUpdatedDate());
            pre.setString(9, c.getThumbnailUrl());
            pre.setInt(10, c.getNumberOfLessons());
            pre.setString(11, c.getTopicId());
            pre.setString(12, c.getExpertId());
            pre.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    private Course getEntityFromResultSet(Course s, ResultSet rs) throws SQLException {
        while (rs.next()) {
            s = buildCourse(rs);
        }
        return s;
    }
    
    public List<RegistrationDTO> pagingRegistrationDto(
            int index,
            int numberOfLine,
            String subject,
            String status,
            LocalDate validFrom,
            LocalDate validTo,
            String email
    ) throws Exception {
        List<RegistrationDTO> list = new ArrayList<>();

        // GIẢI PHÁP:
        // 1. Đổi thành LEFT JOIN để lấy cả những đăng ký của khóa học chưa có gói giá.
        // 2. Thêm GROUP BY để chống trùng lặp dữ liệu khi một khóa học có nhiều gói giá.
        // 3. Dùng MIN() để chọn ra 1 gói giá nếu có nhiều gói (bạn có thể đổi thành MAX() tùy logic).
        StringBuilder sql = new StringBuilder(
                "SELECT "
                + "pc.id, "
                + "acc.email, "
                + "pc.enroll_date AS registration_time, "
                + "s.name AS subject_name, "
                + "MIN(pp.title) AS package_name, " // Dùng MIN() để lấy 1 giá trị
                + "MIN(pp.sale_price) AS total_cost, " // Dùng MIN()
                + "pc.status, "
                + "pc.enroll_date AS valid_from, "
                + "pc.expire_date AS valid_to "
                + "FROM personalcourse pc "
                + "JOIN account acc ON pc.account_id = acc.id "
                + "JOIN course c ON pc.course_id = c.id "
                + "JOIN topic t ON c.topic_id = t.id "
                + "JOIN subject s ON t.subject_id = s.id "
                + "LEFT JOIN pricepackage pp ON pc.course_id = pp.course_id " // GIẢI PHÁP 1: Dùng LEFT JOIN
                + "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        // Các điều kiện WHERE giữ nguyên, không thay đổi
        if (subject != null && !subject.isEmpty()) {
            sql.append("AND s.name = ? ");
            params.add(subject);
        }
        if (status != null && !status.isEmpty()) {
            sql.append("AND pc.status = ? ");
            params.add(status);
        }
        if (validFrom != null) {
            sql.append("AND pc.enroll_date >= ? ");
            params.add(validFrom);
        }
        if (validTo != null) {
            sql.append("AND pc.expire_date <= ? ");
            params.add(validTo);
        }
        if (email != null && !email.isEmpty()) {
            sql.append("AND acc.email LIKE ? ");
            params.add("%" + email + "%");
        }

        // GIẢI PHÁP 2: Thêm GROUP BY
        sql.append("GROUP BY pc.id, acc.email, pc.enroll_date, s.name, pc.status, pc.expire_date ");
        sql.append("ORDER BY pc.enroll_date DESC LIMIT ? OFFSET ? ");
        params.add(numberOfLine);
        params.add((index - 1) * numberOfLine);

        try (Connection conn = getConnection(); PreparedStatement pre = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pre.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pre.executeQuery()) {
                while (rs.next()) {
                    RegistrationDTO dto = RegistrationDTO.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .email(rs.getString("email"))
                            .registrationTime(rs.getObject("registration_time", LocalDate.class))
                            .subject(rs.getString("subject_name"))
                            .packageName(rs.getString("package_name")) // Có thể NULL, builder sẽ xử lý
                            .totalCost(rs.getFloat("total_cost")) // Sẽ là 0 nếu NULL
                            .status(rs.getString("status"))
                            .validFrom(rs.getObject("valid_from", LocalDate.class))
                            .validTo(rs.getObject("valid_to", LocalDate.class))
                            .build();
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            // logger.log(Level.SEVERE, e.getMessage(), e); // Nên có logger
            throw e;
        }

        return list;
    }

    public int getTotalRegistrationDto(
            String subject,
            String status,
            LocalDate validFrom,
            LocalDate validTo,
            String email
    ) throws Exception {
        int total = 0;

        // GIẢI PHÁP: Câu lệnh SQL phải nhất quán với phương thức ở trên.
        // Do GROUP BY trên nhiều cột, cách đếm chính xác nhất là đếm số dòng từ một subquery.
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) as total FROM ( "
                + "SELECT pc.id "
                + "FROM personalcourse pc "
                + "JOIN account acc ON pc.account_id = acc.id "
                + "JOIN course c ON pc.course_id = c.id "
                + "JOIN topic t ON c.topic_id = t.id "
                + "JOIN subject s ON t.subject_id = s.id "
                // LỖI GỐC Ở ĐÂY: Thiếu JOIN với pricepackage
                // GIẢI PHÁP: Thêm LEFT JOIN và GROUP BY để đồng bộ logic
                + "LEFT JOIN pricepackage pp ON pc.course_id = pp.course_id "
                + "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        // Các điều kiện WHERE giữ nguyên
        if (subject != null && !subject.isEmpty()) {
            sql.append("AND s.name = ? ");
            params.add(subject);
        }
        if (status != null && !status.isEmpty()) {
            sql.append("AND pc.status = ? ");
            params.add(status);
        }
        if (validFrom != null) {
            sql.append("AND pc.enroll_date >= ? ");
            params.add(validFrom);
        }
        if (validTo != null) {
            sql.append("AND pc.expire_date <= ? ");
            params.add(validTo);
        }
        if (email != null && !email.isEmpty()) {
            sql.append("AND acc.email LIKE ? ");
            params.add("%" + email + "%");
        }

        sql.append(" GROUP BY pc.id) AS subquery"); // Đóng subquery

        try (Connection conn = getConnection(); PreparedStatement pre = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pre.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt("total");
                }
            }
        } catch (Exception e) {
            // logger.log(Level.SEVERE, e.getMessage(), e); // Nên có logger
            throw e;
        }

        return total;
    }


    private Course buildCourse(ResultSet rs) throws SQLException {
        return Course.builder()
                .id(UUID.fromString(rs.getString("id")))
                .title(rs.getString("title"))
                .duration(rs.getFloat("duration"))
                .status(rs.getInt("status") == 1)
                .description(rs.getString("description"))
                .createdDate(rs.getObject("created_date", LocalDate.class))
                .updatedDate(rs.getObject("updated_date", LocalDate.class))
                .thumbnailUrl(rs.getString("thumbnail_url"))
                .numberOfLessons(rs.getInt("number_of_lessons"))
                .topicId(rs.getString("topic_id"))
                .contact(rs.getString("contact"))
                .expertId(rs.getString("expert_id"))
                .build();
    }
}
