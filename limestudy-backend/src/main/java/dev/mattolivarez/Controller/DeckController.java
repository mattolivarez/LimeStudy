package dev.mattolivarez.Controller;

import dev.mattolivarez.Model.DeckModel;
import dev.mattolivarez.Service.DeckService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classes/{classId}/decks")
public class DeckController
{
    @Autowired
    DeckService deckService;


    @GetMapping("")
    public ResponseEntity<List<DeckModel>> getAllDecks(HttpServletRequest request,
                                                       @PathVariable("classId") Integer classId)
    {
        int userId = (Integer) request.getAttribute("userId");
        List<DeckModel> deckModels = deckService.fetchAllDecks(userId, classId);
        return new ResponseEntity<>(deckModels, HttpStatus.OK);
    }

    @GetMapping("/{deckId}")
    public ResponseEntity<DeckModel> getDeckById(HttpServletRequest request,
                                                 @PathVariable("classId") Integer classId,
                                                 @PathVariable("deckId") Integer deckId)
    {
        int userId = (Integer) request.getAttribute("userId");
        DeckModel deckModel = deckService.fetchDeckById(userId, classId, deckId);
        return new ResponseEntity<>(deckModel, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<DeckModel> addDeck(HttpServletRequest request,
                                             @PathVariable("classId") Integer classId,
                                             @RequestBody Map<String, Object> deckMap)
    {
        int userId = (Integer) request.getAttribute("userId");
        String deck_name = (String) deckMap.get("note");
        Long deck_created_on = (Long) deckMap.get("deck_created_on");
        DeckModel deckModel = deckService.addDeck(userId, classId, deck_name, deck_created_on);
        return new ResponseEntity<>(deckModel, HttpStatus.CREATED);
    }

    @PutMapping("/{deckId}")
    public ResponseEntity<Map<String, Boolean>> updateDeck(HttpServletRequest request,
                                                           @PathVariable("classId") Integer classId,
                                                           @PathVariable("deckId") Integer deckId,
                                                           @RequestBody DeckModel deckModel)
    {
        int userId = (Integer) request.getAttribute("userId");
        deckService.updateDeck(userId, classId, deckId, deckModel);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{deckId}")
    public ResponseEntity<Map<String, Boolean>> deleteDeck(HttpServletRequest request,
                                                           @PathVariable("classId") Integer classId,
                                                           @PathVariable("deckId") Integer deckId)
    {
        int userId = (Integer) request.getAttribute("userId");
        deckService.removeDeck(userId, classId, deckId);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
