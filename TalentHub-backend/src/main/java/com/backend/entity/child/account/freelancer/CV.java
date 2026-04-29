package com.backend.entity.child.account.freelancer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.entity.AbstractEntity;
import com.backend.entity.child.job.FreelancerJob;

import java.util.List;


@Entity
@Table(name = "cv")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CV extends AbstractEntity<Long> {

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "url", length = 500)
    private String url;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    @JsonIgnore
    private Freelancer freelancer;

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore
    private List<FreelancerJob> freelancerJobs;

}