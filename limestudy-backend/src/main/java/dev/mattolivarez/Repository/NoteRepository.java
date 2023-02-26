package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.NoteModel;

import java.util.List;

public interface NoteRepository
{
    List<NoteModel> findAll(Integer userId, Integer classId);

    NoteModel findById(Integer userId, Integer classId, Integer noteId) throws ResourceNotFoundException;

    Integer create(Integer userId, Integer classId, String note_name, String note_body, Long note_created_on) throws BadRequestException;

    void update(Integer userId, Integer classId, Integer noteId, NoteModel noteModel) throws BadRequestException;

    void removeById(Integer userId, Integer classId, Integer noteId) throws ResourceNotFoundException;
}
