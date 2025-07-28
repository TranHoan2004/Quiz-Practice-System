package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalSubject {
    private String accountId;
    private String subjectId;
    private String status;
    private LocalDate registrationTime;
    private String packageName;
    private LocalDate validFrom;
    private LocalDate validTo;
    private float price;
}
