package dev.mattolivarez.Model;

public class FlashcardModel
{
    private Integer flashcardId;
    private Integer deckId;
    private Integer classId;
    private Integer userId;
    private String question;
    private String answer;
    private Long flashcard_created_on;
    //private Response response;

    public FlashcardModel() {}

    public FlashcardModel(Integer flashcardId, Integer deckId, Integer classId, Integer userId, String question, String answer, Long flashcard_created_on) {
        this.flashcardId = flashcardId;
        this.deckId = deckId;
        this.classId = classId;
        this.userId = userId;
        this.question = question;
        this.answer = answer;
        this.flashcard_created_on = flashcard_created_on;
    }

    public Integer getFlashcardId() {
        return flashcardId;
    }

    public void setFlashcardId(Integer flashcardId) {
        this.flashcardId = flashcardId;
    }

    public Integer getDeckId() {
        return deckId;
    }

    public void setDeckId(Integer deckId) {
        this.deckId = deckId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getFlashcard_created_on() {
        return flashcard_created_on;
    }

    public void setFlashcard_created_on(Long flashcard_created_on) {
        this.flashcard_created_on = flashcard_created_on;
    }
}
