package com.backend.entity.child.account.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.entity.AbstractEntity;
import com.backend.entity.child.job.FreelancerJob;
import com.backend.enums.TypeAppointment;

import java.time.LocalDateTime;

@Table(name = "appointments")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Appointment extends AbstractEntity<Long> {

    @Column(name = "topic")
    private String topic;

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startTime;

    @Column(name = "duration")
    private Long duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeAppointment type;

    @Column(name = "description")
    private String description;

    @Column(name = "link")
    private String link;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne
    @JoinColumn(name = "freelancer_job_id")
    private FreelancerJob freelancerJob;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;
}
