package dev.mattolivarez.Service;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.NoteModel;
import dev.mattolivarez.Repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class NoteServiceImpl implements NoteService
{

    @Autowired
    NoteRepository noteRepository;

    @Override
    public List<NoteModel> fetchAllNotes(Integer userId, Integer classId) {
        return noteRepository.findAll(userId, classId);
    }

    @Override
    public NoteModel fetchNoteById(Integer userId, Integer classId, Integer noteId) throws ResourceNotFoundException {
        return noteRepository.findById(userId, classId, noteId);
    }

    @Override
    public NoteModel addNote(Integer userId, Integer classId, String note_name, String note_body, Long note_created_on) throws BadRequestException {
        int noteId = noteRepository.create(userId, classId, note_name, note_body, note_created_on);
        return noteRepository.findById(userId, classId, noteId);
    }

    @Override
    public void updateNote(Integer userId, Integer classId, Integer noteId, NoteModel noteModel) throws BadRequestException {
        noteRepository.update(userId, classId, noteId, noteModel);
    }

    @Override
    public void removeNote(Integer userId, Integer classId, Integer noteId) throws ResourceNotFoundException {
        noteRepository.removeById(userId, classId, noteId);
    }
}
