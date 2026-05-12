package com.uniride.app.model;

public class User {
    private String uid;
    private String name;
    private String email;
    private String studentId;   // matrícula
    private String photoUrl;
    private String university;

    public User() {} // Requerido por Firestore

    public User(String uid, String name, String email,
                String studentId, String university) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.studentId = studentId;
        this.university = university;
    }

    // Getters y Setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }
}