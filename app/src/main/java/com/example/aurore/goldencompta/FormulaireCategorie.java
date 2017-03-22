package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import static com.example.aurore.goldencompta.MainActivity.BUDGET;
import static com.example.aurore.goldencompta.MainActivity.CAMERA;
import static com.example.aurore.goldencompta.MainActivity.CATEGORIE;
import static com.example.aurore.goldencompta.MainActivity.DEPENSE;
import static com.example.aurore.goldencompta.MainActivity.IMAGE;

public class FormulaireCategorie extends BaseActivity {
    Activity main = this;

    /**
     * Méthode qui permet d'initialiser notre Intent
     * @param savedInstanceState le bundle utilisé pour crée la méthode
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferencesD = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferencesD.getBoolean(PREF_DARK_THEME, false);

        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire_categorie);

        Switch toggle = (Switch) findViewById(R.id.switch1);
        toggle.setChecked(useDarkTheme);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                toggleTheme(isChecked);
            }
        });

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
}