package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FormulaireEditDepense extends BaseActivity {
    String d= "";
    Date ajd = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferencesD = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferencesD.getBoolean(PREF_DARK_THEME, false);

        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire_edit_depense);

        Switch toggle = (Switch) findViewById(R.id.switch1);
        toggle.setChecked(useDarkTheme);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                toggleTheme(isChecked);
            }
        });

        final DepenseBDD depenseBdd = new DepenseBDD(this);
        final CategorieBDD categorieBDD = new CategorieBDD(this);
        final Depense depense = new Depense();
        final int idD, idC;
        Cursor c;
        List<String> listCategorie = new ArrayList<String>();
        List<String> list = new ArrayList<String>();

        final EditText montant = (EditText) findViewById(R.id.montant);
        //final EditText date = (EditText) findViewById(R.id.date);

        final DatePicker date = (DatePicker) findViewById(R.id.date);
        EditText dateBase = (EditText)findViewById(R.id.date);

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

        String dBis = date.getYear() + "/" + (date.getMonth() + 1) + "/" + date.getDayOfMonth();
        Date saisi = new Date(dBis);

        if (saisi.before(ajd) || saisi.equals(ajd)) {
            if (date.getDayOfMonth() < 10) {
                d = "0" + date.getDayOfMonth();
            } else {
                d = String.valueOf(date.getDayOfMonth());
            }
            if (date.getMonth() < 9) {
                d += "/0" + (date.getMonth() + 1) + "/" + date.getYear();
            } else {
                d += "/" + (date.getMonth() + 1) + "/" + date.getYear();
            }


        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)   {
                depense.setMontant(Float.parseFloat(montant.getText().toString()));
                depense.setId(idD);
                depense.setCategorie(spinnerCat.getSelectedItem().toString());
                depense.setDate(d.toString());
                depenseBdd.updateDepense(idD, depense);
                finish();
            }
        });finish();
        } else {
            Toast.makeText(FormulaireEditDepense.this,"La date doit être inférieure ou égale à la date d'aujourd'hui", Toast.LENGTH_SHORT).show();
        }

        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)   {
                finish();
            }
        });

        montant.setText(c.getString(1));
        //date.setText(c.getString(2));
    }
}