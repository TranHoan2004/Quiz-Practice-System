package com.qps.domain.blog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"blog"})
@Table(name = "blogmedia", schema = "swp391")
public class BlogMedia {
    @Id
    @Size(max = 36)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    @NotNull
    @Lob
    @Column(name = "media_type", nullable = false)
    private String mediaType;

    @Size(max = 1000)
    @NotNull
    @Column(name = "file_path", nullable = false, length = 1000)
    private String filePath;

    @Lob
    @Column(name = "caption")
    private String caption;

    @ColumnDefault("0")
    @Column(name = "display_order")
    private Integer displayOrder;

}