package com.qps.domain.setting.model;

import com.qps.domain.setting.model.id.SettingSubjectId;
import com.qps.domain.subject.model.Subject;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "setting_subject", schema = "swp391")
public class SettingSubject {
    @EmbeddedId
    private SettingSubjectId id;

    @MapsId("settingId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "setting_id", nullable = false)
    private Setting setting;

    @MapsId("subjectId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

}