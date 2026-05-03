package com.backend.service.impl.account.client;

import lombok.RequiredArgsConstructor;
import com.backend.dto.request.account.client.AppointmentDetailDTORequest;
import com.backend.dto.response.account.client.AppointmentDetailDTOResponse;
import com.backend.entity.child.account.User;
import com.backend.entity.child.account.client.Appointment;
import com.backend.entity.child.account.client.Client;
import com.backend.entity.child.account.freelancer.Freelancer;
import com.backend.entity.child.job.FreelancerJob;
import com.backend.entity.child.job.Job;
import com.backend.exception.BadRequestException;
import com.backend.exception.NotFoundException;
import com.backend.mapper.Account.client.AppointmentMapper;
import com.backend.repository.AppointmentRepository;
import com.backend.repository.ClientRepository;
import com.backend.repository.FreelancerJobRepository;
import com.backend.repository.FreelancerRepository;
import com.backend.service.impl.notify.NotifyService;
import com.backend.service.intf.account.client.AppointmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final FreelancerRepository freelancerRepository;
    private final FreelancerJobRepository freelancerJobRepository;
    private final ClientRepository clientRepository;
    private final NotifyService notifyService;

    @Override
    public AppointmentDetailDTOResponse create(AppointmentDetailDTORequest appointmentDetailDTORequest) {
        if (appointmentDetailDTORequest == null) {
            throw new BadRequestException("appointmentDetailDTORequest cannot be null");
        }

        if (appointmentDetailDTORequest.getClientId() == null) {
            throw new BadRequestException("clientId cannot be null");
        }

        if (appointmentDetailDTORequest.getFreelancerJobId() == null) {
            throw new BadRequestException("freelancerJobId cannot be null");
        }

        if (appointmentDetailDTORequest.getStartTime() == null) {
            throw new BadRequestException("startTime cannot be null");
        }

        if (appointmentDetailDTORequest.getLink() == null) {
            throw new BadRequestException("link cannot be null");
        }

        Optional<FreelancerJob> freelancerJob = freelancerJobRepository.findById(appointmentDetailDTORequest.getFreelancerJobId());

        Freelancer freelancer = freelancerRepository.findById(freelancerJob.get().getFreelancer().getId()).get();
        User user = freelancer.getUser();
        Client client = clientRepository.findById(appointmentDetailDTORequest.getClientId()).get();

        Appointment appointment = appointmentMapper.toEntity(appointmentDetailDTORequest);
        appointment.setClient(client);
        appointment.setFreelancerJob(freelancerJob.get());
        appointmentRepository.save(appointment);

        AppointmentDetailDTOResponse appointmentDetailDTOResponse = appointmentMapper.toResponseDto(appointment);
        appointmentDetailDTOResponse.setName(freelancer.getUser().getLastName() + " " + freelancer.getUser().getFirstName());
        appointmentDetailDTOResponse.setMail(user.getAccount().getEmail());
        appointmentDetailDTOResponse.setPhone(user.getPhoneNumber());
        notifyService.sendNotification(freelancerJob.get().getFreelancer().getUser().getId(), "Bạn có lịch hẹn với khách hàng " + client.getUser().getFirstName() + " " + client.getUser().getLastName(), "freelancer/appointment");

        return appointmentDetailDTOResponse;
    }

    @Override
    public Optional<AppointmentDetailDTOResponse> getById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<AppointmentDetailDTOResponse> getAll() {
        return List.of();
    }

    @Override
    public Boolean deleteById(Long aLong) {
        return null;
    }

    @Override
    public List<AppointmentDetailDTOResponse> getAllAppointmentsByClientId(Long clientId) {
        if (clientId == null) {
            throw new BadRequestException("ClientId cannot be null");
        }

        List<Appointment> appointments = appointmentRepository.findAppointmentsByClient_Id(clientId);

        return appointments.stream()
                .map(appointment -> {
                    AppointmentDetailDTOResponse response = appointmentMapper.toResponseDto(appointment);

                    FreelancerJob freelancerJob = appointment.getFreelancerJob();
                    Freelancer freelancer = freelancerJob.getFreelancer();
                    User user = freelancer.getUser();
                    Job job = freelancerJob.getJob();
                    response.setImage(user.getImage());
                    response.setName(freelancer.getUser().getLastName() + " " + freelancer.getUser().getFirstName());
                    response.setMail(user.getAccount().getEmail());
                    response.setPhone(user.getPhoneNumber());
                    response.setJobId(job.getId());
                    response.setJobTitle(job.getTitle());
                    response.setIsCompleted(appointment.getIsCompleted());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<AppointmentDetailDTOResponse> getAllAppointmentsByFreelancerId(Long freelancerId) {
        if (freelancerId == null) {
            throw new BadRequestException("freelancerId cannot be null");
        }

        List<Appointment> appointments = appointmentRepository.findAppointmentsByFreelancerJob_Freelancer_Id(freelancerId);

        return appointments.stream().map(appointment -> {
            AppointmentDetailDTOResponse response = appointmentMapper.toResponseDto(appointment);

            Client client = appointment.getClient();
            User user = client.getUser();
            FreelancerJob freelancerJob = appointment.getFreelancerJob();
            Job job = freelancerJob.getJob();
            response.setImage(user.getImage());
            response.setName(user.getLastName() + " " + user.getFirstName());
            response.setMail(user.getAccount().getEmail());
            response.setPhone(user.getPhoneNumber());
            response.setJobId(job.getId());
            response.setJobTitle(job.getTitle());
            response.setIsCompleted(appointment.getIsCompleted());
            return response;
        }).collect(Collectors.toList());
    }


    @Override
    public AppointmentDetailDTOResponse update(Long id, AppointmentDetailDTORequest request) {
        if (id == null) {
            throw new BadRequestException("Appointment id cannot be null");
        }

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found with id: " + id));

        if (request.getTopic() != null) {
            appointment.setTopic(request.getTopic());
        }

        if (request.getStartTime() != null) {
            appointment.setStartTime(request.getStartTime());
        }

        if (request.getDuration() != null) {
            appointment.setDuration(request.getDuration());
        }

        if (request.getDescription() != null) {
            appointment.setDescription(request.getDescription());
        }

        if (request.getLink() != null) {
            appointment.setLink(request.getLink());
        }
        if (request.getType() != null) {
            appointment.setType(request.getType());
        }


        if (request.getFreelancerJobId() != null &&
                !request.getFreelancerJobId().equals(appointment.getFreelancerJob().getId())) {

            FreelancerJob freelancerJob = freelancerJobRepository.findById(request.getFreelancerJobId())
                    .orElseThrow(() -> new NotFoundException("FreelancerJob not found with id: " + request.getFreelancerJobId()));
            appointment.setFreelancerJob(freelancerJob);
        }

        appointmentRepository.save(appointment);

        AppointmentDetailDTOResponse response = appointmentMapper.toResponseDto(appointment);

        FreelancerJob freelancerJob = appointment.getFreelancerJob();
        Freelancer freelancer = freelancerJob.getFreelancer();
        User user = freelancer.getUser();
        Job job = freelancerJob.getJob();

        response.setName(user.getLastName() + " " + user.getFirstName());
        response.setMail(user.getAccount().getEmail());
        response.setPhone(user.getPhoneNumber());
        response.setJobId(job.getId());
        response.setJobTitle(job.getTitle());
        notifyService.sendNotification(freelancerJob.getFreelancer().getUser().getId(), "Khách hàng có công việc: "+ freelancerJob.getJob().getTitle() + " vừa thay đổi lịch hẹn, vui lòng kiểm tra lại", "freelancer/appointment");

        return response;
    }

    @Override
    @Transactional
    public String markAppointmentAsCompleted(Long id) {
        return appointmentRepository.findById(id)
                .map(appointment -> {
                    appointment.setIsCompleted(true);
                    appointmentRepository.save(appointment);
                    return "Appointment marked as completed successfully";
                })
                .orElseThrow(() -> new RuntimeException("Appointment with ID " + id + " not found"));
    }
}
