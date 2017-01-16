package com.example.aurore.goldencompta;

/**
 * Created by roros on 11/11/2016.
 */

public class Categorie {
    private int id;
    private String nom;

    /**
     * Constructeur qui crée une Catégorie vide
     */
    public Categorie(){};

    /**
     * Méthode qui crée une Catégorie en initialisant l'attribut nom
     * @param nom Chaine de caractère qui va être la valeur de l'attribut nom
     */
    public Categorie(String nom){
        this.nom=nom;
    }

    /**
     * Méthode qui va retourner l''identifiant de la Catégorie
     * @return entier qui représente l'identifaint de la Categorie
     */
    public int getId(){
        return id;
    }

    /**
     * Méthode qui permet de modifier la valeur de l'identifiant
     * @param id entier qui représente la nouvelle valeur de l'identifiant
     */
    public void setId(int id){
        this.id=id;
    }

    /**
     * Méthode qui retourne le nom de l'identifiant
     * @return Chaine de caractères qui représente le nom de la Catégorie
     */
    public String getNom() {
        return nom;
    }

    /**
     * Méthode qui permet de modifier le nom de la Catégorie
     * @param nom Chaine de caractères qui représente le nouveau nom de la Catégorie
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Méthode qui transforme une Catégorie en chaine de caractères
     * @return Chaine de caractère symbolisant une Catégorie
     */
    public String toString(){
        return "Id : "+id+"\nNom : "+nom+"\n";
    }
}

