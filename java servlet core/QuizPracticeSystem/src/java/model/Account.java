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
public class Account {

    private UUID id;
    private String email;
    private String fullName;
    private String password;
    private LocalDate dob;
    private int gender; // 0 = Nam, 1 = Nữ, 2 for Others
    private LocalDate createdDate;
    private boolean status; // true = active, false = inactive
    private String phoneNumber;
    private String imageUrl;
    private String roleId;
    
}
