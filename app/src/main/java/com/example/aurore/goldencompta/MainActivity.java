package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    public final static int CHOOSE_BUTTON_REQUEST = 0;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tableLayout=(TableLayout)findViewById(R.id.tableLayout);


        //Création de l'instance de la classe CategorieBDD
        CategorieBDD categBdd = new CategorieBDD(this);
        Categorie categ = new Categorie("Animaux");


        //On ouvre la base de données pour écrire dedans
        categBdd.open();
        //On insère le livre que l'on vient de créer
        categBdd.insertCategorie(categ);


        //Pour vérifier que l'on a bien créé notre catégorie dans la BDD
        //on extrait le livre de la BDD grâce au titre de la catégorie que l'on a créé précédemment
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


        for (int i=0;i<5;i++){
            View tableRow = LayoutInflater.from(this).inflate(R.layout.activity_main,null,false);
            TextView history_display_no  = (TextView) tableRow.findViewById(R.id.history_display_no);
            TextView history_display_date  = (TextView) tableRow.findViewById(R.id.history_display_nom);

            history_display_no.setText(""+(i+1));
            history_display_date.setText("2014-02-05");
            tableLayout.addView(tableRow);
        }

        categBdd.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //case R.id.menu_about:
            case R.id.menu_category:
                // Comportement du bouton "Catégorie"
                Intent intentCategory = new Intent(this, FormulaireCategorie.class);
                startActivityForResult(intentCategory, CHOOSE_BUTTON_REQUEST);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
