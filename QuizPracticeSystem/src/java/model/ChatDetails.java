package model;

import java.time.LocalDate;
import java.util.UUID;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatDetails {
    private UUID id;
    private String message;
    private LocalDate timestamp;
    private String sender;
    private String chatId;
}
