package com.example.lirecsvfile;

import java.time.LocalDate;
import java.util.Date;

public class client {
    private String nom;
    private String prenom;
    private String dateNaissance;
    private  String profession ;

    public client(String nom, String prenom, String dateNaissance, String profession) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.profession = profession ;
    }

    // Les getters et setters pour les attributs nom, prenom et dateNaissance

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}
