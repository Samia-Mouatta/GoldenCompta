package com.example.aurore.goldencompta;

/**
 * Created by roros on 11/11/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CategorieBDD {

    private static final int VERSION_BDD = 4;
    private static final String NOM_BDD = "goldenCompta.db";

    private static final String TABLE_CATEGORIE = "table_categorie";
    private static final String COL_ID = "id";
    private static final int NUM_COL_ID = 0;
    private static final String COL_NOM = "nom";
    private static final int NUM_COL_NOM = 1;

    private String[] allColumns = { COL_ID,
            COL_NOM};

    private String[] colNom = { COL_NOM};

    private SQLiteDatabase bdd;

    private MaBaseSQLite maBaseSQLite;

    public CategorieBDD(Context context){
        //On crée la BDD et sa table
        maBaseSQLite = new MaBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        //on ouvre la BDD en écriture
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close(){
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public Categorie insertCategorie(Categorie categ){
        ContentValues values = new ContentValues();
        //values.put(COL_ID, categ.getId());
        values.put(COL_NOM, categ.getNom());

        long insertId = bdd.insert(TABLE_CATEGORIE, null, values);
        Cursor cursor = bdd.query(TABLE_CATEGORIE,
                allColumns, COL_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Categorie newCat = cursorToCategorie(cursor);
        cursor.close();
        return newCat;
    }

    public int updateCategorie(int id, Categorie categ){
        //La mise à jour d'une catégorie dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simplement préciser quel catégorie on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_NOM, categ.getNom());
        return bdd.update(TABLE_CATEGORIE, values, COL_ID + " = " +id, null);
    }

    public int removeCategorieWithID(int id){
        //Suppression d'une catégorie de la BDD grâce à l'ID
        return bdd.delete(TABLE_CATEGORIE, COL_ID + " = " +id, null);
    }

    public Categorie getCategorieWithNom(String nom){
        //Récupère dans un Cursor les valeurs correspondant à une catégorie contenue dans la BDD (ici on sélectionne la catégorie grâce à son nom)
        Cursor c = bdd.query(TABLE_CATEGORIE, new String[] {COL_ID, COL_NOM}, COL_NOM + " LIKE \"" + nom +"\"", null, null, null, null);
        return cursorToCategorie(c);
    }


    public List<Categorie> getAllCategories() {
        List<Categorie> categories = new ArrayList<Categorie>();
        Cursor cursor = bdd.query(TABLE_CATEGORIE,
                allColumns, null, null, null, null, COL_ID + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Categorie categorie = cursorToCategorie(cursor);
            categories.add(categorie);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return categories;
    }

    public List<String> getAllCategoriesName(){
        List<String> listeCategories = new ArrayList<String>();
        String nomCategorie = " " ;
        Cursor cursor = bdd.query(TABLE_CATEGORIE,
                colNom, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            nomCategorie = cursor.getString(0);
            listeCategories.add(nomCategorie);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return listeCategories;

    }

    //Cette méthode permet de convertir un cursor en une catégorie
    private Categorie cursorToCategorie(Cursor c){
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Categorie categ = new Categorie();
        categ.setId(c.getInt(NUM_COL_ID));
        categ.setNom(c.getString(NUM_COL_NOM));
        c.close();
        return categ;
    }

}