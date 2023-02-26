package dev.mattolivarez.Service;



import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.NoteModel;

import java.util.List;

public interface NoteService
{
    List<NoteModel> fetchAllNotes(Integer userId, Integer classId);

    NoteModel fetchNoteById(Integer userId, Integer classId, Integer noteId) throws ResourceNotFoundException;

    NoteModel addNote(Integer userId, Integer classId, String note_name, String note_body, Long deck_created_on) throws BadRequestException;

    void updateNote(Integer userId, Integer classId, Integer noteId, NoteModel noteModel) throws BadRequestException;

    void removeNote(Integer userId, Integer classId, Integer noteId) throws ResourceNotFoundException;
}
