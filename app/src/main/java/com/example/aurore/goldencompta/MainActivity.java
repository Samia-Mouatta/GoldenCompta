package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import static java.lang.Integer.parseInt;


public class MainActivity extends Activity {
    public final static int CHOOSE_BUTTON_REQUEST = 0;
    DepenseBDD depense;
    TableLayout t1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intentBDD = new Intent(this, MaBaseSQLite.class);
//        startActivityForResult(intentBDD, CHOOSE_BUTTON_REQUEST);
//        startActivityForResult(intentBDD, CHOOSE_BUTTON_REQUEST);
        // t1 = (TableLayout) findViewById(R.id.main_table);
//        MaBaseSQLite DataBase = new MaBaseSQLite();

        //Création de l'instance de la classe CategorieBDD
        CategorieBDD categBdd = new CategorieBDD(this);
        Categorie categ = new Categorie("TestBDD");

        //Création de l'instance de la classe DepenseBDD
        DepenseBDD depenseBdd = new DepenseBDD(this);
        Depense depens = new Depense();


        //On ouvre la base de données pour écrire dedans
        categBdd.open();
        //On insère le livre que l'on vient de créer
        categBdd.insertCategorie(categ);


        //Pour vérifier que l'on a bien créé notre catégorie dans la BDD
        //on extrait la catégorie de la BDD grâce au titre de la catégorie que l'on a créée précédemment
        Categorie categFromBdd = categBdd.getCategorieWithNom(categ.getNom());
        //Si une categorie est retournée (donc si la catégorie à bien été ajoutée à la BDD)
        if (categFromBdd != null) {
            //On affiche les infos de la catégorie dans un Toast
            Toast.makeText(this, categFromBdd.toString(), Toast.LENGTH_LONG).show();
            //On modifie le titre du livre
            categFromBdd.setNom("J'ai modifié le nom de la catégorie");
            //Puis on met à jour la BDD
            categBdd.updateCategorie(categFromBdd.getId(), categFromBdd);
        }


        categBdd.close();

        //Affichage du tableau----------------------------------------------------------------------
        TableLayout tl = (TableLayout) findViewById(R.id.tdyn);
        TableRow tr;
        TableRow trEntete;
        String monthTmp = "";
        String monthDepense = "";
        Cursor lesDepenses = depenseBdd.selectDepense();
        String contenu = "ok";
        String[] tabMois={"Janvier","Fevrier","Mars","Avril","Mai","Juin","Juillet","Aout","Septembre","Octobre","Novembre","Decembre"};
        int nbMonth;
        LayoutParams linLayout = new LayoutParams();

//
        LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        layoutParams.setMargins(2, 2, 2, 2);

        if (lesDepenses.moveToFirst())
            for (int i = 0; i < depenseBdd.selectDepense().getCount(); i++) {
                monthDepense = lesDepenses.getString(2);
                monthDepense = monthDepense.substring(3,5);


                if (monthDepense == monthTmp || monthTmp == "")  {
                    nbMonth = parseInt(monthDepense)-1;
                    trEntete = new TableRow(this);
                    trEntete.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                    trEntete.addView(generateTextView(tabMois[nbMonth], layoutParams));
                    tl.addView(trEntete, layoutParams);

                    monthTmp = monthDepense;
                }

                tr = new TableRow(this);
                tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

                for (int j = 0; j<4 ; j++) {
                    tr.addView(generateTextView(lesDepenses.getString(j), layoutParams));
                }

                tl.addView(tr, layoutParams);
                lesDepenses.moveToNext();
            }
//        depenseBdd.close();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public TextView generateTextView(String texte, LayoutParams ly) {
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

        //Fin affichage du tableau------------------------------------------------------------------
/*
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
        if (resultCode == RESULT_OK) {
            //Si ok on ajoute dans la base de données correspondante
            Categorie newCateg = new Categorie(data.getStringExtra("NewCateg"));
            CategorieBDD categBdd = new CategorieBDD(this);
            categBdd.open();
            categBdd.insertCategorie(newCateg);
            categBdd.close();
            /*Categ = data.getStringExtra("NewCateg");
            SaisieintervalleMax.setText(IntervalleMax);
            intervalleMax = Integer.parseInt(IntervalleMax.toString());*/
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
                startActivityForResult(intentCategory, CHOOSE_BUTTON_REQUEST);
                return true;
            case R.id.menu_depense:
                //Comportement du bouton "Dépense"
                Intent intentDepense = new Intent(this, FormulaireDepense.class);
                startActivityForResult(intentDepense, CHOOSE_BUTTON_REQUEST);
                return true;
            case R.id.menu_budget:
                //Comportement du bouton "budget"
                Intent intentBudget = new Intent(this, FormulaireBudget.class);
                startActivityForResult(intentBudget, CHOOSE_BUTTON_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

/*
    private void BuildTable() {

        depense.open();
        Cursor c = depense.populateTable();

        int rows = c.getCount();
        int cols = c.getColumnCount();

        c.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 0; j < cols; j++) {

                TextView tv = new TextView(this);
                tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                //tv.setBackgroundResource(R.drawable.cell_shape);
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(18);
                tv.setPadding(0, 5, 0, 5);

                tv.setText(c.getString(j));

                row.addView(tv);

            }

            c.moveToNext();

            t1.addView(row);

        }
        depense.close();
    }*/


//// affichage tableau
//    private void BuildTable() {
//        DepenseBDD depense = new DepenseBDD(this);
//        Cursor mCur = depense.populateTable();
//       System.out.println("Successfully inserted a row at "+mCur.getCount());
//        if (mCur.getCount() != 0) {
//            if (mCur.moveToFirst()) {
//                do {
//                    int rows = mCur.getCount();
//                    int cols = mCur.getColumnCount();
//
//                    // outer for loop
//                    for (int i = 0; i < rows; i++) {
//
//                        TableRow row = new TableRow(this);
//                        TableLayout.LayoutParams tableRowParams=
//                                new TableLayout.LayoutParams
//                                        (TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
//                        row.setLayoutParams(tableRowParams);
//
//                        // inner for loop
//                        for (int j = 0; j < cols; j++) {
//
//                            TextView tv = new TextView(this);
//                            tv.setLayoutParams(new ViewGroup.LayoutParams(
//                                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                                    ViewGroup.LayoutParams.WRAP_CONTENT));
//                            tv.setGravity(Gravity.CENTER);
//                            tv.setTextSize(18);
//                            tv.setPadding(0, 5, 0, 5);
//
//                            tv.setText(mCur.getString(j));
//                            row.addView(tv);
//
//                        }
//                        t1.addView(row);
//                    }
//                } while (mCur.moveToNext());
//            }
//        }
//    }


}
