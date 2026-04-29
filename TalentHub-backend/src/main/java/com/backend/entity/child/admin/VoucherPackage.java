package com.backend.entity.child.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.entity.AbstractEntity;
import com.backend.entity.child.account.Account;
import com.backend.enums.TypePackage;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "voucher_packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoucherPackage extends AbstractEntity<Long> {
    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "number_post")
    private Long NumberPost;

    @Column(name = "type_package")
    @Enumerated(EnumType.STRING)
    private TypePackage typePackage;

    @Column(name = "status")
    private boolean status;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "account", nullable = false)
    @JsonIgnore
    private Account account;
}
