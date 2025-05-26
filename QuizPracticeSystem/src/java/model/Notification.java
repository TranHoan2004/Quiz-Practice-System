package model;

import java.util.UUID;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    private UUID id;
    private String link;
    private String message;
    private String title;
    private boolean status;
    private String accountId;
}
