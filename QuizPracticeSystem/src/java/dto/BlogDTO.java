package dto;


import lombok.*;
import model.BlogMedia;

import java.time.LocalDate;
import java.util.List;

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
    private String categoryId;
    private String category;
    private String accountId;
    private String accountName;
    private String avatarUrl;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    private boolean flagFeature;
    private int views;
    private boolean status;
    private String blogMediaJson;
    private List<BlogMedia> blogMediaList;
}
