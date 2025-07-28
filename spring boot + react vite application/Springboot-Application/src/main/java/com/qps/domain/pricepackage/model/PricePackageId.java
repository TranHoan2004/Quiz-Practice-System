package com.qps.domain.pricepackage.model;

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
public class PricePackageId implements Serializable {
    @Serial
    private static final long serialVersionUID = 3697324696368653200L;
    @Size(max = 36)
    @NotNull
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Size(max = 36)
    @NotNull
    @Column(name = "course_id", nullable = false, length = 36)
    private String courseId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PricePackageId entity = (PricePackageId) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.courseId, entity.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseId);
    }

}