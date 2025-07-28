/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.UUID;
import lombok.*;

/**
 * @author Lenovo
 * Đại diện cho một tệp media (hình ảnh/video) liên quan đến một câu hỏi.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionMedia {
    private UUID id;
    private String questionId; // ID của câu hỏi mà media này thuộc về (String vì question.id là String khi set/get)
    private String mediaType;  // "image" hoặc "video" (tương ứng với ENUM trong DB)
    private String filePath;   // Đường dẫn đến tệp media
    private String caption;    // Chú thích cho media
    private int displayOrder;  // Thứ tự hiển thị
}
