/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.UUID;

import lombok.*;

/**
 * @author Lenovo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lesson {

    private UUID id;
    private boolean status;
    private String name;
    private String courseId;
    private String lessonTypeId; // setting
}
