package dev.mattolivarez.Model;

public class SessionModel
{
    Integer sessionId;
    Integer flashcardId;
    Integer deckId;
    Integer classId;
    Integer userId;
    Integer session_correct;
    Integer session_incorrect;
    String session_date;

    public SessionModel() {};

    public SessionModel(Integer sessionId, Integer flashcardId, Integer deckId, Integer classId, Integer userId, Integer session_correct, Integer session_incorrect, String session_date) {
        this.sessionId = sessionId;
        this.flashcardId = flashcardId;
        this.deckId = deckId;
        this.classId = classId;
        this.userId = userId;
        this.session_correct = session_correct;
        this.session_incorrect = session_incorrect;
        this.session_date = session_date;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getDeckId() {
        return deckId;
    }

    public void setDeckId(Integer deckId) {
        this.deckId = deckId;
    }

    public Integer getFlashcardId() {
        return flashcardId;
    }

    public void setFlashcardId(Integer flashcardId) {
        this.flashcardId = flashcardId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getSession_correct() {
        return session_correct;
    }

    public void setSession_correct(Integer session_correct) {
        this.session_correct = session_correct;
    }

    public Integer getSession_incorrect() {
        return session_incorrect;
    }

    public void setSession_incorrect(Integer session_incorrect) {
        this.session_incorrect = session_incorrect;
    }

    public String getSession_date() {
        return session_date;
    }

    public void setSession_date(String session_date) {
        this.session_date = session_date;
    }
}
