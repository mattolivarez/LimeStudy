package dev.mattolivarez.Controller;

import dev.mattolivarez.Model.DeckModel;
import dev.mattolivarez.Model.NoteModel;
import dev.mattolivarez.Service.DeckService;
import dev.mattolivarez.Service.NoteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classes/{classId}/notes")
public class NoteController
{
    @Autowired
    NoteService noteService;


    @GetMapping("")
    public ResponseEntity<List<NoteModel>> getAllNotes(HttpServletRequest request,
                                                       @PathVariable("classId") Integer classId)
    {
        int userId = (Integer) request.getAttribute("userId");
        List<NoteModel> noteModels = noteService.fetchAllNotes(userId, classId);
        return new ResponseEntity<>(noteModels, HttpStatus.OK);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<NoteModel> getNoteById(HttpServletRequest request,
                                                 @PathVariable("classId") Integer classId,
                                                 @PathVariable("noteId") Integer noteId)
    {
        int userId = (Integer) request.getAttribute("userId");
        NoteModel noteModel = noteService.fetchNoteById(userId, classId, noteId);
        return new ResponseEntity<>(noteModel, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<NoteModel> addNote(HttpServletRequest request,
                                             @PathVariable("classId") Integer classId,
                                             @RequestBody Map<String, Object> noteMap)
    {
        int userId = (Integer) request.getAttribute("userId");
        String note_name = (String) noteMap.get("note_name");
        String note_body = (String) noteMap.get("note_body");
        Long note_created_on = (Long) noteMap.get("note_created_on");
        NoteModel noteModel = noteService.addNote(userId, classId, note_name, note_body, note_created_on);
        return new ResponseEntity<>(noteModel, HttpStatus.CREATED);
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<Map<String, Boolean>> updateNote(HttpServletRequest request,
                                                           @PathVariable("classId") Integer classId,
                                                           @PathVariable("noteId") Integer noteId,
                                                           @RequestBody NoteModel noteModel)
    {
        int userId = (Integer) request.getAttribute("userId");
        noteService.updateNote(userId, classId, noteId, noteModel);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Map<String, Boolean>> deleteNote(HttpServletRequest request,
                                                           @PathVariable("classId") Integer classId,
                                                           @PathVariable("noteId") Integer noteId)
    {
        int userId = (Integer) request.getAttribute("userId");
        noteService.removeNote(userId, classId, noteId);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}

