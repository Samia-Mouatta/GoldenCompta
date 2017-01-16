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

import static android.content.ContentValues.TAG;

/**
 * Created by roros on 16/11/2016.
 */

public class DepenseBDD {

    private static final int VERSION_BDD = 10;
    private static final String NOM_BDD = "goldenCompta.db";

    private static final String TABLE_DEPENSE = "table_depense";
    public static final String COL_ID = "id";
    public static final String COL_MONTANT ="montant";
    public static final String COL_DATE = "date";
    public static final String COL_CATEG = "categorie";
    private static final int NUM_COL_ID = 0;
    private static final int NUM_COL_MONTANT = 1;
    private static final int NUM_COL_DATE = 2;
    private static final int NUM_COL_CATEG = 3;


    private String[] allColumns = { COL_ID,
            COL_MONTANT, COL_DATE, COL_CATEG};

    private SQLiteDatabase bdd;

    private MaBaseSQLite maBaseSQLite;

    /**
     * Méthode d'initialisation de la base de données
     * @param context
     */
    public DepenseBDD(Context context){
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
        Cursor cursor = bdd.query(maBaseSQLite.TABLE_DEPENSE, columns, null, null, null, null, null);

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
     * Méthode d'insertion d'une dépense
     * @param dep la dépense à ajouter
     * @return la dépense qui a bien été ajoutée
     */
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

    /**
     * Méthode de mise a jour d'une dépense
     * @param id id de la dépense
     * @param dep dépense à modifier
     * @return la dépense modifiée
     */
    public int updateDepense(int id, Depense dep){
        //La mise à jour d'une dépense dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simplement préciser quel dépense on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();

        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        String s = formatter.format(dep.getDate());
        values.put(COL_DATE, s);

        values.put(COL_MONTANT, dep.getMontant());
        values.put(COL_CATEG, dep.getCategorie());
        return bdd.update(TABLE_DEPENSE, values, COL_ID + " = " +id, null);
    }

    /**
     * Méthode de suppression d'une dépense
     * @param id id de la dépense
     * @return la dépense supprimée
     */
    public int removeDepenseWithID(int id){
        //Suppression d'une dépense de la BDD grâce à l'ID
        return bdd.delete(TABLE_DEPENSE, COL_ID + " = " +id, null);
    }

    /**
     * Méthode retournant les dépenses classée par catégorie
     * @param inputText
     * @return curseur de dépense
     * @throws SQLException
     */
    public Cursor fetchDepensesByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = bdd.query(maBaseSQLite.TABLE_DEPENSE, new String[] {COL_CATEG,
                            COL_DATE, COL_MONTANT},
                    null, null, null, null, null);

        }
        else {
            mCursor = bdd.query(true, maBaseSQLite.TABLE_DEPENSE, new String[] {COL_CATEG,
                            COL_DATE, COL_MONTANT},
                    COL_CATEG + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Méthode retournant toutes les dépenses classées par date
     * @return curseur de dépense
     */
    public Cursor selectDepense(){

        String TABLE_NAME = "table_depense";
        this.open();

        return bdd.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY date", null);


    }

    /**
     * Méthode retournant les dépenses classées par catégorie
     * @return curseur de dépense
     */
    public Cursor selectDepenseByCat(){

        String TABLE_NAME = "table_depense";
        this.open();

        return bdd.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY categorie", null);


    }

    /**
     * Méthode retournant les dépenses efféctuées entre une fourchette de montant
     * @param min borne minimale du montnant
     * @param max borne maximale du montnant
     * @return curseur de dépense
     */
    public Cursor selectDepenseByMontant(String min, String max){

        String TABLE_NAME = "table_depense";
        this.open();
        String querry = "SELECT SUM(montant) FROM table_depense WHERE montant BETWEEN '"+ min +"' AND '"+ max +"'";
        //String querry = "SELECT COUNT(id) FROM table_depense WHERE montant BETWEEN '"+ min +"' AND '"+ max +"'";
        return bdd.rawQuery(querry, null);


    }

    /**
     * Méthode retournant le total du montant des dépenses pour chaque mois
     * @return curseur de dépense
     */
    public Cursor selectDepenseMois(){

        String TABLE_NAME = "table_depense";
        this.open();

        return bdd.rawQuery("SELECT montant,strftime('%m',date) as Mois FROM " + TABLE_NAME +  "", null);
    }

    /**
     * Méthode retournant le montant des dépense d'un mois
     * @param month mois donné
     * @return curseur de dépense
     */
    public Cursor selectDepenseOneMois(String month){

        String TABLE_NAME = "table_depense";
        this.open();
        return bdd.rawQuery("SELECT montant, strftime('%m',date) as Mois FROM table_depense WHERE strftime('%m', date)='"+ month +"'", null);

    }

    /**
     * méthode permetant de convertir un cursor en une dépense
     * @param c curseur d'entré
     * @return dépense de sortie
     */
    private static final Depense cursorToDepense(Cursor c){

        if (c.getCount() == 0)
            return null;

        Depense dep = new Depense();
        dep.setId(c.getInt(NUM_COL_ID));
        dep.setMontant(c.getFloat(NUM_COL_MONTANT));
        dep.setDate(c.getString(NUM_COL_DATE));
        dep.setCategorie(c.getString(NUM_COL_CATEG));
        return dep;
    }

    /**
     * Méthode permettant d'obtenir toutes les dépenses en array de string
     * @return array de string
     */
    public ArrayList<String> getAllDepense(){

        ArrayList<String> listeDepense = new ArrayList<String>();
        String ligne;
        Depense d;
        Cursor cursor = selectDepense();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            d = DepenseBDD.cursorToDepense(cursor);
            ligne = "Montant : " + d.getMontant() + "\nDate : " + d.getDate() +  "\nCategorie : " + d.getCategorie();
            listeDepense.add(ligne);
            cursor.moveToNext();
        }

        // Make sure to close the cursor
        cursor.close();
        return listeDepense;

    }
}
