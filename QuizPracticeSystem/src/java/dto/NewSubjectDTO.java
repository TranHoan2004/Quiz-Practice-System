/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Lenovo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewSubjectDTO {
    private String title;             
    private String thumbnailURL;     
    private String topicId;          
    private String expertId; 
    private String contactId;
    private boolean active;          
    private boolean featured;        
    private String description;
}
