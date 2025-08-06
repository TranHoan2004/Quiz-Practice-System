package com.qps.domain.setting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"settingType"})
@Table(name = "setting", schema = "swp391")
public class Setting {
    @Id
    @Size(max = 36)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Size(max = 255)
    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status = false;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "setting_type_id", nullable = false)
    private SettingType settingType;

}