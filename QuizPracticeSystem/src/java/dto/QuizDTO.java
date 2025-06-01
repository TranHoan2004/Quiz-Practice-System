/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author Admin
 */
import java.time.LocalDate;
import java.util.UUID;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizDTO {
    private UUID id;
    private String lessonId;
    private int duration;
    private boolean check;
    private float passRate;
    private LocalDate updatedDate;
    private int numberOfQuestions;
    private String description;
    private String title;
    private String subjectId;
    private String type; 
    private String level; 
}
