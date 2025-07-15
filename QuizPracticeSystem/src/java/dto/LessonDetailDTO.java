/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Lenovo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LessonDetailDTO {
    
    private String lessonId;
    private String name;
    private String type;         
    private String topicId;        
    private int order;           
    private String videoLink;   
    private String htmlContent;  
    private boolean active;
}

