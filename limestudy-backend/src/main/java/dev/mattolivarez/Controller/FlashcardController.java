package dev.mattolivarez.Controller;


import dev.mattolivarez.Model.FlashcardModel;
import dev.mattolivarez.Service.FlashcardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classes/{classId}/decks/{deckId}/flashcards")
public class FlashcardController
{

    @Autowired
    FlashcardService flashcardService;

    @GetMapping("")
    public ResponseEntity<List<FlashcardModel>> getAllFlashcards(HttpServletRequest request,
                                                                 @PathVariable("classId") Integer classId,
                                                                 @PathVariable("deckId") Integer deckId)
    {
        int userId = (Integer) request.getAttribute("userId");
        List<FlashcardModel> flashcardModels = flashcardService.fetchAllFlashcards(userId, classId, deckId);
        return new ResponseEntity<>(flashcardModels, HttpStatus.OK);
    }

    @GetMapping("/{flashcardId}")
    public ResponseEntity<FlashcardModel> getFlashcardById(HttpServletRequest request,
                                                           @PathVariable("classId") Integer classId,
                                                           @PathVariable("deckId") Integer deckId,
                                                           @PathVariable("flashcardId") Integer flashcardId)
    {
        int userId = (Integer) request.getAttribute("userId");
        FlashcardModel flashcardModel = flashcardService.fetchFlashcardById(userId, classId, deckId, flashcardId);
        return new ResponseEntity<>(flashcardModel, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<FlashcardModel> addFlashcard(HttpServletRequest request,
                                                       @PathVariable("classId") Integer classId,
                                                       @PathVariable("deckId") Integer deckId,
                                                       @RequestBody Map<String, Object> flashcardMap)
    {
        int userId = (Integer) request.getAttribute("userId");
        String question = (String) flashcardMap.get("question");
        String answer = (String) flashcardMap.get("answer");
        String flashcard_created_on = (String) flashcardMap.get("flashcard_created_on");
        Integer correct = (Integer) flashcardMap.get("correct");
        Integer incorrect = (Integer) flashcardMap.get("incorrect");
        String last_studied_on = (String) flashcardMap.get("last_studied_on");
        Double occurrence_rate = (Double) flashcardMap.get("occurrence_rate");
        Integer occurrence_rate_input = (Integer) flashcardMap.get("occurrence_rate_input");
        FlashcardModel flashcardModel = flashcardService.addFlashcard(userId, classId, deckId,
                question, answer, flashcard_created_on, correct, incorrect, last_studied_on, occurrence_rate, occurrence_rate_input);
        return new ResponseEntity<>(flashcardModel, HttpStatus.CREATED);
    }

    @PutMapping("/{flashcardId}")
    public ResponseEntity<Map<String, Boolean>> updateFlashcard(HttpServletRequest request,
                                                           @PathVariable("classId") Integer classId,
                                                           @PathVariable("deckId") Integer deckId,
                                                           @PathVariable("flashcardId") Integer flashcardId,
                                                           @RequestBody FlashcardModel flashcardModel)
    {
        int userId = (Integer) request.getAttribute("userId");
        flashcardService.updateFlashcard(userId, classId, deckId, flashcardId, flashcardModel);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{flashcardId}")
    public ResponseEntity<Map<String, Boolean>> deleteDeck(HttpServletRequest request,
                                                           @PathVariable("classId") Integer classId,
                                                           @PathVariable("deckId") Integer deckId,
                                                           @PathVariable("flashcardId") Integer flashcardId)
    {
        int userId = (Integer) request.getAttribute("userId");
        flashcardService.removeFlashcard(userId, classId, deckId, flashcardId);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/traditional-study")
    public ResponseEntity<List<FlashcardModel>> getFlashcardsForTraditionalStudy(HttpServletRequest request,
                                                                 @PathVariable("classId") Integer classId,
                                                                 @PathVariable("deckId") Integer deckId)
    {
        int userId = (Integer) request.getAttribute("userId");
        List<FlashcardModel> flashcardModels = flashcardService.fetchTraditionalStudySet(userId, classId, deckId);
        return new ResponseEntity<>(flashcardModels, HttpStatus.OK);
    }
}
