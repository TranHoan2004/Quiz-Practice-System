/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.*;

/**

@author Lenovo
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationDTO {

private UUID id;
private String email;
private LocalDate registrationTime;
private String subject;
private String packageName;
private float totalCost;
private String status;
private LocalDate validFrom;
private LocalDate validTo;
}