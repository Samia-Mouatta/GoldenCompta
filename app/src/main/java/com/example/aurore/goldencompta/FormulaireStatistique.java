package com.example.aurore.goldencompta;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Bastien on 11/01/2017.
 */

public class FormulaireStatistique extends Activity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    /** Méthode d'initialisation de l'intent
     * @param savedInstanceState le bundle utilisé pour crée la méthode
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire_statistique);
        chartView();
        chartPie();
        chartLine();

    }


    private void chartView(){

        XYSeries series = new XYSeries("London Temperature hourly");

        CategorieBDD categorieBDD = new CategorieBDD(this);
        categorieBDD.open();
        DepenseBDD depenseBDD = new DepenseBDD(this);
        depenseBDD.open();
        Cursor depenseListe;
        Cursor periode;
        int i;

        String[] tranche = { "00,00","10,00","25,00","50,00","90,00"};

        for(i=1; i<tranche.length-1; i++) {
            depenseListe = depenseBDD.selectDepenseByMontant(tranche[i],tranche[i+1]);
            depenseListe.moveToFirst();
            if (depenseListe.getCount() == 0) {
                series.add(i, 0);
            } else {
                //series.add(i, Double.parseDouble(depenseListe.getString(0).replace(",", ".")));
                series.add(i, 8);
            }

        }


        LinearLayout layoutView = (LinearLayout) findViewById(R.id.chartView);
        // Now we create the renderer
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(5);
        renderer.setColor(Color.RED);
// Include low and max value
        renderer.setDisplayBoundingPoints(true);
// we add point markers
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setPointStrokeWidth(3);

        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);


        // We want to avoid black border
// transparent margins
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
// Disable Pan on two axis
        mRenderer.setPanEnabled(false, false);
        mRenderer.setYAxisMax(35);
        mRenderer.setYAxisMin(0);
        mRenderer.setShowGrid(true); // we show the grid
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);

        //Ligne
        //GraphicalView chartView = ChartFactory.getLineChartView(this, dataset, mRenderer);

        //barre
        GraphicalView chartView = ChartFactory.getBarChartView(this, dataset, mRenderer, BarChart.Type.DEFAULT);

        layoutView.addView(chartView,0);
    }

    private void chartLine(){

        CategorieBDD categorieBDD = new CategorieBDD(this);
        categorieBDD.open();
        DepenseBDD depenseBDD = new DepenseBDD(this);
        depenseBDD.open();
        double value;
        String mois;
        int i;

        Date date = new Date();
        Calendar myCalendar = GregorianCalendar.getInstance();
        myCalendar.setTime(date);
        int year = myCalendar.YEAR;
        String yearStr = String.valueOf(year);

        Cursor periode;
        //periode = depenseBDD.selectDepenseMois();
        periode = depenseBDD.selectAllDepensesAnnee("2007");
        periode.moveToFirst();


        XYSeries series = new XYSeries("Dépense sur l'année");


        for (i = 0; i < periode.getCount(); i++) {
            value = Double.parseDouble(periode.getString(0).replace(",", "."));
            mois = periode.getString(1);
            series.add(i, value);

            periode.moveToNext();
        }

        LinearLayout layoutLine = (LinearLayout) findViewById(R.id.chartLine);
        // Now we create the renderer
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(5);
        renderer.setColor(Color.RED);
        // Include low and max value
        renderer.setDisplayBoundingPoints(true);
        // we add point markers
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setPointStrokeWidth(3);


        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        mRenderer.setBarWidth(5);
        mRenderer.setBackgroundColor(15);



        // We want to avoid black border
        // transparent margins
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        // Disable Pan on two axis
        mRenderer.setPanEnabled(false, false);
        mRenderer.setYAxisMax(100);
        mRenderer.setYAxisMin(0);
        mRenderer.setShowGrid(true); // we show the grid
        mRenderer.setLabelsTextSize(25);
        mRenderer.setLegendTextSize(25);
        mRenderer.setScale((float) 1);
        mRenderer.setPointSize(20);

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);



        //Ligne
        GraphicalView chartView = ChartFactory.getLineChartView(this, dataset, mRenderer);


        layoutLine.addView(chartView,0);
    }

    public void chartPie()   {
        //camenbert
        LinearLayout layoutPie = (LinearLayout) findViewById(R.id.chartPie);

        CategorieBDD categorieBDD = new CategorieBDD(this);
        categorieBDD.open();
        DepenseBDD depenseBDD = new DepenseBDD(this);
        depenseBDD.open();

        Cursor categorieListe = categorieBDD.categorieDepense();
        Cursor depenseListe;
        Cursor periode;

        double value;
        String name;

        CategorySeries categorie = new CategorySeries("categorie");

        categorieListe.moveToFirst();


        for (int y = 0; y < categorieListe.getCount(); y++) {
            value = Double.parseDouble(categorieListe.getString(1));
            name = categorieListe.getString(0);
            categorie.add(name, value);
            categorieListe.moveToNext();
        }

        double[] distribution = { 3.9, 12.9, 55.8, 1.9, 23.7, 1.8 } ;
        int[] colors = { Color.BLUE, Color.MAGENTA, Color.GREEN, Color.CYAN, Color.RED,
                Color.YELLOW };

        // Instantiating a renderer for the Pie Chart
        DefaultRenderer defaultRenderer  = new DefaultRenderer();
        for(int i = 0 ;i<distribution.length;i++){
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        defaultRenderer.setChartTitle("Android version distribution as on October 1, 2012 ");
        defaultRenderer.setChartTitleTextSize(20);
        defaultRenderer.setZoomButtonsVisible(true);

        defaultRenderer.setLabelsTextSize(25);
        defaultRenderer.setLegendTextSize(25);

        GraphicalView chartView = ChartFactory.getPieChartView(this, categorie, defaultRenderer);


        layoutPie.addView(chartView,0);
    }

    public void googlChart()    {

        int i;
        String name, value;
        WebView webView = new WebView(this);

        //WebView depenseByCategorie = (WebView) findViewById(R.id.depenseByCategorie);
        //WebView depenseByMontant = (WebView) findViewById(R.id.depenseByMontant);
        //WebView depenseByPeriode = (WebView) findViewById(R.id.depenseByPeriode);


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


        for (i = 0; i < categorieListe.getCount(); i++) {
            value = categorieListe.getString(1);
            url = url + value + ",";

            categorieListe.moveToNext();


        }

        url = url.substring(0, url.length() - 1);
        url = url + "&chl=";
        categorieListe.moveToFirst();

        for (i = 0; i < categorieListe.getCount(); i++) {
            name = categorieListe.getString(0);
            url = url + name + "|";
            categorieListe.moveToNext();
        }

        url = url.substring(0, url.length() - 1);

        webView.loadUrl(url);


        //**********************************************
        webView.setDrawingCacheEnabled(true);
        webView.buildDrawingCache(false);


        //depenseByCategorie.loadUrl(url);


        //DEUXIEME GRAPHIQUE *************************

        url = "http://chart.apis.google.com/chart?cht=bvs&chs=300x120&chd=t:";

        depenseListe = depenseBDD.selectDepenseByMontant("00,00", "10,00");
        depenseListe.moveToFirst();
        if (depenseListe.getCount() == 0) {
            url = url + "0,";
        } else {
            url = url + depenseListe.getString(0) + ",";
        }

        depenseListe = depenseBDD.selectDepenseByMontant("10,00", "25,00");
        depenseListe.moveToFirst();
        if (depenseListe.getCount() == 0) {
            url = url + "0,";
        } else {
            url = url + depenseListe.getString(0) + ",";
        }

        depenseListe = depenseBDD.selectDepenseByMontant("20,00", "50,00");
        depenseListe.moveToFirst();
        if (depenseListe.getCount() == 0) {
            url = url + "0,";
        } else {
            url = url + depenseListe.getString(0) + ",";
        }

        depenseListe = depenseBDD.selectDepenseByMontant("50,00", "90,00");
        depenseListe.moveToFirst();
        if (depenseListe.getCount() == 0) {
            url = url + "0&chl=";
        } else {
            url = url + depenseListe.getString(0) + "&chl=";
        }


        url = url + "10|20|50|100";
        url = url.replace("null", "0");
        //depenseByMontant.loadUrl(url);


        //TROISIEME GRAPHIQUE *************************

        url = "http://chart.apis.google.com/chart?cht=lc&chs=300x120&chd=t:";

        periode = depenseBDD.selectDepenseMois();
        periode.moveToFirst();

        for (i = 1; i < periode.getCount(); i++) {
            value = periode.getString(0);
            url = url + value + ",";

            periode.moveToNext();
        }

        url = url.substring(0, url.length() - 1);

        //depenseByPeriode.loadUrl(url);



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("FormulaireStatistique Page") // TODO: Define a title for the content shown.
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
        //client.connect();
        //AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //AppIndex.AppIndexApi.end(client, getIndexApiAction());
        //client.disconnect();
    }
}
