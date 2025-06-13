/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import lombok.*;

/**
 *
 * @author Lenovo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LessonDTO {

    private String id;
    private String title; 
    private String type;
    private boolean active;     // Trạng thái: true = Active, false = Inactive
}
