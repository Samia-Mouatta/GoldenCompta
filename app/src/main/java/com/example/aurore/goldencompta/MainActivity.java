package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends Activity {

    public static int CATEGORIE = 0;
    public static int DEPENSE = 1;
    public static int BUDGET = 2;

    DepenseBDD depenseBDD = new DepenseBDD(this);
    TableLayout t1;

    public final static int CHOOSE_BUTTON_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String BUD = "budget";
        int mois;
        double monBudget, mesDepenses, value;
        String myBudget, month, depenseString;
        Date date;

        SharedPreferences preferences = getSharedPreferences(BUD, 0);
        myBudget = preferences.getString(BUD, "Aucun budget");
        monBudget = Double.parseDouble(myBudget);

        date = new Date();
        Calendar myCalendar = GregorianCalendar.getInstance();
        myCalendar.setTime(date);
        mois = myCalendar.MONTH;
        month = String.valueOf(mois);
        if (mois < 10) {
            month = "0" + month;
        }
        DepenseBDD depense = new DepenseBDD(this);
        Cursor depenseMois = depense.selectDepenseOneMois(month);
        depenseMois.moveToFirst();
        if (depenseMois.getCount() != 0) {
            depenseString = depenseMois.getString(0).replace(",", ".");
        }
        else {
            depenseString = "0";
        }

        mesDepenses = Double.parseDouble(depenseString);

        Toast toast = Toast.makeText(getApplicationContext(),  "nb " + mois + "testo " + depenseMois.getString(0) , Toast.LENGTH_SHORT);
        toast.show();
        value = mesDepenses * 100 / monBudget;

        WebView budget = (WebView) findViewById(R.id.budget);
        String url = "http://chart.apis.google.com/chart?cht=gom&chco=12FE01,F6FE01,FE0101&chs=300x120&chd=t:"+ value + "&chxt=x,y&chxl=0:|"+ mesDepenses +"|1:|0|"+ monBudget;
        budget.loadUrl(url);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00e6ac"));
        // couleur de l'actionBar
        getActionBar().setBackgroundDrawable(colorDrawable);
        // icon
        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);

        TextView estVide;

        estVide = (TextView) findViewById(R.id.vide);


        ListView listView;
        listView = (ListView) findViewById(R.id.listView1);
        ArrayList<String> values = new ArrayList<String>();



        values = depenseBDD.getAllDepense();
        System.out.println("Categorie : " + values.get(1));

        if (values.size() != 0) {
            System.out.println("Categorie : " + values.get(1));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
            listView.setAdapter(adapter);
            System.out.println("Categorie : " + adapter.getItem(1));
        } else {
            estVide.setVisibility(View.VISIBLE);
        }


        //Création de l'instance de la classe CategorieBDD
        CategorieBDD categBdd = new CategorieBDD(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CATEGORIE) {
            if (resultCode == RESULT_OK) {
                //Si ok on ajoute dans la base de données correspondante
                Categorie newCateg = new Categorie(data.getStringExtra("newCateg"));

                //Ajout dans la base de données
                CategorieBDD categBdd = new CategorieBDD(this);
                categBdd.open();
                // categBdd.insertCategorie(newCateg);
                Categorie existCateg = categBdd.getCategorieWithNom(newCateg.getNom());

                if (existCateg == null) {
                    //Si la catégorie n'existe pas dans la BDD, on l'ajoute
                    categBdd.insertCategorie(new Categorie(data.getStringExtra("newCateg")));

                }//Si le catégorie existe (mais normalement il ne devrait pas)
                else {
                    //on affiche un message indiquant que la catégorie existe dans la BDD
                    Toast.makeText(this, "Cette catégorie existe dans la BDD", Toast.LENGTH_LONG).show();
                }
                categBdd.close();
            }
        } else if (requestCode == DEPENSE) {
            if (resultCode == RESULT_OK) {
                //Si ok on ajoute dans la base de données correspondante
                float montant = Float.parseFloat(data.getStringExtra("NEWDEPENSE"));
                Depense newDep = new Depense(data.getStringExtra("DATE"), montant, data.getStringExtra("CATEGORIE"));

                //Ajout dans la base de données
                DepenseBDD cdepBdd = new DepenseBDD(this);
                cdepBdd.open();
                cdepBdd.insertDepense(newDep);
                cdepBdd.close();

            } else {
                Toast.makeText(this, "Erreur lors de l'insertion", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Cliquer sur le bouton ok une fois on lit le message
    private final class OkOnClickListener implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            MainActivity.this.finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //Retour à la page d'accueil
            case R.id.menu_main:

                return true;
            //case R.id.menu_about:
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
            case R.id.menu_camera:
                //Comportement du bouton "camera"
                Intent intentCamera = new Intent(this, FormulaireCamera.class);
                startActivityForResult(intentCamera, BUDGET);
                return true;
            case R.id.menu_statistique:
                //Comportement du bouton "camera"
                Intent intentStat = new Intent(this, FormulaireStatistique.class);
                startActivityForResult(intentStat, BUDGET);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

/*
    public TextView generateTextView(String texte, TableRow.LayoutParams ly) {
        TextView result = new TextView(this);
        result.setBackgroundColor(Color.LTGRAY);
        result.setTextColor(Color.DKGRAY);
        result.setGravity(Gravity.CENTER);
        result.setPadding(2, 2, 2, 2);
        result.setText(texte);
        result.setTextSize(20);
        result.setVisibility(View.VISIBLE);
        result.setLayoutParams(ly);
        return result;
    }
*/

}