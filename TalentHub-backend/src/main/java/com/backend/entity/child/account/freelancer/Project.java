package com.backend.entity.child.account.freelancer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.entity.AbstractEntity;

@Table(name = "projects")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class  Project extends AbstractEntity<Long> {

    @Column(name = "title")
    private String title;

    @Column(name = "tech")
    private String tech;

    @Column(name = "description")
    private String description;

    @Column(name = "link")
    private String link;

    @Column(name ="image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", nullable = false)
    @JsonIgnore
    private Freelancer freelancer;
}
