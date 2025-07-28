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
public class Blog {

    private UUID id;
    private LocalDate updatedDate;
    private String thumbnailUrl;
    private String title;
    private String briefInfo;
    private String content;
    private boolean status;
    private LocalDate createdDate;
    private UUID category; // setting
    private String accountId;
    private int views;
    private boolean flagFeature;
}
