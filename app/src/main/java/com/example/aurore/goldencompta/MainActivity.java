package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.*;
import android.widget.ListView;
import android.widget.*;

public class MainActivity extends Activity {
    public final static int CHOOSE_BUTTON_REQUEST = 0;
    /*public DepenseBDD depense;
    private TableLayout t1;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // t1 = (TableLayout) findViewById(R.id.main_table);


        //Création de l'instance de la classe CategorieBDD
        CategorieBDD categBdd = new CategorieBDD(this);
        Categorie categ = new Categorie("TestBDD");


        //On ouvre la base de données pour écrire dedans
        categBdd.open();
        //On insère le livre que l'on vient de créer
        categBdd.insertCategorie(categ);


        //Pour vérifier que l'on a bien créé notre catégorie dans la BDD
        //on extrait la catégorie de la BDD grâce au titre de la catégorie que l'on a créée précédemment
        Categorie categFromBdd = categBdd.getCategorieWithNom(categ.getNom());
        //Si une categorie est retournée (donc si la catégorie à bien été ajoutée à la BDD)
        if(categFromBdd != null){
            //On affiche les infos de la catégorie dans un Toast
            Toast.makeText(this, categFromBdd.toString(), Toast.LENGTH_LONG).show();
            //On modifie le titre du livre
            categFromBdd.setNom("J'ai modifié le nom de la catégorie");
            //Puis on met à jour la BDD
            categBdd.updateCategorie(categFromBdd.getId(), categFromBdd);
        }


        categBdd.close();
/*
        depense = new DepenseBDD(this);
        BuildTable();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //Si ok on ajoute dans la base de données correspondante
            Categorie newCateg = new Categorie(data.getStringExtra("NewCateg"));
            CategorieBDD categBdd = new CategorieBDD(this);
            categBdd.open();
            categBdd.insertCategorie(newCateg);
            categBdd.close();
            /*Categ = data.getStringExtra("NewCateg");
            SaisieintervalleMax.setText(IntervalleMax);
            intervalleMax = Integer.parseInt(IntervalleMax.toString());*/
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //Retour à la page d'accueil
            case R.id.menu_main:

                return true;
            //case R.id.menu_about:
            case R.id.menu_category:
                // Comportement du bouton "Catégorie"
                Intent intentCategory = new Intent(this, FormulaireCategorie.class);
                startActivityForResult(intentCategory, CHOOSE_BUTTON_REQUEST);
                return true;
            case R.id.menu_depense:
                //Comportement du bouton "Dépense"
                Intent intentDepense = new Intent(this, FormulaireDepense.class);
                startActivityForResult(intentDepense, CHOOSE_BUTTON_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
 /* affichage tableau
    private void BuildTable() {
        Cursor mCur = depense.populateTable();
       System.out.println("Successfully inserted a row at "+mCur.getCount());
        if (mCur.getCount() != 0) {
            if (mCur.moveToFirst()) {
                do {
                    int rows = mCur.getCount();
                    int cols = mCur.getColumnCount();

                    // outer for loop
                    for (int i = 0; i < rows; i++) {

                        TableRow row = new TableRow(this);
                        TableLayout.LayoutParams tableRowParams=
                                new TableLayout.LayoutParams
                                        (TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
                        row.setLayoutParams(tableRowParams);

                        // inner for loop
                        for (int j = 0; j < cols; j++) {

                            TextView tv = new TextView(this);
                            tv.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(18);
                            tv.setPadding(0, 5, 0, 5);

                            tv.setText(mCur.getString(j));
                            row.addView(tv);

                        }
                        t1.addView(row);
                    }
                } while (mCur.moveToNext());
            }
        }
    }
*/
}
