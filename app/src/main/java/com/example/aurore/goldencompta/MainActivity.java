package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
    public static int CATEGORIE = 0;
    public static int DEPENSE = 1;
    public static int BUDGET = 2;
    public static int IMAGE = 3;
    public static int CAMERA = 4;
    private Button button_tabl, button_stat, button_scan, button_param;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_tabl = (Button) findViewById(R.id.button_tab_dep);
        button_stat = (Button) findViewById(R.id.button_statistiques);
        button_scan= (Button) findViewById(R.id.button_scan);
        button_param= (Button) findViewById(R.id.button_parametre);

        button_tabl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tabDep = new Intent(MainActivity.this, TableauDepense.class);
                MainActivity.this.startActivity(tabDep);
            }
        });

        button_stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentStat = new Intent(MainActivity.this, FormulaireStatistique.class);
                startActivity(intentStat);
            }
        });

        button_param.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentStat = new Intent(MainActivity.this, FormulaireBudget.class);
                startActivity(intentStat);
            }
        });

        button_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCamera = new Intent(MainActivity.this, FormulaireCamera.class);
                startActivityForResult(intentCamera, CAMERA);
            }
        });


        Switch toggle = (Switch) findViewById(R.id.switch1);
        toggle.setChecked(useDarkTheme);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                toggleTheme(isChecked);
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
                //Comportement du bouton "Stat"
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

    private void toggleTheme(boolean darkTheme) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(PREF_DARK_THEME, darkTheme);
        editor.apply();

        Intent intent = getIntent();
        finish();

        startActivity(intent);
    }

}