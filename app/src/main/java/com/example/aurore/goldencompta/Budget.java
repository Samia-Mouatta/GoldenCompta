package com.example.aurore.goldencompta;


import java.util.Date;

public class Budget {
    private int id;
    private float montant;
    private String dateDeb;
    private String dateFin;

    public Budget(){};

    public Budget(float montant){
        this.montant = montant;
        Date ajd = new Date();
        this.dateDeb = ajd.toString();
    }

    public float getMontant() {
        return montant;
    }

    public String getDateDeb() {
        return dateDeb;
    }

    public String getDateFin() {
        return dateFin;
    }

    public int getId() {
        return id;
    }

    public void setDateDeb(String dateDeb) {
        this.dateDeb = dateDeb;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMontant(float montant){
        this.montant = montant;
    }

}
