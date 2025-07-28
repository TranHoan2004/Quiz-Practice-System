/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author Admin
 */
import java.time.LocalDate; // Nếu bạn dùng LocalDate cho ngày tháng khác (ví dụ created_date)
import java.util.UUID;
import lombok.*;

/**
 * @author Lenovo
 * Đại diện cho một câu hỏi trong hệ thống.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionDTO {
    private UUID id;
    private String content;      
    private String level;
    private String subjectId;// Thuộc tính mới thêm, ID Subject của câu hỏi
    private String subjectName;
    private String lessonId;// Thuộc tính mới thêm, ID Lesson của câu hỏi
    private String lessonName;
    private boolean status;   // Thuộc tính mới thêm, trạng thái câu hỏi (0/1)
    private String explanation; // Thuộc tính mới thêm, giải thích chung cho câu hỏi
    private String dimensionId;
    private String dimensionName;
}

