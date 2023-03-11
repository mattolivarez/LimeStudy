package dev.mattolivarez.Service;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.EventModel;

import java.util.List;

public interface EventService
{
    List<EventModel> fetchAllEvents(Integer userId);

    EventModel fetchEventById(Integer userId, Integer eventId) throws ResourceNotFoundException;

    EventModel addEvent(Integer userId, String event_date, String event_description, String event_created_on) throws BadRequestException;

    void updateEvent(Integer userId, Integer eventId, EventModel eventModel) throws BadRequestException;

    void removeEvent(Integer userId, Integer eventId) throws ResourceNotFoundException;
}
