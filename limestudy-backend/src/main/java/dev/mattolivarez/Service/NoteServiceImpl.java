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
    public List<NoteModel> fetchAllNotes(Integer userId) {
        return noteRepository.findAll(userId);
    }

    @Override
    public List<NoteModel> fetchAllNotesBelongingToClass(Integer userId, Integer classId){
        return noteRepository.findAllBelongingToClass(userId, classId);
    }

    @Override
    public NoteModel fetchNoteById(Integer userId, Integer noteId) throws ResourceNotFoundException {
        return noteRepository.findById(userId, noteId);
    }

    @Override
    public NoteModel addNote(Integer userId, Integer classId, String note_name, String note_body, String note_created_on) throws BadRequestException {
        int noteId = noteRepository.create(userId, classId, note_name, note_body, note_created_on);
        return noteRepository.findById(userId, noteId);
    }

    @Override
    public NoteModel addNoteNoClass(Integer userId, String note_name, String note_body, String note_created_on) throws BadRequestException {
        int noteId = noteRepository.createWithNoClass(userId, note_name, note_body, note_created_on);
        return noteRepository.findById(userId, noteId);
    }

    @Override
    public void updateNote(Integer userId, Integer noteId, NoteModel noteModel) throws BadRequestException {
        noteRepository.update(userId, noteId, noteModel);
    }

    @Override
    public void updateNoteNoClass(Integer userId, Integer noteId, NoteModel noteModel) throws BadRequestException {
        noteRepository.updateNoClass(userId, noteId, noteModel);
    }

    @Override
    public void removeNote(Integer userId, Integer noteId) throws ResourceNotFoundException {
        noteRepository.removeById(userId, noteId);
    }
}
