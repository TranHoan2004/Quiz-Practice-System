package model;

import java.util.UUID;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chat {
    private UUID id;
    private String firstPerson; // account
    private String secondPerson; // account
}
