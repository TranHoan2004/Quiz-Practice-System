package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SliderResponse {
    private String author;
    private String id;
    private String title;
    private String imageUrl;
    private String backlinkUrl;
    private String note;
    private boolean status;
}
