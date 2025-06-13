/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizQuestionSourceConfig {

    private UUID id;
    private String quizId;
    private String sourceType; // 'topic', 'group', 'domain'
    private String sourceId;     // ID của topic hoặc setting
    private int numberOfQuestions;
}

