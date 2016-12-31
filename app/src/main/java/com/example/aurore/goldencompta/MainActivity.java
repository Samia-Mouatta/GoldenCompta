package com.example.aurore.goldencompta;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static java.lang.Integer.numberOfLeadingZeros;
import static java.lang.Integer.parseInt;

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

        Depense d;


        values = depenseBDD.getAllDepense();
        System.out.println("Categorie : " + values.get(1));

        if (values.size() != 0) {
            System.out.println("Categorie : " + values.get(1));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, values);
            listView.setAdapter(adapter);
            System.out.println("Categorie : " + adapter.getItem(1));
        } else {
            estVide.setVisibility(View.VISIBLE);
        }


        //Création de l'instance de la classe CategorieBDD
        CategorieBDD categBdd = new CategorieBDD(this);

        //Création de l'instance de la classe DepenseBDD
        //  depenseBdd = new DepenseBDD(this);


       /* //Affichage du tableau----------------------------------------------------------------------
        TableLayout tl = (TableLayout) findViewById(R.id.tdyn);
        TableRow tr;
        depenseBdd.open();
        Cursor lesDepenses = depenseBdd.populateTable();

        LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        layoutParams.setMargins(2, 2, 2, 2);

        if (lesDepenses.moveToFirst()) {
            for (int i = 0; i < depenseBdd.populateTable().getCount(); i++) {

                tr = new TableRow(this);
                tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                tr.addView(generateTextView(lesDepenses.getString(0), layoutParams));
                tr.addView(generateTextView(lesDepenses.getString(1), layoutParams));
                tr.addView(generateTextView(lesDepenses.getString(2), layoutParams));
                tl.addView(tr, layoutParams);
                lesDepenses.moveToNext();
            }
        }
        depenseBdd.close();
    }

    public TextView generateTextView(String texte, LayoutParams ly) {
        TextView result = new TextView(this);
        result.setBackgroundColor(Color.WHITE);
        result.setTextColor(Color.DKGRAY);
        result.setGravity(Gravity.CENTER);
        result.setPadding(2, 2, 2, 2);
        result.setText(texte);
        result.setTextSize(20);
        result.setVisibility(View.VISIBLE);
        result.setLayoutParams(ly);
        return result;

        //Fin affichage du tableau------------------------------------------------------------------

        depense = new DepenseBDD(this);
        BuildTable();*/

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

                /*  FormulaireBudget fBudget=new FormulaireBudget();
                int budget = fBudget.getValBudget();

            //    System.out.println(" total des dépenses" + depenseBDD.getDepenseMois());

                //  Si le montant a ajouté ou le montant total du mois dépasse le budget
                if (montant > budget || depenseBDD.getDepenseMois() > budget) {
                    // afficher une boite de dialogue d'alerte
                    Builder builder = new Builder(this);
                    builder.setMessage("Attention!Le montant de la dépense dépasse votre budget par mois");
                    builder.setCancelable(true);
                    builder.setPositiveButton("OK", new OkOnClickListener());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {*/

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    public void tableauDepenses(Cursor lesDepenses){
        TableLayout tl = (TableLayout) findViewById(R.id.tdyn);
        TableRow tr;
        TableRow trEntete;
        String monthTmp = "";
        String monthDepense = "";
        String contenu = "ok";
        String[] tabMois={"Janvier","Fevrier","Mars","Avril","Mai","Juin","Juillet","Aout","Septembre","Octobre","Novembre","Decembre"};
        int nbMonth;
        TableRow.LayoutParams linLayout = new TableRow.LayoutParams();
        //
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT);
        layoutParams.setMargins(2, 2, 2, 2);
        if (lesDepenses.moveToFirst())
            for (int i = 0; i < lesDepenses.getCount(); i++) {
                monthDepense = lesDepenses.getString(2);
                monthDepense = monthDepense.substring(3,5);
                if (monthDepense == monthTmp || monthTmp == "")  {
                    nbMonth = parseInt(monthDepense)-1;
                    trEntete = new TableRow(this);
                    trEntete.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    trEntete.addView(generateTextView(tabMois[nbMonth], layoutParams));
                    tl.addView(trEntete, layoutParams);
                    monthTmp = monthDepense;
                }
                tr = new TableRow(this);
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                for (int j = 0; j<4 ; j++) {
                    tr.addView(generateTextView(lesDepenses.getString(j), layoutParams));
                }
                tl.addView(tr, layoutParams);
                lesDepenses.moveToNext();
            }

    }
    */


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

}