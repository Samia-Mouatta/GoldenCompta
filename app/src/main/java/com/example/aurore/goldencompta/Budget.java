package com.example.aurore.goldencompta;


import java.util.Date;

public class Budget {
    private int id;
    private float montant;
    private String datedeb;
    private String datefin;

    public Budget(){};

    public Budget(float montant){
        this.montant = montant;
        Date ajd = new Date();
        this.datedeb = ajd.toString();
        this.datefin = null;
    }

    public Budget(int id, float montant, String datedeb, String datefin){
        this.id = id;
        this.montant = montant;
        this.datedeb = datedeb;
        this.datefin = datefin;
    }

    /**
     * Méthode qui va retourner le montant du Budget
     * @return réel qui représente le montant du Budget
     */
    public float getMontant() {
        return montant;
    }

    /**
     * Méthode qui va retourner la date de début du Budget
     * @return chaine de caractère qui représente la date de début du Budget
     */
    public String getDateDeb() {
        return datedeb;
    }

    /**
     * Méthode qui va retourner la date de fin du Budget
     * @return chaine de caractère qui représente la date de fin du Budget
     */
    public String getDateFin() {
        return datefin;
    }

    /**
     * Méthode qui va retourner l'identifiant du Budget
     * @return entier qui représente l'identifiant du Budget
     */
    public int getId() {
        return id;
    }

    /**
     * Méthode qui permet de modifier la date de début
     * @param dateDeb chaine de caractère qui représente la nouvelle valeur de la date de début
     */
    public void setDateDeb(String dateDeb) {
        this.datedeb = dateDeb;
    }

    /**
     * Méthode qui permet de modifier la date de fin
     * @param dateFin chaine de caractère qui représente la nouvelle valeur de la date de fin
     */
    public void setDateFin(String dateFin) {
        this.datefin = dateFin;
    }

    /**
     * Méthode qui permet de modifier la valeur de l'identifiant
     * @param id entier qui représente la nouvelle valeur de l'identifiant
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Méthode qui permet de modifier le montant
     * @param montant chaine de caractère qui représente la nouvelle valeur du montant
     */
    public void setMontant(float montant){
        this.montant = montant;
    }

}
