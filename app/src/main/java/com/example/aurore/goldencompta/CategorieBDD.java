package com.example.aurore.goldencompta;

/**
 * Created by roros on 11/11/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class CategorieBDD {

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "goldenCompta.db";

    private static final String TABLE_CATEGORIE = "table_categorie";
    private static final String COL_ID = "id";
    private static final int NUM_COL_ID = 0;
    private static final String COL_NOM = "nom";
    private static final int NUM_COL_NOM = 1;

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

    public long insertCategorie(Categorie categ){
        ContentValues values = new ContentValues();
        //values.put(COL_ID, categ.getId());
        values.put(COL_NOM, categ.getNom());
        return bdd.insert(TABLE_CATEGORIE, null, values);
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

    //Cette méthode permet de convertir un cursor en une catégirie
    private Categorie cursorToCategorie(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé une catégorie
        Categorie categ = new Categorie(c.getString(NUM_COL_NOM));
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        categ.setId(c.getInt(NUM_COL_ID));
        categ.setNom(c.getString(NUM_COL_NOM));
        //On ferme le cursor
        c.close();

        //On retourne la catégorie
        return categ;
    }
}