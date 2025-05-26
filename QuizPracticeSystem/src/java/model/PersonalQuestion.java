/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import lombok.*;

/**
 * @author Lenovo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalQuestion {

    private String accountId;
    private String lessonId;
    private String courseId;
    private String questionId;
    private boolean hasAnswer;
    private boolean isMark;
}
