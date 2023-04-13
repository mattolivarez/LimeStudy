package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.FlashcardModel;

import java.util.List;

public interface FlashcardRepository
{
    List<FlashcardModel> findAll(Integer userId, Integer classId, Integer deckId);

    FlashcardModel findById(Integer userId, Integer classId, Integer deckId, Integer flashcardId)
            throws ResourceNotFoundException;

    Integer create(Integer userId, Integer classId, Integer deckId, String question, String answer,
                   String flashcard_created_on, Integer correct, Integer incorrect, String last_studied_on,
                   Double occurrence_rate, Integer occurrence_rate_input)
            throws BadRequestException;

    void update(Integer userId, Integer classId, Integer deckId, Integer flashcardId, FlashcardModel flashcardModel)
            throws BadRequestException;

    void removeById(Integer userId, Integer classId, Integer deckId, Integer flashcardId)
            throws ResourceNotFoundException;

    List<FlashcardModel> findTraditionalStudySet(Integer userId, Integer classId, Integer deckId);

    List<FlashcardModel> findPracticeStudySet(Integer userId, Integer classId, Integer deckId);

    void reset(Integer userId) throws BadRequestException;

}
