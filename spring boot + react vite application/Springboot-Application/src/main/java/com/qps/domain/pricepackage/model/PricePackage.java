package com.qps.domain.pricepackage.model;

import com.qps.domain.course.model.Course;
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
@Table(name = "pricepackage", schema = "swp391")
public class PricePackage {
    @EmbeddedId
    private PricePackageId id;

    @MapsId("courseId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @NotNull
    @Column(name = "sale_price", nullable = false)
    private Integer salePrice;

    @NotNull
    @Column(name = "access_duration", nullable = false)
    private Integer accessDuration;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "status", nullable = false)
    private Boolean status = false;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

}