package com.backend.repository;

import com.backend.entity.child.account.client.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAppointmentsByClient_Id(Long clientId);
    List<Appointment> findAppointmentsByFreelancerJob_Freelancer_Id(Long freelancerJobId);

    Optional<Appointment> findByFreelancerJobId(Long freelancerJobId);
}
