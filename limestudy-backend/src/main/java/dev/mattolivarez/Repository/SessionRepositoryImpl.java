package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.SessionModel;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SessionRepositoryImpl implements SessionRepository
{
    private static final String SQL_CREATE = "INSERT INTO \"SESSION\"(SESSION_ID, FLASHCARD_ID, DECK_ID, CLASS_ID, USER_ID, SESSION_CORRECT, SESSION_INCORRECT, SESSION_DATE) " +
            "VALUES(NEXTVAL('SESSION_SEQ'), ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT SESSION_ID, FLASHCARD_ID, DECK_ID, CLASS_ID, USER_ID, SESSION_CORRECT, SESSION_INCORRECT, TO_CHAR(SESSION_DATE, 'MM/DD/YYYY') AS SESSION_DATE " +
            "FROM \"SESSION\" " +
            "WHERE USER_ID = ? AND CLASS_ID = ? AND DECK_ID = ? AND FLASHCARD_ID = ? AND SESSION_ID = ?";

    private static final String SQL_FIND_ALL = "SELECT SESSION_ID, FLASHCARD_ID, DECK_ID, CLASS_ID, USER_ID, SESSION_CORRECT, SESSION_INCORRECT, SESSION_DATE " +
            "FROM \"SESSION\" " +
            "WHERE USER_ID = ? AND CLASS_ID = ? AND DECK_ID = ? AND FLASHCARD_ID = ?";

    private static final String SQL_UPDATE = "UPDATE \"SESSION\" " +
            "SET SESSION_CORRECT = ?, SESSION_INCORRECT = ?, SESSION_DATE = ? " +
            "WHERE USER_ID = ? AND CLASS_ID = ? AND DECK_ID = ? AND FLASHCARD_ID = ? AND SESSION_ID = ?";

    private static final String SQL_DELETE = "DELETE FROM \"SESSION\" " +
            "WHERE USER_ID = ? AND CLASS_ID = ? AND DECK_ID = ? AND FLASHCARD_ID = ? AND SESSION_ID = ?";

    private static final String SQL_FIND_IF_EXISTS = "SELECT SESSION_ID, FLASHCARD_ID, DECK_ID, CLASS_ID, USER_ID, SESSION_DATE, SESSION_CORRECT, SESSION_INCORRECT " +
            "FROM \"SESSION\" " +
            "WHERE USER_ID = ? AND CLASS_ID = ? AND DECK_ID = ? AND FLASHCARD_ID = ? AND SESSION_DATE = CURRENT_DATE";

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public List<SessionModel> findAll(Integer userId, Integer classId, Integer deckId, Integer flashcardId) {
        return jdbcTemplate.query(SQL_FIND_ALL, new Object[]{userId, classId, deckId, flashcardId}, sessionRowMapper);
    }

    @Override
    public SessionModel findById(Integer userId, Integer classId, Integer deckId, Integer flashcardId, Integer sessionId)
            throws ResourceNotFoundException
    {
        try
        {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID,
                    new Object[]{userId, classId, deckId, flashcardId, sessionId},
                    sessionRowMapper);
        }
        catch (Exception e)
        {
            throw new ResourceNotFoundException("Session not found");
        }
    }

    @Override
    public Integer create(Integer userId, Integer classId, Integer deckId, Integer flashcardId, Integer session_correct,
                          Integer session_incorrect, String session_date)
            throws BadRequestException
    {
        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date sessionDateNew = simpleDateFormat.parse(session_date);
            java.sql.Date sessionDate = new java.sql.Date(sessionDateNew.getTime());

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, flashcardId);
                ps.setInt(2, deckId);
                ps.setInt(3, classId);
                ps.setInt(4, userId);
                ps.setInt(5, session_correct);
                ps.setInt(6, session_incorrect);
                ps.setDate(7, sessionDate);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("SESSION_ID");
        }
        catch (Exception e)
        {
            throw new BadRequestException("Invalid request. Could not create session.");
        }
    }

    @Override
    public void update(Integer userId, Integer classId, Integer deckId, Integer flashcardId, Integer sessionId, SessionModel sessionModel)
            throws BadRequestException
    {
        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date sessionDateNew = simpleDateFormat.parse(sessionModel.getSession_date());
            java.sql.Date sessionDate = new java.sql.Date(sessionDateNew.getTime());

            jdbcTemplate.update(SQL_UPDATE,
                    new Object[]{
                            sessionModel.getSession_correct(),
                            sessionModel.getSession_incorrect(),
                            sessionDate,
                            userId, classId, deckId, flashcardId, sessionId});
        }
        catch (Exception e)
        {
            throw new BadRequestException("Invalid request. Could not update session.");
        }
    }

    @Override
    public void removeById(Integer userId, Integer classId, Integer deckId, Integer flashcardId, Integer sessionId)
            throws ResourceNotFoundException
    {
        int count = jdbcTemplate.update(SQL_DELETE, new Object[]{userId, classId, deckId, flashcardId, sessionId});
        if (count == 0)
        {
            throw new ResourceNotFoundException("Session not found");
        }
    }

    @Override
    public SessionModel findByIdIfExists(Integer userId, Integer classId, Integer deckId, Integer flashcardId)
            throws ResourceNotFoundException
    {
        try
        {
            return jdbcTemplate.queryForObject(SQL_FIND_IF_EXISTS,
                    new Object[]{userId, classId, deckId, flashcardId},
                    sessionRowMapper);
        }
        catch (Exception e)
        {
            throw new ResourceNotFoundException("Session not found");
        }
    }

    private RowMapper<SessionModel> sessionRowMapper = ((rs, rowNum) -> {
        return new SessionModel(rs.getInt("SESSION_ID"),
                rs.getInt("FLASHCARD_ID"),
                rs.getInt("DECK_ID"),
                rs.getInt("CLASS_ID"),
                rs.getInt("USER_ID"),
                rs.getInt("SESSION_CORRECT"),
                rs.getInt("SESSION_INCORRECT"),
                rs.getString("SESSION_DATE"));
    });
}
