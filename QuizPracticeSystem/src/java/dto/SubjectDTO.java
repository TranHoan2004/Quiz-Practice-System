package dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SubjectDTO {
    private String subjectName;
    private String tagline;
    private String thumbnailUrl;
}
