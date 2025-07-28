package com.qps.domain.quiz.model;

import com.qps.domain.subject.model.Subject;
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
@Table(name = "quiz", schema = "swp391")
public class Quiz {
    @Id
    @Size(max = 36)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "status", nullable = false)
    private Boolean status = false;

    @Column(name = "pass_rate")
    private Float passRate;

    @NotNull
    @Column(name = "updated_date", nullable = false)
    private LocalDate updatedDate;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "number_of_question", nullable = false)
    private Integer numberOfQuestion;

    @Size(max = 255)
    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "type", nullable = false)
    private QuizType type;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "level", nullable = false)
    private QuizLevel level;

}