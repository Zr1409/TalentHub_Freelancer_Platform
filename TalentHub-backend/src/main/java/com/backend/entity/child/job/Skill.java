package com.backend.entity.child.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.entity.AbstractEntity;
import com.backend.entity.child.account.freelancer.FreelancerSkill;

import java.util.List;

@Table(name = "skill")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Skill extends AbstractEntity<Long> {

    @Column(name = "skill_name")
    private String skillName;

    @OneToMany(mappedBy = "skill")
    @JsonIgnore
    private List<FreelancerSkill> freelancerSkills;
}
