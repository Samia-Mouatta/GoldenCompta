package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class FormulaireEconomie extends BaseActivity {

    private Activity main = this;
    String d, date_deb,date_fin;
    Date systeme = new Date();
    Date saisiDeb,saisiFin;

    /**
     * Méthode qui permet d'initialiser notre Intent
     * @param savedInstanceState le bundle utilisé pour créer la méthode
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire_economie);

        final String economie;
        final String eco = "Economie";
        final Intent intent = new Intent();

        final DatePicker deb = (DatePicker) findViewById(R.id.dateDeb);
        final DatePicker fin = (DatePicker) findViewById(R.id.dateFin);

        final Button ok = (Button) findViewById(R.id.Ok);
        final Button retour = (Button) findViewById(R.id.retour);

        final TextView ecoView = (TextView) findViewById(R.id.ecoView);


      /*  SharedPreferences preferences = getSharedPreferences (eco,0);
        economie = preferences.getString(eco, "Vos economies sont à 0 pour le moment");
        ecoMontant.setText(economie);*/

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mois_deb, mois_fin, annee_deb, annee_fin;
                date_deb = deb.getYear() + "/" + (deb.getMonth() + 1) + "/" + deb.getDayOfMonth();
                System.out.println(" date : " + date_deb);
                saisiDeb = new Date(date_deb);

                date_fin = fin.getYear() + "/" + (fin.getMonth() + 1) + "/" + fin.getDayOfMonth();
                saisiFin = new Date(date_fin);

                if ((saisiDeb.before(systeme) || saisiDeb.equals(systeme)) && (saisiFin.before(systeme) || saisiFin.equals(systeme)) && saisiDeb.before(saisiFin)) {
                    if ((deb.getMonth() < 9) || (fin.getMonth() < 9)) {
                        mois_deb = "0" + (deb.getMonth() + 1);
                        mois_fin = "0" + (fin.getMonth() + 1);
                    } else {
                        mois_deb = " " + (deb.getMonth() + 1);
                        mois_fin = " " + (fin.getMonth() + 1);
                    }

                    annee_deb = Integer.toString(deb.getYear());
                    annee_fin = Integer.toString(fin.getYear());

                    DepenseBDD depense = new DepenseBDD(main);
                    BudgetBDD budget = new BudgetBDD(main);
                    double total, economie;
                    int mois_d, annee_d, mois_f, annee_f;
                    mois_d = Integer.parseInt(mois_deb);
                    annee_d = Integer.parseInt(annee_deb);
                    mois_f =  Integer.parseInt(mois_fin);
                    annee_f = Integer.parseInt(annee_fin);

                    total = 0;
                    float totalDepense = depense.selectDepenseBewteenMonth(mois_d,annee_d ,mois_f, annee_f);
                    float totalBudget = budget.selectBudgetBetweenMonth(mois_d,annee_d ,mois_f, annee_f);
                    Cursor last = budget.gettLastBudget();
                    last.moveToLast();

                    float lastBud = last.getFloat(1);
                    lastBud = lastBud * ((12 * (annee_f - annee_d)) + (mois_f - mois_d));

                    economie = totalBudget - lastBud;

                    SharedPreferences preferences = getSharedPreferences(eco, 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(eco,String.valueOf(total));
                    editor.commit();
                    ecoView.setText(String.valueOf(economie));
                    intent.putExtra("Economie", economie);
                    main.setResult(RESULT_OK, intent);
                    // finish();
                }else {
                    Toast.makeText(FormulaireEconomie.this, "La date doit être inférieure ou égale à la date d'aujourd'hui", Toast.LENGTH_SHORT).show();
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
                int r2 = 1;
                String s2 = data.getStringExtra("newCateg");
                s2 = Normalizer.normalize(s2, Normalizer.Form.NFD);
                s2 = s2.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                s2 = s2.toLowerCase();
                int i = 0;
                while(i < listCategorie.size() && r2==1) {
                    s1 = listCategorie.get(i);
                    s1 = Normalizer.normalize(s1, Normalizer.Form.NFD);
                    s1 = s1.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                    s1 = s1.toLowerCase();

                    if(s1.equals(s2))
                        r2 = 0;
                    else
                        i++;
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

                boolean exists = false;

                int i = 0;
                int taille = listDep.size();
                while(!exists && i < listDep.size()){
                    ldp = listDep.get(i);
                    //comparer les dépenses existants avec la nouvelle dépense à ajouter
                    exists = ldp.equals(newDep);
                    i++;
                }
                // si les dépenses sont différents
                if (!exists || taille == 0) {
                    cdepBdd.insertDepense(newDep);
                } else {
                    Toast.makeText(this, " Votre dépense existe déjà dans la liste", Toast.LENGTH_LONG).show();
                }
                cdepBdd.close();

            } else {
                Toast.makeText(this, "Erreur lors de l'insertion", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == IMAGE || requestCode == CAMERA) {
            if (resultCode == RESULT_OK) {
                float montant = Float.parseFloat(data.getStringExtra("NEWDEPENSE_IMAGE"));
                Depense newDep = new Depense(data.getStringExtra("DATE_IMAGE"), montant, data.getStringExtra("CATEGORIE_IMAGE"));

                //Ajout dans la base de données
                DepenseBDD cdepBdd = new DepenseBDD(this);
                cdepBdd.open();

                List<Depense> listDep = new ArrayList<Depense>();
                listDep = cdepBdd.getAllDepenses();

                Depense ldp = new Depense();

                boolean exists = false;

                int i = 0;
                int taille = listDep.size();
                while(!exists && i < listDep.size()){
                    ldp = listDep.get(i);
                    //comparer les dépenses existants avec la nouvelle dépense à ajouter
                    exists = ldp.equals(newDep);
                    i++;
                }
                // si les dépenses sont différents
                if (!exists || taille == 0) {
                    cdepBdd.insertDepense(newDep);
                } else {
                    Toast.makeText(this, " Votre dépense existe déjà dans la liste", Toast.LENGTH_LONG).show();
                }
                cdepBdd.close();
            }
        } else if (requestCode == BUDGET) {
            if (resultCode == RESULT_OK) {
                float montant = Float.parseFloat(data.getStringExtra("NEWBUDGET"));
                String nettoyage = data.getStringExtra("NEWDATENETTOYAGE");

                Budget budget = new Budget(montant);
                BudgetBDD budgetBDD = new BudgetBDD(this);
                budgetBDD.open();
                Budget lastbugd = new Budget();
                lastbugd = budgetBDD.selectLastBudget();
                budgetBDD.insertBudget(budget);

                if (lastbugd != null) {
                    Date ajd = new Date();
                    lastbugd.setDateFin(ajd.toString());

                    budgetBDD.updateBudget(lastbugd.getId(), lastbugd);
                }
                Toast.makeText(this, "Budget enregistré", Toast.LENGTH_LONG).show();
                depenseBDD.nettoyage(nettoyage);
                budgetBDD.close();
            }
        }
    }
}