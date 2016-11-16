package com.example.aurore.goldencompta;

/**
 * Created by roros on 11/11/2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MaBaseSQLite extends SQLiteOpenHelper {

    public  static final String TABLE_CATEGORIE = "table_categorie";
    public  static final String COL_ID_CATEG = "id";
    public  static final String COL_NOM = "nom";

    public static final String TABLE_DEPENSE = "table_depense";
    private static final String COL_ID_DEPENSE = "id";
    private static final String COL_DATE = "date";
    private static final String COL_CATEG = "categorie";


    public  static final String CREATE_BDD = "CREATE TABLE " + TABLE_CATEGORIE + " ("
            + COL_ID_CATEG + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NOM + " TEXT NOT NULL); " +
            "CREATE TABLE "+ TABLE_DEPENSE + " (" + COL_ID_DEPENSE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_DATE + " DATE NOT NULL, " + COL_CATEG + "); ";

    public MaBaseSQLite(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //on crée la table à partir de la requête écrite dans la variable CREATE_BDD
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On peut faire ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
        //comme ça lorsque je change la version les id repartent de 0
        db.execSQL("DROP TABLE " + TABLE_CATEGORIE + ";");
        db.execSQL("DROP TABLE " + TABLE_DEPENSE + ";");
        onCreate(db);
    }
}