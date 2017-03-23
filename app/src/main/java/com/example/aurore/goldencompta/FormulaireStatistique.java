
package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

public class FormulaireStatistique extends BaseActivity {

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
        SharedPreferences preferencesD = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferencesD.getBoolean(PREF_DARK_THEME, false);

        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire_statistique);

        Switch toggle = (Switch) findViewById(R.id.switch1);
        toggle.setChecked(useDarkTheme);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                toggleTheme(isChecked);
            }
        });
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
                int i = 0;
                while(i < listCategorie.size() && r2==1) {
                    s1 = listCategorie.get(i);
                    s1 = Normalizer.normalize(s1, Normalizer.Form.NFD);
                    s1 = s1.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

                    r2 = s1.compareToIgnoreCase(s2);
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
