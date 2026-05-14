package com.backend.service.impl;

import com.backend.dto.request.BannerDTORequest;
import com.backend.dto.response.BannerDTOResponse;
import com.backend.dto.response.LogoDTOResponse;
import com.backend.entity.child.Banner;
import com.backend.exception.NotFoundException;
import com.backend.repository.BannerRepository;
import com.backend.service.intf.image.CloudinaryImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BannerServiceImplTest {

    @Mock
    private BannerRepository bannerRepository;

    @Mock
    private CloudinaryImageService cloudinaryImageService;

    @InjectMocks
    private BannerServiceImpl bannerService;

    private Banner banner;
    private BannerDTORequest bannerDTORequest;
    private MultipartFile mockImage;
    private MultipartFile mockLogo;

    @BeforeEach
    void setUp() {
        // Setup mock banner
        banner = new Banner();
        banner.setId(1L);
        banner.setTitle("Test Banner");
        banner.setImage("http://example.com/image.jpg");
        banner.setLogo("http://example.com/logo.jpg");
        banner.setStatus(true);
        banner.setVendor("Test Vendor");
        banner.setPrice(100.0);
        banner.setStartTime(LocalDateTime.now());
        banner.setEndTime(LocalDateTime.now().plusDays(30));

        // Setup mock request
        bannerDTORequest = new BannerDTORequest();
        bannerDTORequest.setTitle("Test Banner");
        bannerDTORequest.setStatus(true);
        bannerDTORequest.setVendor("Test Vendor");
        bannerDTORequest.setPrice(100.0);
        bannerDTORequest.setStartTime(LocalDateTime.now());
        bannerDTORequest.setEndTime(LocalDateTime.now().plusDays(30));

        // Setup mock files
        mockImage = mock(MultipartFile.class);
        mockLogo = mock(MultipartFile.class);
        bannerDTORequest.setImage(mockImage);
        bannerDTORequest.setLogo(mockLogo);
    }

    @Test
    void createBanner_ShouldReturnBannerDTOResponse() {
        // Arrange
        when(cloudinaryImageService.uploadImage(any(MultipartFile.class), "talenthub_images"))
                .thenReturn("http://example.com/image.jpg")
                .thenReturn("http://example.com/logo.jpg");
        when(bannerRepository.save(any(Banner.class))).thenReturn(banner);

        // Act
        BannerDTOResponse response = bannerService.create(bannerDTORequest);

        // Assert
        assertNotNull(response);
        assertEquals(banner.getId(), response.getId());
        assertEquals(banner.getTitle(), response.getTitle());
        assertEquals(banner.getImage(), response.getImage());
        assertEquals(banner.getLogo(), response.getLogo());
        assertEquals(banner.isStatus(), response.isStatus());
        assertEquals(banner.getVendor(), response.getVendor());
        assertEquals(banner.getPrice(), response.getPrice());

        verify(cloudinaryImageService, times(2)).uploadImage(any(MultipartFile.class), "talenthub_images");
        verify(bannerRepository).save(any(Banner.class));
    }

    @Test
    void updateBanner_WhenBannerExists_ShouldReturnUpdatedBanner() {
        // Arrange
        Long bannerId = 1L;
        when(bannerRepository.findById(bannerId)).thenReturn(Optional.of(banner));
        when(cloudinaryImageService.uploadImage(any(MultipartFile.class), "talenthub_images"))
                .thenReturn("http://example.com/new-image.jpg")
                .thenReturn("http://example.com/new-logo.jpg");
        when(bannerRepository.save(any(Banner.class))).thenReturn(banner);

        // Act
        BannerDTOResponse response = bannerService.update(bannerId, bannerDTORequest);

        // Assert
        assertNotNull(response);
        assertEquals(banner.getId(), response.getId());
        assertEquals(banner.getTitle(), response.getTitle());

        verify(bannerRepository).findById(bannerId);
        verify(cloudinaryImageService, times(2)).uploadImage(any(MultipartFile.class), "talenthub_images");
        verify(bannerRepository).save(any(Banner.class));
    }

    @Test
    void updateBanner_WhenBannerDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        Long bannerId = 1L;
        when(bannerRepository.findById(bannerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> bannerService.update(bannerId, bannerDTORequest));
        verify(bannerRepository).findById(bannerId);
        verifyNoMoreInteractions(cloudinaryImageService, bannerRepository);
    }

    @Test
    void getById_WhenBannerExists_ShouldReturnBannerDTOResponse() {
        // Arrange
        Long bannerId = 1L;
        when(bannerRepository.findById(bannerId)).thenReturn(Optional.of(banner));

        // Act
        Optional<BannerDTOResponse> response = bannerService.getById(bannerId);

        // Assert
        assertTrue(response.isPresent());
        assertEquals(banner.getId(), response.get().getId());
        assertEquals(banner.getTitle(), response.get().getTitle());

        verify(bannerRepository).findById(bannerId);
    }

    @Test
    void getById_WhenBannerDoesNotExist_ShouldReturnEmptyOptional() {
        // Arrange
        Long bannerId = 1L;
        when(bannerRepository.findById(bannerId)).thenReturn(Optional.empty());

        // Act
        Optional<BannerDTOResponse> response = bannerService.getById(bannerId);

        // Assert
        assertFalse(response.isPresent());
        verify(bannerRepository).findById(bannerId);
    }

    @Test
    void getAll_ShouldReturnListOfBannerDTOResponses() {
        // Arrange
        Banner banner2 = new Banner();
        banner2.setId(2L);
        banner2.setTitle("Test Banner 2");
        banner2.setImage("http://example.com/image2.jpg");
        banner2.setLogo("http://example.com/logo2.jpg");
        banner2.setStatus(false);
        banner2.setVendor("Test Vendor 2");
        banner2.setPrice(200.0);
        banner2.setStartTime(LocalDateTime.now());
        banner2.setEndTime(LocalDateTime.now().plusDays(15));

        when(bannerRepository.findAll()).thenReturn(Arrays.asList(banner, banner2));

        // Act
        List<BannerDTOResponse> responses = bannerService.getAll();

        // Assert
        assertEquals(2, responses.size());
        assertEquals(banner.getId(), responses.get(0).getId());
        assertEquals(banner2.getId(), responses.get(1).getId());

        verify(bannerRepository).findAll();
    }

    @Test
    void getAllLogo_ShouldReturnListOfLogoDTOResponses() {
        // Arrange
        Banner banner2 = new Banner();
        banner2.setId(2L);
        banner2.setVendor("Test Vendor 2");
        banner2.setLogo("http://example.com/logo2.jpg");
        banner2.setStatus(false);

        when(bannerRepository.findAll()).thenReturn(Arrays.asList(banner, banner2));

        // Act
        List<LogoDTOResponse> responses = bannerService.getAllLogo();

        // Assert
        assertEquals(2, responses.size());
        assertEquals(banner.getId(), responses.get(0).getId());
        assertEquals(banner.getVendor(), responses.get(0).getVendor());
        assertEquals(banner.getLogo(), responses.get(0).getLogo());
        assertEquals(banner2.getId(), responses.get(1).getId());
        assertEquals(banner2.getVendor(), responses.get(1).getVendor());
        assertEquals(banner2.getLogo(), responses.get(1).getLogo());

        verify(bannerRepository).findAll();
    }

    @Test
    void deleteById_WhenBannerExists_ShouldReturnTrue() {
        // Arrange
        Long bannerId = 1L;
        when(bannerRepository.findById(bannerId)).thenReturn(Optional.of(banner));

        // Act
        Boolean result = bannerService.deleteById(bannerId);

        // Assert
        assertTrue(result);
        verify(bannerRepository).findById(bannerId);
        verify(bannerRepository).delete(banner);
    }

    @Test
    void deleteById_WhenBannerDoesNotExist_ShouldReturnFalse() {
        // Arrange
        Long bannerId = 1L;
        when(bannerRepository.findById(bannerId)).thenReturn(Optional.empty());

        // Act
        Boolean result = bannerService.deleteById(bannerId);

        // Assert
        assertFalse(result);
        verify(bannerRepository).findById(bannerId);
        verify(bannerRepository, never()).delete(any(Banner.class));
    }
}