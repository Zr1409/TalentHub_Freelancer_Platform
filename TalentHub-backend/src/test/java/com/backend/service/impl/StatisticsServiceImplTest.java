package com.backend.service.impl;

import com.backend.enums.StatusFreelancerJob;
import com.backend.enums.StatusJob;
import com.backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private FreelancerRepository freelancerRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private FreelancerJobRepository freelancerJobRepository;

    @Mock
    private SoldPackageRepository soldPackageRepository;

    @Mock
    private BannerRepository bannerRepository;

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @BeforeEach
    void setUp() {
        // Setup default mock responses
        when(clientRepository.count()).thenReturn(10L);
        when(freelancerRepository.count()).thenReturn(20L);
        when(jobRepository.countByStatusNot(StatusJob.DRAFT)).thenReturn(15L);
        when(freelancerJobRepository.countByStatus(StatusFreelancerJob.Approved)).thenReturn(8L);
        when(soldPackageRepository.getTotalSoldPackageRevenue()).thenReturn(1000.0);
        when(bannerRepository.getTotalBannerRevenue()).thenReturn(500.0);
    }

    @Test
    void getOverallStatistics_ShouldReturnCorrectStatistics() {
        // Act
        Map<String, Object> statistics = statisticsService.getOverallStatistics();

        // Assert
        assertNotNull(statistics);
        assertTrue((Boolean) statistics.get("success"));
        assertEquals(30L, statistics.get("totalAccounts"));
        assertEquals(15L, statistics.get("postedJobs"));
        assertEquals(10L, statistics.get("totalClients"));
        assertEquals(20L, statistics.get("totalFreelancers"));
        assertEquals(8L, statistics.get("approvedFreelancerJobs"));
        assertEquals(1500.0, statistics.get("totalRevenue"));
        assertNotNull(statistics.get("timestamp"));

        // Verify interactions
        verify(clientRepository).count();
        verify(freelancerRepository).count();
        verify(jobRepository).countByStatusNot(StatusJob.DRAFT);
        verify(freelancerJobRepository).countByStatus(StatusFreelancerJob.Approved);
        verify(soldPackageRepository).getTotalSoldPackageRevenue();
        verify(bannerRepository).getTotalBannerRevenue();
    }

    @Test
    void getOverallStatistics_WhenExceptionThrown_ShouldReturnErrorMap() {
        // Arrange
        when(clientRepository.count()).thenThrow(new RuntimeException("Database error"));

        // Act
        Map<String, Object> statistics = statisticsService.getOverallStatistics();

        // Assert
        assertNotNull(statistics);
        assertFalse((Boolean) statistics.get("success"));
        assertTrue(statistics.containsKey("error"));
        assertTrue(((String) statistics.get("error")).contains("Error fetching statistics"));

        // Verify interactions
        verify(clientRepository).count();
        verifyNoMoreInteractions(freelancerRepository, jobRepository, freelancerJobRepository,
                soldPackageRepository, bannerRepository);
    }

    @Test
    void getOverallStatistics_WithZeroValues_ShouldHandleCorrectly() {
        // Arrange
        when(clientRepository.count()).thenReturn(0L);
        when(freelancerRepository.count()).thenReturn(0L);
        when(jobRepository.countByStatusNot(StatusJob.DRAFT)).thenReturn(0L);
        when(freelancerJobRepository.countByStatus(StatusFreelancerJob.Approved)).thenReturn(0L);
        when(soldPackageRepository.getTotalSoldPackageRevenue()).thenReturn(0.0);
        when(bannerRepository.getTotalBannerRevenue()).thenReturn(0.0);

        // Act
        Map<String, Object> statistics = statisticsService.getOverallStatistics();

        // Assert
        assertNotNull(statistics);
        assertTrue((Boolean) statistics.get("success"));
        assertEquals(0L, statistics.get("totalAccounts"));
        assertEquals(0L, statistics.get("postedJobs"));
        assertEquals(0L, statistics.get("totalClients"));
        assertEquals(0L, statistics.get("totalFreelancers"));
        assertEquals(0L, statistics.get("approvedFreelancerJobs"));
        assertEquals(0.0, statistics.get("totalRevenue"));
        assertNotNull(statistics.get("timestamp"));

        // Verify interactions
        verify(clientRepository).count();
        verify(freelancerRepository).count();
        verify(jobRepository).countByStatusNot(StatusJob.DRAFT);
        verify(freelancerJobRepository).countByStatus(StatusFreelancerJob.Approved);
        verify(soldPackageRepository).getTotalSoldPackageRevenue();
        verify(bannerRepository).getTotalBannerRevenue();
    }
}