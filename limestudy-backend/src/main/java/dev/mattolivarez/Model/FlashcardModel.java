/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Backend
Contains Model for Flashcard Entity
*/

package dev.mattolivarez.Model;

public class FlashcardModel
{
    private Integer flashcardId;
    private Integer deckId;
    private Integer classId;
    private Integer userId;
    private String question;
    private String answer;
    private String flashcard_created_on;
    private Integer correct;
    private Integer incorrect;
    private String last_studied_on;
    private Double occurrence_rate;
    private Integer occurrence_rate_input;
    //private Double

    public FlashcardModel() {}

    public FlashcardModel(Integer flashcardId, Integer deckId, Integer classId, Integer userId, String question,
                          String answer, String flashcard_created_on, Integer correct, Integer incorrect,
                          String last_studied_on, Double occurrence_rate, Integer occurrence_rate_input) {
        this.flashcardId = flashcardId;
        this.deckId = deckId;
        this.classId = classId;
        this.userId = userId;
        this.question = question;
        this.answer = answer;
        this.flashcard_created_on = flashcard_created_on;
        this.correct = correct;
        this.incorrect = incorrect;
        this.last_studied_on = last_studied_on;
        this.occurrence_rate = occurrence_rate;
        this.occurrence_rate_input = occurrence_rate_input;
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

    public String getFlashcard_created_on() {
        return flashcard_created_on;
    }

    public void setFlashcard_created_on(String flashcard_created_on) {
        this.flashcard_created_on = flashcard_created_on;
    }

    public Integer getCorrect() {
        return correct;
    }

    public void setCorrect(Integer correct) {
        this.correct = correct;
    }

    public Integer getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(Integer incorrect) {
        this.incorrect = incorrect;
    }

    public String getLast_studied_on() {
        return last_studied_on;
    }

    public void setLast_studied_on(String last_studied_on) {
        this.last_studied_on = last_studied_on;
    }

    public Double getOccurrence_rate() {
        return occurrence_rate;
    }

    public void setOccurrence_rate(Double occurrence_rate) {
        this.occurrence_rate = occurrence_rate;
    }

    public Integer getOccurrence_rate_input() {
        return occurrence_rate_input;
    }

    public void setOccurrence_rate_input(Integer occurrence_rate_input) {
        this.occurrence_rate_input = occurrence_rate_input;
    }
}
