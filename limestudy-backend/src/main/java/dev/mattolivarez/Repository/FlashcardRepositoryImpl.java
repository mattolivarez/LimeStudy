/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Backend
Flashcard Repository Implementation file
*/

package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.FlashcardModel;
import dev.mattolivarez.NecessaryFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
public class FlashcardRepositoryImpl implements FlashcardRepository
{

    private static final String SQL_CREATE = "INSERT INTO \"FLASHCARD\"(FLASHCARD_ID, DECK_ID, CLASS_ID, USER_ID, QUESTION, ANSWER, FLASHCARD_CREATED_ON, CORRECT, INCORRECT, LAST_STUDIED_ON, OCCURRENCE_RATE, OCCURRENCE_RATE_INPUT) " +
                                             "VALUES(NEXTVAL('FLASHCARD_SEQ'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT FLASHCARD_ID, DECK_ID, CLASS_ID, USER_ID, QUESTION, ANSWER, TO_CHAR(FLASHCARD_CREATED_ON, 'MM/DD/YYYY') AS FLASHCARD_CREATED_ON, CORRECT, INCORRECT, TO_CHAR(LAST_STUDIED_ON, 'MM/DD/YYYY') AS LAST_STUDIED_ON, OCCURRENCE_RATE, OCCURRENCE_RATE_INPUT " +
                                                 "FROM \"FLASHCARD\" " +
                                                 "WHERE USER_ID = ? AND CLASS_ID = ? AND DECK_ID = ? AND FLASHCARD_ID = ?";

    private static final String SQL_FIND_ALL = "SELECT FLASHCARD_ID, DECK_ID, CLASS_ID, USER_ID, QUESTION, ANSWER, TO_CHAR(FLASHCARD_CREATED_ON, 'MM/DD/YYYY') AS FLASHCARD_CREATED_ON, CORRECT, INCORRECT, TO_CHAR(LAST_STUDIED_ON, 'MM/DD/YYYY') AS LAST_STUDIED_ON, OCCURRENCE_RATE, OCCURRENCE_RATE_INPUT " +
                                               "FROM \"FLASHCARD\" " +
                                               "WHERE USER_ID = ? AND CLASS_ID = ? AND DECK_ID = ?";

    private static final String SQL_UPDATE = "UPDATE \"FLASHCARD\" " +
                                             "SET QUESTION = ?, ANSWER = ?, FLASHCARD_CREATED_ON = ?, CORRECT = ?, INCORRECT = ?, LAST_STUDIED_ON = ?, OCCURRENCE_RATE = ?, OCCURRENCE_RATE_INPUT = ? " +
                                             "WHERE USER_ID = ? AND CLASS_ID = ? AND DECK_ID = ? AND FLASHCARD_ID = ?";

    private static final String SQL_DELETE = "DELETE FROM \"FLASHCARD\" " +
                                             "WHERE USER_ID = ? AND CLASS_ID = ? AND DECK_ID = ? AND FLASHCARD_ID = ?";

    private static final String SQL_FIND_TRADITIONAL_STUDY_SET = "SELECT FLASHCARD_ID, DECK_ID, CLASS_ID, USER_ID, QUESTION, ANSWER, TO_CHAR(FLASHCARD_CREATED_ON, 'MM/DD/YYYY') AS FLASHCARD_CREATED_ON, CORRECT, INCORRECT, TO_CHAR(LAST_STUDIED_ON, 'MM/DD/YYYY') AS LAST_STUDIED_ON, OCCURRENCE_RATE, OCCURRENCE_RATE_INPUT " +
            "FROM \"FLASHCARD\" " +
            "WHERE USER_ID = ? AND CLASS_ID = ? AND DECK_ID = ? AND (LAST_STUDIED_ON <= CURRENT_DATE - INTERVAL '1' DAY * (SELECT FLASHCARD_DELAY_SETTING+1 FROM \"USER\" WHERE USER_ID = ?) OR (OCCURRENCE_RATE <= 0.5 AND LAST_STUDIED_ON != CURRENT_DATE)) " +
            "ORDER BY OCCURRENCE_RATE DESC";

    private static final String SQL_FIND_PRACTICE_STUDY_SET = "SELECT FLASHCARD_ID, DECK_ID, CLASS_ID, USER_ID, QUESTION, ANSWER, TO_CHAR(FLASHCARD_CREATED_ON, 'MM/DD/YYYY') AS FLASHCARD_CREATED_ON, CORRECT, INCORRECT, TO_CHAR(LAST_STUDIED_ON, 'MM/DD/YYYY') AS LAST_STUDIED_ON, OCCURRENCE_RATE, OCCURRENCE_RATE_INPUT " +
            "FROM \"FLASHCARD\" " +
            "WHERE USER_ID = ? AND CLASS_ID = ? AND DECK_ID = ? " +
            "ORDER BY RANDOM() LIMIT 4";

    private static final String SQL_RESET = "UPDATE \"FLASHCARD\" " +
            "SET OCCURRENCE_RATE = 0.5, OCCURRENCE_RATE_INPUT = 0, LAST_STUDIED_ON = TO_CHAR(CURRENT_DATE, 'MM/DD/YYYY') " +
            "WHERE USER_ID  = ?, CURRENT_DATE > LAST_STUDIED_ON + INTERVAL '14' DAY";

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public List<FlashcardModel> findAll(Integer userId, Integer classId, Integer deckId) {
        return jdbcTemplate.query(SQL_FIND_ALL, new Object[]{userId, classId, deckId}, flashcardRowMapper);
    }

    @Override
    public FlashcardModel findById(Integer userId, Integer classId, Integer deckId, Integer flashcardId)
            throws ResourceNotFoundException
    {
        try
        {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId, classId, deckId, flashcardId}, flashcardRowMapper);
        }
        catch (Exception e)
        {
            throw new ResourceNotFoundException("Flashcard not found");
        }
    }

    @Override
    public Integer create(Integer userId, Integer classId, Integer deckId, String question, String answer,
                          String flashcard_created_on, Integer correct, Integer incorrect, String last_studied_on,
                          Double occurrence_rate, Integer occurrence_rate_input)
            throws BadRequestException
    {
        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date flashcardCreatedDate = simpleDateFormat.parse(flashcard_created_on);
            java.sql.Date flashcardCreatedOn = new java.sql.Date(flashcardCreatedDate.getTime());

            Date lastStudiedDate = simpleDateFormat.parse(last_studied_on);
            java.sql.Date lastStudiedOn = new java.sql.Date(lastStudiedDate.getTime());
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, deckId);
                ps.setInt(2, classId);
                ps.setInt(3, userId);
                ps.setString(4, question);
                ps.setString(5, answer);
                ps.setDate(6, flashcardCreatedOn);
                ps.setInt(7, correct);
                ps.setInt(8, incorrect);
                ps.setDate(9, lastStudiedOn);
                ps.setDouble(10, occurrence_rate);
                ps.setDouble(11, occurrence_rate_input);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("FLASHCARD_ID");
        }
        catch (Exception e)
        {
            throw new BadRequestException("Invalid request. Could not create flashcard.");
        }
    }

    @Override
    public void update(Integer userId, Integer classId, Integer deckId, Integer flashcardId, FlashcardModel flashcardModel)
            throws BadRequestException
    {
        try
        {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date flashcardCreatedDate = simpleDateFormat.parse(flashcardModel.getFlashcard_created_on());
            java.sql.Date flashcardCreatedOn = new java.sql.Date(flashcardCreatedDate.getTime());

            Date lastStudiedDate = simpleDateFormat.parse(flashcardModel.getLast_studied_on());
            java.sql.Date lastStudiedOn = new java.sql.Date(lastStudiedDate.getTime());

            flashcardModel.setOccurrence_rate(NecessaryFunctions.logisticFunction(flashcardModel.getOccurrence_rate_input()));

            jdbcTemplate.update(SQL_UPDATE,
                    new Object[]{
                            flashcardModel.getQuestion(),
                            flashcardModel.getAnswer(),
                            flashcardCreatedOn,
                            flashcardModel.getCorrect(),
                            flashcardModel.getIncorrect(),
                            lastStudiedOn,
                            flashcardModel.getOccurrence_rate(),
                            flashcardModel.getOccurrence_rate_input(),
                            userId, classId, deckId, flashcardId});
        }
        catch (Exception e)
        {
            throw new BadRequestException("Invalid request. Could not update flashcard.");
        }
    }

    @Override
    public void removeById(Integer userId, Integer classId, Integer deckId, Integer flashcardId)
            throws ResourceNotFoundException
    {
        int count = jdbcTemplate.update(SQL_DELETE, new Object[]{userId, classId, deckId, flashcardId});
        if (count == 0)
        {
            throw new ResourceNotFoundException("Flashcard not found");
        }
    }

    @Override
    public List<FlashcardModel> findTraditionalStudySet(Integer userId, Integer classId, Integer deckId) {
        return jdbcTemplate.query(SQL_FIND_TRADITIONAL_STUDY_SET, new Object[]{userId, classId, deckId, userId}, flashcardRowMapper);
    }

    @Override
    public List<FlashcardModel> findPracticeStudySet(Integer userId, Integer classId, Integer deckId) {
        return jdbcTemplate.query(SQL_FIND_PRACTICE_STUDY_SET, new Object[]{userId, classId, deckId}, flashcardRowMapper);
    }

    @Override
    public void reset(Integer userId) {
        try
        {
            jdbcTemplate.update(SQL_RESET,
                    new Object[]{userId});
        }
        catch (Exception e)
        {
            throw new BadRequestException("Invalid request. Could not update flashcard.");
        }
    }

    private RowMapper<FlashcardModel> flashcardRowMapper = ((rs, rowNum) -> {
        return new FlashcardModel(rs.getInt("FLASHCARD_ID"),
                rs.getInt("DECK_ID"),
                rs.getInt("CLASS_ID"),
                rs.getInt("USER_ID"),
                rs.getString("QUESTION"),
                rs.getString("ANSWER"),
                rs.getString("FLASHCARD_CREATED_ON"),
                rs.getInt("CORRECT"),
                rs.getInt("INCORRECT"),
                rs.getString("LAST_STUDIED_ON"),
                rs.getDouble("OCCURRENCE_RATE"),
                rs.getInt("OCCURRENCE_RATE_INPUT"));
    });
}
