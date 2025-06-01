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
    private int id;                 // Mã khóa học
    private String name;           // Tên môn học
    private String category;       // Danh mục (Math, CS, etc.)
    private int numberOfLessons;   // Số bài học trong môn
    private String owner;          // Tên người phụ trách (Expert/Admin)
    private boolean published;     // Trạng thái: true = Published, false = Unpublished
}