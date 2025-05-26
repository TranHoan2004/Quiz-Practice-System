package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactInfo {
    private String id;
    private String name;
    private Map<String, String> link;
    private String email;
    private String phone;
    private String address;
}
