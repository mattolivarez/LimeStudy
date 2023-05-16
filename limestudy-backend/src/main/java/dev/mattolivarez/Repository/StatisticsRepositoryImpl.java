/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Backend
Statistics Repository Implementation file
*/

package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.StatisticsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StatisticsRepositoryImpl implements StatisticsRepository
{

    private static final String SQL_FIND_DAILY = "SELECT SUM(SESSION_CORRECT)::INT AS CORRECT, SUM(SESSION_INCORRECT)::INT AS INCORRECT, SUM(SESSION_CORRECT+SESSION_INCORRECT)::INT AS TOTAL, COUNT(F)::INT AS TOTAL_CARDS, D.DECK_NAME, CL.CLASS_NAME, S.USER_ID, CURRENT_DATE AS DATE " +
            "FROM \"SESSION\" AS S JOIN \"FLASHCARD\" AS F ON S.FLASHCARD_ID = F.FLASHCARD_ID " +
            "JOIN \"DECK\" AS D ON F.DECK_ID = D.DECK_ID " +
            "JOIN \"CLASS\" AS CL ON D.CLASS_ID = CL.CLASS_ID " +
            "WHERE S.USER_ID = ? AND TO_CHAR(SESSION_DATE, 'dd') = TO_CHAR(CURRENT_DATE, 'dd') " +
            "GROUP BY D.DECK_NAME, CL.CLASS_NAME, S.USER_ID";
    private static final String SQL_FIND_WEEKLY = "SELECT SUM(CASE WHEN USER_ID = ? THEN S.SESSION_CORRECT END)::INT AS CORRECT, SUM(CASE WHEN USER_ID = ? THEN S.SESSION_INCORRECT END)::INT AS INCORRECT, SUM(CASE WHEN USER_ID = ? THEN S.SESSION_CORRECT+S.SESSION_INCORRECT ELSE 0 END)::INT AS TOTAL, COUNT(CASE WHEN USER_ID = ? THEN S.FLASHCARD_ID END)::INT as TOTAL_CARDS, 'DECK_NAME' AS DECK_NAME, 'CLASS_NAME' AS CLASS_NAME, S.USER_ID, D.DATE::DATE AS DATE " +
            "FROM \"SESSION\" AS S RIGHT OUTER JOIN " +
            "(SELECT TO_CHAR(DATE_TRUNC('DAY', (CURRENT_DATE - OFFS)), 'YYYY-MM-DD') AS DATE " +
            "FROM GENERATE_SERIES(0, 6, 1) AS OFFS) AS D " +
            "ON TO_CHAR(DATE_TRUNC('DAY', S.SESSION_DATE), 'YYYY-MM-DD') = D.DATE " +
            "GROUP BY D.DATE, S.USER_ID " +
            "ORDER BY D.DATE DESC";
    private static final String SQL_FIND_MONTHLY = "SELECT SUM(SESSION_CORRECT)::INT CORRECT, SUM(SESSION_INCORRECT)::INT INCORRECT, SUM(SESSION_CORRECT+SESSION_INCORRECT)::INT TOTAL, COUNT(F)::INT TOTAL_CARDS, D.DECK_NAME, CL.CLASS_NAME, S.USER_ID, CURRENT_DATE AS DATE " +
            "FROM \"SESSION\" AS S JOIN \"FLASHCARD\" F ON S.FLASHCARD_ID = F.FLASHCARD_ID " +
            "JOIN \"DECK\" D ON F.DECK_ID = D.DECK_ID " +
            "JOIN \"CLASS\" CL ON D.CLASS_ID = CL.CLASS_ID " +
            "WHERE S.USER_ID = ? AND TO_CHAR(S.SESSION_DATE, 'MM') = TO_CHAR(CURRENT_DATE, 'MM') " +
            "GROUP BY D.DECK_NAME, CL.CLASS_NAME, S.USER_ID";
    private static final String SQL_FIND_LIFETIME = "SELECT SUM(F.CORRECT)::INT AS CORRECT, SUM(F.INCORRECT)::INT AS INCORRECT, SUM(F.CORRECT+F.INCORRECT)::INT AS TOTAL, COUNT(F)::INT AS TOTAL_CARDS, D.DECK_NAME, CL.CLASS_NAME, F.USER_ID, CURRENT_DATE AS DATE " +
            "FROM \"FLASHCARD\" AS F JOIN \"DECK\" AS D ON F.DECK_ID = D.DECK_ID " +
            "JOIN \"CLASS\" AS CL ON D.CLASS_ID = CL.CLASS_ID " +
            "WHERE F.USER_ID = ? " +
            "GROUP BY D.DECK_NAME, CL.CLASS_NAME, F.USER_ID";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<StatisticsModel> findDailyStatistics(Integer userId) throws ResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_DAILY, new Object[]{userId}, statisticsRowMapper);
    }

    @Override
    public List<StatisticsModel> findWeeklyStatistics(Integer userId) throws ResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_WEEKLY, new Object[]{userId, userId, userId, userId}, statisticsRowMapper);
    }

    @Override
    public List<StatisticsModel> findMonthlyStatistics(Integer userId) throws ResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_MONTHLY, new Object[]{userId}, statisticsRowMapper);
    }

    @Override
    public List<StatisticsModel> findLifeTimeStatistics(Integer userId) throws ResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_LIFETIME, new Object[]{userId}, statisticsRowMapper);
    }

    private RowMapper<StatisticsModel> statisticsRowMapper = ((rs, rowNum) -> {
        return new StatisticsModel(rs.getInt("CORRECT"),
                rs.getInt("INCORRECT"),
                rs.getInt("TOTAL"),
                rs.getInt("TOTAL_CARDS"),
                rs.getString("DECK_NAME"),
                rs.getString("CLASS_NAME"),
                rs.getInt("USER_ID"),
                rs.getString("DATE"));
    });
}
