package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

/**
 * Created by utilisateur on 16/11/2016.
 */

public class FormulaireDepense extends Activity {
    Activity main = this;
    String d, dBis;
    Date systeme = new Date();
    Date saisi;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire_depense);

        final Intent intent = new Intent();

        final EditText newDepense = (EditText) findViewById(R.id.newDepense);

        final DatePicker date = (DatePicker) findViewById(R.id.date);

        final Button save = (Button) findViewById(R.id.Save);

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
        //loadSpinnerData();

        save.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {

                String dep =  newDepense.getText().toString();
                String cat = spinner.getSelectedItem().toString();
                // si le champ catégorie est vide
                if (dep.equals("")) {
                    Toast.makeText(FormulaireDepense.this,R.string.champ_vide, Toast.LENGTH_SHORT).show();
                }else {
                    dBis = date.getYear() + "/" + (date.getMonth() + 1) + "/" + date.getDayOfMonth();
                    saisi = new Date(dBis);

                    if (saisi.before(systeme) || saisi.equals(systeme)) {
                        d = date.getDayOfMonth() + "/" + (date.getMonth() + 1) + "/" + date.getYear();
                        main.setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(FormulaireDepense.this,"La date doit être inférieur ou égale à la date d'aujourd'hui", Toast.LENGTH_SHORT).show();
                }





                System.out.println("Date du calendrier2 : " + d);
                System.out.println("Depense : " + newDepense.getText());
                System.out.println("Catégorie : " + cat);
                System.out.print("Date : " + d.toString());


                intent.putExtra("NEWDEPENSE", newDepense.getText().toString());
                intent.putExtra("CATEGORIE",cat );
                intent.putExtra("DATE", d);

               }
            }
        });


    }


}
