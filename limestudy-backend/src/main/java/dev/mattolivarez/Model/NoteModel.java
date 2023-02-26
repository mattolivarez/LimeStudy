package dev.mattolivarez.Model;

public class NoteModel
{
    private Integer note_id;
    private Integer class_id;
    private Integer user_id;
    private String note_name;
    private String note_body;
    private Long note_created_on;

    public NoteModel() {};
    public NoteModel(Integer note_id, Integer class_id, Integer user_id, String note_name, String note_body, Long note_created_on) {
        this.note_id = note_id;
        this.class_id = class_id;
        this.user_id = user_id;
        this.note_name = note_name;
        this.note_body = note_body;
        this.note_created_on = note_created_on;
    }

    public Integer getNote_id() {
        return note_id;
    }

    public void setNote_id(Integer note_id) {
        this.note_id = note_id;
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

    public String getNote_name() {
        return note_name;
    }

    public void setNote_name(String note_name) {
        this.note_name = note_name;
    }

    public String getNote_body() {
        return note_body;
    }

    public void setNote_body(String note_body) {
        this.note_body = note_body;
    }

    public Long getNote_created_on() {
        return note_created_on;
    }

    public void setNote_created_on(Long note_created_on) {
        this.note_created_on = note_created_on;
    }
}
