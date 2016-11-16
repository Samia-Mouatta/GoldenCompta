package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.view.View.OnClickListener;

import java.util.Date;

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
        final EditText categorie = (EditText) findViewById(R.id.Categorie);

        final View inferieur = (View) findViewById(R.id.inf);

        final DatePicker date = (DatePicker) findViewById(R.id.date);

        final Button save = (Button) findViewById(R.id.Save);

        save.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){


                dBis =  date.getYear()+"/" + (date.getMonth()+1) +"/"+date.getDayOfMonth();
                saisi = new Date(dBis);

                if(saisi.before(systeme) || saisi.equals(systeme)) {
                    //System.out.println(saisi.toString());
                    d =  date.getDayOfMonth() +"/" + (date.getMonth()+1) +"/"+date.getYear();
                    main.setResult(RESULT_OK, intent);
                    finish();
                }else{
                    inferieur.setVisibility(View.VISIBLE);
                }

                /*
                System.out.println("Date du calendrier2 : " + d);
                System.out.println("Depense : " + newDepense.getText());
                System.out.println("Catégorie : " + categorie.getText());
                System.out.print("Date : " + d.toString());
                */

                intent.putExtra("NEWDEPENSE", newDepense.getText().toString());
                intent.putExtra("CATEGORIE", categorie.getText().toString());
                intent.putExtra("DATE", d);


            }
        });


    }


}
