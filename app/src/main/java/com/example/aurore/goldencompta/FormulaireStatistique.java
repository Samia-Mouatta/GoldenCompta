package com.example.aurore.goldencompta;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by Bastien on 11/01/2017.
 */

public class FormulaireStatistique extends Activity {

    @Override
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
        Toast toast = Toast.makeText(getApplicationContext(), "valeur " + periode.getString(0) + "nb " + periode.getCount() , Toast.LENGTH_SHORT);
        toast.show();
        for (i=1; i<periode.getCount(); i++) {
            value = periode.getString(0);
            url = url + value + ",";

            periode.moveToNext();
        }

        url = url.substring(0, url.length()-1);

        depenseByPeriode.loadUrl(url);

        /*webView.loadUrl(url);
        webView.
        setContentView(webView);*/


    }

}
