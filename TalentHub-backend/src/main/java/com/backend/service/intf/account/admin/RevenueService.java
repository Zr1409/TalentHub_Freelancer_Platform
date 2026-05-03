package com.backend.service.intf.account.admin;

import com.backend.dto.response.account.admin.RevenueDTOResponse;

import java.util.List;
import java.util.Map;

public interface RevenueService {
    List<RevenueDTOResponse> getRevenueByMonth(int year);

    List<RevenueDTOResponse> getRevenueByQuater(int year);

    List<RevenueDTOResponse> getRevenueByYear();

    List<RevenueDTOResponse> getRevenueBannerByMonth(int year);

    List<RevenueDTOResponse> getRevenueBannerByQuater(int year);

    List<RevenueDTOResponse> getRevenueBannerByYear();

    Map<String, Object> getGrowthRates();

    /*List<RevenueDTOResponse> getRevenueBannerByWeek(int year, int month);

    List<RevenueDTOResponse> getRevenueByWeek(int year, int month);*/
}