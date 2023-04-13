package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.EventModel;
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
public class EventRepositoryImpl implements EventRepository
{

    private static final String SQL_CREATE = "INSERT INTO \"EVENT\"(EVENT_ID, USER_ID, EVENT_DATE, EVENT_DESCRIPTION, EVENT_CREATED_ON) " +
            "VALUES(NEXTVAL('EVENT_SEQ'), ?, ?, ?, ?) ";

    private static final String SQL_FIND_BY_ID = "SELECT EVENT_ID, USER_ID, TO_CHAR(EVENT_DATE, 'MM/DD/YYYY') AS EVENT_DATE, EVENT_DESCRIPTION, TO_CHAR(EVENT_CREATED_ON, 'MM/DD/YYYY') AS EVENT_CREATED_ON " +
            "FROM \"EVENT\" " +
            "WHERE USER_ID = ? AND EVENT_ID = ? ";

    private static final String SQL_FIND_ALL = "SELECT EVENT_ID, USER_ID, TO_CHAR(EVENT_DATE, 'MM/DD/YYYY') AS EVENT_DATE, EVENT_DESCRIPTION, TO_CHAR(EVENT_CREATED_ON, 'MM/DD/YYYY') AS EVENT_CREATED_ON " +
            "FROM \"EVENT\" " +
            "WHERE USER_ID = ? ";

    private static final String SQL_UPDATE = "UPDATE \"EVENT\" " +
            "SET EVENT_DATE = ?, EVENT_DESCRIPTION = ?, EVENT_CREATED_ON = ? " +
            "WHERE USER_ID = ? AND EVENT_ID = ?";

    private static final String SQL_DELETE = "DELETE FROM \"EVENT\" " +
            "WHERE USER_ID = ? AND EVENT_ID = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<EventModel> findAll(Integer userId) throws ResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_ALL, new Object[]{userId}, eventRowMapper);
    }

    @Override
    public EventModel findById(Integer userId, Integer eventId) throws ResourceNotFoundException {
        try
        {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId, eventId}, eventRowMapper);
        }
        catch (Exception e)
        {
            throw new ResourceNotFoundException("Created event not found");
        }
    }

    @Override
    public Integer create(Integer userId, String event_date, String event_description, String event_created_on) throws BadRequestException {
        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date eventDateCreated = simpleDateFormat.parse(event_date);
            java.sql.Date eventDate = new java.sql.Date(eventDateCreated.getTime());
            Date eventCreatedOnDate = simpleDateFormat.parse(event_created_on);
            java.sql.Date eventCreatedOn = new java.sql.Date(eventCreatedOnDate.getTime());
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setDate(2, eventDate);
                ps.setString(3, event_description);
                ps.setDate(4, eventCreatedOn);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("EVENT_ID");
        }
        catch (Exception e)
        {
            throw new BadRequestException("Invalid request. Could not create event.");
        }
    }

    @Override
    public void update(Integer userId, Integer eventId, EventModel eventModel) throws BadRequestException {
        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date eventDateCreated = simpleDateFormat.parse(eventModel.getEvent_date());
            java.sql.Date eventDate = new java.sql.Date(eventDateCreated.getTime());
            Date eventCreatedDate = simpleDateFormat.parse(eventModel.getEvent_created_on());
            java.sql.Date eventCreatedOn = new java.sql.Date(eventCreatedDate.getTime());
            jdbcTemplate.update(SQL_UPDATE,
                    new Object[]{eventDate, eventModel.getEvent_description(), eventCreatedOn, userId, eventId});
        }
        catch(Exception e)
        {
            throw new BadRequestException("Invalid request. Could not update event.");
        }
    }

    @Override
    public void removeById(Integer userId, Integer eventId) {
        jdbcTemplate.update(SQL_DELETE, new Object[]{userId, eventId});
    }

    private RowMapper<EventModel> eventRowMapper = ((rs, rowNum) -> {
        return new EventModel(rs.getInt("USER_ID"),
                rs.getInt("EVENT_ID"),
                rs.getString("EVENT_DATE"),
                rs.getString("EVENT_DESCRIPTION"),
                rs.getString("EVENT_CREATED_ON"));
    });
}
