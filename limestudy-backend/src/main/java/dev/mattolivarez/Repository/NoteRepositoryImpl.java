package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.NoteModel;
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
public class NoteRepositoryImpl implements NoteRepository
{

    private static final String SQL_CREATE = "INSERT INTO \"NOTE\"(NOTE_ID, CLASS_ID, USER_ID, NOTE_NAME, NOTE_BODY, NOTE_CREATED_ON) " +
                                             "VALUES(NEXTVAL('NOTE_SEQ'), ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT NOTE_ID, CLASS_ID, USER_ID, NOTE_NAME, NOTE_BODY, NOTE_CREATED_ON " +
            "FROM \"NOTE\" " +
            "WHERE USER_ID = ? AND CLASS_ID = ? AND NOTE_ID = ?";

    private static final String SQL_FIND_ALL = "SELECT NOTE_ID, CLASS_ID, USER_ID, NOTE_NAME, NOTE_BODY, NOTE_CREATED_ON " +
            "FROM \"NOTE\" " +
            "WHERE USER_ID = ? AND CLASS_ID = ?";

    private static final String SQL_UPDATE = "UPDATE \"NOTE\" " +
            "SET NOTE_NAME = ?, NOTE_BODY = ?, NOTE_CREATED_ON = ? " +
            "WHERE USER_ID = ? AND CLASS_ID = ? AND NOTE_ID = ?";

    private static final String SQL_DELETE = "DELETE FROM \"NOTE\" " +
            "WHERE USER_ID = ? AND CLASS_ID = ? AND NOTE_ID = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<NoteModel> findAll(Integer userId, Integer classId) {
        return jdbcTemplate.query(SQL_FIND_ALL, new Object[]{userId, classId}, noteRowMapper);
    }

    @Override
    public NoteModel findById(Integer userId, Integer classId, Integer noteId) throws ResourceNotFoundException {
        try
        {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId, classId, noteId}, noteRowMapper);
        }
        catch (Exception e)
        {
            throw new ResourceNotFoundException("Note not found");
        }
    }

    @Override
    public Integer create(Integer userId, Integer classId, String note_name, String note_body, Long note_created_on) throws BadRequestException {
        try
        {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, classId);
                ps.setInt(2, userId);
                ps.setString(3, note_name);
                ps.setString(4, note_body);
                ps.setLong(5, note_created_on);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("NOTE_ID");
        }
        catch (Exception e)
        {
            throw new BadRequestException("Invalid request. Could not create note.");
        }
    }

    @Override
    public void update(Integer userId, Integer classId, Integer noteId, NoteModel noteModel) throws BadRequestException {
        try
        {
            jdbcTemplate.update(SQL_UPDATE, new Object[]{noteModel.getNote_name(), noteModel.getNote_body(), noteModel.getNote_created_on(), userId, classId, noteId});
        }
        catch (Exception e)
        {
            throw new BadRequestException("Invalid request");
        }
    }

    @Override
    public void removeById(Integer userId, Integer classId, Integer noteId) throws ResourceNotFoundException {
        int count = jdbcTemplate.update(SQL_DELETE, new Object[]{userId, classId, noteId});
        if (count == 0)
        {
            throw new ResourceNotFoundException("Note not found");
        }
    }

    private RowMapper<NoteModel> noteRowMapper = ((rs, rowNum) -> {
        return new NoteModel(rs.getInt("NOTE_ID"),
                rs.getInt("CLASS_ID"),
                rs.getInt("USER_ID"),
                rs.getString("NOTE_NAME"),
                rs.getString("NOTE_BODY"),
                rs.getLong("NOTE_CREATED_ON"));
    });
}
