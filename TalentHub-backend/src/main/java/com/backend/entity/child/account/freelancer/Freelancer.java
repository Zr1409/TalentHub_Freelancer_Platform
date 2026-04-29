package com.backend.entity.child.account.freelancer;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.entity.AbstractEntity;
import com.backend.entity.child.account.User;
import com.backend.entity.child.job.Category;
import com.backend.entity.child.job.FreelancerJob;

import java.math.BigDecimal;
import java.util.List;

@Table(name = "freelancer")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Freelancer extends AbstractEntity<Long> {

    /*@Size(max = 255)
    @Column(name = "image")
    private String image;*/

    @Column(name = "hourly_rate", precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<FreelancerJob> freelancerJobs;

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<FreelancerSkill> freelancerSkills;

    /*@OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CV> CVs;*/

    @JsonIgnore
    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations;

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Experience> experiences;

}
