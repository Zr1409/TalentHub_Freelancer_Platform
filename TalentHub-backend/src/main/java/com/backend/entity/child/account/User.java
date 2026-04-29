package com.backend.entity.child.account;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.entity.AbstractEntity;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractEntity<Long> {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "ward")
    private String ward;

    @Column(name = "province")
    private String province;

    @Column(name = "title")
    private String title;

    @Column(name = "introduction")
    private String introduction;

    @Size(max = 255)
    @Column(name = "image")
    private String image;
    @OneToOne
//    @JsonIgnore
    @JoinColumn(name = "account_id", unique = true)
    private Account account;

//    @OneToOne
//    @JsonIgnore
//    @JoinColumn(name = "freelancer_id")
//    private Freelancer freelancer;
//
//    @OneToOne
//    @JsonIgnore
//    @JoinColumn(name = "client_id")
//    private Client client;

}
