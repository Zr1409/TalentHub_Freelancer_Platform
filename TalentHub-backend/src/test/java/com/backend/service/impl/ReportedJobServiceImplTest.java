package com.backend.service.impl;

import com.backend.dto.request.ReportedJobDTORequest;
import com.backend.dto.response.ReportedJobDTOResponse;
import com.backend.entity.child.ReportedJob;
import com.backend.entity.child.account.freelancer.Freelancer;
import com.backend.entity.child.job.Job;
import com.backend.enums.ReportedJobStatus;
import com.backend.exception.NotFoundException;
import com.backend.mapper.ReportedJobMapper;
import com.backend.repository.FreelancerRepository;
import com.backend.repository.JobRepository;
import com.backend.repository.ReportedJobRepository;
import com.backend.service.intf.image.CloudinaryImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportedJobServiceImplTest {

    @Mock
    private ReportedJobRepository reportedJobRepository;

    @Mock
    private FreelancerRepository freelancerRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private ReportedJobMapper reportedJobMapper;

    @Mock
    private CloudinaryImageService cloudinaryImageService;

    @InjectMocks
    private ReportedJobServiceImpl reportedJobService;

    private ReportedJobDTORequest reportedJobDTORequest;
    private ReportedJob reportedJob;
    private ReportedJobDTOResponse reportedJobDTOResponse;
    private Freelancer freelancer;
    private Job job;
    private MultipartFile mockImage;

    @BeforeEach
    void setUp() {
        // Setup test data
        freelancer = new Freelancer();
        freelancer.setId(1L);

        job = new Job();
        job.setId(1L);

        reportedJob = new ReportedJob();
        reportedJob.setId(1L);
        reportedJob.setReasonFreelancer("Test Reason Freelancer");
        reportedJob.setReasonAdmin("Test Reason Admin");
        reportedJob.setDescription("Test Description");
        reportedJob.setStatus(ReportedJobStatus.IN_PROGRESS);
        reportedJob.setFreelancer(freelancer);
        reportedJob.setJob(job);
        reportedJob.setImage("http://example.com/image.jpg");

        reportedJobDTORequest = new ReportedJobDTORequest();
        reportedJobDTORequest.setReasonFreelancer("Test Reason Freelancer");
        reportedJobDTORequest.setReasonAdmin("Test Reason Admin");
        reportedJobDTORequest.setDescription("Test Description");
        reportedJobDTORequest.setStatus(ReportedJobStatus.IN_PROGRESS);
        reportedJobDTORequest.setFreelancerId(1L);
        reportedJobDTORequest.setJobId(1L);

        mockImage = mock(MultipartFile.class);
        reportedJobDTORequest.setImage(mockImage);

        reportedJobDTOResponse = new ReportedJobDTOResponse();
        reportedJobDTOResponse.setId(1L);
        reportedJobDTOResponse.setReasonFreelancer("Test Reason Freelancer");
        reportedJobDTOResponse.setReasonAdmin("Test Reason Admin");
        reportedJobDTOResponse.setDescription("Test Description");
        reportedJobDTOResponse.setStatus(ReportedJobStatus.IN_PROGRESS);
        reportedJobDTOResponse.setFreelancerId(1L);
        reportedJobDTOResponse.setJobId(1L);
        reportedJobDTOResponse.setFullName("Test Freelancer");
        reportedJobDTOResponse.setJobTitle("Test Job");
        reportedJobDTOResponse.setImage("http://example.com/image.jpg");
    }

    @Test
    void create_ShouldReturnReportedJobDTOResponse() {
        // Arrange
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(cloudinaryImageService.uploadImage(any(MultipartFile.class), "talenthub_images")).thenReturn("http://example.com/image.jpg");
        when(reportedJobRepository.save(any(ReportedJob.class))).thenReturn(reportedJob);
        when(reportedJobMapper.toDTO(reportedJob)).thenReturn(reportedJobDTOResponse);

        // Act
        ReportedJobDTOResponse response = reportedJobService.create(reportedJobDTORequest);

        // Assert
        assertNotNull(response);
        assertEquals(reportedJobDTOResponse.getId(), response.getId());
        assertEquals(reportedJobDTOResponse.getReasonFreelancer(), response.getReasonFreelancer());
        assertEquals(reportedJobDTOResponse.getReasonAdmin(), response.getReasonAdmin());
        assertEquals(reportedJobDTOResponse.getDescription(), response.getDescription());
        assertEquals(reportedJobDTOResponse.getStatus(), response.getStatus());
        assertEquals(reportedJobDTOResponse.getFreelancerId(), response.getFreelancerId());
        assertEquals(reportedJobDTOResponse.getJobId(), response.getJobId());

        verify(freelancerRepository).findById(1L);
        verify(jobRepository).findById(1L);
        verify(cloudinaryImageService).uploadImage(mockImage, "talenthub_images");
        verify(reportedJobRepository).save(any(ReportedJob.class));
        verify(reportedJobMapper).toDTO(reportedJob);
    }

    @Test
    void create_FreelancerNotFound_ShouldThrowNotFoundException() {
        // Arrange
        when(freelancerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> reportedJobService.create(reportedJobDTORequest));
        verify(freelancerRepository).findById(1L);
        verifyNoInteractions(cloudinaryImageService, reportedJobRepository, reportedJobMapper);
    }

    @Test
    void create_JobNotFound_ShouldThrowNotFoundException() {
        // Arrange
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(jobRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> reportedJobService.create(reportedJobDTORequest));
        verify(freelancerRepository).findById(1L);
        verify(jobRepository).findById(1L);
        verifyNoInteractions(cloudinaryImageService, reportedJobRepository, reportedJobMapper);
    }

    @Test
    void update_ShouldReturnUpdatedReportedJobDTOResponse() {
        // Arrange
        when(reportedJobRepository.findById(1L)).thenReturn(Optional.of(reportedJob));
        when(reportedJobRepository.save(reportedJob)).thenReturn(reportedJob);
        when(reportedJobMapper.toDTO(reportedJob)).thenReturn(reportedJobDTOResponse);

        // Act
        ReportedJobDTOResponse response = reportedJobService.update(1L, reportedJobDTORequest);

        // Assert
        assertNotNull(response);
        assertEquals(reportedJobDTOResponse.getId(), response.getId());
        assertEquals(reportedJobDTOResponse.getReasonAdmin(), response.getReasonAdmin());
        assertEquals(reportedJobDTOResponse.getStatus(), response.getStatus());

        verify(reportedJobRepository).findById(1L);
        verify(reportedJobRepository).save(reportedJob);
        verify(reportedJobMapper).toDTO(reportedJob);
    }

    @Test
    void update_ReportedJobNotFound_ShouldThrowNotFoundException() {
        // Arrange
        when(reportedJobRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> reportedJobService.update(1L, reportedJobDTORequest));
        verify(reportedJobRepository).findById(1L);
        verifyNoMoreInteractions(reportedJobRepository, reportedJobMapper);
    }

    @Test
    void getById_ShouldReturnReportedJobDTOResponse() {
        // Arrange
        when(reportedJobRepository.findById(1L)).thenReturn(Optional.of(reportedJob));
        when(reportedJobMapper.toDTO(reportedJob)).thenReturn(reportedJobDTOResponse);

        // Act
        Optional<ReportedJobDTOResponse> response = reportedJobService.getById(1L);

        // Assert
        assertTrue(response.isPresent());
        assertEquals(reportedJobDTOResponse, response.get());
        verify(reportedJobRepository).findById(1L);
        verify(reportedJobMapper).toDTO(reportedJob);
    }

    @Test
    void getById_ReportedJobNotFound_ShouldReturnEmptyOptional() {
        // Arrange
        when(reportedJobRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<ReportedJobDTOResponse> response = reportedJobService.getById(1L);

        // Assert
        assertFalse(response.isPresent());
        verify(reportedJobRepository).findById(1L);
        verifyNoInteractions(reportedJobMapper);
    }

    @Test
    void getAll_ShouldReturnListOfReportedJobDTOResponses() {
        // Arrange
        ReportedJob reportedJob2 = new ReportedJob();
        reportedJob2.setId(2L);

        ReportedJobDTOResponse reportedJobDTOResponse2 = new ReportedJobDTOResponse();
        reportedJobDTOResponse2.setId(2L);

        when(reportedJobRepository.findAll()).thenReturn(Arrays.asList(reportedJob, reportedJob2));
        when(reportedJobMapper.toDTO(reportedJob)).thenReturn(reportedJobDTOResponse);
        when(reportedJobMapper.toDTO(reportedJob2)).thenReturn(reportedJobDTOResponse2);

        // Act
        List<ReportedJobDTOResponse> responses = reportedJobService.getAll();

        // Assert
        assertEquals(2, responses.size());
        assertEquals(reportedJobDTOResponse, responses.get(0));
        assertEquals(reportedJobDTOResponse2, responses.get(1));
        verify(reportedJobRepository).findAll();
        verify(reportedJobMapper, times(2)).toDTO(any(ReportedJob.class));
    }

    @Test
    void deleteById_ReportedJobExists_ShouldReturnTrue() {
        // Arrange
        when(reportedJobRepository.findById(1L)).thenReturn(Optional.of(reportedJob));

        // Act
        Boolean result = reportedJobService.deleteById(1L);

        // Assert
        assertTrue(result);
        verify(reportedJobRepository).findById(1L);
        verify(reportedJobRepository).delete(reportedJob);
    }

    @Test
    void deleteById_ReportedJobNotFound_ShouldReturnFalse() {
        // Arrange
        when(reportedJobRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Boolean result = reportedJobService.deleteById(1L);

        // Assert
        assertFalse(result);
        verify(reportedJobRepository).findById(1L);
        verify(reportedJobRepository, never()).delete(any(ReportedJob.class));
    }

    @Test
    void getByJobId_ShouldReturnListOfReportedJobDTOResponses() {
        // Arrange
        when(reportedJobRepository.findByJobId(1L)).thenReturn(List.of(reportedJob));
        when(reportedJobMapper.toDTO(reportedJob)).thenReturn(reportedJobDTOResponse);

        // Act
        List<ReportedJobDTOResponse> responses = reportedJobService.getByJobId(1L);

        // Assert
        assertEquals(1, responses.size());
        assertEquals(reportedJobDTOResponse, responses.get(0));
        verify(reportedJobRepository).findByJobId(1L);
        verify(reportedJobMapper).toDTO(reportedJob);
    }

    @Test
    void getByFreelancerId_ShouldReturnListOfReportedJobDTOResponses() {
        // Arrange
        when(reportedJobRepository.findByFreelancerId(1L)).thenReturn(List.of(reportedJob));
        when(reportedJobMapper.toDTO(reportedJob)).thenReturn(reportedJobDTOResponse);

        // Act
        List<ReportedJobDTOResponse> responses = reportedJobService.getByFreelancerId(1L);

        // Assert
        assertEquals(1, responses.size());
        assertEquals(reportedJobDTOResponse, responses.get(0));
        verify(reportedJobRepository).findByFreelancerId(1L);
        verify(reportedJobMapper).toDTO(reportedJob);
    }
}