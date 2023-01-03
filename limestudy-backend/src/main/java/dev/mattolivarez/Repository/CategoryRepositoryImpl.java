package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.PrimitiveIterator;


@Repository
public class CategoryRepositoryImpl implements CategoryRepository
{

    private static final String SQL_CREATE = "INSERT INTO \"CATEGORY\"(CATEGORY_ID, USER_ID, TITLE, DESCRIPTION) " +
                                             "VALUES(NEXTVAL('CATEGORY_SEQ'), ?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT C.CATEGORY_ID, C.USER_ID, C.TITLE, C.DESCRIPTION, COALESCE(SUM(T.AMOUNT),0) AS TOTAL_EXPENSE " +
                                                 "FROM \"TRANSACTION\" T RIGHT OUTER JOIN \"CATEGORY\" C ON C.CATEGORY_ID = T.CATEGORY_ID " +
                                                 "WHERE C.USER_ID = ? AND C.CATEGORY_ID = ? " +
                                                 "GROUP BY C.CATEGORY_ID";

    private static final String SQL_FIND_ALL = "SELECT C.CATEGORY_ID, C.USER_ID, C.TITLE, C.DESCRIPTION, COALESCE(SUM(T.AMOUNT),0) AS TOTAL_EXPENSE " +
                                               "FROM \"TRANSACTION\" T RIGHT OUTER JOIN \"CATEGORY\" C ON C.CATEGORY_ID = T.CATEGORY_ID " +
                                               "WHERE C.USER_ID = ? " +
                                               "GROUP BY C.CATEGORY_ID";

    private static final String SQL_UPDATE = "UPDATE \"CATEGORY\" " +
                                             "SET TITLE = ?, DESCRIPTION = ? " +
                                             "WHERE USER_ID = ? AND CATEGORY_ID = ?";

    private static final String SQL_DELETE = "DELETE FROM \"CATEGORY\" " +
                                             "WHERE USER_ID = ? AND CATEGORY_ID = ?";

    private static final String SQL_DELETE_ALL_TRANSACTIONS = "DELETE FROM \"TRANSACTION\" " +
                                                              "WHERE CATEGORY_ID = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Category> findAll(Integer userId) throws ResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_ALL, new Object[]{userId}, categoryRowMapper);
    }

    @Override
    public Category findById(Integer userId, Integer categoryId) throws ResourceNotFoundException {
        try
        {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId, categoryId}, categoryRowMapper);
        }
        catch (Exception e)
        {
            throw new ResourceNotFoundException("Category not found");
        }
    }

    @Override
    public Integer create(Integer userId, String title, String description) throws BadRequestException {
        try
        {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setString(2, title);
                ps.setString(3, description);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("CATEGORY_ID");
        }
        catch (Exception e)
        {
            throw new BadRequestException("Invalid request");
        }
    }

    @Override
    public void update(Integer userId, Integer categoryId, Category category) throws BadRequestException {
        try
        {
            jdbcTemplate.update(SQL_UPDATE, new Object[]{category.getTitle(), category.getDescription(), userId, categoryId});
        }
        catch(Exception e)
        {
            throw new BadRequestException("Invalid request");
        }
    }

    @Override
    public void removeById(Integer userId, Integer categoryId) {
        this.removeAllCatTransactions(categoryId);
        jdbcTemplate.update(SQL_DELETE, new Object[]{userId, categoryId});
    }

    private void removeAllCatTransactions(Integer categoryId)
    {
        jdbcTemplate.update(SQL_DELETE_ALL_TRANSACTIONS, new Object[]{categoryId});
    }

    private RowMapper<Category> categoryRowMapper = ((rs, rowNum) -> {
        return new Category(rs.getInt("CATEGORY_ID"),
                            rs.getInt("USER_ID"),
                            rs.getString("TITLE"),
                            rs.getString("DESCRIPTION"),
                            rs.getDouble("TOTAL_EXPENSE"));
    });
}
