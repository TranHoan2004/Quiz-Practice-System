package com.qps.domain.quiz.model;

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
@Table(name = "personalquiz", schema = "swp391")
public class PersonalQuiz {
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
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "has_passed", nullable = false)
    private Boolean hasPassed = false;

    @Column(name = "mark")
    private Integer mark;

    @Column(name = "number_of_correct_question")
    private Integer numberOfCorrectQuestion;

    @NotNull
    @Column(name = "taken_date", nullable = false)
    private LocalDate takenDate;

}