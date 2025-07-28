package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contact {
    private UUID id;
    private String name;
    private Map<String, String> link;
    private String email;
    private String phone;
    private String address;
}
