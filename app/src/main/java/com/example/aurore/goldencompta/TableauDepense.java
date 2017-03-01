package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.aurore.goldencompta.MainActivity.BUDGET;
import static com.example.aurore.goldencompta.MainActivity.CAMERA;
import static com.example.aurore.goldencompta.MainActivity.CATEGORIE;
import static com.example.aurore.goldencompta.MainActivity.DEPENSE;
import static com.example.aurore.goldencompta.MainActivity.IMAGE;

public class TableauDepense  extends Activity {

    DepenseBDD depenseBDD = new DepenseBDD(this);
    ArrayAdapter<String> adapter = null;

    Spinner spinnerMois, spinnerAnnee;
    DateAdapter dataAdapterMois, dataAdapterYear;
    TextView dateView;
    ArrayList<String> values;
    ListView listView;
    String moisSelectionne, anneeSelectionnee;
    ArrayAdapter<String> categAdapter = null;
    TextView estVide;
    private Activity main = this;
    private Button retour;
    private Intent intent;


    /**
     * Méthode principale du lancement de l'application
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tableau_depenses);
        moisSelectionne = "";
        anneeSelectionnee = "";
        retour = (Button) findViewById(R.id.retour);
        intent = new Intent();

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00e6ac"));
        // couleur de l'actionBar
        getActionBar().setBackgroundDrawable(colorDrawable);

        // icon
        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);

        values = new ArrayList<String>();
        //Création de l'instance de la classe CategorieBDD
        CategorieBDD categBdd = new CategorieBDD(this);

        //Array list of depenses
        values = depenseBDD.getAllDepense();
        estVide = (TextView) findViewById(R.id.vide);

        listView = (ListView) findViewById(R.id.listView1);


        //spinnerCateg = (Spinner) findViewById(R.id.spinCat);

        List<String> list = new ArrayList<String>();
        List<String> listCategorie = new ArrayList<String>();

        categBdd.open();
        listCategorie = categBdd.getAllCategoriesName();
        categBdd.close();
        list.add("");
        for (String categ : listCategorie) {
            list.add(categ);
        }

        categAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, list);

        categAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        dateView = (TextView) findViewById(R.id.date);

        spinnerMois = (Spinner)findViewById(R.id.spinnerMois);

        dataAdapterMois = new DateAdapter(this,
                android.R.layout.simple_spinner_item, loadMonth()

        );
        dataAdapterMois.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMois.setAdapter(dataAdapterMois);
        spinnerMois.setSelection(0);

        spinnerAnnee = (Spinner)findViewById(R.id.spinnerAnnee);

        dataAdapterYear = new DateAdapter(this,
                android.R.layout.simple_spinner_item, loadYear()

        );
        dataAdapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnnee.setAdapter(dataAdapterYear);
        spinnerAnnee.setSelection(0);

        values = depenseBDD.getAllDepense();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        spinnerMois.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
          {
              @Override
              public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                  String item = dataAdapterMois.getItem(position);
                  moisSelectionne = spinnerMois.getSelectedItem().toString();
                  if (!anneeSelectionnee.isEmpty()) {
                      // Assign adapter to ListView
                      listView.setAdapter(adapter);
                      if (item != null || !item.equals("")) {
                          values.clear();
                          //values = depenseBDD.getDepensesByMonth(moisSelectionne);
                          values = depenseBDD.getDepensesByMonthYear(moisSelectionne, anneeSelectionnee);
                          adapter.clear();
                          for (int i = 0; i < values.size(); i++) {
                              adapter.insert(values.get(i), i);
                          }
                          adapter.notifyDataSetChanged();
                      } else {
                          values = depenseBDD.getAllDepense();
                          adapter.clear();
                          for (int i = 0; i < values.size(); i++) {
                              adapter.insert(values.get(i), i);
                          }
                          adapter.notifyDataSetChanged();
                      }
                  }
              }

              @Override
              public void onNothingSelected(AdapterView<?> adapterView) {
                  if (!anneeSelectionnee.isEmpty()) {
                      values = depenseBDD.getDepensesByAnnee(anneeSelectionnee);
                      adapter.clear();
                      for (int i = 0; i < values.size(); i++) {
                          adapter.insert(values.get(i), i);
                      }
                      adapter.notifyDataSetChanged();
                  } else {
                      values = depenseBDD.getAllDepense();
                      adapter.clear();
                      for (int i = 0; i < values.size(); i++) {
                          adapter.insert(values.get(i), i);
                      }
                      adapter.notifyDataSetChanged();

                  }

              }
          }

        );

        spinnerAnnee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
           {
               @Override
               public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                   String item = dataAdapterYear.getItem(position);
                   anneeSelectionnee = spinnerAnnee.getSelectedItem().toString();
                   if (!moisSelectionne.isEmpty()) {
                       listView.setAdapter(adapter);
                       if (item != null || !item.equals("") || !item.equals("")) {
                           values.clear();
                           //values = depenseBDD.getDepensesByAnnee(anneeSelectionnee);
                           values = depenseBDD.getDepensesByMonthYear(moisSelectionne, anneeSelectionnee);
                           adapter.clear();
                           for (int i = 0; i < values.size(); i++) {
                               adapter.insert(values.get(i), i);
                           }
                           adapter.notifyDataSetChanged();
                       } else {
                           values = depenseBDD.getAllDepense();
                           adapter.clear();
                           for (int i = 0; i < values.size(); i++) {
                               adapter.insert(values.get(i), i);
                           }
                           adapter.notifyDataSetChanged();
                       }
                   } else {
                       values.clear();
                       //values = depenseBDD.getDepensesByMonthYear(moisSelectionne, anneeSelectionnee);
                       adapter.clear();
                       for (int i = 0; i < values.size(); i++) {
                           adapter.insert(values.get(i), i);
                       }
                       adapter.notifyDataSetChanged();

                   }
               }

               @Override
               public void onNothingSelected(AdapterView<?> adapterView) {
                   if (!moisSelectionne.isEmpty()) {
                       if (!anneeSelectionnee.isEmpty()) {
                           values = depenseBDD.getDepensesByMonth(moisSelectionne);
                           adapter.clear();
                           for (int i = 0; i < values.size(); i++) {
                               adapter.insert(values.get(i), i);
                           }
                           adapter.notifyDataSetChanged();
                       } else {
                           values = depenseBDD.getAllDepense();
                           adapter.clear();
                           for (int i = 0; i < values.size(); i++) {
                               adapter.insert(values.get(i), i);
                           }
                           adapter.notifyDataSetChanged();
                       }
                   }

               }
           }

        );

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        ArrayList<String> action = new ArrayList<String>();
        ArrayAdapter<String> dataAdapterAction = new DateAdapter(this, android.R.layout.simple_spinner_item, action);


        registerForContextMenu(listView);


    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextal_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String txt;
        int posMin, posMax, id;
        txt = adapter.getItem(info.position);
        String montant, date, cat;
        DepenseBDD depense = new DepenseBDD(this);
        depense.open();
        CategorieBDD categorie = new CategorieBDD(this);

        posMax = txt.indexOf("-");
        id = Integer.parseInt(txt.substring(0,posMax-1));
        //Toast.makeText(this, "-"+id+"-", Toast.LENGTH_LONG).show();
        switch (item.getItemId()) {
            case R.id.delete:
                Log.i("Hello!", "Clicked! 1!");
                depense.removeDepenseWithID(id);
                onResume();
                return true;
            case R.id.edit:
                Log.i("Hello!", "Clicked! 2!");
                Intent intentEdit = new Intent(this, FormulaireEditDepense.class);
                intentEdit.putExtra("id", id);
                startActivityForResult(intentEdit, 5);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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



    public int compare(String s1, String s2) {
        s1 = s1.replaceAll("[^a-zA-Z0-9]+", "");
        s2 = s2.replaceAll("[^a-zA-Z0-9]+", "");
        return s1.compareToIgnoreCase(s2);
    }


    @Override
    protected void onResume() {
        super.onResume();

        affichageBudget();
        afficherDepenses();
    }

    /**
     * Méthode de mise a jour de la base de données
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CATEGORIE) {
            if (resultCode == RESULT_OK) {
                //Si ok on ajoute dans la base de données correspondante
                Categorie newCateg = new Categorie(data.getStringExtra("newCateg"));

                //Ajout dans la base de données
                CategorieBDD categBdd = new CategorieBDD(this);
                categBdd.open();

                List<String> listCategorie = new ArrayList<String>();

                listCategorie = categBdd.getAllCategoriesName();
                String s1 = "";
                int r2 = 0;
                for (int i = 0; i < listCategorie.size(); i++) {
                    s1 = listCategorie.get(i);
                    s1 = Normalizer.normalize(s1, Normalizer.Form.NFD);
                    s1 = s1.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                }
                String s2 = data.getStringExtra("newCateg");
                s2 = Normalizer.normalize(s2, Normalizer.Form.NFD);
                s2 = s2.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                for (int i = 0; i < listCategorie.size(); i++) {
                    r2 = s1.compareToIgnoreCase(s2);
                }
                if (r2 != 0) {
                    categBdd.insertCategorie(new Categorie(data.getStringExtra("newCateg")));
                } else {
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

                List<Depense> listDep = new ArrayList<Depense>();
                listDep = cdepBdd.getAllDepenses();

                Depense ldp = new Depense();

                boolean exists = true;

                int i = 0;
                int taille = listDep.size();
                while(!exists && i < listDep.size()){
                    ldp = listDep.get(i);
                    //comparer les dépenses existants avec la nouvelle dépense à ajouter
                    exists = ldp.equals(newDep);
                    i++;
                }
                // si les dépenses sont différents
                if (!exists) {
                    cdepBdd.insertDepense(newDep);
                } else {
                    Toast.makeText(this, " Votre dépense existe déjà dans la liste", Toast.LENGTH_LONG).show();
                }
                cdepBdd.close();

            } else {
                Toast.makeText(this, "Erreur lors de l'insertion", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == IMAGE) {
            if (resultCode == RESULT_OK) {
                float montant = Float.parseFloat(data.getStringExtra("NEWDEPENSE_IMAGE"));
                Depense newDep = new Depense(data.getStringExtra("DATE_IMAGE"), montant, data.getStringExtra("CATEGORIE_IMAGE"));

                //Ajout dans la base de données
                DepenseBDD cdepBdd = new DepenseBDD(this);
                cdepBdd.open();
                cdepBdd.insertDepense(newDep);
                cdepBdd.close();
            }
        }
    }

    private void afficherDepenses() {
        ListView listView;
        listView = (ListView) findViewById(R.id.listView1);
        ArrayList<String> values = new ArrayList<String>();
        Depense d;

        values = depenseBDD.getAllDepense();
        estVide = (TextView) findViewById(R.id.vide);
        if (values.size() != 0) {

            CategorieBDD categBdd = new CategorieBDD(this);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
            // Assign adapter to ListView
            listView.setAdapter(adapter);
            listView.setTextFilterEnabled(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                    // When clicked, show a toast with the TextView text
                    Toast.makeText(getApplicationContext(),((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                }

            });
            EditText myFilter = (EditText) findViewById(R.id.search);
            myFilter.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {

                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s.toString());

                }
            });


        } else {
            estVide.setVisibility(View.VISIBLE);
        }
    }

    // Cliquer sur le bouton ok une fois on lit le message
    private final class OkOnClickListener implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            TableauDepense.this.finish();
        }
    }
    /**
     * Initialisation des années dans le menu déroulant
     *
     * @return la liste des 7 dernières années
     */
    private List<String> loadYear(){
        List listYear = new ArrayList<String>();
        listYear.add("");
        listYear.add("2010");
        listYear.add("2011");
        listYear.add("2012");
        listYear.add("2013");
        listYear.add("2014");
        listYear.add("2015");
        listYear.add("2016");
        listYear.add("2017");
        return listYear;
    }

    /**
     * Initialisation des mois dans le menu déroulant
     *
     * @return la liste des mois d'une année
     */
    private List<String> loadMonth() {
        List listMois = new ArrayList<String>();
        listMois.add("");
        listMois.add("Janvier");
        listMois.add("Février");
        listMois.add("Mars");
        listMois.add("Avril");
        listMois.add("Mai");
        listMois.add("Juin");
        listMois.add("Juillet");
        listMois.add("Août");
        listMois.add("Septembre");
        listMois.add("Octobre");
        listMois.add("Novembre");
        listMois.add("Décembre");
        return listMois;
    }
}
