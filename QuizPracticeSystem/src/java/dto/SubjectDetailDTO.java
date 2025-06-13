/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import lombok.*;
import model.PricePackage;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDetailDTO {

    private String id;                  
    private String name;             
    private String thumbnailUrl;     
    private boolean featured;        
    private String courseId;
    private String category;         
    private String description;      
    private boolean published;       
    private String owner;    
    private List<SubjectDimensionDTO> dimensions;
    private List<PricePackage> pricePackages; 
}