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
public class LearningMaterial {

    private UUID id;
    private String title;
    private LocalDate updatedDate;
    private int duration;
    private String videoContentUrl;
    private String htmlContent;
}
