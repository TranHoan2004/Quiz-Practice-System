package com.qps.domain.quiz.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quiz_question_source_config", schema = "swp391")
public class QuizQuestionSourceConfig {
    @Id
    @Size(max = 36)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Size(max = 10)
    @NotNull
    @Column(name = "source_type", nullable = false, length = 10)
    private String sourceType;

    @Size(max = 36)
    @NotNull
    @Column(name = "source_id", nullable = false, length = 36)
    private String sourceId;

    @NotNull
    @Column(name = "number_of_questions", nullable = false)
    private Integer numberOfQuestions;

}