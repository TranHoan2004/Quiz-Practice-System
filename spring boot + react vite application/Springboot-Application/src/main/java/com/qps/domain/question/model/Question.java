package com.qps.domain.question.model;

import com.qps.domain.lesson.model.Lesson;
import com.qps.domain.quiz.model.Quiz;
import com.qps.domain.topic.model.Topic;
import com.qps.domain.subject.model.Subject;
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
@ToString(exclude = {"subject", "lesson", "quiz"})
@Table(name = "question", schema = "swp391")
public class Question {
    @Id
    @Size(max = 36)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Size(max = 255)
    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Size(max = 255)
    @NotNull
    @Column(name = "level", nullable = false)
    private String level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "status", nullable = false)
    private Boolean status = false;

    @Lob
    @Column(name = "explanation")
    private String explanation;

}