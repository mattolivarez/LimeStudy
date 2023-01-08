package dev.mattolivarez.Service;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.FlashcardModel;

import java.util.List;

public interface FlashcardService
{
    List<FlashcardModel> fetchAllFlashcards(Integer userId, Integer classId, Integer deckId);

    FlashcardModel fetchFlashcardById(Integer userId, Integer classId, Integer deckId, Integer flashcardId) throws ResourceNotFoundException;

    FlashcardModel addFlashcard(Integer userId, Integer classId, Integer deckId, String question, String answer, Long flashcard_created_on) throws BadRequestException;

    void updateFlashcard(Integer userId, Integer classId, Integer deckId, Integer flashcardId, FlashcardModel flashcardModel) throws BadRequestException;

    void removeFlashcard(Integer userId, Integer classId, Integer deckId, Integer flashcardId) throws ResourceNotFoundException;
}
