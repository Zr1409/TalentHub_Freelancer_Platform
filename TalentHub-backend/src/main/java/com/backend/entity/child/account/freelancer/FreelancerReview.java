package com.backend.entity.child.account.freelancer;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.entity.AbstractEntity;


@Table(name = "freelancer_review")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FreelancerReview extends AbstractEntity<Long> {

    //change from on_time to rating, remember drop and create new database
    @Column(name = "rating")
    private Float rating;

    @Column(name = "note")
    private String note;

    /*@OneToMany(mappedBy = "freelancerReview", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<FreelancerJob> freelancerJobs;*/
}
