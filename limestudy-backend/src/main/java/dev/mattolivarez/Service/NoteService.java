/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Backend
Note Service Interface file
*/

package dev.mattolivarez.Service;



import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.NoteModel;

import java.util.List;

public interface NoteService
{
    List<NoteModel> fetchAllNotes(Integer userId);
    List<NoteModel> fetchAllNotesBelongingToClass(Integer userId, Integer classId);

    NoteModel fetchNoteById(Integer userId, Integer noteId) throws ResourceNotFoundException;

    NoteModel addNote(Integer userId, Integer classId, String note_name, String note_body, String note_created_on) throws BadRequestException;

    NoteModel addNoteNoClass(Integer userId, String note_name, String note_body, String note_created_on) throws BadRequestException;

    void updateNote(Integer userId, Integer noteId, NoteModel noteModel) throws BadRequestException;

    void updateNoteNoClass(Integer userId, Integer noteId, NoteModel noteModel) throws BadRequestException;

    void removeNote(Integer userId, Integer noteId) throws ResourceNotFoundException;
}
