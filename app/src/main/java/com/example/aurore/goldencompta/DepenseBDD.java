package com.example.aurore.goldencompta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * Created by roros on 16/11/2016.
 */

public class DepenseBDD {

    private static final int VERSION_BDD = 3;
    private static final String NOM_BDD = "goldenCompta.db";

    private static final String TABLE_DEPENSE = "table_depense";
    private static final String COL_ID = "id";
    private static final String COL_MONTANT ="montant";
    private static final String COL_DATE = "date";
    private static final String COL_CATEG = "categorie";
    private static final int NUM_COL_ID = 0;
    private static final int NUM_COL_MONTANT = 1;
    private static final int NUM_COL_DATE = 2;
    private static final int NUM_COL_CATEG = 3;


    private String[] allColumns = { COL_ID,
            COL_MONTANT, COL_DATE, COL_CATEG};

    private SQLiteDatabase bdd;

    private MaBaseSQLite maBaseSQLite;

    public DepenseBDD(Context context){
        //On crée la BDD et sa table
        maBaseSQLite = new MaBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        //on ouvre la BDD en écriture
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close(){
        //on ferme l'accès à la BDD
        bdd.close();
    }

    public Cursor populateTable(){

        String[] columns = {maBaseSQLite.COL_CATEG,maBaseSQLite.COL_DATE,maBaseSQLite.COL_MONTANT};
        Cursor cursor = bdd.query(maBaseSQLite.TABLE_DEPENSE, columns, null, null, null, null, null);

        return cursor;
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }



    public Depense insertDepense(Depense dep){
        ContentValues values = new ContentValues();
        //values.put(COL_ID, categ.getId());
        values.put(COL_MONTANT, dep.getMontant());
        values.put(COL_DATE, dep.getDate());
        values.put(COL_CATEG, dep.getCategorie());

        long insertId = bdd.insert(TABLE_DEPENSE, null, values);
        Cursor cursor = bdd.query(TABLE_DEPENSE,
                allColumns, COL_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Depense newDep = cursorToDepense(cursor);
        cursor.close();
        return newDep;
    }

    public int updateDepense(int id, Depense dep){
        //La mise à jour d'une catégorie dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simplement préciser quel catégorie on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();

        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        String s = formatter.format(dep.getDate());
        values.put(COL_DATE, s);

        values.put(COL_MONTANT, dep.getMontant());
        values.put(COL_CATEG, dep.getCategorie());
        return bdd.update(TABLE_DEPENSE, values, COL_ID + " = " +id, null);
    }

    public int removeDepenseWithID(int id){
        //Suppression d'une dépense de la BDD grâce à l'ID
        return bdd.delete(TABLE_DEPENSE, COL_ID + " = " +id, null);
    }

    public Cursor selectDepense(){

//        String[] columns = {maBaseSQLite.COL_CATEG,maBaseSQLite.COL_DATE,maBaseSQLite.COL_MONTANT};
////        Cursor cursor = bdd.query(maBaseSQLite.TABLE_DEPENSE, columns, null, null, null, null, null);
////        return cursor;
//
//        SQLiteDatabase bdd= maBaseSQLite.getReadableDatabase();
////        Cursor c = bdd.rawQuery("select " + "*" + " from " + "table_depense", new String[]{"1"});
////        return c;
//        Cursor cursor = bdd.query(TABLE_DEPENSE,
//                columns, null, null, null, null, COL_ID + " DESC");
//        return cursor;

        String TABLE_NAME = "table_depense";
        this.open();

        return bdd.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY date", null);


    }

    public Cursor selectDepenseByCat(){

        String TABLE_NAME = "table_depense";
        this.open();

        return bdd.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY categorie", null);


    }


    //Cette méthode permet de convertir un cursor en une dépense
    private Depense cursorToDepense(Cursor c){
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Depense dep = new Depense();
        dep.setId(c.getInt(NUM_COL_ID));
        dep.setMontant(c.getFloat(NUM_COL_MONTANT));
        dep.setDate(c.getString(NUM_COL_DATE));
        dep.setCategorie(c.getString(NUM_COL_CATEG));
        c.close();
        return dep;
    }
}
