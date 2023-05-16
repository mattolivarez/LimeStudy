/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Backend
Contains Model for Class Entity
*/

package dev.mattolivarez.Model;


public class ClassModel
{
    private Integer classId;
    private Integer userId;
    private String class_name;
    private String class_created_on;
    //private Integer number_of_decks;
    //private Double class_accuracy_percentage;
    //private String class_color_code;

    public ClassModel() {}

    public ClassModel(Integer classId, Integer userId, String class_name, String class_created_on) {
        this.classId = classId;
        this.userId = userId;
        this.class_name = class_name;
        this.class_created_on = class_created_on;
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

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClass_created_on() {
        return class_created_on;
    }

    public void setClass_created_on(String class_created_on) {
        this.class_created_on = class_created_on;
    }
}
