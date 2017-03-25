package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FormulaireCamera extends BaseActivity {
    protected Activity main = this;
    private static final int REQUEST_CODE = 99;
    private Button cameraButton;
    private ImageView scannedImageView;
    protected EditText ocr;
    protected String datapath = "";
    protected TessBaseAPI mTess;
    private String contenu = "";
    private String[] splitContenu;
    private Pattern montantExp = Pattern.compile("[Mm].[Nn][Tt]..[Tt]");
    private Pattern totalExp = Pattern.compile("[Tt].[Tt]..");
    private Matcher matcheMontant;
    private Matcher matchTotal;
    private int pos;
    private boolean motCleTrouver;
    private float montant;
    private boolean montantTrouver;
    private Button confirmer, refuser;
    protected String d, dBis;
    protected Date systeme = new Date();
    protected Date saisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferencesD = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferencesD.getBoolean(PREF_DARK_THEME, false);

        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire_camera);

        Switch toggle = (Switch) findViewById(R.id.switch1);
        toggle.setChecked(useDarkTheme);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                toggleTheme(isChecked);
            }
        });

        final Spinner spinner = (Spinner) findViewById(R.id.spinnerC);
        confirmer = (Button) findViewById(R.id.accept);
        refuser = (Button) findViewById(R.id.refus);
        final DatePicker date = (DatePicker) findViewById(R.id.date);
        final Intent intent = new Intent();

        List<String> list = new ArrayList<String>();
        List<String> listCategorie = new ArrayList<String>();

        CategorieBDD categBdd = new CategorieBDD(this);
        categBdd.open();
        listCategorie = categBdd.getAllCategoriesName();
        categBdd.close();
        for (String categ : listCategorie) {
            list.add(categ);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        refuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
        confirmer.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 String cat = spinner.getSelectedItem().toString();
                 montant = Float.valueOf(ocr.getText().toString());

                 dBis = date.getYear() + "/" + (date.getMonth() + 1) + "/" + date.getDayOfMonth();
                 saisi = new Date(dBis);

                 if (saisi.before(systeme) || saisi.equals(systeme)) {
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


                     intent.putExtra("NEWDEPENSE_IMAGE", String.valueOf(montant));
                     intent.putExtra("CATEGORIE_IMAGE", cat);
                     intent.putExtra("DATE_IMAGE", d);
                     main.setResult(RESULT_OK, intent);
                     finish();
                 } else {
                     Toast.makeText(FormulaireCamera.this, "La date doit être inférieure ou égale à la date d'aujourd'hui", Toast.LENGTH_SHORT).show();
                 }



                 }

         });
        init();
    }

    private void init() {
        cameraButton = (Button) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new ScanButtonClickListener(ScanConstants.OPEN_CAMERA));
        scannedImageView = (ImageView) findViewById(R.id.imageView1);
    }

    private class ScanButtonClickListener implements View.OnClickListener {

        private int preference;

        public ScanButtonClickListener(int preference) {
            this.preference = preference;
        }

        public ScanButtonClickListener() {
        }

        @Override
        public void onClick(View v) {
            startScan(preference);
        }
    }

    protected void startScan(int preference) {
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);
                scannedImageView.setImageBitmap(bitmap);

                ocr = (EditText) findViewById(R.id.texteOCR);
                String language = "eng";

                datapath = getFilesDir() + "/tesseract/";
                mTess = new TessBaseAPI();

                checkFile(new File(datapath + "tessdata/"));

                mTess.init(datapath, language);
                mTess.setImage(bitmap);
                contenu = mTess.getUTF8Text();

                contenu = contenu.replaceAll(",", ".");
                contenu = contenu.replaceAll("\n", " ");
                splitContenu = contenu.split(" ");
                pos = 0;
                matcheMontant = montantExp.matcher(splitContenu[pos]);
                matchTotal = totalExp.matcher(splitContenu[pos]);
                motCleTrouver = false;
                montant = 0;
                montantTrouver = false;

                while (pos < splitContenu.length && !montantTrouver) {
                    if (matcheMontant.find() || matchTotal.find()) {
                        motCleTrouver = true;
                    } else if (motCleTrouver) {
                        if (estReel(splitContenu[pos])) {
                            montant = Float.valueOf(splitContenu[pos]);
                            montantTrouver = true;
                        }
                    }
                    pos++;
                }

                if (montantTrouver) {
                    ocr.setText(String.valueOf(montant));
                } else {
                    ocr.setText("0");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }  else  if (requestCode == CATEGORIE) {
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

                    if(s1.equals(s2))
                        r2 = 0;
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

    private Bitmap convertByteArrayToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }



    private void checkFile(File dir) {
        if (!dir.exists() && dir.mkdirs()) {
            copyFiles();
        }
        if (dir.exists()) {
            String datafilepath = datapath + "/tessdata/eng.traineddata";
            File datafile = new File(datafilepath);

            if (!datafile.exists()) {
                copyFiles();
            }
        }
    }

    private void copyFiles() {
        try {
            String filepath = datapath + "/tessdata/eng.traineddata";
            AssetManager assetManager = getAssets();

            InputStream instream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }


            outstream.flush();
            outstream.close();
            instream.close();

            File file = new File(filepath);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean estReel(String nombre) {
        try {
            Float.parseFloat(nombre);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
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
}