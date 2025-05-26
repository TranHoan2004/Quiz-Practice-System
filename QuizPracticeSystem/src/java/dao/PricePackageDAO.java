package dao;

import model.PricePackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
        String sql = """
                SELECT * FROM pricepackage
                """;
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {
            while (rs.next()) {
                list.add(getEntity(rs));
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return list;
    }

    public PricePackage getByCourse(String id) throws Exception {
        log.info("getByCourse");
        PricePackage pp = PricePackage.builder().build();
        String sql = """
                SELECT * FROM pricepackage p
                WHERE p.course_id=?
                """;
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pp = getEntity(rs);
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return pp;
    }

    public void deleteById(String id) throws Exception {
        String sql = """
                DELETE FROM pricepackage p
                WHERE p.id=?
                """;
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    public void create(PricePackage pp) throws Exception {
        String sql = """
                INSERT INTO pricepackage (id, course_id, title, price, sale_price, access_duration, status, description)
                VALUES (?,?,?,?,?,?,?,?)
                """;
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, pp.getId().toString());
            ps.setString(2, pp.getCourseId());
            ps.setString(3, pp.getTitle());
            ps.setInt(4, pp.getPrice());
            ps.setInt(5, pp.getSalePrice());
            ps.setInt(6, pp.getAccessDuration());
            ps.setBoolean(7, pp.isStatus());
            ps.setString(8, pp.getDescription());
            ps.executeUpdate();
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            throw e;
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
