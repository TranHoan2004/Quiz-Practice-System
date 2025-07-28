package com.qps.domain.lesson.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "learningmaterial", schema = "swp391")
public class LearningMaterial {
    @Id
    @Size(max = 36)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private Lesson lesson;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Size(max = 1000)
    @Column(name = "video_content_url", length = 1000)
    private String videoContentUrl;

    @Size(max = 1000)
    @NotNull
    @Column(name = "html_content", nullable = false, length = 1000)
    private String htmlContent;

}