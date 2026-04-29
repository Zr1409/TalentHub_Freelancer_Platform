package com.backend.entity.child.account.client;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.entity.AbstractEntity;
import com.backend.entity.child.account.User;
import com.backend.entity.child.job.Job;

import java.util.List;

@Table(name = "client")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client extends AbstractEntity<Long> {

    @Column(name = "from_price")
    private Double fromPrice;

    @Column(name = "to_price")
    private Double toPrice;

    @Column(name = "type_price")
    private String typePrice;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    /*@OneToOne
    @JoinColumn(name = "company_id")
    private Company company;*/


    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Job> jobs;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Appointment> appointments;
}
