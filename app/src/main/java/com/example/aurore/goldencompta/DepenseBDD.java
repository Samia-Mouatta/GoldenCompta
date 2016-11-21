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

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "goldenCompta.db";

    private static final String TABLE_DEPENSE = "table_depense";
    private static final String COL_ID = "id";
    private static final String COL_MONTANT ="montant";
    private static final String COL_DATE = "date";
    private static final String COL_CATEG = "categorie";

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


    public long insertDepense(Depense dep){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_ID, dep.getId());

        //Passage de la date en chaine de caractères
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        String s = formatter.format(dep.getDate());
        values.put(COL_DATE, s);
        values.put(COL_MONTANT, dep.getMontant());

        values.put(COL_CATEG, dep.getCategorie().getNom());
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_DEPENSE, null, values);
    }

    public int updateDepense(int id, Depense dep){
        //La mise à jour d'une catégorie dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simplement préciser quel catégorie on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();

        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        String s = formatter.format(dep.getDate());
        values.put(COL_DATE, s);

        values.put(COL_MONTANT, dep.getMontant());
        values.put(COL_CATEG, dep.getCategorie().getNom());
        return bdd.update(TABLE_DEPENSE, values, COL_ID + " = " +id, null);
    }

    public int removeDepenseWithID(int id){
        //Suppression d'une dépense de la BDD grâce à l'ID
        return bdd.delete(TABLE_DEPENSE, COL_ID + " = " +id, null);
    }
}
