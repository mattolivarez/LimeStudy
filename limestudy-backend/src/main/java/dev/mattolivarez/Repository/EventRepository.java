/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Backend
Event Repository Interface file
*/

package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.EventModel;

import java.util.List;

public interface EventRepository
{
    List<EventModel> findAll(Integer userId) throws ResourceNotFoundException;

    EventModel findById(Integer userId, Integer eventId) throws ResourceNotFoundException;

    Integer create(Integer userId, String event_date, String event_description, String event_created_on)
            throws BadRequestException;

    void update(Integer userId, Integer eventId, EventModel eventModel) throws BadRequestException;

    void removeById(Integer userId, Integer eventId);
}
