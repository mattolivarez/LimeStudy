package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.ClassModel;
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
public class ClassRepositoryImpl implements ClassRepository
{

    private static final String SQL_CREATE = "INSERT INTO \"CLASS\"(CLASS_ID, USER_ID, CLASS_NAME, CLASS_CREATED_ON) " +
                                             "VALUES(NEXTVAL('CLASS_SEQ'), ?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT CLASS_ID, USER_ID, CLASS_NAME, CLASS_CREATED_ON " +
                                                 "FROM \"CLASS\" " +
                                                 "WHERE USER_ID = ? AND CLASS_ID = ? ";

    private static final String SQL_FIND_ALL = "SELECT CLASS_ID, USER_ID, CLASS_NAME, CLASS_CREATED_ON " +
                                               "FROM \"CLASS\" " +
                                               "WHERE USER_ID = ? ";

    private static final String SQL_UPDATE = "UPDATE \"CLASS\" " +
                                             "SET CLASS_NAME = ?, CLASS_CREATED_ON = ? " +
                                             "WHERE USER_ID = ? AND CLASS_ID = ?";

    private static final String SQL_DELETE = "DELETE FROM \"CLASS\" " +
                                             "WHERE USER_ID = ? AND CLASS_ID = ?";

    private static final String SQL_DELETE_ALL_DECKS = "DELETE FROM \"DECK\" " +
                                                              "WHERE USER_ID = ? AND CLASS_ID = ?";

    private static final String SQL_DELETE_ALL_FLASHCARDS = "DELETE FROM \"FLASHCARD\" " +
                                                            "WHERE USER_ID = ? AND CLASS_ID = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<ClassModel> findAll(Integer userId) throws ResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_ALL, new Object[]{userId}, classRowMapper);
    }

    @Override
    public ClassModel findById(Integer userId, Integer classId) throws ResourceNotFoundException {
        try
        {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId, classId}, classRowMapper);
        }
        catch (Exception e)
        {
            throw new ResourceNotFoundException("Created Class not found");
        }
    }

    @Override
    public Integer create(Integer userId, String class_name, Long class_created_on) throws BadRequestException {
        try
        {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setString(2, class_name);
                ps.setLong(3, class_created_on);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("CLASS_ID");
        }
        catch (Exception e)
        {
            throw new BadRequestException("Invalid request. Could not create class.");
        }
    }

    @Override
    public void update(Integer userId, Integer classId, ClassModel classModel) throws BadRequestException {
        try
        {
            jdbcTemplate.update(SQL_UPDATE, new Object[]{classModel.getClass_name(), classModel.getClass_created_on(), userId, classId});
        }
        catch(Exception e)
        {
            throw new BadRequestException("Invalid request. Could not update class.");
        }
    }

    @Override
    public void removeById(Integer userId, Integer classId) {
        //this.removeAllCatTransactions(classId);
        jdbcTemplate.update(SQL_DELETE, new Object[]{userId, classId});
    }

    /*private void removeAllCatTransactions(Integer classId)
    {
        jdbcTemplate.update(SQL_DELETE_ALL_TRANSACTIONS, new Object[]{classId});
    }*/

    private RowMapper<ClassModel> classRowMapper = ((rs, rowNum) -> {
        return new ClassModel(rs.getInt("CLASS_ID"),
                            rs.getInt("USER_ID"),
                            rs.getString("CLASS_NAME"),
                            rs.getLong("CLASS_CREATED_ON"));
    });
}
