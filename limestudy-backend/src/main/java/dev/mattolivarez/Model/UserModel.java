package dev.mattolivarez.Model;


import java.util.Date;

public class UserModel
{

    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String account_created_on;

    public UserModel(){}

    public UserModel(Integer userId, String firstName, String lastName, String email, String account_created_on) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.account_created_on = account_created_on;
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
}
