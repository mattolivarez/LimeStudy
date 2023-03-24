package dev.mattolivarez.Model;

import java.math.BigInteger;

public class StatisticsModel
{
    private Integer correct;
    private Integer incorrect;
    private Integer total;
    private Integer total_cards;
    private String deck_name;
    private String class_name;
    private Integer userId;
    private String date;

    // model for no deck name, class name, or user
    public StatisticsModel(Integer correct, Integer incorrect, Integer total, Integer total_cards, String date)
    {
        this(correct, incorrect, total, total_cards, null, null, null, date);
    }

    // model for no date
    public StatisticsModel(Integer correct, Integer incorrect, Integer total, Integer total_cards, String deck_name, String class_name, Integer userId) {
        this(correct, incorrect, total, total_cards, deck_name, class_name, userId, null);
    }

    public StatisticsModel(Integer correct, Integer incorrect, Integer total, Integer total_cards, String deck_name, String class_name, Integer userId, String date) {
        this.correct = correct;
        this.incorrect = incorrect;
        this.total = total;
        this.total_cards = total_cards;
        this.deck_name = deck_name;
        this.class_name = class_name;
        this.userId = userId;
        this.date = date;
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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotal_cards() {
        return total_cards;
    }

    public void setTotal_cards(Integer total_cards) {
        this.total_cards = total_cards;
    }

    public String getDeck_name() {
        return deck_name;
    }

    public void setDeck_name(String deck_name) {
        this.deck_name = deck_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
