package dev.mattolivarez.Service;

import dev.mattolivarez.Model.StatisticsModel;

import java.util.List;

public interface StatisticsService
{
    List<StatisticsModel> fetchDailyStatistics(Integer userId);
    List<StatisticsModel> fetchWeeklyStatistics(Integer userId);
    List<StatisticsModel> fetchMonthlyStatistics(Integer userId);
    List<StatisticsModel> fetchLifeTimeStatistics(Integer userId);
}
