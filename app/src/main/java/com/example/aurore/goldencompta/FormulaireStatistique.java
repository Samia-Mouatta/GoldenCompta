
package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.aurore.goldencompta.MainActivity.BUDGET;
import static com.example.aurore.goldencompta.MainActivity.CAMERA;
import static com.example.aurore.goldencompta.MainActivity.CATEGORIE;
import static com.example.aurore.goldencompta.MainActivity.DEPENSE;
import static com.example.aurore.goldencompta.MainActivity.IMAGE;

/**
 * Created by Bastien on 11/01/2017.
 */

public class FormulaireStatistique extends Activity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private Activity main = this;
    private Intent intent;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

    @Override
    /** Méthode d'initialisation de l'intent
     * @param savedInstanceState le bundle utilisé pour crée la méthode
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire_statistique);
        Button retour = (Button) findViewById(R.id.retour);
        TextView txtViewLine = (TextView) findViewById(R.id.textViewLine);


        Date date = new Date();
        Calendar myCalendar = GregorianCalendar.getInstance();
        myCalendar.setTime(date);
        int year = myCalendar.get(Calendar.YEAR);
        txtViewLine.setText("Dépense au cour de l'année " + Integer.toString(year));

        //chartView();
        chartPie();
        chartLine();


        intent = new Intent();


        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Méthode d'affichage du graphique chart, dépense par montant
     */
    private void chartView() {

        XYSeries series = new XYSeries("London Temperature hourly");

        CategorieBDD categorieBDD = new CategorieBDD(this);
        categorieBDD.open();
        DepenseBDD depenseBDD = new DepenseBDD(this);
        depenseBDD.open();
        Cursor depenseListe;
        Cursor periode;
        int i;

        String[] tranche = {"00,00", "10,00", "25,00", "50,00", "90,00"};

        for (i = 1; i < tranche.length - 1; i++) {
            depenseListe =
                    depenseBDD.selectDepenseByMontant(tranche[i], tranche[i + 1]);
            depenseListe.moveToFirst();
            if (depenseListe.getCount() == 0) {
                series.add(i, 0);
            } else {
                //series.add(i,
                Double.parseDouble(depenseListe.getString(0).replace(",", "."));
                series.add(i, 8);
            }

        }


        LinearLayout layoutView = (LinearLayout)
                findViewById(R.id.chartView);
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
        GraphicalView chartView = ChartFactory.getLineChartView(this, dataset, mRenderer);

        //barre
        //GraphicalView chartView = ChartFactory.getBarChartView(this, dataset, mRenderer, BarChart.Type.DEFAULT);

        layoutView.addView(chartView, 0);
    }

    /**
     * Methode d'affichage du graphique Line, dépense au court de l'année
     */
    private void chartLine() {

        CategorieBDD categorieBDD = new CategorieBDD(this);
        categorieBDD.open();
        DepenseBDD depenseBDD = new DepenseBDD(this);
        depenseBDD.open();
        double value;
        String mois;
        int i, montantMax;


        Date date = new Date();
        Calendar myCalendar = GregorianCalendar.getInstance();
        myCalendar.setTime(date);
        int year = myCalendar.get(Calendar.YEAR);
        String yearStr = String.valueOf(year);

        ArrayList<String> periode;
        //periode = depenseBDD.selectDepenseMois();
        periode = depenseBDD.selectAllDepensesAnnee1(year);
        montantMax = depenseBDD.montantMaxYear(year);


        if (periode.size() != 0) {
            XYSeries series = new XYSeries("Dépense sur l'année");


            for (i = 0; i < 12; i++) {
                //mois = periode.get(i).get(0);
                value = Double.parseDouble(periode.get(i).replace(",", "."));
                series.add(i, value);
            }

            series.add(i + 1, 1);

            LinearLayout layoutLine = (LinearLayout)
                    findViewById(R.id.chartLine);
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
            //mRenderer.setYAxisMax(Double.parseDouble(Integer.toString(montantMax)));
            mRenderer.setYAxisMax(montantMax + 0.1 * montantMax);
            mRenderer.setYAxisMin(0);
            mRenderer.setShowGrid(true); // we show the grid
            mRenderer.setLabelsTextSize(35);
            mRenderer.setLegendTextSize(35);
            mRenderer.setScale((float) 1);
            mRenderer.setPointSize(20);
            mRenderer.setYTitle("Montant");
            mRenderer.setXTitle("Mois");
            mRenderer.setAxisTitleTextSize(30);
            //mRenderer.setXAxisMin(1.00);


            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
            dataset.addSeries(series);

            //Ligne
            GraphicalView chartView = ChartFactory.getLineChartView(this,
                    dataset, mRenderer);


            layoutLine.addView(chartView, 0);
        }
    }

    /**
     * Méthode d'affichage du graphique Pie, dépense par catégorie
     */
    public void chartPie() {
        //camenbert
        LinearLayout layoutPie = (LinearLayout) findViewById(R.id.chartPie);

        CategorieBDD categorieBDD = new CategorieBDD(this);
        categorieBDD.open();
        DepenseBDD depenseBDD = new DepenseBDD(this);
        depenseBDD.open();
        Cursor categorieListe = categorieBDD.categorieDepense();

        double value;
        String name;

        CategorySeries categorie = new CategorySeries("categorie");

        if (categorieListe.getCount() != 0) {
            categorieListe.moveToFirst();


            for (int y = 0; y < categorieListe.getCount(); y++) {
                value = Double.parseDouble(categorieListe.getString(1));
                name = categorieListe.getString(0);
                categorie.add(name, value);
                categorieListe.moveToNext();
            }

            if (categorieListe.getCount() == 0) {
                categorie.add("Aucune dépense", 100);
            }

        /*for (int y = 0; y <5; y++) {
            value = 15;
            name = "salut";
            categorie.add(name, value);
        }*/

            int[] colors = {Color.BLUE, Color.MAGENTA, Color.GREEN,
                    Color.CYAN, Color.RED,
                    Color.YELLOW};

            // Instantiating a renderer for the Pie Chart
            DefaultRenderer defaultRenderer = new DefaultRenderer();
            defaultRenderer.setLabelsColor(Color.BLACK);
            defaultRenderer.setBackgroundColor(Color.CYAN);


            for (int i = 0; i < categorieListe.getCount(); i++) {
                SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
                seriesRenderer.setColor(colors[i % 6]);
                // Adding a renderer for a slice
                defaultRenderer.addSeriesRenderer(seriesRenderer);
            }


            defaultRenderer.setChartTitle("Android version distribution as on October 1, 2012 ");
            defaultRenderer.setChartTitleTextSize(20);
            defaultRenderer.setZoomButtonsVisible(true);

            defaultRenderer.setLabelsTextSize(25);
            defaultRenderer.setLegendTextSize(25);

            GraphicalView chartView = ChartFactory.getPieChartView(this, categorie, defaultRenderer);

            layoutPie.addView(chartView, 0);
        }
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
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();

        // ATTENTION: This was auto-generated to implement the AppIndexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client.connect();
        //AppIndex.AppIndexApi.start(client, getIndexApiAction());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client2, getIndexApiAction0());
    }

    @Override
    public void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client2, getIndexApiAction0());

        // ATTENTION: This was auto-generated to implement the AppIndexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //AppIndex.AppIndexApi.end(client, getIndexApiAction());
        //client.disconnect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.disconnect();
    }

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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction0() {
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
}
