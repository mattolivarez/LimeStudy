package dev.mattolivarez.Model;

public class ClassModel
{
    private Integer classId;
    private Integer userId;
    private String class_name;
    private Long class_created_on;
    //private Integer number_of_decks;
    //private String class_color_code;

    public ClassModel() {}

    public ClassModel(Integer classId, Integer userId, String class_name, Long class_created_on) {
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

    public Long getClass_created_on() {
        return class_created_on;
    }

    public void setClass_created_on(Long class_created_on) {
        this.class_created_on = class_created_on;
    }
}
