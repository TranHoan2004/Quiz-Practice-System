/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CourseDTO {
    private String id;                 // Mã khóa học
    private String title;           // Tên môn học
    private String description;
    private String image;
    private String category;       // Danh mục (Math, CS, etc.)
    private int numberOfLessons;   // Số bài học trong môn
    private String owner;          // Tên người phụ trách (Expert/Admin)
    private boolean published;     // Trạng thái: true = Published, false = Unpublished
    private double price;
    private double salePrice;
    private String subjectId;
}