/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Backend
Contains Model for Event Entity
*/

package dev.mattolivarez.Model;

public class EventModel
{
    Integer userId;
    Integer eventId;
    String event_date;
    String event_description;
    String event_created_on;

    public EventModel() {};

    public EventModel(Integer userId, Integer eventId, String event_date, String event_description, String event_created_on) {
        this.userId = userId;
        this.eventId = eventId;
        this.event_date = event_date;
        this.event_description = event_description;
        this.event_created_on = event_created_on;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public String getEvent_created_on() {
        return event_created_on;
    }

    public void setEvent_created_on(String event_created_on) {
        this.event_created_on = event_created_on;
    }
}
