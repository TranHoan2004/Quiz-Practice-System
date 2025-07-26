package dao;

import model.PricePackage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author TranHoan
 */
public class PricePackageDAO extends DBContext {

    private final Logger log;

    public PricePackageDAO() {
        log = Logger.getLogger(this.getClass().getName());
    }

    public List<PricePackage> getAll() throws Exception {
        List<PricePackage> list = new ArrayList<>();
        var sql = """
                SELECT * FROM `swp391`.pricepackage
                """;
        try (var connection = getConnection(); var pre = connection.prepareStatement(sql); var rs = pre.executeQuery()) {
            while (rs.next()) {
                list.add(getEntity(rs));
            }
        }
        return list;
    }

    public PricePackage getByCourse(String id) throws Exception {
        var pp = PricePackage.builder().build();
        var sql = """
                SELECT * FROM `swp391`.pricepackage p
                WHERE p.course_id=?
                """;
        try (var connection = getConnection(); var ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    pp = getEntity(rs);
                }
            }
        }
        return pp;
    }

    public void deleteById(String id) throws Exception {
        var sql = """
                DELETE FROM `swp391`.pricepackage p
                WHERE p.id=?
                """;
        try (var connection = getConnection(); var ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    public void create(PricePackage pp) throws Exception {
        var sql = """
                INSERT INTO `swp391`.pricepackage (id, course_id, title, price, sale_price, access_duration, status, description)
                VALUES (?,?,?,?,?,?,?,?)
                """;
        try (var connection = getConnection(); var ps = connection.prepareStatement(sql)) {
            ps.setString(1, pp.getId().toString());
            ps.setString(2, pp.getCourseId());
            ps.setString(3, pp.getTitle());
            ps.setInt(4, pp.getPrice());
            ps.setInt(5, pp.getSalePrice());
            ps.setInt(6, pp.getAccessDuration());
            ps.setBoolean(7, pp.isStatus());
            ps.setString(8, pp.getDescription());
            ps.executeUpdate();
        }
    }

    public List<PricePackage> getByCourseId(String courseId) throws Exception {
        List<PricePackage> list = new ArrayList<>();
        var sql = """
                SELECT * FROM `swp391`.pricepackage
                WHERE course_id = ?
                """;
        try (var connection = getConnection(); var ps = connection.prepareStatement(sql)) {
            ps.setString(1, courseId);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(getEntity(rs));
                }
            }
        }
        return list;
    }

    public void update(PricePackage pp) throws Exception {
        var sql = """
                    UPDATE `swp391`.pricepackage
                    SET title = ?, price = ?, sale_price = ?, access_duration = ?, status = ?
                    WHERE id = ?
                """;
        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, pp.getTitle());
            ps.setInt(2, pp.getPrice());
            ps.setInt(3, pp.getSalePrice());
            ps.setInt(4, pp.getAccessDuration());
            ps.setBoolean(5, pp.isStatus());
            ps.setString(6, pp.getId().toString());
            ps.executeUpdate();
        }
    }

    private PricePackage getEntity(ResultSet rs) throws Exception {
        return PricePackage.builder()
                .id(UUID.fromString(rs.getString("id")))
                .courseId(rs.getString("course_id"))
                .title(rs.getString("title"))
                .price(rs.getInt("price"))
                .salePrice(rs.getInt("sale_price"))
                .accessDuration(rs.getInt("access_duration"))
                .status(rs.getBoolean("status"))
                .description(rs.getString("description"))
                .build();
    }

}
