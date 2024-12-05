package com.example.tp7.Taches;

import java.io.Serializable;

public class Tache implements Serializable {
    private int id;
    private String nom;
    private int h;
    private int m;
    private String jour;

    public Tache(String nom, int h, String jour, int m) {
        this.nom = nom;
        this.h = h;
        this.jour = jour;
        this.m = m;
    }

    public Tache(int id, String nom, int h, int m, String jour) {
        this.id = id;
        this.nom = nom;
        this.h = h;
        this.m = m;
        this.jour = jour;
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

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    @Override
    public String toString() {
        return "Tache{" +
                "nom= '" + nom + " "+"heure " +
                h +":"+
                m +
                ", jour : '" + jour + '\'' +
                '}'+"\n";
    }
}