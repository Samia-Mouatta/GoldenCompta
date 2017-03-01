package com.example.aurore.goldencompta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.content.ContentValues.TAG;

public class BudgetBDD {

    private static final int VERSION_BDD = 10;
    private static final String NOM_BDD = "goldenCompta.db";

    private static final String TABLE_DEPENSE = "table_budget";
    public static final String COL_ID = "id";
    public static final String COL_MONTANT ="montant";
    public static final String COL_DATE_DEB = "dateDeb";
    public static final String COL_DATE_FIN = "dateFin";
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
    public Budget insert(Budget budget){
        ContentValues values = new ContentValues();
        values.put(COL_MONTANT, budget.getMontant());
        values.put(COL_DATE_DEB, budget.getDateDeb().toString());

        long insertId = bdd.insert(TABLE_DEPENSE, null, values);
        Cursor cursor = bdd.query(TABLE_DEPENSE,
                allColumns, COL_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Budget Newbudget = cursorToBudget(cursor);
        cursor.close();
        return Newbudget;
    }


    /**
     * méthode permetant de convertir un cursor en une dépense
     * @param c curseur d'entré
     * @return dépense de sortie
     */
    public static final Budget cursorToBudget(Cursor c){

        if (c.getCount() == 0)
            return null;

        Budget dep = new Budget();
        dep.setId(c.getInt(NUM_COL_ID));
        dep.setMontant(c.getFloat(NUM_COL_MONTANT));
        dep.setDateDeb(c.getString(NUM_COL_DATE_DEB));
        return dep;
    }

}
