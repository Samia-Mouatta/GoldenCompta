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

        import java.util.Date;

        import static com.example.aurore.goldencompta.MainActivity.BUDGET;
        import static com.example.aurore.goldencompta.MainActivity.CAMERA;
        import static com.example.aurore.goldencompta.MainActivity.CATEGORIE;
        import static com.example.aurore.goldencompta.MainActivity.DEPENSE;
        import static com.example.aurore.goldencompta.MainActivity.IMAGE;

/**
 * Created by samia on 01/03/2017.
 */


public class FormulaireEconomie extends Activity {

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

        // icon
        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);

      /*  SharedPreferences preferences = getSharedPreferences (eco,0);
        economie = preferences.getString(eco, "Vos economies sont à 0 pour le moment");
        ecoMontant.setText(economie);*/

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_deb = deb.getYear() + "/" + (deb.getMonth() + 1) + "/" + deb.getDayOfMonth();
                System.out.println(" date : " + date_deb);
                saisiDeb = new Date(date_deb);

                date_fin = fin.getYear() + "/" + (fin.getMonth() + 1) + "/" + fin.getDayOfMonth();
                System.out.println(" date : " + date_fin);
                saisiFin = new Date(date_fin);

                if ((saisiDeb.before(systeme) || saisiDeb.equals(systeme)) && (saisiFin.before(systeme) || saisiFin.equals(systeme)) && saisiDeb.before(saisiFin)) {
                    if ((deb.getMonth() < 9) || (fin.getMonth() < 9)) {
                        date_deb = "0" + (deb.getMonth() + 1);
                        date_fin = "0" + (fin.getMonth() + 1);
                        System.out.println(" mois deb : " + date_deb);
                        System.out.println(" mois fin: " + date_fin);
                    } else {
                        date_deb = " " + (deb.getMonth() + 1);
                        date_fin = " " + (fin.getMonth() + 1);
                        System.out.println(" mois deb: " + date_deb);
                        System.out.println(" mois fin: " + date_fin);
                    }

                    DepenseBDD depense = new DepenseBDD(main);
                    Cursor sommeCursor;
                    double total = 0;
                    sommeCursor = depense.selectDepenseBewteenMonth(date_deb,date_fin);

                    if (sommeCursor.getCount() != 0) {
                        sommeCursor.moveToFirst();
                        total = sommeCursor.getDouble(0);
                    }
                    System.out.println("Total entre: " + date_deb+" et "+date_fin+" = "+total);
                    Budget bud= new Budget();
                    BudgetBDD newbud = new BudgetBDD(main);
                    bud = newbud.selectLastBudget();
                    double economie = total - bud.getMontant();
                    System.out.println("vos economies = : " + economie);
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
            case R.id.menu_economie:
                Intent intentEco = new Intent(this, FormulaireEconomie.class);
                startActivity(intentEco);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}