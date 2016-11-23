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


    public  static final String CREATE_BDD = "CREATE TABLE if not exists " + TABLE_CATEGORIE + " ("
            + COL_ID_CATEG + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NOM + " TEXT NOT NULL); " +
            "CREATE TABLE if not exists "+ TABLE_DEPENSE + " (" + COL_ID_DEPENSE + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ COL_MONTANT + " REAL NOT NULL "
            + COL_DATE + " DATE NOT NULL, " + COL_CATEG + "); ";

    public  static final String CREATE_BDD2 = "CREATE TABLE categorie (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT NOT NULL); CREATE TABLE table_depense (id INTEGER PRIMARY KEY AUTOINCREMENT, montant REAL NOT NULL "
            + "date DATE NOT NULL, categorie);";

    public MaBaseSQLite(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //on crée la table à partir de la requête écrite dans la variable CREATE_BDD
//        db.execSQL(CREATE_BDD);

        db.execSQL("CREATE TABLE table_categorie (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT NOT NULL);");
        db.execSQL("CREATE TABLE table_depense (id INTEGER PRIMARY KEY AUTOINCREMENT, montant REAL NOT NULL, date string NOT NULL, categorie string);");


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

        String ROW11 = "INSERT INTO " + TABLE_DEPENSE + " Values ('1', '12,20','12/06/2009', 'Animaux');";
        db.execSQL(ROW11);

        String ROW22 = "INSERT INTO " + TABLE_DEPENSE + " Values ('2','56,32','13/06/2009', 'Courses');";
        db.execSQL(ROW22);

        String ROW33 = "INSERT INTO " + TABLE_DEPENSE + " Values ('3','14,50','14/06/2009', 'Pizza');";
        db.execSQL(ROW33);


//        String ROW35 = "INSERT INTO dep Values ('4', null, null, null, null);";
//        db.execSQL(ROW35);


//        String ROW11 = "INSERT INTO " + TABLE_DEPENSE + " Values ('1', '12,20,'12/06/2009', '120', 'Animaux');";
//        db.execSQL(ROW11);

//        String ROW22 = "INSERT INTO " + TABLE_DEPENSE + " Values ('2','56,32,'13/06/2009', '1650', 'Courses');";
//        db.execSQL(ROW22);
//
//        String ROW33 = "INSERT INTO " + TABLE_DEPENSE + " Values ('3','14,50','14/06/2009', '10', 'Pizza');";
//        db.execSQL(ROW33);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On peut faire ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
        //comme ça lorsque je change la version les id repartent de 0
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPENSE + ";");
        onCreate(db);
    }

    public void onOpen()    {

    }


}