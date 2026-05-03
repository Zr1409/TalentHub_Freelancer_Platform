package com.backend.service.impl.job;

import lombok.RequiredArgsConstructor;
import com.backend.entity.child.job.Job;
import com.backend.enums.StatusJob;
import com.backend.repository.JobRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobSchedulerServiceImpl {
    private final JobRepository jobRepository;

    @Scheduled(cron = "0 */1 * * * ?")
    @Transactional
    public void checkAndCloseExpiredJobs() {
        Date now = new Date();

        List<Job> expiredJobs = jobRepository.findByEndDateLessThanEqualAndStatusNot(now, StatusJob.CLOSED);

        if (!expiredJobs.isEmpty()) {
            expiredJobs.forEach(job -> job.setStatus(StatusJob.CLOSED));

            jobRepository.saveAll(expiredJobs);

            System.out.println("Updated " + expiredJobs.size() + " jobs to CLOSED.");
        }
    }
}
