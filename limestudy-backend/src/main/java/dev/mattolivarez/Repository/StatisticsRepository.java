package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.StatisticsModel;

import java.util.List;

public interface StatisticsRepository
{
    List<StatisticsModel> findDailyStatistics(Integer userId) throws ResourceNotFoundException;
    List<StatisticsModel> findWeeklyStatistics(Integer userId) throws ResourceNotFoundException;
    List<StatisticsModel> findMonthlyStatistics(Integer userId) throws ResourceNotFoundException;
    List<StatisticsModel> findLifeTimeStatistics(Integer userId) throws ResourceNotFoundException;
}
