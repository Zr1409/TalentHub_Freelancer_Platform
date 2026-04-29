package com.backend.entity.child.account.freelancer;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.entity.AbstractEntity;
import com.backend.entity.child.job.Skill;

@Table(name = "freelancer_skill")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FreelancerSkill extends AbstractEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private Freelancer freelancer;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;
}
