/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Backend
Contains Model for User Entity
*/

package dev.mattolivarez.Model;


public class UserModel
{

    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String account_created_on;
    private Integer flashcard_delay_setting;

    public UserModel(){}

    public UserModel(Integer userId, String firstName, String lastName, String email, String account_created_on, Integer flashcard_delay_setting) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.account_created_on = account_created_on;
        this.flashcard_delay_setting = flashcard_delay_setting;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccount_created_on() {
        return account_created_on;
    }

    public void setAccount_created_on(String account_created_on) {
        this.account_created_on = account_created_on;
    }

    public Integer getFlashcard_delay_setting() {
        return flashcard_delay_setting;
    }

    public void setFlashcard_delay_setting(Integer flashcard_delay_setting) {
        this.flashcard_delay_setting = flashcard_delay_setting;
    }
}
