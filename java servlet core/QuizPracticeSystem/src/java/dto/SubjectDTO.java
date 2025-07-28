package dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SubjectDTO {
    private String id;
    private String subjectName;
    private String tagline;
    private String thumbnailUrl;
}
