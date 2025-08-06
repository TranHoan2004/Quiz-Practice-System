package com.qps.domain.blog.model;

import com.qps.domain.setting.model.Setting;
import com.qps.domain.user.model.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"category", "account"})
@Table(name = "blog", schema = "swp391")
public class Blog {
    @Id
    @Size(max = 36)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 255)
    @NotNull
    @Column(name = "brief_info", nullable = false)
    private String briefInfo;

    @Lob
    @Column(name = "content")
    private String content;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "status", nullable = false)
    private Boolean status = false;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category", nullable = false)
    private Setting category;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ColumnDefault("1")
    @Column(name = "flag_feature")
    private Boolean flagFeature;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "views", nullable = false)
    private Integer views;

}