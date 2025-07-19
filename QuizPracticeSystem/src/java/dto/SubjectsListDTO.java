package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectsListDTO {
    private String id;
    private String name;
    private String thumbnailURL;
    private List<String> tagline;
    private String lowestPrice;
    private String salePrice;
    private String updatedDate;
    private ContactInfo contactInfo;
    private Map<String, Integer> pricePackage;
}
