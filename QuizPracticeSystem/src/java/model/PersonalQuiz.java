/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Lenovo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalQuiz {
    private UUID id;
    private String accountId;
    private String quizId;
    private boolean hasPassed;
    private int mark;
    private int numberOfCorrectQuestion;
    private LocalDate takenDate;
}
