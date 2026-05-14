package com.backend.controller.statistics;

import lombok.RequiredArgsConstructor;
import com.backend.service.impl.StatisticsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsServiceImpl statisticsService;

    @GetMapping("home")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(statisticsService.getOverallStatistics());
    }
}