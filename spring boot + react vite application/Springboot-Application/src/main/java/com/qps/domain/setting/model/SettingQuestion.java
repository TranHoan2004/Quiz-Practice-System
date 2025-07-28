package com.qps.domain.setting.model;

import com.qps.domain.question.model.Question;
import com.qps.domain.setting.model.id.SettingQuestionId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "setting_question", schema = "swp391")
public class SettingQuestion {
    @EmbeddedId
    private SettingQuestionId id;

    @MapsId("settingId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "setting_id", nullable = false)
    private Setting setting;

    @MapsId("questionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

}