package dev.mattolivarez.Service;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.FlashcardModel;
import dev.mattolivarez.Repository.FlashcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FlashcardServiceImpl implements FlashcardService
{

    @Autowired
    FlashcardRepository flashcardRepository;

    @Override
    public List<FlashcardModel> fetchAllFlashcards(Integer userId, Integer classId, Integer deckId) {
        return flashcardRepository.findAll(userId, classId, deckId);
    }

    @Override
    public FlashcardModel fetchFlashcardById(Integer userId, Integer classId, Integer deckId, Integer flashcardId) throws ResourceNotFoundException {
        return flashcardRepository.findById(userId, classId, deckId, flashcardId);
    }

    @Override
    public FlashcardModel addFlashcard(Integer userId, Integer classId, Integer deckId, String question, String answer, Long flashcard_created_on) throws BadRequestException {
        int flashcardId = flashcardRepository.create(userId, classId, deckId, question, answer, flashcard_created_on);
        return flashcardRepository.findById(userId, classId, deckId, flashcardId);
    }

    @Override
    public void updateFlashcard(Integer userId, Integer classId, Integer deckId, Integer flashcardId, FlashcardModel flashcardModel) throws BadRequestException {
        flashcardRepository.update(userId, classId, deckId, flashcardId, flashcardModel);
    }

    @Override
    public void removeFlashcard(Integer userId, Integer classId, Integer deckId, Integer flashcardId) throws ResourceNotFoundException {
        flashcardRepository.removeById(userId, classId, deckId, flashcardId);
    }
}
