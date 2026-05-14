package com.backend.service.impl;

import com.backend.dto.request.account.client.AppointmentDetailDTORequest;
import com.backend.dto.response.account.client.AppointmentDetailDTOResponse;
import com.backend.entity.child.account.Account;
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
import com.backend.service.impl.account.client.AppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @Mock
    private FreelancerRepository freelancerRepository;

    @Mock
    private FreelancerJobRepository freelancerJobRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private AppointmentDetailDTORequest validRequest;
    private AppointmentDetailDTOResponse mockResponse;
    private Appointment mockAppointment;
    private FreelancerJob mockFreelancerJob;
    private Freelancer mockFreelancer;
    private User mockFreelancerUser;
    private Account mockFreelancerAccount;
    private Client mockClient;
    private Job mockJob;

    @BeforeEach
    void setUp() {
        // Setup valid request
        validRequest = new AppointmentDetailDTORequest();
        validRequest.setClientId(1L);
        validRequest.setFreelancerJobId(1L);
        validRequest.setTopic("Test Meeting");
        validRequest.setStartTime(LocalDateTime.now().plusDays(1));
        validRequest.setDuration(60L);
        validRequest.setDescription("Test Description");
        validRequest.setLink("https://meet.google.com/test");

        // Setup mock entities
        mockJob = new Job();
        mockJob.setId(10L);
        mockJob.setTitle("Test Job");

        mockFreelancerAccount = new Account();
        mockFreelancerAccount.setId(3L);
        mockFreelancerAccount.setEmail("freelancer@test.com");

        mockFreelancerUser = new User();
        mockFreelancerUser.setId(2L);
        mockFreelancerUser.setFirstName("John");
        mockFreelancerUser.setLastName("Doe");
        mockFreelancerUser.setPhoneNumber("1234567890");
        mockFreelancerUser.setAccount(mockFreelancerAccount);

        mockFreelancer = new Freelancer();
        mockFreelancer.setId(1L);
        mockFreelancer.setUser(mockFreelancerUser);

        mockFreelancerJob = new FreelancerJob();
        mockFreelancerJob.setId(1L);
        mockFreelancerJob.setFreelancer(mockFreelancer);
        mockFreelancerJob.setJob(mockJob);

        mockClient = new Client();
        mockClient.setId(1L);

        mockAppointment = new Appointment();
        mockAppointment.setId(1L);
        mockAppointment.setTopic("Test Meeting");
        mockAppointment.setStartTime(LocalDateTime.now().plusDays(1));
        mockAppointment.setDuration(60L);
        mockAppointment.setDescription("Test Description");
        mockAppointment.setLink("https://meet.google.com/test");
        mockAppointment.setClient(mockClient);
        mockAppointment.setFreelancerJob(mockFreelancerJob);

        // Setup mock response
        mockResponse = new AppointmentDetailDTOResponse();
        mockResponse.setId(1L);
        mockResponse.setTopic("Test Meeting");
        mockResponse.setStartTime(LocalDateTime.now().plusDays(1));
        mockResponse.setDuration(60L);
        mockResponse.setDescription("Test Description");
        mockResponse.setLink("https://meet.google.com/test");
        mockResponse.setName("Doe John");
        mockResponse.setMail("freelancer@test.com");
        mockResponse.setPhone("1234567890");
        mockResponse.setJobId(10L);
        mockResponse.setJobTitle("Test Job");
    }

    @Test
    void create_WithValidRequest_ShouldReturnAppointmentDetailDTOResponse() {
        // Arrange
        when(freelancerJobRepository.findById(1L)).thenReturn(Optional.of(mockFreelancerJob));
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(mockFreelancer));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));
        when(appointmentMapper.toEntity(validRequest)).thenReturn(mockAppointment);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(mockAppointment);
        when(appointmentMapper.toResponseDto(mockAppointment)).thenReturn(mockResponse);

        // Act
        AppointmentDetailDTOResponse result = appointmentService.create(validRequest);

        // Assert
        assertNotNull(result);
        assertEquals(mockResponse.getId(), result.getId());
        assertEquals(mockResponse.getTopic(), result.getTopic());
        assertEquals(mockResponse.getDescription(), result.getDescription());
        assertEquals(mockResponse.getName(), result.getName());
        assertEquals(mockResponse.getMail(), result.getMail());
        assertEquals(mockResponse.getPhone(), result.getPhone());

        verify(freelancerJobRepository).findById(1L);
        verify(freelancerRepository).findById(1L);
        verify(clientRepository).findById(1L);
        verify(appointmentMapper).toEntity(validRequest);
        verify(appointmentRepository).save(any(Appointment.class));
        verify(appointmentMapper).toResponseDto(mockAppointment);
    }

    @Test
    void create_WithNullRequest_ShouldThrowBadRequestException() {
        // Act & Assert
        assertThrows(BadRequestException.class, () -> appointmentService.create(null));

        verifyNoInteractions(freelancerJobRepository, freelancerRepository, clientRepository,
                appointmentMapper, appointmentRepository);
    }

    @Test
    void create_WithNullClientId_ShouldThrowBadRequestException() {
        // Arrange
        validRequest.setClientId(null);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> appointmentService.create(validRequest));

        verifyNoInteractions(freelancerJobRepository, freelancerRepository, clientRepository,
                appointmentMapper, appointmentRepository);
    }

    @Test
    void create_WithNullFreelancerJobId_ShouldThrowBadRequestException() {
        // Arrange
        validRequest.setFreelancerJobId(null);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> appointmentService.create(validRequest));

        verifyNoInteractions(freelancerJobRepository, freelancerRepository, clientRepository,
                appointmentMapper, appointmentRepository);
    }

    @Test
    void create_WithNullStartTime_ShouldThrowBadRequestException() {
        // Arrange
        validRequest.setStartTime(null);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> appointmentService.create(validRequest));

        verifyNoInteractions(freelancerJobRepository, freelancerRepository, clientRepository,
                appointmentMapper, appointmentRepository);
    }

    @Test
    void create_WithNullLink_ShouldThrowBadRequestException() {
        // Arrange
        validRequest.setLink(null);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> appointmentService.create(validRequest));

        verifyNoInteractions(freelancerJobRepository, freelancerRepository, clientRepository,
                appointmentMapper, appointmentRepository);
    }

    @Test
    void getAllAppointmentsByClientId_ShouldReturnListOfAppointments() {
        // Arrange
        Long clientId = 1L;
        Appointment appointment1 = mockAppointment;
        Appointment appointment2 = mock(Appointment.class);
        when(appointment2.getFreelancerJob()).thenReturn(mockFreelancerJob);

        List<Appointment> appointments = Arrays.asList(appointment1, appointment2);
        when(appointmentRepository.findAppointmentsByClient_Id(clientId)).thenReturn(appointments);

        AppointmentDetailDTOResponse response1 = mockResponse;
        AppointmentDetailDTOResponse response2 = new AppointmentDetailDTOResponse();
        when(appointmentMapper.toResponseDto(appointment1)).thenReturn(response1);
        when(appointmentMapper.toResponseDto(appointment2)).thenReturn(response2);

        // Act
        List<AppointmentDetailDTOResponse> results = appointmentService.getAllAppointmentsByClientId(clientId);

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(response1, results.get(0));
        assertEquals(response2, results.get(1));

        verify(appointmentRepository).findAppointmentsByClient_Id(clientId);
        verify(appointmentMapper, times(2)).toResponseDto(any(Appointment.class));
    }

    @Test
    void getAllAppointmentsByClientId_WithNullClientId_ShouldThrowBadRequestException() {
        // Act & Assert
        assertThrows(BadRequestException.class, () -> appointmentService.getAllAppointmentsByClientId(null));

        verifyNoInteractions(appointmentRepository, appointmentMapper);
    }

    @Test
    void getAllAppointmentsByFreelancerId_ShouldReturnListOfAppointments() {
        // Arrange
        Long freelancerId = 1L;

        User clientUser = new User();
        clientUser.setFirstName("Jane");
        clientUser.setLastName("Smith");
        clientUser.setPhoneNumber("0987654321");
        clientUser.setAccount(new Account());
        clientUser.getAccount().setEmail("client@test.com");

        Client client = new Client();
        client.setUser(clientUser);

        Appointment appointment1 = new Appointment();
        appointment1.setFreelancerJob(mockFreelancerJob);
        appointment1.setClient(client);

        List<Appointment> appointments = List.of(appointment1);
        when(appointmentRepository.findAppointmentsByFreelancerJob_Freelancer_Id(freelancerId)).thenReturn(appointments);

        AppointmentDetailDTOResponse response1 = new AppointmentDetailDTOResponse();
        when(appointmentMapper.toResponseDto(appointment1)).thenReturn(response1);

        // Act
        List<AppointmentDetailDTOResponse> results = appointmentService.getAllAppointmentsByFreelancerId(freelancerId);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());

        verify(appointmentRepository).findAppointmentsByFreelancerJob_Freelancer_Id(freelancerId);
        verify(appointmentMapper).toResponseDto(any(Appointment.class));
    }

    @Test
    void getAllAppointmentsByFreelancerId_WithNullFreelancerId_ShouldThrowBadRequestException() {
        // Act & Assert
        assertThrows(BadRequestException.class, () -> appointmentService.getAllAppointmentsByFreelancerId(null));

        verifyNoInteractions(appointmentRepository, appointmentMapper);
    }

    @Test
    void update_WithValidRequest_ShouldReturnUpdatedAppointment() {
        // Arrange
        Long appointmentId = 1L;
        AppointmentDetailDTORequest updateRequest = new AppointmentDetailDTORequest();
        updateRequest.setTopic("Updated Topic");
        updateRequest.setDescription("Updated Description");
        updateRequest.setStartTime(LocalDateTime.now().plusDays(2));
        updateRequest.setDuration(90L);
        updateRequest.setLink("https://meet.google.com/updated");

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(mockAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(mockAppointment);
        when(appointmentMapper.toResponseDto(mockAppointment)).thenReturn(mockResponse);

        // Act
        AppointmentDetailDTOResponse result = appointmentService.update(appointmentId, updateRequest);

        // Assert
        assertNotNull(result);

        // Verify the appointment was updated with new values
        verify(appointmentRepository).findById(appointmentId);
        verify(appointmentRepository).save(mockAppointment);
        verify(appointmentMapper).toResponseDto(mockAppointment);

        // Verify each field was updated
        assertEquals("Updated Topic", mockAppointment.getTopic());
        assertEquals("Updated Description", mockAppointment.getDescription());
        assertEquals(90, mockAppointment.getDuration());
        assertEquals("https://meet.google.com/updated", mockAppointment.getLink());
    }

    @Test
    void update_WithNullId_ShouldThrowBadRequestException() {
        // Act & Assert
        assertThrows(BadRequestException.class, () ->
                appointmentService.update(null, new AppointmentDetailDTORequest()));

        verifyNoInteractions(appointmentRepository, appointmentMapper);
    }

    @Test
    void update_WithNonExistentAppointment_ShouldThrowNotFoundException() {
        // Arrange
        Long appointmentId = 999L;
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () ->
                appointmentService.update(appointmentId, new AppointmentDetailDTORequest()));

        verify(appointmentRepository).findById(appointmentId);
        verifyNoMoreInteractions(appointmentRepository, appointmentMapper);
    }


    @Test
    void update_WithNonExistentFreelancerJob_ShouldThrowNotFoundException() {
        // Arrange
        Long appointmentId = 1L;
        AppointmentDetailDTORequest updateRequest = new AppointmentDetailDTORequest();
        updateRequest.setFreelancerJobId(999L);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(mockAppointment));
        when(freelancerJobRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () ->
                appointmentService.update(appointmentId, updateRequest));

        verify(appointmentRepository).findById(appointmentId);
        verify(freelancerJobRepository).findById(999L);
        verifyNoMoreInteractions(appointmentRepository, appointmentMapper);
    }
}