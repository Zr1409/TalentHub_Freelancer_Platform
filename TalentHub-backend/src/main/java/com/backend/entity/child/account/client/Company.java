package com.backend.entity.child.account.client;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.entity.AbstractEntity;

@Table(name = "company")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company extends AbstractEntity<Long> {

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_image")
    private String companyImage;

    @Column(name = "phone_contact")
    private String phoneContact;

    @Column(name = "address")
    private String address;

    @Column(name = "industry")
    private String Industry;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

}
