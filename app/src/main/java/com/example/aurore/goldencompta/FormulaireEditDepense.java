package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bastien on 24/02/2017.
 */

public class FormulaireEditDepense extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire_edit_depense);

        final DepenseBDD depenseBdd = new DepenseBDD(this);
        final CategorieBDD categorieBDD = new CategorieBDD(this);
        final Depense depense = new Depense();
        final int idD, idC, num;
        Cursor c;
        List<String> listCategorie = new ArrayList<String>();
        List<String> list = new ArrayList<String>();

        final EditText montant = (EditText) findViewById(R.id.montant);
        final EditText date = (EditText) findViewById(R.id.date);
        final Spinner spinnerCat = (Spinner) findViewById(R.id.spinnerCategorie);
        Button save = (Button) findViewById(R.id.save);
        Button exit = (Button) findViewById(R.id.exit);

        depenseBdd.open();
        categorieBDD.open();

        Intent intent = getIntent();
        idD = intent.getIntExtra("id", 0);

        c = depenseBdd.getDepenseByID(idD);
        c.moveToFirst();

        idC = categorieBDD.getIdCategorie(c.getString(3));

        listCategorie=categorieBDD.getAllCategoriesName();
        categorieBDD.close();
        for (String categ:listCategorie) {
            list.add(categ);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);


        spinnerCat.setAdapter(dataAdapter);
        spinnerCat.setSelection(idC-1);

        Toast.makeText(this, c.getString(1), Toast.LENGTH_LONG).show();
        /*depense = depenseBdd.cursorToDepense(depenseBdd.getDepenseByID(id));*/



        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)   {
                depense.setMontant(Float.parseFloat(montant.getText().toString()));
                depense.setId(idD);
                depense.setCategorie(spinnerCat.getSelectedItem().toString());
                depense.setDate(date.getText().toString());
                depenseBdd.updateDepense(idD, depense);
                finish();

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)   {
                finish();
            }
        });



        montant.setText(c.getString(1));
        date.setText(c.getString(2));


    }


}
