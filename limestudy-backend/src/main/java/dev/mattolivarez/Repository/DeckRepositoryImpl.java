package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.DeckModel;
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
public class DeckRepositoryImpl implements DeckRepository
{

    private static final String SQL_CREATE = "INSERT INTO \"DECK\"(DECK_ID, CLASS_ID, USER_ID, DECK_NAME, DECK_CREATED_ON) " +
                                             "VALUES(NEXTVAL('DECK_SEQ'), ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT DECK_ID, CLASS_ID, USER_ID, DECK_NAME, DECK_CREATED_ON " +
                                                 "FROM \"DECK\" " +
                                                 "WHERE USER_ID = ? AND CLASS_ID = ? AND DECK_ID = ?";

    private static final String SQL_FIND_ALL = "SELECT DECK_ID, CLASS_ID, USER_ID, DECK_NAME, DECK_CREATED_ON " +
                                               "FROM \"DECK\" " +
                                               "WHERE USER_ID = ? AND CLASS_ID = ?";

    private static final String SQL_UPDATE = "UPDATE \"DECK\" " +
                                             "SET DECK_NAME = ? " +
                                             "WHERE USER_ID = ? AND CLASS_ID = ? DECK_ID = ?";

    private static final String SQL_DELETE = "DELETE FROM \"DECK\" " +
                                             "WHERE USER_ID = ? AND CLASS_ID = ? AND DECK_ID = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<DeckModel> findAll(Integer userId, Integer classId) {
        return jdbcTemplate.query(SQL_FIND_ALL, new Object[]{userId, classId}, deckRowMapper);
    }

    @Override
    public DeckModel findById(Integer userId, Integer classId, Integer deckId) throws ResourceNotFoundException {
        try
        {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId, classId, deckId}, deckRowMapper);
        }
        catch (Exception e)
        {
            throw new ResourceNotFoundException("Deck not found");
        }
    }

    @Override
    public Integer create(Integer userId, Integer classId, String deck_name, Long deck_created_on) throws BadRequestException {
        try
        {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, classId);
                ps.setInt(2, userId);
                ps.setString(3, deck_name);
                ps.setLong(4, deck_created_on);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("DECK_ID");
        }
        catch (Exception e)
        {
            throw new BadRequestException("Invalid request. Could not create deck.");
        }
    }

    @Override
    public void update(Integer userId, Integer classId, Integer deckId, DeckModel deckModel) throws BadRequestException {
        try
        {
            jdbcTemplate.update(SQL_UPDATE, new Object[]{deckModel.getDeck_name(), userId, classId, deckId});
        }
        catch (Exception e)
        {
            throw new BadRequestException("Invalid request");
        }
    }

    @Override
    public void removeById(Integer userId, Integer classId, Integer deckId) throws ResourceNotFoundException {
        int count = jdbcTemplate.update(SQL_DELETE, new Object[]{userId, classId, deckId});
        if (count == 0)
        {
            throw new ResourceNotFoundException("Transaction not found");
        }
    }

    private RowMapper<DeckModel> deckRowMapper = ((rs, rowNum) -> {
        return new DeckModel(rs.getInt("DECK_ID"),
                               rs.getInt("CLASS_ID"),
                               rs.getInt("USER_ID"),
                               rs.getString("DECK_NAME"),
                               rs.getLong("DECK_CREATED_ON"));
    });
}
