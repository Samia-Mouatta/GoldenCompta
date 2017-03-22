package com.example.aurore.goldencompta;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import java.util.List;

import static com.example.aurore.goldencompta.MainActivity.BUDGET;
import static com.example.aurore.goldencompta.MainActivity.CAMERA;
import static com.example.aurore.goldencompta.MainActivity.CATEGORIE;
import static com.example.aurore.goldencompta.MainActivity.DEPENSE;
import static com.example.aurore.goldencompta.MainActivity.IMAGE;


public class FormulaireBudget extends BaseActivity {
    Activity main = this;

    /**
     * Methode d'initialisation de l'intent
     * @param savedInstanceState le bundle utilisé pour créer la méthode
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferencesD = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferencesD.getBoolean(PREF_DARK_THEME, false);

        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire_budget);

        Switch toggle = (Switch) findViewById(R.id.switch1);
        toggle.setChecked(useDarkTheme);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                toggleTheme(isChecked);
            }
        });

        final String BUD = "budget";
        final String NETT = "dateNettoyage";
        final String budget;
        final int nettoyage;
        final Intent intent = new Intent();
        final Button save = (Button) findViewById(R.id.Save);
        final Button retour = (Button) findViewById(R.id.retour);
        final TextView montantActuel = (TextView) findViewById(R.id.MontantActuel);
        final EditText montant = (EditText) findViewById(R.id.montant);
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerChoix);

        List<String> list = new ArrayList<String>();

        list.add("Jamais");
        list.add("1 mois");
        list.add("6 mois");
        list.add("1 ans");
        list.add("2 ans");
        list.add("3 ans");
        list.add("Tout supprimer");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        // AFFICHAGE DU BUDGET
        // On veut la chaîne de caractères d'identifiant FAVORITE_COLOR
        // Si on ne trouve pas cette valeur, on veut rendre "FFFFFF"
        SharedPreferences preferences = getSharedPreferences (BUD ,MODE_PRIVATE);
        budget = preferences.getString(BUD, "Aucun budget");
        nettoyage = preferences.getInt(NETT, 0);

        montantActuel.setText(budget);
        int pos = preferences.getInt(NETT,0);
        spinner.setSelection(pos);
        //Listener sur le bouton valider pour mettre à jour le budget
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = montant.getText().toString();
                String dateNett = spinner.getSelectedItem().toString();

                if (str.equals("")) {
                    Builder builder = new Builder(FormulaireBudget.this);
                    builder.setTitle("Alerte");
                    builder.setIcon(R.mipmap.alert);
                    builder.setMessage("Attention! Vous n'avez pas rempli le champ budget");
                    builder.setCancelable(true);
                    // ajouter un bouton
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    SharedPreferences preferences = getSharedPreferences(BUD, 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(BUD, str);
                    editor.putInt(NETT, spinner.getSelectedItemPosition());
                    editor.commit();

                    montantActuel.setText(str);

                    intent.putExtra("NEWBUDGET", str);
                    intent.putExtra("NEWDATENETTOYAGE", dateNett);
                    main.setResult(RESULT_OK, intent);
                    finish();
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