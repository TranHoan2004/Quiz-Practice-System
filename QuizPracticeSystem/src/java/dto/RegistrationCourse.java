package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationCourse {
    private String courseId;
    private String subject;
    private LocalDate registrationTime;
    private String packageName;
    private float totalCost;
    private String status;
    private LocalDate validFrom;
    private LocalDate validTo;
}
