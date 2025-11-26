package ru.fa.internetshop.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.fa.internetshop.service.StatisticsService;

import java.time.Duration;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final StatisticsService statisticsService;

    public AdminController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public String adminHome() {
        return "admin/dashboard";
    }

    @GetMapping("/statistics")
    public String statistics(Model model) {
        model.addAttribute("userCount", statisticsService.getUserCount());
        model.addAttribute("productCount", statisticsService.getProductCount());
        model.addAttribute("orderCount", statisticsService.getOrderCount());

        Optional<Duration> avg = statisticsService.getAverageOrderConfirmationDuration();
        avg.ifPresent(duration -> {
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            long seconds = duration.toSecondsPart();
            String formatted = String.format("%d ч %d мин %d с", hours, minutes, seconds);
            model.addAttribute("avgOrderWait", formatted);
        });

        return "admin/statistics";
    }
}
