/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * @author Lenovo
 */

import java.time.LocalDate;
import java.util.UUID;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Quiz {

    private UUID lessonId;
    private int duration;
    private float passRate;
    private LocalDate updatedDate;
    private int numberOfQuestions;
    private String description;
    private String title;
    private String topicId;
    private String type; // setting
    private String level; // setting
}
