package com.qps.domain.option.model;

import com.qps.domain.question.model.Question;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`option`", schema = "swp391")
public class Option {
    @Id
    @Size(max = 36)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Size(max = 255)
    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "is_true", nullable = false)
    private Boolean isTrue = false;

    @Size(max = 255)
    @NotNull
    @Column(name = "explanation", nullable = false)
    private String explanation;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

}