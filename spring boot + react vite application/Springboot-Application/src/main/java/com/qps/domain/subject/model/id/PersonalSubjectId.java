package com.qps.domain.subject.model.id;

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
public class PersonalSubjectId implements Serializable {
    @Serial
    private static final long serialVersionUID = -7315020606475167144L;
    @Size(max = 36)
    @NotNull
    @Column(name = "account_id", nullable = false, length = 36)
    private String accountId;

    @Size(max = 36)
    @NotNull
    @Column(name = "subject_id", nullable = false, length = 36)
    private String subjectId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PersonalSubjectId entity = (PersonalSubjectId) o;
        return Objects.equals(this.accountId, entity.accountId) &&
                Objects.equals(this.subjectId, entity.subjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, subjectId);
    }

}