package com.backend.entity.child.account.freelancer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.entity.AbstractEntity;

import java.util.List;

@Table(name = "major")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Major extends AbstractEntity<Long> {

    @Column(name = "major_name")
    private String majorName;

    @JsonIgnore
    @OneToMany(mappedBy = "major", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations;
}
