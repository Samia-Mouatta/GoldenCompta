package com.example.aurore.goldencompta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.content.ContentValues.TAG;

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

        values.put(COL_DATE, dep.getDate());
        values.put(COL_MONTANT, dep.getMontant());
        values.put(COL_CATEG, dep.getCategorie());
        return bdd.update(TABLE_DEPENSE, values, COL_ID + " = " +id, null);
    }

    /**
     * Méthode de suppression d'une dépense
     * @param id id de la dépense
     * @return la dépense supprimée
     */
    public void removeDepenseWithID(Integer id){
        //Suppression d'une dépense de la BDD grâce à l'ID
        System.out.println("Suppression du numéro : " + id);
        bdd.delete(TABLE_DEPENSE, COL_ID + " = " +id, null);
    }

    public void removeDepenseWithInfo(String montant, String date, String cat){
        //Suppression d'une dépense de la BDD grâce à l'ID
        bdd.delete(TABLE_DEPENSE, COL_MONTANT + " = " +montant+ "and" + COL_DATE + "=" + date + "and" + COL_CATEG + " = " + cat, null);
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
        return bdd.rawQuery(querry, null);
    }

    public Cursor getDepenseByID(Integer id){

        String TABLE_NAME = "table_depense";
        this.open();
        String querry = "SELECT * FROM table_depense WHERE id = '"+ id +"'";
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

    public ArrayList<String> selectAllDepensesAnnee1 (Integer annee)   {
        Cursor c = selectDepense();
        Cursor result;

        int i, year, month, mois;
        float totalDepense;
        String date;
        Depense d;
        ArrayList<String> res = new ArrayList<String>();
        ArrayList<String> tmp = new ArrayList<String>();


            for (i = 0; i<12; i++) {
                totalDepense = 0;
                if (c.getCount()!=0)    {
                    c.moveToFirst();
                    totalDepense = 0;
                    while (!c.isAfterLast()) {
                        d = cursorToDepense(c);
                        date = d.getDate();
                        year = Integer.parseInt(date.substring(6,10));
                        month = Integer.parseInt(date.substring(3,5));

                        if (year == annee && month == i) {
                            Log.i(TAG, "mois " + i + " - "+ month + " annee " + year + " - " + annee + " - " + d.getMontant());
                            totalDepense = totalDepense + d.getMontant();

                        }
                        c.moveToNext();
                    }

            }
                Log.i(TAG, "total mois de " + i + " - " + Float.toString(totalDepense));

            res.add(Float.toString(totalDepense));
        }


        return res;
    }

    public float getTotalDepenseMois ()   {
        Cursor c = selectDepense();
        Cursor result;
        Date dateToday = new Date();
        Calendar myCalendar = GregorianCalendar.getInstance();
        myCalendar.setTime(dateToday);
        int annee = myCalendar.get(Calendar.YEAR);
        int mois = myCalendar.get(Calendar.MONTH)+1;

        int i, year, month;
        float totalDepense;
        String date;
        Depense d;


        totalDepense = 0;
        if (c.getCount()!=0)    {
            c.moveToFirst();

            while (!c.isAfterLast()) {
                d = cursorToDepense(c);
                date = d.getDate();
                year = Integer.parseInt(date.substring(6,10));
                month = Integer.parseInt(date.substring(3,5));

                if (year == annee && month == mois) {
                    totalDepense = totalDepense + d.getMontant();
                }
                c.moveToNext();
            }

        }

        return totalDepense;
    }


    public Cursor selectAllDepensesAnnee(String year) {

        String TABLE_NAME = "table_depense";
        this.open();
        int i;
        Cursor c = bdd.rawQuery("SELECT montant,strftime('%m',date) as Mois FROM " + TABLE_NAME +  " WHERE strftime('%Y', date)='"+ year +"' GROUP BY Mois, montant ORDER BY Mois", null);
        Cursor result;
       //return bdd.rawQuery("SELECT montant,strftime('%m',date) as Mois FROM " + TABLE_NAME +  " WHERE strftime('%Y', date)='"+ year +"' GROUP BY Mois, montant ORDER BY Mois", null);
        //return bdd.rawQuery("SELECT montant,strftime('%m',date) as Mois FROM " + TABLE_NAME +  " WHERE strftime('%Y', date)='"+ year +"'", null);
        c.moveToFirst();

        for (i =0; i<12; i++)   {
            if (c.getString(1) != String.valueOf(i)) {

            }

        }

        return c;

    }

    /**
     * Méthode retournant le montant des dépenses d'un mois
     * @param month mois donné
     * @return curseur de dépense
     */
    public Cursor selectDepenseOneMois(String month){

        String TABLE_NAME = "table_depense";
        this.open();
        return bdd.rawQuery("SELECT montant, strftime('%m',date) as Mois FROM table_depense WHERE strftime('%m', date)='"+ month +"'", null);

    }

    /**
     * Méthode retournant toutes les dépenses d'un mois
     * @param month mois donné
     * @return curseur de dépense
     */
    public Cursor selectDepenseMois(String month){
        if(month!="00") {
            this.open();
            return bdd.rawQuery("SELECT * FROM table_depense WHERE date >= '01/" + month + "/2017' AND date <= '31/" + month + "/2017'", null);
        } else{
            return null;
        }
    }

    /**
     * Méthode retournant toutes les dépenses d'une annee
     * @param annee annee donné
     * @return curseur de dépense
     */
    public Cursor selectDepensesAnnee(String annee){
        this.open();
        return bdd.rawQuery("SELECT * FROM table_depense WHERE date >= '01/01" + annee + "' AND date <= '31/12" + annee + "'", null);
    }

    /**
     * méthode permetant de convertir un cursor en une dépense
     * @param c curseur d'entré
     * @return dépense de sortie
     */
    public static final Depense cursorToDepense(Cursor c){
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
            ligne = d.getId() + " - Montant : " + d.getMontant() + "\nDate : " + d.getDate() +  "\nCategorie : " + d.getCategorie();
            listeDepense.add(ligne);
            cursor.moveToNext();
        }
        cursor.close();
        return listeDepense;
    }

    public ArrayList<Depense> getAllDepenses(){
        ArrayList<Depense> listeDepenses = new ArrayList<>();
        Cursor cursor = selectDepense();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            listeDepenses.add(DepenseBDD.cursorToDepense(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return listeDepenses;
    }

    /**
     *
     * @param mois
     * @return
     */
    public ArrayList<String> getDepensesByMonth(String mois){
        ArrayList<String> listeDepenses = new ArrayList<String>();
        String ligne;
        Depense d;
        String month;
        switch (mois){
            case "Janvier" : month ="01"; break;
            case "Février" : month ="02"; break;
            case "Mars" : month ="03"; break;
            case "Avril" : month ="04"; break;
            case "Mai" : month ="05"; break;
            case "Juin" : month ="06"; break;
            case "Juillet" : month ="07"; break;
            case "Août" : month ="08"; break;
            case "Septembre" : month ="09"; break;
            case "Octobre" : month ="10"; break;
            case "Novembre" : month="11"; break;
            case "Décembre" : month ="12"; break;
            default: month = "00";
        }
        Cursor cursor = selectDepenseMois(month);
        if(cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                d = DepenseBDD.cursorToDepense(cursor);
                String[] date = d.getDate().split("/");
                if(date[1].equals(month)) {
                    ligne = "Montant : " + d.getMontant() + "\nDate : " + d.getDate() + "\nCategorie : " + d.getCategorie();
                    listeDepenses.add(ligne);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        if (listeDepenses.isEmpty()) {
            listeDepenses.add("Aucune dépense trouvée");
        }
        return listeDepenses;
    }

    public ArrayList<String> getDepensesByAnnee(String annee){
        ArrayList<String> listeDepenses = new ArrayList<String>();
        String ligne;
        Depense d;
        Cursor cursor = selectDepensesAnnee(annee);
        if(cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                d = DepenseBDD.cursorToDepense(cursor);
                String[] date = d.getDate().split("/");
                if(date[2].equals(annee)) {
                    ligne = "Montant : " + d.getMontant() + "\nDate : " + d.getDate() + "\nCategorie : " + d.getCategorie();
                    listeDepenses.add(ligne);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        if (listeDepenses.isEmpty()) {
            listeDepenses.add("Aucune dépense trouvée");
        }
        return listeDepenses;
    }


    public ArrayList<String> getDepensesByMonthYear(String mois, String annee){
        ArrayList<String> listeDepenses = new ArrayList<String>();
        String ligne;
        Depense d;
        String month;
        switch (mois){
            case "Janvier" : month ="01"; break;
            case "Février" : month ="02"; break;
            case "Mars" : month ="03"; break;
            case "Avril" : month ="04"; break;
            case "Mai" : month ="05"; break;
            case "Juin" : month ="06"; break;
            case "Juillet" : month ="07"; break;
            case "Août" : month ="08"; break;
            case "Septembre" : month ="09"; break;
            case "Octobre" : month ="10"; break;
            case "Novembre" : month="11"; break;
            case "Décembre" : month ="12"; break;
            default: month = "00";
        }
        Cursor cursor = selectDepense();
        if(cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                d = DepenseBDD.cursorToDepense(cursor);
                String[] date = d.getDate().split("/");
                if(date[1].equals(month) && date[2].equals(annee)) {
                    ligne = "Montant : " + d.getMontant() + "\nDate : " + d.getDate() + "\nCategorie : " + d.getCategorie();
                    listeDepenses.add(ligne);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        if(mois.isEmpty()){
            listeDepenses.add("Pas de mois selectionné");
        } else if (annee.isEmpty()) {
            listeDepenses.add("Pas d'année selectionnée");
        } else if (listeDepenses.isEmpty()) {
            listeDepenses.add("Aucune dépense trouvée");
        }
        return listeDepenses;
    }

    public  Integer montantMaxYear(int Year)   {
        Integer res;
        float montant;
        String annee;
        res = 0;
        Cursor c = this.populateTable();
        c.moveToFirst();
        while(!c.isAfterLast()) {
            annee = c.getString(1);
            annee = annee.substring(6);
            montant = Float.parseFloat(c.getString(2));
            if (Integer.parseInt(annee) == Year && montant > res)   {
                res = (int)montant;
            }
            c.moveToNext();
        }

        return res;

    }

    /**
     * Fonction permettant de supprimer les dépenses antérieures à une certaine date
     * @param dateChoose
     */

    public void nettoyage(String dateChoose){
        ContentValues values = new ContentValues();

        ArrayList<Depense> listeDepenses = getAllDepenses();
        int taille = listeDepenses.size();
        Date ajd = new Date();
        Integer mois = ajd.getMonth();
        Integer annee = ajd.getYear();
        Date date = new Date (1990, 01, 01);

        switch (dateChoose){
            case  "1 mois" :
                //Nettoyage tous les mois avant celui en cours
                date = new Date(annee, mois-1, 31);
                break;
            case "6 mois":
                //Nettoyage en janvier et en juillet
                if(mois >= 07)
                    date = new Date(annee, 07, 31);
                else
                    date = new Date(annee, 01, 31);
                break;
            case "1 ans":
                //Nettoyage tous les 1ers de l'an
                date = new Date(annee-1, 01, 01);
                break;
            case "2 ans":
                date = new Date(annee-2, 01, 01);
                break;
           case "3 ans":
               date = new Date(annee-3, 01, 01);
                break;
            case "Tout supprimer":
                date = new Date();
            default: //Jamais
        }


        for(int i = 0 ; i < taille; i++){
            System.out.println("Avant le bordel : " + listeDepenses.get(i).getDate());
            if (listeDepenses.get(i).getDateD().before(date)) {
                //listeDepenses.remove(i);
                removeDepenseWithID(listeDepenses.get(i).getId());
            }
            System.out.println("Après le bordel : " + listeDepenses.get(i).getDate());
        }


    }
}
