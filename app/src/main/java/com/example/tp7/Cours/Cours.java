package com.example.tp7.Cours;

public class Cours {
    private int id;
    private String nom;
    private Float nbrh;
    private String type;
    private int idTeacher;

    public Cours() {
    }

    public Cours(String nom, Float nbrh, String type, int idTeacher) {
        this.nom = nom;
        this.nbrh = nbrh;
        this.type = type;
        this.idTeacher = idTeacher;
    }

    public Cours(int id, String nom, Float nbrh, String type, int idTeacher) {
        this.id = id;
        this.nom = nom;
        this.nbrh = nbrh;
        this.type = type;
        this.idTeacher = idTeacher;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Float getNbrh() {
        return nbrh;
    }

    public void setNbrh(Float nbrh) {
        this.nbrh = nbrh;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(int idTeacher) {
        this.idTeacher = idTeacher;
    }
}
