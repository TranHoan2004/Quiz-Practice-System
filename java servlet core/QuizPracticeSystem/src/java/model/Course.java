/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import java.util.UUID;

import lombok.*;

/**
 * @author Lenovo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {

    private UUID id;
    private String title;
    private float duration;
    private boolean status;
    private String description;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    private String thumbnailUrl;
    private int numberOfLessons;
    private String topicId;
    private String contact;
    private String expertId;

}
