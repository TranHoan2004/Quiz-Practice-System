package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogMedia {
    private UUID id;
    private UUID blogId;
    private String mediaType;    // Loại media (image, video, etc.)
    private String mediaUrl;     // URL của file media
    private String caption;      // Chú thích cho media
    private Integer displayOrder; // Thứ tự hiển thị trong blog
}