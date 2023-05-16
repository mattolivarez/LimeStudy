/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Backend
Controller file for Event Model
*/

package dev.mattolivarez.Controller;


import dev.mattolivarez.Model.EventModel;
import dev.mattolivarez.Service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController
{
    @Autowired
    EventService eventService;

    @GetMapping("")
    public ResponseEntity<List<EventModel>> getAllEvents(HttpServletRequest request)
    {
        int userId = (Integer) request.getAttribute("userId");
        List<EventModel> events = eventService.fetchAllEvents(userId);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventModel> getEventById(HttpServletRequest request, @PathVariable("eventId") Integer eventId)
    {
        int userId = (Integer) request.getAttribute("userId");
        EventModel eventModel = eventService.fetchEventById(userId, eventId);
        return new ResponseEntity<>(eventModel, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<EventModel> addEvent(HttpServletRequest request, @RequestBody Map<String, Object> eventMap)
    {
        int userId = (Integer) request.getAttribute("userId");
        String event_date = (String) eventMap.get("event_date");
        String event_description = (String) eventMap.get("event_description");
        String event_created_on = (String) eventMap.get("event_created_on");
        EventModel eventModel = eventService.addEvent(userId, event_date, event_description, event_created_on);
        return new ResponseEntity<>(eventModel, HttpStatus.CREATED);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Map<String, Boolean>> updateEvent(HttpServletRequest request,
                                                            @PathVariable("eventId") Integer eventId,
                                                            @RequestBody EventModel eventModel)
    {
        int userId = (Integer) request.getAttribute("userId");
        eventService.updateEvent(userId, eventId, eventModel);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Map<String, Boolean>> deleteEvent(HttpServletRequest request,
                                                            @PathVariable("eventId") Integer eventId)
    {
        int userId = (Integer) request.getAttribute("userId");
        eventService.removeEvent(userId, eventId);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
