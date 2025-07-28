package com.qps.domain.course.model;

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
@Table(name = "personalcourse", schema = "swp391")
public class PersonalCourse {
    @Id
    @Size(max = 36)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "expire_date")
    private LocalDate expireDate;

    @NotNull
    @Column(name = "enroll_date", nullable = false)
    private LocalDate enrollDate;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "progress", nullable = false)
    private Integer progress;

    @Size(max = 255)
    @ColumnDefault("'SENT'")
    @Column(name = "status")
    private String status;

}