package com.example.aurore.goldencompta;

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
        return "Montant : "+ montant +"\nDate : "+dateDepense+"\nCatégorie : "+categ;
    }

    public boolean equals(Object obj) {
        return  (obj instanceof Depense) &&
                (((Depense)obj).getMontant() == this.montant) &&
                ((Depense)obj).getCategorie().equals(categ) &&
                ((Depense)obj).getDate().equals(dateDepense);
    }
}
