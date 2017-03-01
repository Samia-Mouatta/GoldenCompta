package com.example.aurore.goldencompta;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MaBaseSQLite extends SQLiteOpenHelper {

    public  static final String TABLE_CATEGORIE = "table_categorie";
    public  static final String COL_ID_CATEG = "id";
    public  static final String COL_NOM = "nom";

    public static final String TABLE_DEPENSE = "table_depense";
    private static final String COL_ID_DEPENSE = "id";
    public static final String COL_DATE = "date";
    public static final String COL_CATEG = "categorie";
    public static final String COL_MONTANT ="montant";

    public static final String TABLE_BUDGET = "table_budget";
    private static final String COL_ID_BUDGET = "id";
    public static final String COL_DATE_DEB = "dateDeb";
    public static final String COL_DATE_FIN = "dateFin";

    /**
     * Méthode pour créer/ouvrir ou gérer une base de donnée
     * @param context Context à utiliser pour ouvrir ou créer la base de données
     * @param name Chaine de caractère pour le nom du fichier de la base de données
     * @param factory CursorFactory pour crée des cursor
     * @param version entier pour la version de la base de donnée
     */
    public MaBaseSQLite(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Méthode qui initialise notre base de donnée
     * @param db La base de donnée où l'on va créer les tables
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //on crée la table à partir de la requête écrite dans la variable CREATE_BDD

        db.execSQL("CREATE TABLE table_categorie (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT NOT NULL);");
        db.execSQL("CREATE TABLE table_depense (id INTEGER PRIMARY KEY AUTOINCREMENT, montant REAL NOT NULL, date string NOT NULL, categorie string);");
        db.execSQL("CREATE TABLE table_budget (id INTEGER PRIMARY KEY AUTOINCREMENT, montant REAL NOT NULL, dateDeb DATE NOT NULL, dateFin DATE);");


        String ROW1 = "INSERT INTO " + TABLE_CATEGORIE + " Values ('1','Animaux');";
        db.execSQL(ROW1);

        String ROW2 = "INSERT INTO " + TABLE_CATEGORIE + " Values ('2','Pizza');";
        db.execSQL(ROW2);

        String ROW3 = "INSERT INTO " + TABLE_CATEGORIE + " Values ('3','Courses');";
        db.execSQL(ROW3);

        String ROW4 = "INSERT INTO " + TABLE_CATEGORIE + " Values ('4','Pharmacie');";
        db.execSQL(ROW4);

        String ROW5 = "INSERT INTO " + TABLE_CATEGORIE + " Values ('5','Médecin');";
        db.execSQL(ROW5);

        String ROW6 = "INSERT INTO " + TABLE_CATEGORIE + " Values ('6','Electricité');";
        db.execSQL(ROW6);

        String ROW7 = "INSERT INTO " + TABLE_CATEGORIE + " Values ('7','Assurances');";
        db.execSQL(ROW7);

        String ROW8 = "INSERT INTO " + TABLE_CATEGORIE + " Values ('8','Logement');";
        db.execSQL(ROW8);

        String ROW9 = "INSERT INTO " + TABLE_CATEGORIE + " Values ('9','Transports');";
        db.execSQL(ROW9);

        String ROW10 = "INSERT INTO " + TABLE_CATEGORIE + " Values ('10','Sports');";
        db.execSQL(ROW10);

        String ROW12 = "INSERT INTO " + TABLE_CATEGORIE + " Values ('12','Voyages');";
        db.execSQL(ROW12);

        String ROW13 = "INSERT INTO " + TABLE_CATEGORIE + " Values ('13','Vêtements');";
        db.execSQL(ROW13);

        String ROW14 = "INSERT INTO " + TABLE_CATEGORIE + " Values ('14','Restaurants');";
        db.execSQL(ROW14);

        String ROW15 = "INSERT INTO " + TABLE_CATEGORIE + " Values ('15','Loisirs');"; //cinéma - théatre - spéctacle ...
        db.execSQL(ROW15);

        String ROW16 = "INSERT INTO " + TABLE_CATEGORIE + " Values ('16','Cadeaux');";
        db.execSQL(ROW16);

    }

    /**
     * Méthode qui permet de changer la version de la base de donnée
     * @param db la base de donné
     * @param oldVersion numéro de version actuel de la base de donnée
     * @param newVersion numéro de la nouvelle version de la base de donnée
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On peut faire ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
        //comme ça lorsque je change la version les id repartent de 0
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPENSE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET + ";");
        onCreate(db);
    }

    public void onOpen()    {

    }


}