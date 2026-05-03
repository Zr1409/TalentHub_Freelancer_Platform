package com.backend.service.intf.job;

import com.backend.dto.request.job.ClientReviewDTORequest;
import com.backend.dto.request.job.FreelancerJobDTORequest;
import com.backend.dto.request.job.FreelancerReviewDTORequest;
import com.backend.dto.response.account.freelancer.ApplicantResponseDTO;
import com.backend.dto.response.job.ClientReviewDTOResponse;
import com.backend.dto.response.job.FreelancerJobDTOResponse;
import com.backend.dto.response.job.FreelancerReviewDTOResponse;
import com.backend.dto.response.job.SaveJobDTOResponse;
import com.backend.entity.child.account.client.Appointment;
import com.backend.entity.child.account.freelancer.CV;
import com.backend.entity.child.job.FreelancerJob;
import com.backend.service.BaseService;
import com.backend.dto.response.job.FreelancerJobDetailDTOResponse;

import java.util.List;

public interface FreelancerJobService extends BaseService<FreelancerJobDTORequest, FreelancerJobDTOResponse, Long> {
    FreelancerJobDTOResponse applyJob(FreelancerJobDTORequest request);

    FreelancerJobDTOResponse saveJob(FreelancerJobDTORequest request);

    FreelancerJobDTOResponse unSaveJob(FreelancerJobDTORequest request);

    List<ApplicantResponseDTO> getApplicantByJobId(Long jobId);

    FreelancerJobDTOResponse approveApplication(Long jobId, Long freelancerId);

    FreelancerJobDTOResponse rejectApplication(Long jobId, Long freelancerId);

    FreelancerJobDTOResponse unapplyJob(Long jobId, Long freelancerId);

    List<SaveJobDTOResponse> getSavedJobs(Long freelancerId);

    FreelancerJob findById(Long jobId);

    public Appointment getAppointmentByFreelancerJobId(Long freelancerJobId);

    CV getCVByFreeLancer_IdAndJob_Id(Long freeLancerId, Long jobId);

    ClientReviewDTOResponse freelancerReview(Long freelancerJobId, ClientReviewDTORequest request);

    FreelancerReviewDTOResponse clientReview(Long freelancerJobId, FreelancerReviewDTORequest request);

    List<FreelancerJobDetailDTOResponse> getJobDetailsByFreelancerId(Long freelancerId);
}
