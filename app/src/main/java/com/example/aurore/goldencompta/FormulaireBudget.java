package com.example.aurore.goldencompta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.aurore.goldencompta.MainActivity.BUDGET;
import static com.example.aurore.goldencompta.MainActivity.CAMERA;
import static com.example.aurore.goldencompta.MainActivity.CATEGORIE;
import static com.example.aurore.goldencompta.MainActivity.DEPENSE;
import static com.example.aurore.goldencompta.MainActivity.IMAGE;

public class FormulaireBudget extends Activity {
    Activity main = this;

    /**
     * Methode d'initialisation de l'intent
     * @param savedInstanceState le bundle utilisé pour créer la méthode
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire_budget);

        final String budget;
        final String BUD = "budget";
        final String Test = "budget";
        final Intent intent = new Intent();
        final Button save = (Button) findViewById(R.id.Save);
        final Button retour = (Button) findViewById(R.id.retour);
        final TextView montantActuel = (TextView) findViewById(R.id.MontantActuel);
        final EditText montant = (EditText) findViewById(R.id.montant);

//        AFIICHAGE DU BUDGET
        // On veut la chaîne de caractères d'identifiant FAVORITE_COLOR
        // Si on ne trouve pas cette valeur, on veut rendre "FFFFFF"
        SharedPreferences preferences = getSharedPreferences (BUD,0);
        budget = preferences.getString(BUD, "Aucun budget");
        montantActuel.setText(budget);
        //Listener sur le bouton valider pour mettre à jours le budget
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = montant.getText().toString();

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
                    editor.commit();

                    Toast toast = Toast.makeText(getApplicationContext(), "Budget modifié", Toast.LENGTH_SHORT);
                    toast.show();
                    montantActuel.setText(str);
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


