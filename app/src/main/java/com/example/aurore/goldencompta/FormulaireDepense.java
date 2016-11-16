package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

/**
 * Created by utilisateur on 16/11/2016.
 */

public class FormulaireDepense extends Activity {
    Activity main = this;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire_depense);

        final Intent intent = new Intent();
        final EditText newDepense = (EditText) findViewById(R.id.newDepense);
        final EditText categorie = (EditText) findViewById(R.id.Categorie);
        final EditText date = (EditText) findViewById(R.id.date);
        final Button save = (Button) findViewById(R.id.Save);

        save.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("Depense : " + newDepense.getText());
                System.out.println("Catégorie : " + categorie.getText());
                System.out.println("Date : " +  date.getText());
                intent.putExtra("NEWDEPENSE", newDepense.getText().toString());
                intent.putExtra("CATEGORIE", categorie.getText().toString());
                intent.putExtra("DATE", date.getText().toString());
                main.setResult(RESULT_OK, intent);
                finish();

            }
        });


    }


}
