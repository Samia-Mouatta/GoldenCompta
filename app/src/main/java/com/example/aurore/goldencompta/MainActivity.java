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
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends Activity {

    public static int CATEGORIE = 0;
    public static int DEPENSE = 1;
    public static int BUDGET = 2;
    public static int IMAGE = 3;

    DepenseBDD depenseBDD = new DepenseBDD(this);
    TableLayout t1;
    ArrayAdapter<String> adapter = null;

    public final static int CHOOSE_BUTTON_REQUEST = 0;

    /**
     * Méthode principale du lancement de l'application
     *
     * @param savedInstanceState
     */
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

        affichageBudget();

        values = depenseBDD.getAllDepense();

        if (values.size() != 0) {
            //System.out.println("Categorie : " + values.get(1));

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
            // Assign adapter to ListView
            listView.setAdapter(adapter);
            //enables filtering for the contents of the given ListView
            listView.setTextFilterEnabled(true);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // When clicked, show a toast with the TextView text
                    Toast.makeText(getApplicationContext(),
                            ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
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


            //System.out.println("Categorie : " + adapter.getItem(1));
        } else {
            estVide.setVisibility(View.VISIBLE);
        }


        //Création de l'instance de la classe CategorieBDD
        CategorieBDD categBdd = new CategorieBDD(this);


    }
/*
    private void displayListView() {

        //Array list of depenses
        ArrayList<String> values = new ArrayList<String>();
        values = depenseBDD.getAllDepense();
        TextView estVide;
        estVide = (TextView) findViewById(R.id.vide);

        if (values.size() != 0) {
            //create an ArrayAdaptar from the String Array
            dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
            ListView listView = (ListView) findViewById(R.id.listView1);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);
            //enables filtering for the contents of the given ListView
            listView.setTextFilterEnabled(true);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // When clicked, show a toast with the TextView text
                    Toast.makeText(getApplicationContext(),
                            ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                }
            });
            EditText myFilter = (EditText) findViewById(R.id.search);
            myFilter.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    dataAdapter.getFilter().filter(s.toString());
                }
            });
        }else{
            estVide.setVisibility(View.VISIBLE);
        }


    }*/

    /**
     * Méthode de création du menu
     *
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
     *
     * @param item
     * @return
     */
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
            case R.id.menu_statistique:
                //Comportement du bouton "camera"
                Intent intentStat = new Intent(this, FormulaireStatistique.class);
                startActivityForResult(intentStat, BUDGET);
                return true;
            case R.id.menu_img:
                Intent intentImage = new Intent(this, FormulaireImg.class);
                startActivityForResult(intentImage, IMAGE);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public int compare(String s1, String s2) {
        s1 = s1.replaceAll("[^a-zA-Z0-9]+", "");
        s2 = s2.replaceAll("[^a-zA-Z0-9]+", "");
        return s1.compareToIgnoreCase(s2);
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

                listCategorie=categBdd.getAllCategoriesName();
                String s1="";
                int r2=0;
                for (int i=0;i<listCategorie.size();i++) {
                    s1=listCategorie.get(i);
                  //  System.out.println("s1 - avant: " + s1);
                    s1 = Normalizer.normalize(s1, Normalizer.Form.NFD);
                    s1 = s1.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                  //  System.out.println("s1 - après: " + s1);
                }
                String s2 = data.getStringExtra("newCateg");
               // System.out.println("s2-avant: " + s2);
                s2 = Normalizer.normalize(s2, Normalizer.Form.NFD);
                s2 = s2.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                //System.out.println("s2-après: " + s2);
                for (int i=0;i<listCategorie.size();i++) {
                   r2 = s1.compareToIgnoreCase(s2);
                }
                //compare(r2);
                if(r2!=0){
                    categBdd.insertCategorie(new Categorie(data.getStringExtra("newCateg")));
                }else{
                    Toast.makeText(this, "Cette catégorie existe dans la BDD", Toast.LENGTH_LONG).show();
                }
                categBdd.close();
            }

    }
        else if(requestCode==DEPENSE) {
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

    else if(requestCode==IMAGE)

    {
        if (resultCode == RESULT_OK) {
            float montant = Float.parseFloat(data.getStringExtra("NEWDEPENSE_IMAGE"));
            Depense newDep = new Depense(data.getStringExtra("DATE_IMAGE"), montant, data.getStringExtra("CATEGORIE_IMAGE"));

            //Ajout dans la base de données
            DepenseBDD cdepBdd = new DepenseBDD(this);
            cdepBdd.open();
            cdepBdd.insertDepense(newDep);
            cdepBdd.close();
        }
    }/*else if (requestCode == BUDGET){
            if (monBudget < mesDepenses) {
                // afficher une boite de dialogue d'alerte
                Builder builder = new Builder(this);
                builder.setTitle("Alerte dépassement budget");
                builder.setIcon(R.mipmap.alert);
                builder.setMessage("Attention! Vous avez dépassé votre budget par mois");
                builder.setCancelable(false);
                builder.setNegativeButton("OK", new OkOnClickListener());
                AlertDialog dialog = builder.create();
                dialog.show();
            }


        }*/
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
        System.out.println("Mon Budget = " + myBudget);
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
            Toast toast = Toast.makeText(getApplicationContext(), "nb " + mois + "testo " + depenseMois.getString(0), Toast.LENGTH_SHORT);
            toast.show();
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


// Cliquer sur le bouton ok une fois on lit le message
private final class OkOnClickListener implements
        DialogInterface.OnClickListener {
    public void onClick(DialogInterface dialog, int which) {
        MainActivity.this.finish();
    }
}
}