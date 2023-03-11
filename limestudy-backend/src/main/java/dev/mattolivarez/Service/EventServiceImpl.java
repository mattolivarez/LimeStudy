package dev.mattolivarez.Service;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.EventModel;
import dev.mattolivarez.Repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EventServiceImpl implements EventService
{

    @Autowired
    EventRepository eventRepository;

    @Override
    public List<EventModel> fetchAllEvents(Integer userId) {
        return eventRepository.findAll(userId);
    }

    @Override
    public EventModel fetchEventById(Integer userId, Integer eventId) throws ResourceNotFoundException {
        return eventRepository.findById(userId, eventId);
    }

    @Override
    public EventModel addEvent(Integer userId, String event_date, String event_description, String event_created_on) throws BadRequestException {
        int eventId = eventRepository.create(userId, event_date, event_description, event_created_on);
        return eventRepository.findById(userId, eventId);
    }

    @Override
    public void updateEvent(Integer userId, Integer eventId, EventModel eventModel) throws BadRequestException {
        eventRepository.update(userId, eventId, eventModel);
    }

    @Override
    public void removeEvent(Integer userId, Integer eventId) throws ResourceNotFoundException {
        //this.fetchEventById(userId, eventId);
        eventRepository.removeById(userId, eventId);
    }
}
