/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.SourceItemDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Topic;

/**
 * @author TranHoan
 */
public class TopicDAO extends DBContext {

    private final Logger logger;

    public TopicDAO() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    /**
     * Return Topic object if its id equals to parameter. The return
     * value can be empty or not
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Topic getTopicById(String id) throws Exception {
        logger.log(Level.INFO, "getTopicById {0}", id);
        Topic st = Topic.builder().build();
        String sql = """
                SELECT * FROM `swp391`.topic s
                WHERE s.id = ?
                """;
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setString(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                while (rs.next()) {
                    st = Topic.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .name(rs.getString("name"))
                            .subjectId(rs.getString("subject_id"))
                            .build();
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return st;
    }

    public List<Topic> getTopicBySubjectId(String id) throws Exception {
        logger.log(Level.INFO, "getTopicBySubjectId {0}", id);
        List<Topic> st = new ArrayList<>();
        String sql = """
                SELECT * FROM topic t
                WHERE t.subject_id = ?
                """;
        return query(st, sql);
    }

    public List<Topic> getAllTopic() throws Exception {
        logger.info("getAllTopic");
        List<Topic> list = new ArrayList<>();
        String sql = "SELECT * FROM topic";
        return query(list, sql);
    }

    public void createTopic(Topic topic) throws Exception {
        logger.info("createTopic");
        String sql = """
                INSERT INTO `swp391`.topic (id, name, subject_id)
                VALUES (?, ?, ?)
                """;
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setString(1, topic.getId().toString());
            pre.setString(2, topic.getName());
            pre.setString(3, topic.getSubjectId());
            pre.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    public void deleteTopicById(String id) throws Exception {
        String sql = "DELETE FROM `swp391`.topic t WHERE t.id = ?";
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setString(1, id);
            pre.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }
    
    public String getSubjectIdByTopicId(String topicId) {
        String sql = "SELECT * FROM `swp391`.topic WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setString(1, topicId);  

            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("subject_id");
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }
    
//    Lấy danh sách Topic từ SubjectId
    public List<Topic> getTopicsBySubjectId(String subjectId) throws Exception {
    List<Topic> topicList = new ArrayList<>();

    String sql = "SELECT id, name, subject_id FROM `swp391`.topic WHERE subject_id = ?";

    try (Connection conn = getConnection(); PreparedStatement pre = conn.prepareStatement(sql)) {
        pre.setString(1, subjectId);

        try (ResultSet rs = pre.executeQuery()) {
            while (rs.next()) {
                Topic topic = new Topic();
                topic.setId(UUID.fromString(rs.getString("id")));
                topic.setName(rs.getString("name"));
                topic.setSubjectId(rs.getString("subject_id"));

                topicList.add(topic);
            }
        }
    } catch (Exception e) {
        logger.log(Level.SEVERE, "Error getting topics by subjectId: " + e.getMessage(), e);
        throw e;
    }

    return topicList;
}

    private List<Topic> query(List<Topic> st, String sql) throws Exception {
        try (Connection connection = getConnection();
             PreparedStatement pre = connection.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {
            while (rs.next()) {
                st.add(Topic.builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .name(rs.getString("name"))
                        .subjectId(rs.getString("subject_id"))
                        .build());
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return st;
    }
    
    public List<SourceItemDTO> getListSourceItemTopic(List<Topic> topicList) {
        List<SourceItemDTO> sourceItemDTOList = new ArrayList<>();

        if (topicList == null || topicList.isEmpty()) {
            return sourceItemDTOList;
        }

        for (Topic topic : topicList) {
            if (topic == null) {
                continue; // tránh NullPointerException
            }
            SourceItemDTO sourceItem = new SourceItemDTO();
            sourceItem.setId(topic.getId());
            sourceItem.setValue(topic.getName());
            sourceItem.setSourceType("topic");
            sourceItemDTOList.add(sourceItem);
        }

        return sourceItemDTOList;
    }

}



