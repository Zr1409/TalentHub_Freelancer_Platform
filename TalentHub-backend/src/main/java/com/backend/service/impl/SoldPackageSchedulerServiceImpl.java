package com.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.backend.entity.child.account.client.SoldPackage;
import com.backend.entity.child.admin.VoucherPackage;
import com.backend.enums.TypePackage;
import com.backend.repository.SoldPackageRepository;
import com.backend.repository.VoucherPackageRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SoldPackageSchedulerServiceImpl {
    private final SoldPackageRepository soldPackageRepository;
    private final VoucherPackageRepository voucherPackageRepository;

    @Scheduled(cron = "0 */1 * * * ?") // cron format
    public void checkAndExpirePackages() {
        log.info("Scheduler running: Checking expired sold packages...");

        List<TypePackage> allowedPackages = List.of(TypePackage.SILVER, TypePackage.GOLD, TypePackage.DIAMOND);

        // find expired packages with true status
        List<SoldPackage> expiredPackages = soldPackageRepository
                .findByEndDateBeforeAndStatusAndVoucherPackage_TypePackageIn(LocalDateTime.now(), true, allowedPackages);

        log.info("Found {} packages to expire", expiredPackages.size());

        List<SoldPackage> newNormalPackages = new ArrayList<>();

        for (SoldPackage expiredPackage : expiredPackages) {
            expiredPackage.setStatus(false);
            log.info("Expired package ID: {}", expiredPackage.getId());

            Long clientId = expiredPackage.getClient().getId();

            VoucherPackage normalPackage = voucherPackageRepository.findTopByTypePackageOrderByIdDesc(TypePackage.NORMAL);

            if (normalPackage != null) {
                SoldPackage newPackage = new SoldPackage();
                newPackage.setClient(expiredPackage.getClient());
                newPackage.setVoucherPackage(normalPackage);
                newPackage.setStartDate(LocalDateTime.now());
                newPackage.setEndDate(LocalDateTime.now().plusDays(normalPackage.getDuration()));
                newPackage.setNumberPost(normalPackage.getNumberPost());
                newPackage.setNumberPosted(0L);
                newPackage.setStatus(true);
                newPackage.setPrice(0.0);

                newNormalPackages.add(newPackage);
                log.info("Assigned NORMAL package to client ID: {}", clientId);
            }
        }

        soldPackageRepository.saveAll(expiredPackages);

        if (!newNormalPackages.isEmpty()) {
            soldPackageRepository.saveAll(newNormalPackages);
        }
    }
}
