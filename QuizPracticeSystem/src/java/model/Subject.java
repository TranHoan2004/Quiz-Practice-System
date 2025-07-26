package model;

import java.time.LocalDate;
import java.util.UUID;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Subject {
    private UUID id;
    private String name;
    private String thumbnailURL;
    private boolean featureFlag;
    private String authorId;
    private LocalDate updatedDate;
}
