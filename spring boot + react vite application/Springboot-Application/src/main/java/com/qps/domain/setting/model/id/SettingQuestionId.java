package com.qps.domain.setting.model.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class SettingQuestionId implements Serializable {
    @Serial
    private static final long serialVersionUID = -566196192898706769L;
    @Size(max = 36)
    @NotNull
    @Column(name = "setting_id", nullable = false, length = 36)
    private String settingId;

    @Size(max = 36)
    @NotNull
    @Column(name = "question_id", nullable = false, length = 36)
    private String questionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SettingQuestionId entity = (SettingQuestionId) o;
        return Objects.equals(this.questionId, entity.questionId) &&
                Objects.equals(this.settingId, entity.settingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, settingId);
    }

}