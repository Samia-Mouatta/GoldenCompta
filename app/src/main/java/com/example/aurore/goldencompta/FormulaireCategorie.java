package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.aurore.goldencompta.MainActivity.BUDGET;
import static com.example.aurore.goldencompta.MainActivity.CAMERA;
import static com.example.aurore.goldencompta.MainActivity.CATEGORIE;
import static com.example.aurore.goldencompta.MainActivity.DEPENSE;
import static com.example.aurore.goldencompta.MainActivity.IMAGE;

public class FormulaireCategorie extends Activity {
    Activity main = this;

    /**
     * Méthode qui permet d'initialiser notre Intent
     * @param savedInstanceState le bundle utilisé pour crée la méthode
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire_categorie);

        final Intent intent = new Intent();
        final EditText Newcategorie = (EditText) findViewById(R.id.newCategorie);
        final Button loginButton = (Button) findViewById(R.id.Save);
        final Button retour = (Button) findViewById(R.id.Retour);

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                System.out.println("Intitulé de la nouvelle catégorie : "+ Newcategorie.getText());
                String categ= Newcategorie.getText().toString();
                // si le champ catégorie est vide
                if (categ.equals("")) {
                    Toast.makeText(FormulaireCategorie.this,R.string.categorie_existe, Toast.LENGTH_SHORT).show();
                }else {
                    intent.putExtra("newCateg", Newcategorie.getText().toString());
                    main.setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });

        retour.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                main.setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }


    /**
     * Méthode de création du menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Méthode de navigation dans les items du menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Retour à la page d'accueil
            case R.id.accueil:
                Intent accueil = new Intent(this, MainActivity.class);
                startActivity(accueil);
                return true;
            //case R.id.menu_about:
            case R.id.tab_dep:
                Intent tabDep = new Intent(this, TableauDepense.class);
                startActivity(tabDep);
                return true;
            case R.id.menu_category:
                // Comportement du bouton "Catégorie"
                Intent intentCategory = new Intent(this, FormulaireCategorie.class);
                startActivityForResult(intentCategory, CATEGORIE);
                return true;
            case R.id.menu_depense:
                //Comportement du bouton "Dépense"
                Intent intentDepense = new Intent(this, FormulaireDepense.class);
                startActivityForResult(intentDepense, DEPENSE);
                return true;
            case R.id.menu_budget:
                //Comportement du bouton "budget"
                Intent intentBudget = new Intent(this, FormulaireBudget.class);
                startActivityForResult(intentBudget, BUDGET);
                return true;
            case R.id.menu_statistique:
                //Comportement du bouton "camera"
                Intent intentStat = new Intent(this, FormulaireStatistique.class);
                startActivity(intentStat);
                return true;
            case R.id.menu_img:
                Intent intentImage = new Intent(this, FormulaireImg.class);
                startActivityForResult(intentImage, IMAGE);
                return true;
            case R.id.menu_camera:
                Intent intentCamera = new Intent(this, FormulaireCamera.class);
                startActivityForResult(intentCamera, CAMERA);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}