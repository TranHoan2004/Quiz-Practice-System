package com.qps.domain.slider.model;

import com.qps.domain.user.model.Account;
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
@ToString(exclude = {"account"})
@Table(name = "slider", schema = "swp391")
public class Slider {
    @Id
    @Size(max = 36)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 500)
    @NotNull
    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Size(max = 500)
    @Column(name = "backlink_url", length = 500)
    private String backlinkUrl;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "status", nullable = false)
    private Boolean status = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Size(max = 500)
    @Column(name = "note", length = 500)
    private String note;

}