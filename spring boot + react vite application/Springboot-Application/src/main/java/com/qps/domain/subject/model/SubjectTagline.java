package com.qps.domain.subject.model;

import com.qps.domain.subject.model.id.SubjectTaglineId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"subject", "tagline"})
@Table(name = "subject_tagline", schema = "swp391")
public class SubjectTagline {
    @EmbeddedId
    private SubjectTaglineId id;

    @MapsId("subjectId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @MapsId("taglineId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tagline_id", nullable = false)
    private Tagline tagline;

}