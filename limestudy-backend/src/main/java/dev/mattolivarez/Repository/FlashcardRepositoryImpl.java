package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.DeckModel;
import dev.mattolivarez.Model.FlashcardModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class FlashcardRepositoryImpl implements FlashcardRepository
{

    private static final String SQL_CREATE = "INSERT INTO \"FLASHCARD\"(FLASHCARD_ID, DECK_ID, CLASS_ID, USER_ID, QUESTION, ANSWER, FLASHCARD_CREATED_ON) " +
                                             "VALUES(NEXTVAL('FLASHCARD_SEQ'), ?, ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT FLASHCARD_ID, DECK_ID, CLASS_ID, USER_ID, QUESTION, ANSWER, FLASHCARD_CREATED_ON " +
                                                 "FROM \"FLASHCARD\" " +
                                                 "WHERE USER_ID = ? AND CLASS_ID = ? AND DECK_ID = ? AND FLASHCARD_ID = ?";

    private static final String SQL_FIND_ALL = "SELECT FLASHCARD_ID, DECK_ID, CLASS_ID, USER_ID, QUESTION, ANSWER, FLASHCARD_CREATED_ON " +
                                               "FROM \"FLASHCARD\" " +
                                               "WHERE USER_ID = ? AND CLASS_ID = ? AND DECK_ID = ?";

    private static final String SQL_UPDATE = "UPDATE \"FLASHCARD\" " +
                                             "SET QUESTION = ?, ANSWER = ? " +
                                             "WHERE USER_ID = ? AND CLASS_ID = ? AND DECK_ID = ? AND FLASHCARD_ID = ?";

    private static final String SQL_DELETE = "DELETE FROM \"FLASHCARD\" " +
                                             "WHERE USER_ID = ? AND CLASS_ID = ? AND DECK_ID = ? AND FLASHCARD_ID = ?";


    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public List<FlashcardModel> findAll(Integer userId, Integer classId, Integer deckId) {
        return jdbcTemplate.query(SQL_FIND_ALL, new Object[]{userId, classId, deckId}, flashcardRowMapper);
    }

    @Override
    public FlashcardModel findById(Integer userId, Integer classId, Integer deckId, Integer flashcardId) throws ResourceNotFoundException {
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
    public Integer create(Integer userId, Integer classId, Integer deckId, String question, String answer, Long flashcard_created_on) throws BadRequestException {
        try
        {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, deckId);
                ps.setInt(2, classId);
                ps.setInt(3, userId);
                ps.setString(4, question);
                ps.setString(5, answer);
                ps.setLong(6, flashcard_created_on);
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
    public void update(Integer userId, Integer classId, Integer deckId, Integer flashcardId, FlashcardModel flashcardModel) throws BadRequestException {
        try
        {
            jdbcTemplate.update(SQL_UPDATE, new Object[]{flashcardModel.getQuestion(), flashcardModel.getAnswer(), userId, classId, deckId, flashcardId});
        }
        catch (Exception e)
        {
            throw new BadRequestException("Invalid request. Could not update flashcard.");
        }
    }

    @Override
    public void removeById(Integer userId, Integer classId, Integer deckId, Integer flashcardId) throws ResourceNotFoundException {
        int count = jdbcTemplate.update(SQL_DELETE, new Object[]{userId, classId, deckId, flashcardId});
        if (count == 0)
        {
            throw new ResourceNotFoundException("Flashcard not found");
        }
    }

    private RowMapper<FlashcardModel> flashcardRowMapper = ((rs, rowNum) -> {
        return new FlashcardModel(rs.getInt("FLASHCARD_ID"),
                rs.getInt("DECK_ID"),
                rs.getInt("CLASS_ID"),
                rs.getInt("USER_ID"),
                rs.getString("QUESTION"),
                rs.getString("ANSWER"),
                rs.getLong("FLASHCARD_CREATED_ON"));
    });
}
