package com.example.aurore.goldencompta;

/**
 * Created by roros on 11/11/2016.
 */

public class Categorie {
    private int id;
    private String nom;

    public Categorie(String nom){
        this.id=1; this.nom=nom;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String toString(){
        return "Id : "+id+"\nNom : "+nom+"\n";
    }
}

