package com.example.aurore.goldencompta;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by utilisateur on 16/11/2016.
 */

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
        final TextView montantActuel = (TextView) findViewById(R.id.MontantActuel);
        final EditText montant = (EditText) findViewById(R.id.montant);


//        AFIICHAGE DU BUDGET

        // On veut la chaîne de caractères d'identifiant FAVORITE_COLOR
        // Si on ne trouve pas cette valeur, on veut rendre "FFFFFF"
        SharedPreferences preferences = getSharedPreferences (BUD,0);
        budget = preferences.getString(BUD, "Aucun budget");
        montantActuel.setText(budget);
//        montantActuel.setText("12");



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


    }
}


