package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.SessionModel;

import java.util.List;

public interface SessionRepository
{
    List<SessionModel> findAll(Integer userId, Integer classId, Integer deckId, Integer flashcardId);

    SessionModel findById(Integer userId, Integer classId, Integer deckId, Integer flashcardId, Integer sessionId)
            throws ResourceNotFoundException;

    Integer create(Integer userId, Integer classId, Integer deckId, Integer flashcardId,
                   Integer session_correct, Integer session_incorrect, String session_date)
            throws BadRequestException;

    void update(Integer userId, Integer classId, Integer deckId, Integer flashcardId, Integer sessionId,
                SessionModel sessionModel)
            throws BadRequestException;

    void removeById(Integer userId, Integer classId, Integer deckId, Integer flashcardId, Integer sessionId)
            throws ResourceNotFoundException;

    SessionModel findByIdIfExists(Integer userId, Integer classId, Integer deckId, Integer flashcardId)
            throws ResourceNotFoundException;
}
