package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    }
}