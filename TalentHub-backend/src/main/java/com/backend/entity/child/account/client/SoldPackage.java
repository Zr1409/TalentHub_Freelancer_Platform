package com.backend.entity.child.account.client;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.entity.AbstractEntity;
import com.backend.entity.child.admin.VoucherPackage;

import java.time.LocalDateTime;

@Table(name = "sold_packages")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SoldPackage extends AbstractEntity<Long> {
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "price")
    private Double price;

    @Column(name = "number_post")
    private Long NumberPost;

    @Column(name = "number_posted")
    private Long NumberPosted;

    @Column(name = "status")
    // true = valid; false = expired
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "voucher_packages")
    private VoucherPackage voucherPackage;

    @ManyToOne
    @JoinColumn(name = "client")
    private Client client;
}
