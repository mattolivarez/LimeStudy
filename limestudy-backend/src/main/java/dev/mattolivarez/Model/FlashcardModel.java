package dev.mattolivarez.Model;

public class FlashcardModel
{
    private Integer flashcard_id;
    private Integer deck_id;
    private Integer class_id;
    private Integer user_id;
    private String question;
    private String answer;
    private Long flashcard_created_on;
    //private Response response;

    public FlashcardModel() {}

    public FlashcardModel(Integer flashcard_id, Integer deck_id, Integer class_id, Integer user_id, String question, String answer, Long flashcard_created_on) {
        this.flashcard_id = flashcard_id;
        this.deck_id = deck_id;
        this.class_id = class_id;
        this.user_id = user_id;
        this.question = question;
        this.answer = answer;
        this.flashcard_created_on = flashcard_created_on;
    }

    public Integer getFlashcard_id() {
        return flashcard_id;
    }

    public void setFlashcard_id(Integer flashcard_id) {
        this.flashcard_id = flashcard_id;
    }

    public Integer getDeck_id() {
        return deck_id;
    }

    public void setDeck_id(Integer deck_id) {
        this.deck_id = deck_id;
    }

    public Integer getClass_id() {
        return class_id;
    }

    public void setClass_id(Integer class_id) {
        this.class_id = class_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
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
