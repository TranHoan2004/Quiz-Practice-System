package dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Contact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContactDAO extends DBContext {

    public List<Contact> getAllContacts() throws Exception {
        List<Contact> contacts = new ArrayList<>();
        var sql = "SELECT * FROM `swp391`.contact";
        try (var connection = getConnection();
             var pre = connection.prepareStatement(sql);
             var rs = pre.executeQuery()) {
            while (rs.next()) {
                contacts.add(getContact(rs));
            }
        }
        return contacts;
    }

    public Contact getById(String id) throws Exception {
        var contact = Contact.builder().build();
        var sql = "SELECT * FROM `swp391`.contact WHERE id = ?";
        try (var pre = getConnection().prepareStatement(sql)) {
            pre.setString(1, id);
            try (var rs = pre.executeQuery()) {
                if (rs.next()) {
                    contact = getContact(rs);
                }
            }
        }
        return contact;
    }

    private Contact getContact(ResultSet rs) throws SQLException, JsonProcessingException {
        var mapper = new ObjectMapper();
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
