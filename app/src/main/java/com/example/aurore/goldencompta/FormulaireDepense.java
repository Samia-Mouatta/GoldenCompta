package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.aurore.goldencompta.MainActivity.BUDGET;
import static com.example.aurore.goldencompta.MainActivity.CAMERA;
import static com.example.aurore.goldencompta.MainActivity.CATEGORIE;
import static com.example.aurore.goldencompta.MainActivity.DEPENSE;
import static com.example.aurore.goldencompta.MainActivity.IMAGE;

public class FormulaireDepense extends Activity {
    Activity main = this;
    String d, dBis;
    Date systeme = new Date();
    Date saisi;


    /**
     * Méthode qui permet d'initialiser notre Intent
     * @param savedInstanceState le bundle utilisé pour créer la méthode
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire_depense);

        final Intent intent = new Intent();

        final EditText newDepense = (EditText) findViewById(R.id.newDepense);

        final DatePicker date = (DatePicker) findViewById(R.id.date);

        final Button save = (Button) findViewById(R.id.Save);
        final Button retour = (Button) findViewById(R.id.retour);

        final Spinner spinner = (Spinner) findViewById(R.id.spinnerC);




        List<String> list = new ArrayList<String>();
        List<String> listCategorie = new ArrayList<String>();

        CategorieBDD categBdd = new CategorieBDD(this);
        categBdd.open();
        listCategorie=categBdd.getAllCategoriesName();
        categBdd.close();
        for (String categ:listCategorie) {
            list.add(categ);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        save.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                String dep =  newDepense.getText().toString();
                String cat = spinner.getSelectedItem().toString();

                // si le champ catégorie est vide
                if (dep.equals("")) {
                    Toast.makeText(FormulaireDepense.this,"Les champs doivent etre remplis", Toast.LENGTH_SHORT).show();
               } else {

                    dBis = date.getYear() + "/" + (date.getMonth() + 1) + "/" + date.getDayOfMonth();
                    saisi = new Date(dBis);

                    if (saisi.before(systeme) || saisi.equals(systeme)) {
                        if (date.getDayOfMonth() < 10){
                            d = "0" + date.getDayOfMonth();
                        } else {
                            d = String.valueOf(date.getDayOfMonth());
                        }
                        if (date.getMonth() < 9 ){
                            d +=  "/0" + (date.getMonth() + 1) + "/" + date.getYear();
                        } else {
                            d += "/" + (date.getMonth() + 1) + "/" + date.getYear();
                        }


                        intent.putExtra("NEWDEPENSE", dep);
                        intent.putExtra("CATEGORIE",cat );
                        intent.putExtra("DATE", d);
                        main.setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(FormulaireDepense.this,"La date doit être inférieure ou égale à la date d'aujourd'hui", Toast.LENGTH_SHORT).show();
                }

                    
               }
            }
        });

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
