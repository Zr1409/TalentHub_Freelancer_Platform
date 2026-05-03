package com.backend.service.impl.account.client;

import lombok.RequiredArgsConstructor;
import com.backend.dto.request.account.client.SoldPackageDTORequest;
import com.backend.dto.response.account.client.CurrentPackageDTOResponse;
import com.backend.dto.response.account.client.PackageHistoryDTOResponse;
import com.backend.dto.response.account.client.SoldPackageDTOResponse;
import com.backend.entity.child.account.client.SoldPackage;
import com.backend.entity.child.admin.VoucherPackage;
import com.backend.exception.BadRequestException;
import com.backend.mapper.Account.client.SoldPackageMapper;
import com.backend.repository.SoldPackageRepository;
import com.backend.repository.VoucherPackageRepository;
import com.backend.service.intf.account.client.SoldPackageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SoldPackageServiceImpl implements SoldPackageService {
    private final SoldPackageRepository soldPackageRepository;
    private final SoldPackageMapper soldPackageMapper;
    private final VoucherPackageRepository voucherPackageRepository;

    @Override
    public SoldPackageDTOResponse create(SoldPackageDTORequest soldPackageDTORequest) {
        if (soldPackageDTORequest == null) {
            throw new BadRequestException("SoldPackage DTO cannot be null");
        }

        if (soldPackageDTORequest.getClientId() == null) {
            throw new BadRequestException("Client Id cannot be null");
        }

        if (soldPackageDTORequest.getTypePackage() == null) {
            throw new BadRequestException("Type Package cannot be null");
        }

        SoldPackage oldSoldPackage = soldPackageRepository.findTopByClientIdAndStatusOrderByStartDateDesc(soldPackageDTORequest.getClientId(), true);

        if (oldSoldPackage != null) {
            oldSoldPackage.setStatus(false);
            soldPackageRepository.save(oldSoldPackage);
        }

        VoucherPackage voucherPackage = voucherPackageRepository.findTopByTypePackageOrderByIdDesc(soldPackageDTORequest.getTypePackage());

        SoldPackage soldPackage = soldPackageMapper.toEntity(soldPackageDTORequest);
        soldPackage.setStartDate(LocalDateTime.now());
        soldPackage.setEndDate(LocalDateTime.now().plusDays(30));
        soldPackage.setNumberPost(voucherPackage.getNumberPost());
        soldPackage.setNumberPosted(Long.valueOf(0));
        soldPackage.setVoucherPackage(voucherPackage);

        soldPackage = soldPackageRepository.save(soldPackage);

        return soldPackageMapper.toResponseDto(soldPackage);
    }

    @Override
    public Optional<SoldPackageDTOResponse> getById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<SoldPackageDTOResponse> getAll() {
        return List.of();
    }

    @Override
    public Boolean deleteById(Long aLong) {
        return null;
    }

    @Override
    public Optional<CurrentPackageDTOResponse> getCurrentPackage(Long clientId) {
        if (clientId == null) {
            throw new BadRequestException("Client ID cannot be null");
        }

        SoldPackage currentPackage = soldPackageRepository.findTopByClientIdAndStatusOrderByStartDateDesc(clientId, true);

        if (currentPackage == null) {
            return Optional.empty();
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = currentPackage.getEndDate();

        long remainingHours = 0;
        String remainingFormatted = "Đã hết hạn";

        // Fix: Add null check before using endDate.isAfter(now)
        if (endDate != null && endDate.isAfter(now)) {
            remainingHours = java.time.Duration.between(now, endDate).toHours();
            long days = remainingHours / 24;
            long hours = remainingHours % 24;

            if (days > 0) {
                remainingFormatted = days + " ngày " + hours + " giờ";
            } else {
                remainingFormatted = hours + " giờ";
            }
        }

        Long postsRemaining = currentPackage.getNumberPost() - currentPackage.getNumberPosted();
        if (postsRemaining < 0) postsRemaining = 0L;

        return Optional.of(CurrentPackageDTOResponse.builder()
                .id(currentPackage.getId())
                .startDate(currentPackage.getStartDate())
                .endDate(currentPackage.getEndDate())
                .price(currentPackage.getPrice())
                .numberPost(currentPackage.getNumberPost())
                .numberPosted(currentPackage.getNumberPosted())
                .postsRemaining(postsRemaining)
                .packageType(currentPackage.getVoucherPackage().getTypePackage())
                .packageTypeName(currentPackage.getVoucherPackage().getTypePackage().getDisplayName())
                .remainingTimeInHours(remainingHours)
                .remainingTimeFormatted(remainingFormatted)
                .isActive(currentPackage.isStatus())
                .build());
    }

    @Override
    public List<PackageHistoryDTOResponse> getPackageHistory(Long clientId) {
        if (clientId == null) {
            throw new BadRequestException("Client ID cannot be null");
        }

        List<SoldPackage> packageHistory = soldPackageRepository.findPackageHistoryByClientId(clientId);
        LocalDateTime now = LocalDateTime.now();

        return packageHistory.stream()
                .map(pkg -> {
                    String status;
                    if (pkg.isStatus()) {
                        if (pkg.getNumberPosted() >= pkg.getNumberPost()) {
                            status = "Đã dùng hết bài đăng";
                        } else {
                            status = "Đang sử dụng";
                        }
                    } else {
                        status = "Đã hết hạn";
                    }

                    String usagePeriod = formatDateTime(pkg.getStartDate()) + " - " + formatDateTime(pkg.getEndDate());

                    Long postsUsed = pkg.getNumberPosted();

                    return PackageHistoryDTOResponse.builder()
                            .id(pkg.getId())
                            .startDate(pkg.getStartDate())
                            .endDate(pkg.getEndDate())
                            .price(pkg.getPrice())
                            .numberPost(pkg.getNumberPost())
                            .numberPosted(pkg.getNumberPosted())
                            .postsUsed(postsUsed)
                            .packageType(pkg.getVoucherPackage().getTypePackage())
                            .packageTypeName(pkg.getVoucherPackage().getTypePackage().getDisplayName())
                            .isActive(pkg.isStatus())
                            .usagePeriod(usagePeriod)
                            .status(status)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }
}
