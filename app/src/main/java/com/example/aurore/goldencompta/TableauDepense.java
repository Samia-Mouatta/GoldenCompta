package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.example.aurore.goldencompta.MainActivity.BUDGET;
import static com.example.aurore.goldencompta.MainActivity.CAMERA;
import static com.example.aurore.goldencompta.MainActivity.CATEGORIE;
import static com.example.aurore.goldencompta.MainActivity.DEPENSE;
import static com.example.aurore.goldencompta.MainActivity.IMAGE;

public class TableauDepense  extends Activity {


    DepenseBDD depenseBDD = new DepenseBDD(this);
    ArrayAdapter<String> adapter = null;

    Spinner spinnerMois, spinnerAnnee, spinnerCateg;
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

        affichageBudget();
        afficherDepenses();

        values = new ArrayList<String>();
        //Création de l'instance de la classe CategorieBDD
        CategorieBDD categBdd = new CategorieBDD(this);

        //Array list of depenses
        values = depenseBDD.getAllDepense();
        estVide = (TextView) findViewById(R.id.vide);

        listView = (ListView) findViewById(R.id.listView1);


        spinnerCateg = (Spinner) findViewById(R.id.spinCat);
        // spinnerCateg.setOnItemSelectedListener(this);

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

        spinnerCateg.setAdapter(categAdapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        // Assign adapter to ListView
        spinnerCateg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categ = categAdapter.getItem(position);

                if (values.size() != 0) {

                    // Assign adapter to ListView
                    listView.setAdapter(adapter);
                    if (categ != null || !categ.equals("")) {
                        adapter.getFilter().filter(categ, new Filter.FilterListener() {
                            @Override
                            public void onFilterComplete(int count) {

                            }
                        });
                    } else {
                        listView.setAdapter(adapter);
                    }
                } else {
                    estVide.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        dateView = (TextView)findViewById(R.id.date);

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

        affichageBudget();

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
                // categBdd.insertCategorie(newCateg);
                //    Categorie existCateg = categBdd.getCategorieWithNom(newCateg.getNom());

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

        }else if(requestCode==DEPENSE) {
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
        } else if(requestCode==IMAGE) {
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

    public static void compare(int r) {
        if (r==0) {
            System.out.println("ils sont égaux");
        } else {
            System.out.println("ils sont différents");
        }
    }


    /**
     * Méthode permettant d'afficher la jauge du budget avec l'API google chart
     */
    private void affichageBudget() {

        String BUD = "budget";
        int mois;
        double monBudget, mesDepenses, value;
        String myBudget, month, depenseString;
        Date date;
        DepenseBDD depense = new DepenseBDD(this);

        SharedPreferences preferences = getSharedPreferences(BUD, 0);
        myBudget = preferences.getString(BUD, "Aucun budget");
        if (!myBudget.equals("Aucun budget")) {
            monBudget = Double.parseDouble(myBudget);
        } else {
            monBudget = 0;
        }
        //monBudget = 100;
        date = new Date();
        Calendar myCalendar = GregorianCalendar.getInstance();
        myCalendar.setTime(date);
        mois = myCalendar.MONTH;
        month = String.valueOf(mois);
        if (mois < 10) {
            month = "0" + month;
        }

        Cursor depenseMois = depense.selectDepenseOneMois(month);

        depenseMois.moveToFirst();
        if (depenseMois.getCount() != 0) {
            depenseString = depenseMois.getString(0).replace(",", ".");
        } else {
            depenseString = "0";
        }

        mesDepenses = Double.parseDouble(depenseString);


        if (!myBudget.equals("Aucun budget")) {
            value = mesDepenses * 100 / monBudget;
        } else {
            value = mesDepenses * 100 / 500;
        }

        WebView budget = (WebView) findViewById(R.id.budget);

        String url = "http://chart.apis.google.com/chart?cht=gom&chco=12FE01,F6FE01,FE0101&chs=300x120&chd=t:" + value + "&chxt=x,y&chxl=0:|" + mesDepenses + "|1:|0|" + monBudget;
        budget.loadUrl(url);
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

        } else {
            estVide.setVisibility(View.VISIBLE);
        }
    }
    /**
     * Initialisation des années dans le menu déroulant
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
     * @return la liste des mois d'une année
     */
    private List<String> loadMonth(){
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
