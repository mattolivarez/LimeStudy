package dev.mattolivarez.Service;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.SessionModel;
import dev.mattolivarez.Repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SessionServiceImpl implements SessionService
{

    @Autowired
    SessionRepository sessionRepository;

    @Override
    public List<SessionModel> fetchAllSessions(Integer userId, Integer classId, Integer deckId, Integer flashcardId) {
        return sessionRepository.findAll(userId, classId, deckId, flashcardId);
    }

    @Override
    public SessionModel fetchSessionById(Integer userId, Integer classId, Integer deckId, Integer flashcardId, Integer sessionId)
            throws ResourceNotFoundException {
        return sessionRepository.findById(userId, classId, deckId, flashcardId, sessionId);
    }

    @Override
    public SessionModel addSession(Integer userId, Integer classId, Integer deckId, Integer flashcardId, Integer session_correct,
                                   Integer session_incorrect, String session_date) throws BadRequestException {
        int sessionId = sessionRepository.create(userId, classId, deckId, flashcardId, session_correct, session_incorrect,
                session_date);
        return sessionRepository.findById(userId, classId, deckId, flashcardId, sessionId);
    }

    @Override
    public void updateSession(Integer userId, Integer classId, Integer deckId, Integer flashcardId, Integer sessionId,
                              SessionModel sessionModel) throws BadRequestException {
        sessionRepository.update(userId, classId, deckId, flashcardId, sessionId, sessionModel);
    }

    @Override
    public void removeSession(Integer userId, Integer classId, Integer deckId, Integer flashcardId, Integer sessionId)
            throws ResourceNotFoundException {
        sessionRepository.removeById(userId, classId, deckId, flashcardId, sessionId);
    }

    @Override
    public SessionModel fetchSessionIfExists(Integer userId, Integer classId, Integer deckId, Integer flashcardId)
            throws ResourceNotFoundException
    {
        return sessionRepository.findByIdIfExists(userId, classId, deckId, flashcardId);
    }
}
