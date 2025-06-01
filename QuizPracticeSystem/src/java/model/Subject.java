package model;

import java.util.UUID;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subject {
    private UUID id;
    private String name;
    private String thumbnailURL;
    private boolean featureFlag;
}
