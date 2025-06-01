package dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContactDAO extends DBContext {

    private final Logger logger;

    public ContactDAO() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public List<Contact> getAllContacts() throws Exception {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM `swp391`.contact";
        try (Connection connection = getConnection(); PreparedStatement pre = connection.prepareStatement(sql); ResultSet rs = pre.executeQuery()) {
            while (rs.next()) {
                contacts.add(getContact(rs));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return contacts;
    }

    public Contact getById(String id) throws Exception {
        Contact contact = Contact.builder().build();
        String sql = "SELECT * FROM `swp391`.contact WHERE id = ?";
        try (PreparedStatement pre = getConnection().prepareStatement(sql)) {
            pre.setString(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    contact = getContact(rs);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return contact;
    }

    private Contact getContact(ResultSet rs) throws SQLException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return Contact.builder()
                .id(UUID.fromString(rs.getString("id")))
                .name(rs.getString("name"))
                .link(mapper.readValue(
                        rs.getString("link"),
                        new TypeReference<>() {
                }
                ))
                .email(rs.getString("email"))
                .phone(rs.getString("phone"))
                .address(rs.getString("address"))
                .build();
    }
}
