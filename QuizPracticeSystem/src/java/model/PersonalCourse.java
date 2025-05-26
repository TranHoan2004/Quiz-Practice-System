/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

import lombok.*;

/**
 * @author Lenovo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalCourse {

    private String accountId;
    private String courseId;
    private LocalDate expireDate;
    private LocalDate enrollDate;
    private int progress;
    private PersonalCourseStatus status;
}
