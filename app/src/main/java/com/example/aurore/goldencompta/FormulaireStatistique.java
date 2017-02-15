package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import static com.example.aurore.goldencompta.MainActivity.BUDGET;
import static com.example.aurore.goldencompta.MainActivity.CAMERA;
import static com.example.aurore.goldencompta.MainActivity.CATEGORIE;
import static com.example.aurore.goldencompta.MainActivity.DEPENSE;
import static com.example.aurore.goldencompta.MainActivity.IMAGE;


public class FormulaireStatistique extends Activity {

    @Override
    /** Méthode d'initialisation de l'intent
     * @param savedInstanceState le bundle utilisé pour crée la méthode
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire_statistique);
        int i;
        String name, value;
        WebView webView = new WebView(this);

        WebView depenseByCategorie = (WebView) findViewById(R.id.depenseByCategorie);
        WebView depenseByMontant = (WebView) findViewById(R.id.depenseByMontant);
        WebView depenseByPeriode = (WebView) findViewById(R.id.depenseByPeriode);


        CategorieBDD categorieBDD = new CategorieBDD(this);
        categorieBDD.open();
        DepenseBDD depenseBDD = new DepenseBDD(this);
        depenseBDD.open();

        Cursor categorieListe = categorieBDD.categorieDepense();
        Cursor depenseListe;
        Cursor periode;

        //PREMIER GRAPHIQUE *************************
        categorieListe.moveToFirst();
        String url = "http://chart.apis.google.com/chart?cht=p&chs=300x120&chd=t:";


        for (i=0; i<categorieListe.getCount(); i++) {
           value = categorieListe.getString(1);
            url = url + value + ",";

            categorieListe.moveToNext();


        }

       url = url.substring(0, url.length()-1);
        url = url + "&chl=";
        categorieListe.moveToFirst();

        for (i=0; i<categorieListe.getCount(); i++) {
            name = categorieListe.getString(0);
            url = url + name + "|";
            categorieListe.moveToNext();
        }

        url = url.substring(0, url.length()-1);

        webView.loadUrl(url);



        //**********************************************
        webView.setDrawingCacheEnabled(true);
        webView.buildDrawingCache(false);


        depenseByCategorie.loadUrl(url);


        //DEUXIEME GRAPHIQUE *************************

        url = "http://chart.apis.google.com/chart?cht=bvs&chs=300x120&chd=t:";

        depenseListe = depenseBDD.selectDepenseByMontant("00,00","10,00");
        depenseListe.moveToFirst();
        if (depenseListe.getCount() == 0) {
            url = url + "0,";
        }
        else {
            url = url + depenseListe.getString(0) + ",";
        }

        depenseListe = depenseBDD.selectDepenseByMontant("10,00","25,00");
        depenseListe.moveToFirst();
        if (depenseListe.getCount() == 0) {
            url = url + "0,";
        }
        else {
            url = url + depenseListe.getString(0) + ",";
        }

        depenseListe = depenseBDD.selectDepenseByMontant("20,00","50,00");
        depenseListe.moveToFirst();
        if (depenseListe.getCount() == 0) {
            url = url + "0,";
        }
        else {
            url = url + depenseListe.getString(0) + ",";
        }

        depenseListe = depenseBDD.selectDepenseByMontant("50,00","90,00");
        depenseListe.moveToFirst();
        if (depenseListe.getCount() == 0) {
            url = url + "0&chl=";
        }
        else {
            url = url + depenseListe.getString(0) + "&chl=";
        }


        url = url +"10|20|50|100";
        url = url.replace("null", "0");
        depenseByMontant.loadUrl(url);



        //TROISIEME GRAPHIQUE *************************

        url = "http://chart.apis.google.com/chart?cht=lc&chs=300x120&chd=t:";

        periode = depenseBDD.selectDepenseMois();
        periode.moveToFirst();

        for (i=1; i<periode.getCount(); i++) {
            value = periode.getString(0);
            url = url + value + ",";

            periode.moveToNext();
        }

        url = url.substring(0, url.length()-1);

        depenseByPeriode.loadUrl(url);
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
