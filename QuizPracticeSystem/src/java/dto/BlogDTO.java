package dto;


import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BlogDTO {
    private String id;
    private String title;
    private String briefInfo;
    private String content;
    private String thumbnailUrl;
    private String category;
    private String accountId;
    private String accountName;
    private String avatarUrl;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    private int views;
    private boolean status;



}
