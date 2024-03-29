package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.LSAuthException;
import dev.mattolivarez.Model.UserModel;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;


@Repository
public class UserRepositoryImpl //implements UserRepository
{
/*
    private static final String SQL_CREATE = "INSERT INTO \"USER\"(USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD) " +
                                             "VALUES(NEXTVAL('USER_SEQ'), ?, ?, ?, ?)";

    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) " +
                                                     "FROM \"USER\" " +
                                                     "WHERE EMAIL = ?";

    private static final String SQL_FIND_BY_ID = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD " +
                                                 "FROM \"USER\" " +
                                                 "WHERE USER_ID = ?";

    private static final String SQL_FIND_BY_EMAIL = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD " +
                                                    "FROM \"USER\" " +
                                                    "WHERE EMAIL = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Integer create(String firstName, String lastName, String email, String password) throws LSAuthException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try
        {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, email);
                ps.setString(4, hashedPassword);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("USER_ID");
        }
        catch (Exception e)
        {
            throw new LSAuthException("Invalid details. Failed to create account");
        }
    }

    @Override
    public UserModel findByEmailAndPassword(String email, String password) throws LSAuthException {
        try
        {
            UserModel userModel = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, new Object[]{email}, userRowMapper);
            if (!BCrypt.checkpw(password, userModel.getPassword()))
            {
                throw new LSAuthException("Invalid email/password");
            }
            return userModel;
        }
        catch (EmptyResultDataAccessException e)
        {
            throw new LSAuthException("Invalid email/password");
        }
    }

    @Override
    public Integer getCountByEmail(String email) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, new Object[]{email}, Integer.class);
    }

    @Override
    public UserModel findById(Integer userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId}, userRowMapper);
    }

    private RowMapper<UserModel> userRowMapper = ((rs, rowNum) -> {
        return new UserModel(rs.getInt("USER_ID"),
                rs.getString("FIRST_NAME"),
                rs.getString("LAST_NAME"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD"));
    }); */
}
