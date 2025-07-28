/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.LearningMaterial;

/**
 * @author Lenovo
 */
public class LearningMaterialDAO extends DBContext {

    private final Logger logger;

    public LearningMaterialDAO() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public LearningMaterial getById(String id) throws Exception {
        LearningMaterial material = null;

        var sql = "SELECT * FROM `swp391`.learningmaterial WHERE id = ?";

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, id);
            try (var rs = pre.executeQuery()) {
                if (rs.next()) {
                    material = LearningMaterial.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .title(rs.getString("title"))
                            .updatedDate(rs.getDate("updated_date") != null ? rs.getDate("updated_date").toLocalDate() : null)
                            .duration(rs.getInt("duration"))
                            .videoContentUrl(rs.getString("video_content_url"))
                            .htmlContent(rs.getString("html_content"))
                            .build();
                }
            }
        }
        return material;
    }

    public void insert(LearningMaterial material) throws Exception {
        var sql = """
                INSERT INTO `swp391`.learningmaterial
                (id, title, updated_date, duration, video_content_url, html_content)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, material.getId().toString());
            pre.setString(2, material.getTitle());

            if (material.getUpdatedDate() != null) {
                pre.setDate(3, java.sql.Date.valueOf(material.getUpdatedDate()));
            } else {
                pre.setDate(3, null);
            }

            pre.setInt(4, material.getDuration());
            pre.setString(5, material.getVideoContentUrl());
            pre.setString(6, material.getHtmlContent());

            pre.executeUpdate();
        }
    }

    public void update(LearningMaterial material) throws Exception {
        var sql = """
                UPDATE `swp391`.learningmaterial
                SET
                    title = ?,
                    updated_date = ?,
                    duration = ?,
                    video_content_url = ?,
                    html_content = ?
                WHERE id = ?
                """;

        try (var conn = getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, material.getTitle());

            if (material.getUpdatedDate() != null) {
                pre.setDate(2, java.sql.Date.valueOf(material.getUpdatedDate()));
            } else {
                pre.setDate(2, null);
            }

            pre.setInt(3, material.getDuration());
            pre.setString(4, material.getVideoContentUrl());
            pre.setString(5, material.getHtmlContent());
            pre.setString(6, material.getId().toString());
            pre.executeUpdate();
        }
    }

    public LearningMaterial getByLessonId(String lessonId) throws Exception {
        var sql = "SELECT * FROM `swp391`.learningmaterial WHERE id = ?";
        try (var conn = getConnection(); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, lessonId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return LearningMaterial.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .title(rs.getString("title"))
                            .updatedDate(rs.getDate("updated_date") != null ? rs.getDate("updated_date").toLocalDate() : null)
                            .duration(rs.getInt("duration"))
                            .videoContentUrl(rs.getString("video_content_url"))
                            .htmlContent(rs.getString("html_content"))
                            .build();
                }
            }
        }
        return null;
    }

}
