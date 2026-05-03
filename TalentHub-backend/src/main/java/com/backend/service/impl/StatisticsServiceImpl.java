package com.backend.service.impl;

import com.backend.repository.*;
import lombok.RequiredArgsConstructor;
import com.backend.enums.StatusJob;
import com.backend.enums.StatusFreelancerJob;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl {
    private final ClientRepository clientRepository;
    private final FreelancerRepository freelancerRepository;
    private final JobRepository jobRepository;
    private final FreelancerJobRepository freelancerJobRepository;
    private final SoldPackageRepository soldPackageRepository;
    private final BannerRepository bannerRepository;

    public Map<String, Object> getOverallStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        try {
            long clientCount = clientRepository.count();
            long freelancerCount = freelancerRepository.count();
            long totalAccounts = freelancerCount + clientCount;
            long postedJobsCount = jobRepository.countByStatusNot(StatusJob.DRAFT);
            long approvedFreelancerJobsCount = freelancerJobRepository.countByStatus(StatusFreelancerJob.Approved);
            double totalSoldPackageRevenue = soldPackageRepository.getTotalSoldPackageRevenue();
            double totalBannerRevenue = bannerRepository.getTotalBannerRevenue();
            double totalRevenue = totalSoldPackageRevenue + totalBannerRevenue;

            statistics.put("totalAccounts", totalAccounts);
            statistics.put("postedJobs", postedJobsCount);
            statistics.put("totalClients", clientCount);
            statistics.put("totalFreelancers", freelancerCount);
            statistics.put("approvedFreelancerJobs", approvedFreelancerJobsCount);
            statistics.put("totalRevenue", totalRevenue);
            statistics.put("timestamp", LocalDateTime.now());
            statistics.put("success", true);

            return statistics;
        } catch (Exception e) {
            statistics.put("success", false);
            statistics.put("error", "Error fetching statistics: " + e.getMessage());
            return statistics;
        }
    }
}