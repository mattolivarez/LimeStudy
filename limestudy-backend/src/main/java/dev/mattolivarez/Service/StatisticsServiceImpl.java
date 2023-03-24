package dev.mattolivarez.Service;

import dev.mattolivarez.Model.StatisticsModel;
import dev.mattolivarez.Repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StatisticsServiceImpl implements StatisticsService
{
    @Autowired
    StatisticsRepository statisticsRepository;

    public List<StatisticsModel> fetchDailyStatistics(Integer userId) {
        return statisticsRepository.findDailyStatistics(userId);
    }
    public List<StatisticsModel> fetchWeeklyStatistics(Integer userId) {
        return statisticsRepository.findWeeklyStatistics(userId);
    }
    public List<StatisticsModel> fetchMonthlyStatistics(Integer userId) {
        return statisticsRepository.findMonthlyStatistics(userId);
    }
    public List<StatisticsModel> fetchLifeTimeStatistics(Integer userId) {
        return statisticsRepository.findLifeTimeStatistics(userId);
    }
}
