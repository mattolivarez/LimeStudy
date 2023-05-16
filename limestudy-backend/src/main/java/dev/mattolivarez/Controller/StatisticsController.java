/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Backend
Controller file for Statistics Model
*/

package dev.mattolivarez.Controller;

import dev.mattolivarez.Model.StatisticsModel;
import dev.mattolivarez.Service.StatisticsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController
{
    @Autowired
    StatisticsService statisticsService;

    @GetMapping("/daily")
    public ResponseEntity<List<StatisticsModel>> getDailyStatistics(HttpServletRequest request)
    {
        int userId = (Integer) request.getAttribute("userId");
        List<StatisticsModel> statistics = statisticsService.fetchDailyStatistics(userId);
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<StatisticsModel>> getWeeklyStatistics(HttpServletRequest request)
    {
        int userId = (Integer) request.getAttribute("userId");
        List<StatisticsModel> statistics = statisticsService.fetchWeeklyStatistics(userId);
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<StatisticsModel>> getMonthlyStatistics(HttpServletRequest request)
    {
        int userId = (Integer) request.getAttribute("userId");
        List<StatisticsModel> statistics = statisticsService.fetchMonthlyStatistics(userId);
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    @GetMapping("/life-time")
    public ResponseEntity<List<StatisticsModel>> getLifeTimeStatistics(HttpServletRequest request)
    {
        int userId = (Integer) request.getAttribute("userId");
        List<StatisticsModel> statistics = statisticsService.fetchLifeTimeStatistics(userId);
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }
}
