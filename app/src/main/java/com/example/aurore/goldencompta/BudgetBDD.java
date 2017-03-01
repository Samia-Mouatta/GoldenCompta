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

/**
 * Created by roros on 16/11/2016.
 */

public class BudgetBDD {

    private static final int VERSION_BDD = 10;
    private static final String NOM_BDD = "goldenCompta.db";

    private static final String TABLE_DEPENSE = "table_budget";
    public static final String COL_ID = "id";
    public static final String COL_MONTANT ="montant";
    public static final String COL_DATE_DEB = "date_fin";
    public static final String COL_DATE_FIN = "date_fin";
    private static final int NUM_COL_ID = 0;
    private static final int NUM_COL_MONTANT = 1;
    private static final int NUM_COL_DATE = 2;
    private static final int NUM_COL_CATEG = 3;


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
     * Méthode retournant toutes les dépenses
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

}
