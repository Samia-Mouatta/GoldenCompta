package com.example.aurore.goldencompta;

/**
 * Created by roros on 16/11/2016.
 */

public class Depense {
    int id;
    String dateDepense;
    float montant;
    String categ;

    public Depense(){};

    public Depense(String dateDepense, float montant, String categ){
        this.dateDepense=dateDepense;
        this.montant=montant;
        this.categ=categ;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public String getDate() {
        return dateDepense;
    }

    public void setDate(String dateDepense) {
        this.dateDepense = dateDepense;
    }

    public float getMontant(){return montant;}

    public void setMontant(float montant){this.montant=montant;}

    public String getCategorie(){return categ;}

    public void setCategorie(String categ){this.categ=categ;}

    public String toString(){
        return "Id : "+id+"\nDate dépense : "+dateDepense+"\nMontant : "+montant+"\n";
    }
}
