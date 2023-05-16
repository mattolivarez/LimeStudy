/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Backend
Session Service Interface file
*/

package dev.mattolivarez.Service;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.SessionModel;

import java.util.List;

public interface SessionService
{
    List<SessionModel> fetchAllSessions(Integer userId, Integer classId, Integer deckId, Integer flashcardId);

    SessionModel fetchSessionById(Integer userId, Integer classId, Integer deckId, Integer flashcardId, Integer sessionId)
            throws ResourceNotFoundException;

    SessionModel addSession(Integer userId, Integer classId, Integer deckId, Integer flashcardId,
                                Integer session_correct, Integer session_incorrect, String session_date)
            throws BadRequestException;

    void updateSession(Integer userId, Integer classId, Integer deckId, Integer flashcardId,
                       Integer sessionId, SessionModel sessionModel)
            throws BadRequestException;

    void removeSession(Integer userId, Integer classId, Integer deckId, Integer flashcardId, Integer sessionId)
            throws ResourceNotFoundException;

    SessionModel fetchSessionIfExists(Integer userId, Integer classId, Integer deckId, Integer flashcardId)
            throws ResourceNotFoundException;
}
