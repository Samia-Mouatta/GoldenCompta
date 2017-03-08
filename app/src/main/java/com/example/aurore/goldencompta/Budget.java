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
        System.out.println("J'initialise la date d'ajd ! " + this.datedeb);
        this.datefin = null;
    }

    public Budget(int id, float montant, String datedeb, String datefin){
        this.id = id;
        this.montant = montant;
        this.datedeb = datedeb;
        this.datefin = datefin;
    }

    public float getMontant() {
        return montant;
    }

    public String getDateDeb() {
        return datedeb;
    }

    public String getDateFin() {
        return datefin;
    }

    public int getId() {
        return id;
    }

    public void setDateDeb(String dateDeb) {
        this.datedeb = dateDeb;
    }

    public void setDateFin(String dateFin) {
        this.datefin = dateFin;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMontant(float montant){
        this.montant = montant;
    }

}
