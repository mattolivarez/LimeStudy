/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Backend
Controller file for Note Model
*/

package dev.mattolivarez.Controller;

import dev.mattolivarez.Model.DeckModel;
import dev.mattolivarez.Model.NoteModel;
import dev.mattolivarez.Service.DeckService;
import dev.mattolivarez.Service.NoteService;
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
@RequestMapping("/api/notes")
public class NoteController
{
    @Autowired
    NoteService noteService;


    @GetMapping("")
    public ResponseEntity<List<NoteModel>> getAllNotes(HttpServletRequest request)
    {
        int userId = (Integer) request.getAttribute("userId");
        List<NoteModel> noteModels = noteService.fetchAllNotes(userId);
        return new ResponseEntity<>(noteModels, HttpStatus.OK);
    }

    @GetMapping("/classes/{classId}")
    public ResponseEntity<List<NoteModel>> getAllNotesBelongingToClass(HttpServletRequest request,
                                                                       @PathVariable("classId") Integer classId)
    {
        int userId = (Integer) request.getAttribute("userId");
        List<NoteModel> noteModels = noteService.fetchAllNotesBelongingToClass(userId, classId);
        return new ResponseEntity<>(noteModels, HttpStatus.OK);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<NoteModel> getNoteById(HttpServletRequest request,
                                                 @PathVariable("noteId") Integer noteId)
    {
        int userId = (Integer) request.getAttribute("userId");
        NoteModel noteModel = noteService.fetchNoteById(userId, noteId);
        return new ResponseEntity<>(noteModel, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<NoteModel> addNote(HttpServletRequest request,
                                             @RequestBody Map<String, Object> noteMap)
    {
        int userId = (Integer) request.getAttribute("userId");
        int classId = (Integer) noteMap.get("classId");
//        if (noteMap.get("classId") != null) {
//            classId = (Integer) noteMap.get("classId");
//        }
//        else
//        {
//            classId = 0;
//        }
        String note_name = (String) noteMap.get("note_name");
        String note_body = (String) noteMap.get("note_body");
        String note_created_on = (String) noteMap.get("note_created_on");
        NoteModel noteModel;
        System.out.println(classId);
        if (classId != 0)
        {
            noteModel = noteService.addNote(userId, classId, note_name, note_body, note_created_on);
        }
        else
        {
            noteModel = noteService.addNoteNoClass(userId, note_name, note_body, note_created_on);
        }
        return new ResponseEntity<>(noteModel, HttpStatus.CREATED);
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<Map<String, Boolean>> updateNote(HttpServletRequest request,
                                                           @PathVariable("noteId") Integer noteId,
                                                           @RequestBody NoteModel noteModel)
    {
        int userId = (Integer) request.getAttribute("userId");
        if (noteModel.getClassId() != 0)
        {
            noteService.updateNote(userId, noteId, noteModel);
        }
        else
        {
            noteService.updateNoteNoClass(userId, noteId, noteModel);
        }
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Map<String, Boolean>> deleteNote(HttpServletRequest request,
                                                           @PathVariable("noteId") Integer noteId)
    {
        int userId = (Integer) request.getAttribute("userId");
        noteService.removeNote(userId, noteId);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}

