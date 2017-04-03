package com.example.aurore.goldencompta;

import java.util.Date;

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

    /**
     * Méthode qui va retourner l'identifiant de la Dépense
     * @return entier qui représente l'identifaint de la Dépense
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
     * Méthode qui va retourner la date de la Dépense
     * @return Chaine de caractère qui représente la date de la Dépense
     */
    public String getDate() {
        return dateDepense;
    }

    /**
     * Méthode qui va retourner la date de la Dépense
     * @return Date qui représente la date de la Dépense
     */
    public Date getDateD(){
        String[] date = dateDepense.split("/");
        return  new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[1]));
    }

    /**
     * Méthode qui permet de modifier la valeur de la date de la Dépense
     * @param dateDepense Chaine de caractère qui représente la nouvelle valeur de la date de dépense
     */
    public void setDate(String dateDepense) {
        this.dateDepense = dateDepense;
    }

    /**
     * Méthode qui va retourner le montant de la Dépense
     * @return Réel qui représente le montant de la Dépense
     */
    public float getMontant(){return montant;}

    /**
     * Méthode qui permet de modifier la valeur du montant de la Dépense
     * @param montant Reel qui représente la nouvelle valeur du montant de la dépense
     */
    public void setMontant(float montant){this.montant=montant;}

    /**
     * Méthode qui va retourner la catégorie de la Dépense
     * @return Chaine de carctère  qui représente le montant de la Dépense
     */
    public String getCategorie(){return categ;}

    /**
     * Méthode qui permet de modifier le nom de la catégorie de la Dépense
     * @param categ chaine de caractère qui représente la nouvelle catégorie de la dépense
     */
    public void setCategorie(String categ){this.categ=categ;}

    /**
     * Méthode qui retourne la dépense sous la forme d'une chaine de carctère
     * @return une chaine de caratère représentent une dépense
     */
    public String toString(){
        return "Montant : "+ montant +"\nDate : "+dateDepense+"\nCatégorie : "+categ;
    }

    /**
     * Méthode pour comparer deux dépenses
     * @param obj la dépense a comparé
     * @return un booleen symbolisant l'égalité de la dépense
     */
    public boolean equals(Object obj) {
        return  (obj instanceof Depense) &&
                (((Depense)obj).getMontant() == this.montant) &&
                ((Depense)obj).getCategorie().equals(categ) &&
                ((Depense)obj).getDate().equals(dateDepense);
    }
}
