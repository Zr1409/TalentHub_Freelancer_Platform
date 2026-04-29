package com.backend.entity.child.account.freelancer;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.entity.AbstractEntity;

import java.util.List;

@Table(name = "degree")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Degree extends AbstractEntity<Long> {

    @Column(name = "degree_title", nullable = false)
    private String degreeTitle;

    @OneToMany(mappedBy = "degree", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Education> educations;
}
