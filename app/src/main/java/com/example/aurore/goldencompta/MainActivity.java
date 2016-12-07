package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    public final static int CATEGORIE = 0;
    public final static int DEPENSE = 1;
    public final static int BUDGET = 2;
    private SimpleCursorAdapter dataAdapter;
    DepenseBDD depenseBdd=new DepenseBDD(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Création de l'instance de la classe CategorieBDD
        CategorieBDD categBdd = new CategorieBDD(this);

        //Création de l'instance de la classe DepenseBDD
      //  depenseBdd = new DepenseBDD(this);
        depenseBdd.open();

        displayListView();
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

    private void displayListView() {

        Cursor cursor = depenseBdd.populateTable();

        // The desired columns to be bound
        String[] columns = new String[] {
                DepenseBDD.COL_CATEG,
                DepenseBDD.COL_DATE,
                DepenseBDD.COL_MONTANT
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.categ,
                R.id.date,
                R.id.montant,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.depense_info,
                cursor,
                columns,
                to,
                0);

        System.out.println("Avant list view");
        ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the name's depense from this row in the database.
                String categorieName =
                        cursor.getString(cursor.getColumnIndexOrThrow("categorie"));
                Toast.makeText(getApplicationContext(),
                        categorieName, Toast.LENGTH_SHORT).show();

            }
        });

        EditText myFilter = (EditText) findViewById(R.id.myFilter);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return depenseBdd.fetchDepensesByName(constraint.toString());
            }
        });

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

                if(existCateg == null){
                    //Si la catégorie n'existe pas dans la BDD, on l'ajoute
                    categBdd.insertCategorie(new Categorie(data.getStringExtra("newCateg")));

                }//Si le catégorie existe (mais normalement il ne devrait pas)
                else{
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
}