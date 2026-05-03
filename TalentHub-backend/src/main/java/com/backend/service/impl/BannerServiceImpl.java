package com.backend.service.impl;

import lombok.RequiredArgsConstructor;
import com.backend.dto.request.BannerDTORequest;
import com.backend.dto.response.BannerDTOResponse;
import com.backend.dto.response.LogoDTOResponse;
import com.backend.entity.child.Banner;
import com.backend.exception.NotFoundException;
import com.backend.repository.BannerRepository;
import com.backend.service.intf.BannerService;
import com.backend.service.intf.image.CloudinaryImageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {


    private final BannerRepository bannerRepository;
    private final CloudinaryImageService cloudinaryImageService;

    @Override
    public BannerDTOResponse create(BannerDTORequest bannerDTORequest) {
        String imageUrl = cloudinaryImageService.uploadImage(bannerDTORequest.getImage(), "talenthub_images");
        String logoUrl = cloudinaryImageService.uploadImage(bannerDTORequest.getLogo(), "talenthub_images");
        Banner banner = new Banner();
        banner.setTitle(bannerDTORequest.getTitle());
        banner.setImage(imageUrl);
        banner.setLogo(logoUrl);
        banner.setStatus(bannerDTORequest.isStatus());
        banner.setVendor(bannerDTORequest.getVendor());
        banner.setStartTime(bannerDTORequest.getStartTime());
        banner.setEndTime(bannerDTORequest.getEndTime());
        banner.setPrice(bannerDTORequest.getPrice());
        Banner savedBanner = bannerRepository.save(banner);

        return toDTO(savedBanner);
    }

    @Override
    public BannerDTOResponse update(Long id, BannerDTORequest bannerDTORequest) {
        Banner existingBanner = bannerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Banner not found"));

        if (bannerDTORequest.getImage() != null && !bannerDTORequest.getImage().isEmpty()) {
            String newImageUrl = cloudinaryImageService.uploadImage(bannerDTORequest.getImage(), "talenthub_images");
            existingBanner.setImage(newImageUrl);
        }
        if (bannerDTORequest.getLogo() != null && !bannerDTORequest.getLogo().isEmpty()) {
            String newLogoUrl = cloudinaryImageService.uploadImage(bannerDTORequest.getLogo(), "talenthub_images");
            existingBanner.setLogo(newLogoUrl);
        }

        existingBanner.setTitle(bannerDTORequest.getTitle());
        existingBanner.setStatus(bannerDTORequest.isStatus());
        existingBanner.setVendor(bannerDTORequest.getVendor());
        existingBanner.setEndTime(bannerDTORequest.getEndTime());
        existingBanner.setStartTime(bannerDTORequest.getStartTime());
        existingBanner.setPrice(bannerDTORequest.getPrice());

        Banner updatedBanner = bannerRepository.save(existingBanner);

        return toDTO(updatedBanner);
    }


    @Override
    public Optional<BannerDTOResponse> getById(Long bannerId) {
        Optional<Banner> banner = bannerRepository.findById(bannerId);
        return banner.map(this::toDTO);
    }

    @Override
    public List<BannerDTOResponse> getAll() {
        List<Banner> banners = bannerRepository.findAll();
        return banners.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<LogoDTOResponse> getAllLogo() {
        List<LogoDTOResponse> banners = bannerRepository.findAll().stream().map(banner ->
                LogoDTOResponse.builder()
                        .vendor(banner.getVendor())
                        .logo(banner.getLogo())
                        .status(banner.isStatus())
                        .id(banner.getId()).build()).toList();
        return banners;
    }

    @Override
    public Boolean deleteById(Long bannerId) {
        Optional<Banner> banner = bannerRepository.findById(bannerId);
        if (banner.isPresent()) {
            bannerRepository.delete(banner.get());
            return true;
        }
        return false;
    }

    private BannerDTOResponse toDTO(Banner banner) {
        BannerDTOResponse response = new BannerDTOResponse();
        response.setId(banner.getId());
        response.setTitle(banner.getTitle());
        response.setPrice(banner.getPrice());
        response.setImage(banner.getImage());
        response.setLogo(banner.getLogo());
        response.setStatus(banner.isStatus());
        response.setVendor(banner.getVendor());
        response.setStartTime(banner.getStartTime());
        response.setEndTime(banner.getEndTime() != null ? banner.getEndTime() : null);
        return response;
    }


}
