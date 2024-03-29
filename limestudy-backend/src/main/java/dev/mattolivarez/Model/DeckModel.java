/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Backend
Contains Model for Deck Entity
*/

package dev.mattolivarez.Model;

public class DeckModel
{
    private Integer deckId;
    private Integer classId;
    private Integer userId;
    private String deck_name;
    private String deck_created_on;
    //private Integer number_of_flashcards;
    //private Double deck_accuracy_percentage;
    //private Double deck_accuracy_percentage_for_week;
    //private Double deck_accuracy_percentage_for_month;
    //private String deck_color_code;

    public DeckModel() {}

    public DeckModel(Integer deckId, Integer classId, Integer userId, String deck_name, String deck_created_on) {
        this.deckId = deckId;
        this.classId = classId;
        this.userId = userId;
        this.deck_name = deck_name;
        this.deck_created_on = deck_created_on;
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

    public String getDeck_name() {
        return deck_name;
    }

    public void setDeck_name(String deck_name) {
        this.deck_name = deck_name;
    }

    public String getDeck_created_on() {
        return deck_created_on;
    }

    public void setDeck_created_on(String deck_created_on) {
        this.deck_created_on = deck_created_on;
    }
}
