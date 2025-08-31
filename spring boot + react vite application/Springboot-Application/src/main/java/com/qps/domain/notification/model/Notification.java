package com.qps.domain.notification.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qps.domain.user.model.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"account"})
@Table(name = "notification", schema = "swp391")
public class Notification {
    @Id
    @Size(max = 36)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Size(max = 255)
    @NotNull
    @Column(name = "link", nullable = false)
    private String link;

    @Size(max = 255)
    @NotNull
    @Column(name = "message", nullable = false)
    private String message;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    // true for read, false for unread
    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status = false;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnore
    private Account account;

    public void markAsRead() {
        this.status = true;
    }
}