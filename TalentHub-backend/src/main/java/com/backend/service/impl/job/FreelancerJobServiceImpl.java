package com.backend.service.impl.job;

import com.backend.repository.*;
import lombok.RequiredArgsConstructor;
import com.backend.dto.request.job.ClientReviewDTORequest;
import com.backend.dto.request.job.FreelancerJobDTORequest;
import com.backend.dto.request.job.FreelancerReviewDTORequest;
import com.backend.dto.response.account.freelancer.ApplicantResponseDTO;
import com.backend.dto.response.job.ClientReviewDTOResponse;
import com.backend.dto.response.job.FreelancerJobDTOResponse;
import com.backend.dto.response.job.FreelancerReviewDTOResponse;
import com.backend.dto.response.job.SaveJobDTOResponse;
import com.backend.entity.child.account.client.Appointment;
import com.backend.entity.child.account.client.ClientReview;
import com.backend.entity.child.account.client.Company;
import com.backend.entity.child.account.freelancer.CV;
import com.backend.entity.child.account.freelancer.FreelancerReview;
import com.backend.entity.child.job.FreelancerJob;
import com.backend.entity.child.job.Job;
import com.backend.enums.StatusFreelancerJob;
import com.backend.exception.BadRequestException;
import com.backend.mapper.Account.freelancer.CreateFreelancerMapper;
import com.backend.mapper.job.ClientReviewMapper;
import com.backend.mapper.job.FreelancerJobMapper;
import com.backend.mapper.job.FreelancerReviewMapper;
import com.backend.mapper.job.SaveJobMapper;
import com.backend.service.impl.notify.NotifyService;
import com.backend.service.intf.job.FreelancerJobService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.backend.dto.response.job.FreelancerJobDetailDTOResponse;


@Service
@RequiredArgsConstructor
public class FreelancerJobServiceImpl implements FreelancerJobService {
    private final FreelancerJobRepository freelancerJobRepository;
    private final FreelancerRepository freelancerRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final FreelancerJobMapper freelancerJobMapper;
    private final SaveJobMapper saveJobMapper;
    private final CompanyRepository companyRepository;
    private final CreateFreelancerMapper createFreelancerMapper;
    private final AppointmentRepository appointmentRepository;
    private final ClientReviewRepository clientReviewRepository;
    private final ClientReviewMapper clientReviewMapper;
    private final FreelancerReviewMapper freelancerReviewMapper;
    private final FreelancerReviewRepository freelancerReviewRepository;
    private final CVRepository cvRepository;
    private final NotifyService notifyService;
    @Override
    public FreelancerJobDTOResponse create(FreelancerJobDTORequest freelancerJobDTORequest) {
        return null;
    }

    @Override
    public Optional<FreelancerJobDTOResponse> getById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<FreelancerJobDTOResponse> getAll() {
        return List.of();
    }

    @Override
    public Boolean deleteById(Long aLong) {
        return null;
    }



    /*
    I should have combined these APIs into one and added a parameter.
    That would have been better.
    */

    @Override
    public FreelancerJobDTOResponse applyJob(FreelancerJobDTORequest request) {
        Job job = jobRepository.findById(request.getJobId()).orElse(null);
        System.out.println(job.getClient().getUser().getId());
        notifyService.sendNotification(job.getClient().getUser().getId(), "Có một ứng viên đã ứng tuyển vào " + job.getTitle(), "client/applicants/"+request.getJobId());

        Optional<FreelancerJob> existingFreelancerJob = freelancerJobRepository.findByFreelancer_IdAndJob_Id(
                request.getFreelancerId(), request.getJobId());

        if (!existingFreelancerJob.isPresent()) {
            throw new BadRequestException("Freelancer Job Not Found");
        }

        FreelancerJob freelancerJob = existingFreelancerJob.get();
        freelancerJob.setStatus(StatusFreelancerJob.Applied);
        freelancerJob.setAppliedDate(LocalDateTime.now());
        CV cv = cvRepository.findById(request.getCvId()).orElse(null);

        freelancerJob.setCv(cv);
        FreelancerJob updatedFreelancerJob = freelancerJobRepository.save(freelancerJob);


        return new FreelancerJobDTOResponse(
                updatedFreelancerJob.getId(),
                updatedFreelancerJob.getIsSaved(),
                updatedFreelancerJob.getStatus(),
                updatedFreelancerJob.getJob().getId(),
                updatedFreelancerJob.getFreelancer().getId(),
                updatedFreelancerJob.getCv().getUrl()
        );
    }

    @Override
    public FreelancerJobDTOResponse saveJob(FreelancerJobDTORequest request) {
        Optional<FreelancerJob> existingFreelancerJob = freelancerJobRepository.findByFreelancer_IdAndJob_Id(
                request.getFreelancerId(), request.getJobId());

        if (!existingFreelancerJob.isPresent()) {
            throw new BadRequestException("Freelancer Job Not Found");
        }

        FreelancerJob freelancerJob = existingFreelancerJob.get();
        freelancerJob.setIsSaved(true);
        FreelancerJob updatedFreelancerJob = freelancerJobRepository.save(freelancerJob);

        return new FreelancerJobDTOResponse(
                updatedFreelancerJob.getId(),
                updatedFreelancerJob.getIsSaved(),
                updatedFreelancerJob.getStatus(),
                updatedFreelancerJob.getJob().getId(),
                updatedFreelancerJob.getFreelancer().getId(),
                ""
        );
    }

    @Override
    public FreelancerJobDTOResponse unSaveJob(FreelancerJobDTORequest request) {
        Optional<FreelancerJob> existingFreelancerJob = freelancerJobRepository.findByFreelancer_IdAndJob_Id(
                request.getFreelancerId(), request.getJobId());

        if (!existingFreelancerJob.isPresent()) {
            throw new BadRequestException("Freelancer Job Not Found");
        }

        FreelancerJob freelancerJob = existingFreelancerJob.get();
        freelancerJob.setIsSaved(false);
        FreelancerJob updatedFreelancerJob = freelancerJobRepository.save(freelancerJob);

        return new FreelancerJobDTOResponse(
                updatedFreelancerJob.getId(),
                updatedFreelancerJob.getIsSaved(),
                updatedFreelancerJob.getStatus(),
                updatedFreelancerJob.getJob().getId(),
                updatedFreelancerJob.getFreelancer().getId(),
                ""
        );
    }
    @Override
    public Appointment getAppointmentByFreelancerJobId(Long freelancerJobId) {
        Optional<Appointment> appointment = appointmentRepository.findByFreelancerJobId(freelancerJobId);
        return appointment.orElse(null);
    }

    @Override
    public CV getCVByFreeLancer_IdAndJob_Id(Long freeLancerId, Long jobId) {
        if (freeLancerId == null || jobId == null) {
            throw new BadRequestException("Freelancer Job Id or Job Id Not Found");
        }

        CV cv = freelancerJobRepository.getCVByFreelancer_IdAndJob_Id(freeLancerId, jobId);

        return cv;
    }

    @Override
    public ClientReviewDTOResponse freelancerReview(Long freelancerJobId, ClientReviewDTORequest request) {
        if (freelancerJobId == null) {
            throw new BadRequestException("Freelancer Job Id Not Found");
        }

        if (request == null) {
            throw new BadRequestException("CLientReview Not Found");
        }

        if (request.getRating() == null) {
            throw new BadRequestException("Ratings Not Found");
        }

        if (request.getNote() == null) {
            throw new BadRequestException("Notes Not Found");
        }

        ClientReview review = clientReviewMapper.toEntity(request);
        review = clientReviewRepository.save(review);

        FreelancerJob freelancerJob = freelancerJobRepository.findById(freelancerJobId).get();
        freelancerJob.setClientReview(review);
        notifyService.sendNotification(freelancerJob.getFreelancer().getUser().getId(), "Có 1 khách hàng mới đánh giá bạn", "freelancers/"+freelancerJob.getFreelancer().getId()+"?is_review=true" );
        freelancerJobRepository.save(freelancerJob);

        return clientReviewMapper.toResponseDto(review);
    }

    @Override
    public FreelancerReviewDTOResponse clientReview(Long freelancerJobId, FreelancerReviewDTORequest request) {
        if (freelancerJobId == null) {
            throw new BadRequestException("Freelancer Job Id Not Found");
        }

        if (request == null) {
            throw new BadRequestException("CLientReview Not Found");
        }

        if (request.getRating() == null) {
            throw new BadRequestException("Ratings Not Found");
        }

        if (request.getNote() == null) {
            throw new BadRequestException("Notes Not Found");
        }

        FreelancerReview freelancerReview = freelancerReviewMapper.toEntity(request);
        freelancerReview = freelancerReviewRepository.save(freelancerReview);

        FreelancerJob freelancerJob = freelancerJobRepository.findById(freelancerJobId).get();
        freelancerJob.setFreelancerReview(freelancerReview);
        freelancerJobRepository.save(freelancerJob);

        return freelancerReviewMapper.toResponseDto(freelancerReview);
    }


    @Override
    public List<ApplicantResponseDTO> getApplicantByJobId(Long jobId) {
        List<FreelancerJob> results = freelancerJobRepository.getApplicantByJobId(jobId).stream().filter(
                freelancerJob -> freelancerJob.getStatus() != null && !freelancerJob.getStatus().equals(StatusFreelancerJob.Viewed)
        ).toList();

        return results.stream()
                .map(freelancerJob -> {
                    Appointment appointment = this.getAppointmentByFreelancerJobId(freelancerJob.getId());
                    return freelancerJobMapper.toResponseDto(freelancerJob, appointment == null ? -1 : appointment.getId());
                })
                .collect(Collectors.toList());
    }

    @Override
    public FreelancerJobDTOResponse approveApplication(Long jobId, Long freelancerId) {
        Optional<FreelancerJob> existingFreelancerJob = freelancerJobRepository.findByFreelancer_IdAndJob_Id(
                freelancerId, jobId);

        if (!existingFreelancerJob.isPresent()) {
            throw new BadRequestException("Freelancer Job Not Found");
        }

        FreelancerJob freelancerJob = existingFreelancerJob.get();

        if (freelancerJob.getStatus() != StatusFreelancerJob.Applied) {
            throw new BadRequestException("Can only approve applications with Applied status");
        }

        freelancerJob.setStatus(StatusFreelancerJob.Approved);
        FreelancerJob updatedFreelancerJob = freelancerJobRepository.save(freelancerJob);
        notifyService.sendNotification(updatedFreelancerJob.getFreelancer().getUser().getId(), "Bạn đã được chấp thuận trong công việc " + updatedFreelancerJob.getJob().getTitle(), "jobs/"+updatedFreelancerJob.getJob().getId());
        return new FreelancerJobDTOResponse(
                updatedFreelancerJob.getId(),
                updatedFreelancerJob.getIsSaved(),
                updatedFreelancerJob.getStatus(),
                updatedFreelancerJob.getJob().getId(),
                updatedFreelancerJob.getFreelancer().getId(),
                updatedFreelancerJob.getCv().getUrl()
        );
    }

    @Override
    public FreelancerJobDTOResponse rejectApplication(Long jobId, Long freelancerId) {
        Optional<FreelancerJob> existingFreelancerJob = freelancerJobRepository.findByFreelancer_IdAndJob_Id(
                freelancerId, jobId);

        if (!existingFreelancerJob.isPresent()) {
            throw new BadRequestException("Freelancer Job Not Found");
        }

        FreelancerJob freelancerJob = existingFreelancerJob.get();

        if (freelancerJob.getStatus() != StatusFreelancerJob.Applied) {
            throw new BadRequestException("Can only reject applications with Applied status");
        }

        freelancerJob.setStatus(StatusFreelancerJob.Rejected);

        FreelancerJob updatedFreelancerJob = freelancerJobRepository.save(freelancerJob);

        notifyService.sendNotification(
                updatedFreelancerJob.getFreelancer().getUser().getId(),
                "Rất tiếc, bạn chưa được chọn cho công việc '" + updatedFreelancerJob.getJob().getTitle() + "'. Đừng nản lòng, hãy tiếp tục khám phá các cơ hội khác phù hợp với bạn!",
                "jobs"
        );

        return new FreelancerJobDTOResponse(
                updatedFreelancerJob.getId(),
                updatedFreelancerJob.getIsSaved(),
                updatedFreelancerJob.getStatus(),
                updatedFreelancerJob.getJob().getId(),
                updatedFreelancerJob.getFreelancer().getId(),
                updatedFreelancerJob.getCv().getUrl()
        );
    }

    @Override
    public FreelancerJobDTOResponse unapplyJob(Long jobId, Long freelancerId) {
        Optional<FreelancerJob> existingFreelancerJob = freelancerJobRepository.findByFreelancer_IdAndJob_Id(
                freelancerId, jobId);

        if (!existingFreelancerJob.isPresent()) {
            throw new BadRequestException("Freelancer Job Not Found");
        }

        FreelancerJob freelancerJob = existingFreelancerJob.get();

        if (freelancerJob.getStatus() != StatusFreelancerJob.Applied) {
            throw new BadRequestException("Can only unapply from jobs with Applied status");
        }

        freelancerJob.setStatus(StatusFreelancerJob.Viewed);
        FreelancerJob updatedFreelancerJob = freelancerJobRepository.save(freelancerJob);

        return new FreelancerJobDTOResponse(
                updatedFreelancerJob.getId(),
                updatedFreelancerJob.getIsSaved(),
                updatedFreelancerJob.getStatus(),
                updatedFreelancerJob.getJob().getId(),
                updatedFreelancerJob.getFreelancer().getId(),
                updatedFreelancerJob.getCv().getUrl()
        );
    }

    @Override
    public List<SaveJobDTOResponse> getSavedJobs(Long freelancerId) {
        List<FreelancerJob> freelancerJobs = freelancerJobRepository.getSavedJobs(freelancerId);
        try {
            return freelancerJobs.stream()
                    .map(freelancerJob -> {
                        SaveJobDTOResponse dto = saveJobMapper.toResponseDto(freelancerJob);
                        Long id = freelancerJob.getJob().getClient().getId();
                        Company company = companyRepository.getCompanyByClientId(id)
                                .orElse(null);
                        dto.setCompanyName(company != null ? company.getCompanyName() : null);
                        return dto;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BadRequestException("Error while fetching saved jobs");
        }
    }

    @Override
    public FreelancerJob findById(Long jobId) {
        if (jobId == null) {
            throw new BadRequestException("Job ID is null");
        }

        return freelancerJobRepository.findById(jobId).orElseThrow(() -> new BadRequestException("Job Not Found"));
    }

    @Override
    public List<FreelancerJobDetailDTOResponse> getJobDetailsByFreelancerId(Long freelancerId) {
        if (freelancerId == null) {
            throw new BadRequestException("Freelancer ID is null");
        }

        List<FreelancerJob> freelancerJobs = freelancerJobRepository.findByFreelancer_Id(freelancerId);
        if (freelancerJobs.isEmpty()) {
            return Collections.emptyList();
        }

        return freelancerJobs.stream()
                .filter(freelancerJob -> freelancerJob.getClientReview() != null) 
                .map(freelancerJob -> {
                    Job job = freelancerJob.getJob();
                    if (job == null) {
                        throw new BadRequestException("Job Not Found for FreelancerJob ID: " + freelancerJob.getId());
                    }

                    String clientName = job.getClient() != null && job.getClient().getUser() != null
                            ? job.getClient().getUser().getFirstName() + " " + job.getClient().getUser().getLastName()
                            : "Unknown Client";

                    ClientReview clientReview = freelancerJob.getClientReview();
                    Float rating = clientReview.getRating();
                    String note = clientReview.getNote();

                    Date startDate = job.getCreatedAt();
                    Date endDate = null;
                    if (startDate != null && job.getDuration() != null) {
                        java.util.Calendar calendar = java.util.Calendar.getInstance();
                        calendar.setTime(startDate);
                        calendar.add(java.util.Calendar.DATE, job.getDuration().intValue());
                        endDate = calendar.getTime();
                    }

                    return new FreelancerJobDetailDTOResponse(
                            job.getTitle(),
                            job.getScope(),
                            clientName,
                            job.getDuration(),
                            job.getHourWork(),
                            startDate,
                            endDate,
                            rating,
                            note
                    );
                })
                .collect(Collectors.toList());
    }

}
