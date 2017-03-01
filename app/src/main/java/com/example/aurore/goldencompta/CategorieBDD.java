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

    private static final int VERSION_BDD = 10;
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

    /**
     * Méthode pour initialiser la table de la base de donnée
     * @param context
     */
    public CategorieBDD(Context context){
        //On crée la BDD et sa table
        maBaseSQLite = new MaBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
    }

    /**
     * Méthode pour ouvrir l'accès a la table
     */
    public void open(){
        //on ouvre la BDD en écriture
        bdd = maBaseSQLite.getWritableDatabase();
    }

    /**
     * Méthode pour fermer l'accés à la table
     */
    public void close(){
        bdd.close();
    }

    /**
     * Méthode qui retourne la table de la base de donnée
     * @return la table CATEGORIE de la base de donnée
     */
    public SQLiteDatabase getBDD(){
        return bdd;
    }

    /**
     * Méthode pour inserer une cataegorie dans une base de donnée
     * @param categ la catégorie a ajouter
     * @return la categorie qui a été ajouter dans la table
     */
    public Categorie insertCategorie(Categorie categ){
        ContentValues values = new ContentValues();
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

    /**
     * Méthode qui met a jour une catégorie présente dans la table
     * @param id l'identifiant de la catégorie a modifer
     * @param categ la catégorie qui va remplacer l'ancienne
     * @return la catégorie modifié
     */
    public int updateCategorie(int id, Categorie categ){
        //La mise à jour d'une catégorie dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simplement préciser quel catégorie on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_NOM, categ.getNom());
        return bdd.update(TABLE_CATEGORIE, values, COL_ID + " = " +id, null);
    }

    public int getIdCategorie(String Cat)  {
        Cursor c;
        int id = 0;
        //        colNom, null, "nom = ", null, null, COL_NOM);
        String Querry = "SELECT * from table_categorie WHERE nom = '" + Cat +"'";
        c = bdd.rawQuery(Querry, null);
        if (c.getCount() != 0) {
            c.moveToFirst();
            id = c.getInt(0);
        }
        return id;

    }

    /**
     * Méthode pour supprimer une catégorie selon son identifiant
     * @param id l'identifiant de la catégorie a supprimer
     * @return le nombre de ligne supprimé
     */
    public boolean removeCategorieWithID(int id){
        //Suppression d'une catégorie de la BDD grâce à l'ID
        return bdd.delete(TABLE_CATEGORIE, COL_ID + " = " +id, null) > 0;
    }

    /**
     * Méthode qui retourne une catégorie selon son nom
     * @param nom le nom de la catégorie recherché
     * @return la catégorie recherché dans la table
     */
    public Categorie getCategorieWithNom(String nom){
        //Récupère dans un Cursor les valeurs correspondant à une catégorie contenue dans la BDD (ici on sélectionne la catégorie grâce à son nom)
        Cursor c = bdd.query(TABLE_CATEGORIE, new String[] {COL_ID, COL_NOM}, COL_NOM + " LIKE \"" + nom +"\"", null, null, null, null);
        return cursorToCategorie(c);
    }

    /**
     * Méthode qui retourne toutes les catégorie présentes dans la table
     * @return un tableau de Catégorie
     */
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

    /**
     * Méthode qui retourne tous les nom des catégories présentes dans la table
     * @return un tableau de chaine de caractères
     */
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

    /**
     * Méthode qui recupère le résultat d'un requête
     * @return Cursor contenant le résultat de la requête
     */
    public Cursor categorieDepense(){

        String TABLE_NAME = "table_depense";
        String MY_QUERY = "SELECT c.nom, SUM(d.montant) AS total FROM TABLE_CATEGORIE c JOIN TABLE_DEPENSE d ON d.categorie=c.nom GROUP BY c.nom";
        this.open();
        return bdd.rawQuery(MY_QUERY, null);
    }

    /**
     * Méthode qui permet de convertir un Cursor en Catégorie
     * @param c Le cursor a convertir
     * @return La Catégorie associé au cursor
     */
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