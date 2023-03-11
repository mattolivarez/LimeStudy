package dev.mattolivarez.Controller;

import dev.mattolivarez.Model.SessionModel;
import dev.mattolivarez.Service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classes/{classId}/decks/{deckId}/flashcards/{flashcardId}/sessions")
public class SessionController
{
    @Autowired
    SessionService sessionService;

    @GetMapping("")
    public ResponseEntity<List<SessionModel>> getAllSessions(HttpServletRequest request,
                                                             @PathVariable("classId") Integer classId,
                                                             @PathVariable("deckId") Integer deckId,
                                                             @PathVariable("flashcardId") Integer flashcardId)
    {
        int userId = (Integer) request.getAttribute("userId");
        List<SessionModel> sessionModels = sessionService.fetchAllSessions(userId, classId, deckId, flashcardId);
        return new ResponseEntity<>(sessionModels, HttpStatus.OK);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionModel> getSessionById(HttpServletRequest request,
                                                       @PathVariable("classId") Integer classId,
                                                       @PathVariable("deckId") Integer deckId,
                                                       @PathVariable("flashcardId") Integer flashcardId,
                                                       @PathVariable("sessionId") Integer sessionId)
    {
        int userId = (Integer) request.getAttribute("userId");
        SessionModel sessionModel = sessionService.fetchSessionById(userId, classId, deckId, flashcardId, sessionId);
        return new ResponseEntity<>(sessionModel, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<SessionModel> addSession(HttpServletRequest request,
                                                       @PathVariable("classId") Integer classId,
                                                       @PathVariable("deckId") Integer deckId,
                                                       @PathVariable("flashcardId") Integer flashcardId,
                                                       @RequestBody Map<String, Object> sessionMap)
    {
        int userId = (Integer) request.getAttribute("userId");
        Integer session_correct = (Integer) sessionMap.get("session_correct");
        Integer session_incorrect = (Integer) sessionMap.get("session_incorrect");
        String session_date = (String) sessionMap.get("session_date");
        SessionModel sessionModel = sessionService.addSession(userId, classId, deckId, flashcardId,
                session_correct, session_incorrect, session_date);
        return new ResponseEntity<>(sessionModel, HttpStatus.CREATED);
    }

    @PutMapping("/{sessionId}")
    public ResponseEntity<Map<String, Boolean>> updateSession(HttpServletRequest request,
                                                                @PathVariable("classId") Integer classId,
                                                                @PathVariable("deckId") Integer deckId,
                                                                @PathVariable("flashcardId") Integer flashcardId,
                                                                @PathVariable("sessionId") Integer sessionId,
                                                                @RequestBody SessionModel sessionModel)
    {
        int userId = (Integer) request.getAttribute("userId");
        sessionService.updateSession(userId, classId, deckId, flashcardId, sessionId, sessionModel);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Map<String, Boolean>> deleteSession(HttpServletRequest request,
                                                              @PathVariable("classId") Integer classId,
                                                              @PathVariable("deckId") Integer deckId,
                                                              @PathVariable("flashcardId") Integer flashcardId,
                                                              @PathVariable("sessionId") Integer sessionId)
    {
        int userId = (Integer) request.getAttribute("userId");
        sessionService.removeSession(userId, classId, deckId, flashcardId, sessionId);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
