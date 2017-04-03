package com.example.aurore.goldencompta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

public class BudgetBDD {

    private static final int VERSION_BDD = 14;
    private static final String NOM_BDD = "goldenCompta.db";

    private static final String TABLE_BUDGET = "table_budget";
    public static final String COL_ID = "id";
    public static final String COL_MONTANT ="montant";
    public static final String COL_DATE_DEB = "datedeb";
    public static final String COL_DATE_FIN = "datefin";
    private static final int NUM_COL_ID = 0;
    private static final int NUM_COL_MONTANT = 1;
    private static final int NUM_COL_DATE_DEB = 2;
    private static final int NUM_COL_DATE_FIN = 3;


    private String[] allColumns = { COL_ID,
            COL_MONTANT, COL_DATE_DEB, COL_DATE_FIN};

    private SQLiteDatabase bdd;

    private MaBaseSQLite maBaseSQLite;

    /**
     * Méthode d'initialisation de la base de données
     * @param context
     */
    public BudgetBDD(Context context){
        //On crée la BDD et sa table
        maBaseSQLite = new MaBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
    }

    /**
     * Méthode pour ouvrir l'accès a la base
     */
    public void open(){
        //on ouvre la BDD en écriture
        bdd = maBaseSQLite.getWritableDatabase();
    }

    /**
     * Méthode pour fermer l'accès à la base
     */
    public void close(){
        //on ferme l'accès à la BDD
        bdd.close();
    }

    /**
     * Méthode retournant toutes les budgets
     * @return
     */
    public Cursor populateTable(){

        String[] columns = {maBaseSQLite.COL_CATEG,maBaseSQLite.COL_DATE,maBaseSQLite.COL_MONTANT};
        Cursor cursor = bdd.query(maBaseSQLite.TABLE_BUDGET, columns, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    /**
     * Méthode retournant toute le base
     * @return
     */
    public SQLiteDatabase getBDD(){
        return bdd;
    }


    /**
     * Méthode d'insertion d'un budget
     * @param budget le budget à ajouter
     */
    public void insertBudget(Budget budget){
        ContentValues values = new ContentValues();
        values.put(COL_MONTANT, budget.getMontant());
        Date ajd = new Date();
        values.put(COL_DATE_DEB, ajd.toString());

        long insertId = bdd.insert(TABLE_BUDGET, null, values);
        bdd.query(TABLE_BUDGET,
                allColumns, COL_ID + " = " + insertId, null,
                null, null, null);

    }


    /**
     * méthode permetant de convertir un cursor en une dépense
     * @param c curseur d'entré
     * @return dépense de sortie
     */
    public static final Budget cursorToBudget(Cursor c){
        if (c.getCount() == 0)
            return null;

        return new Budget(c.getInt(NUM_COL_ID), c.getFloat(NUM_COL_MONTANT), c.getString(NUM_COL_DATE_DEB), c.getString(NUM_COL_DATE_FIN));
    }

    /**
     * Méthode retournant toutes les budget classées par date
     * @return curseur de budget
     */
    public Cursor selectBudget(){
        this.open();
        return bdd.rawQuery("SELECT * FROM "+TABLE_BUDGET+" ORDER BY "+COL_DATE_DEB+"", null);
    }

    /**
     * Méthode retournant le dernier budget enregistré
     * @return curseur de budget
     */
    public Cursor gettLastBudget(){
        this.open();
        return bdd.rawQuery("SELECT * FROM "+TABLE_BUDGET, null);
    }

    public Budget selectLastBudget(){
        Cursor cursor = selectBudget();
        cursor.moveToLast();

        return BudgetBDD.cursorToBudget(cursor);
    }

    /**
     * Méthode permettant d'obtenir toutes les budgets en array de string
     * @return array de string
     */
    public ArrayList<String> getAllBudget(){
        ArrayList<String> listeBudget = new ArrayList<String>();
        String ligne;
        Budget d;
        Cursor cursor = selectBudget();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            d = BudgetBDD.cursorToBudget(cursor);
            ligne = d.getId() + " - Montant : " + d.getMontant() + "\nDate Debut: " + d.getDateDeb() +  "\nDate Fin : " + d.getDateFin();
            listeBudget.add(ligne);
            cursor.moveToNext();
        }
        cursor.close();
        return listeBudget;
    }


    /**
     * Méthode qui retourne tous les budgets
     * @return un tableau contenant tous les budgets
     */
    public ArrayList<Budget> getAllBudgets(){
        ArrayList<Budget> listeBudget = new ArrayList<>();
        Cursor cursor = selectBudget();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            listeBudget.add(BudgetBDD.cursorToBudget(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return listeBudget;
    }

    /**
     * Méthode de mise a jour d'une dépense
     * @param id id de la dépense
     * @param dep dépense à modifier
     * @return la dépense modifiée
     */
    public int updateBudget(int id, Budget dep){
        ContentValues values = new ContentValues();

        values.put(COL_MONTANT, dep.getMontant());
        values.put(COL_DATE_DEB, dep.getDateDeb());
        values.put(COL_DATE_FIN, dep.getDateFin());

        return bdd.update(TABLE_BUDGET, values, COL_ID + " = " +id, null);
    }

    /**
     * Méthode qui retourne la montant associé à un budget pendant une période donnée
     * @param mois_deb mois du début du budget
     * @param annee_deb année du début du budget
     * @param mois_fin mois du fin du budget
     * @param annee_fin année du début du budget
     * @return le montant associé au buget trouvé
     */
    public float selectBudgetBetweenMonth(int mois_deb, int annee_deb, int mois_fin, int annee_fin) {
        float total, value;
        total = 0;
        int i;
        String [] date;
        ArrayList<String> month = new ArrayList<String>();
        month.add("Jan");
        month.add("Feb");
        month.add("Mar");
        month.add("Apr");
        month.add("May");
        month.add("Jun");
        month.add("Jul");
        month.add("Aug");
        month.add("Pet");
        month.add("Oct");
        month.add("Nov");
        month.add("Dec");


        int y, m, mois, annee, jour;
        mois = 0;
        String leMois;
        Cursor c = selectBudget();
        c.moveToFirst();
        value = 0;

        for (y = annee_deb; y < annee_fin; y++) {


            if (y ==  annee_deb) {
                for (m = mois_deb; m < 12; m++) {
                    while (!c.isAfterLast()) {
                        date = c.getString(2).split(" ");
                        leMois = date[1];
                        for (i = 0; i<12; i++)   {
                            if (leMois == month.get(i)) {
                                mois = i+1;
                            }
                        }
                        annee = Integer.parseInt(date[5]);
                        jour = Integer.parseInt(date[2]);
                        if (mois == m && annee == y) {
                            value = c.getInt(1);
                        }
                        c.moveToNext();
                    }
                    total = total + value;
                }
            }

            else if (y == annee_fin) {

                for (m = 1; m < mois_fin; m++) {
                    while (!c.isAfterLast()) {
                        date = c.getString(2).split(" ");
                        leMois = date[1];
                        for (i = 0; i<12; i++)   {
                            if (leMois == month.get(i)) {
                                mois = i+1;
                            }
                        }
                        annee = Integer.parseInt(date[5]);
                        jour = Integer.parseInt(date[2]);
                        if (mois == m && annee == y) {
                            value = c.getInt(1);
                        }
                        c.moveToNext();
                    }
                    total = total + value;
                }

            } else {
                for (m = 1; m < 12; m++) {
                    while (!c.isAfterLast()) {
                        date = c.getString(2).split(" ");
                        leMois = date[1];
                        for (i = 0; i<12; i++)   {
                            if (leMois == month.get(i)) {
                                mois = i+1;
                            }
                        }
                        annee = Integer.parseInt(date[5]);
                        jour = Integer.parseInt(date[2]);
                        if (mois == m && annee == y) {
                            value = c.getInt(1);
                        }
                        c.moveToNext();
                    }
                    total = total + value;
                }

            }


        }

        return total;
    }

}