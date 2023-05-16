/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Backend
Contains Model for Note Entity
*/

package dev.mattolivarez.Model;

public class NoteModel
{
    private Integer noteId;
    private Integer classId;
    private Integer userId;
    private String note_name;
    private String note_body;
    private String note_created_on;

    public NoteModel() {};
    public NoteModel(Integer noteId, Integer userId, String note_name, String note_body, String note_created_on)
    {
        this(noteId, null, userId, note_name, note_body, note_created_on);
    }
    public NoteModel(Integer noteId, Integer classId, Integer userId, String note_name, String note_body, String note_created_on) {
        this.noteId = noteId;
        this.classId = classId;
        this.userId = userId;
        this.note_name = note_name;
        this.note_body = note_body;
        this.note_created_on = note_created_on;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer note_id) {
        this.noteId = noteId;
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

    public String getNote_created_on() {
        return note_created_on;
    }

    public void setNote_created_on(String note_created_on) {
        this.note_created_on = note_created_on;
    }
}
