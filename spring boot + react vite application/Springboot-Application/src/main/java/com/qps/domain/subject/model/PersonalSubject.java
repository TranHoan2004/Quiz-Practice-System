package com.qps.domain.subject.model;

import com.qps.domain.subject.model.id.PersonalSubjectId;
import com.qps.domain.user.model.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "personalsubject", schema = "swp391")
public class PersonalSubject {
    @EmbeddedId
    private PersonalSubjectId id;

    @MapsId("accountId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @MapsId("subjectId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Size(max = 50)
    @NotNull
    @ColumnDefault("'sent'")
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @NotNull
    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Size(max = 255)
    @Column(name = "package_name")
    private String packageName;

    @Column(name = "valid_from")
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Column(name = "price")
    private Double price;

}