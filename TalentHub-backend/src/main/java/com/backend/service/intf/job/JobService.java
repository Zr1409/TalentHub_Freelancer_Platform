package com.backend.service.intf.job;

import com.backend.dto.request.job.CreateJobDTORequest;
import com.backend.dto.request.job.JobAdminDTOResponse;
import com.backend.dto.request.job.JobDTORequest;
import com.backend.dto.request.job.JobDetailDTORequest;
import com.backend.dto.response.account.freelancer.FreelancerDTOResponse;
import com.backend.dto.response.job.*;
import com.backend.service.BaseService;

import java.util.List;
import java.util.Optional;

public interface JobService extends BaseService<JobDTORequest, JobDTOResponse, Long> {
    List<JobDTOResponse> findAllJobs(Long freelancerId);

    Optional<DetailJobDTOResponse> getDetailJobById(Long id);

    List<ApplyJobsDTOResponse> getApplyJobs(Long freeLancerId);

    List<PostJobsDTOResponse> getPostedJobs(Long clientId);

    List<JobAdminDTOResponse> getAllAdmin();

    CreateJobDTOResponse createJob(CreateJobDTORequest createJobDTORequest);

    Boolean banJob(Long id);

    Boolean unBanJob(Long id);

    JobDetailDTOResponse getJobById(Long id);

    JobDetailDTOResponse updateJob(Long id, JobDetailDTORequest jobDetailDTORequest);

    List<JobWithPackageDTOResponse> getTop6JobsByTypePriority();

    List<JobDTOResponse> getRecommendedJobsForFreelancer(Long freelancerId);

    List<FreelancerDTOResponse> getFreelancersByClientJobCategories(Long clientId);

    boolean closeJob(Long id);
    void notifyByJobId(Long jobId);
}
