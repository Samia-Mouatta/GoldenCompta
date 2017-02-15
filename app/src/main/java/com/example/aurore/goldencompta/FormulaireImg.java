package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.tesseract.android.TessBaseAPI;

/**
 * Created by Bastien on 07/12/2016.
 */

public class FormulaireImg extends Activity {

    protected Activity main = this;
    final static int PICK_IMAGE = 1;
    protected ImageView imageView;
    protected String datapath = "";
    protected TessBaseAPI mTess;
    protected Bitmap bitmap = null;
    protected String contenu;
    protected TextView ocr;
    protected Button confirmer;
    protected String d, dBis;
    protected Date systeme = new Date();
    protected Date saisi;
    private String montant = null;

    protected String[] listeMotCle = {"MONTANT", "TOTAL", "Montant", "Total", "montant", "total"};
    protected boolean estMotCle = false, soldeTrouver = false;

    /**
     * Méthode qui permet d'initialiser notre Intent
     * @param savedInstanceState le bundle utilisé pour crée la méthode
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire_img);
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerC);
        final DatePicker date = (DatePicker) findViewById(R.id.date);
        final Intent intent = new Intent();
        confirmer = (Button) findViewById(R.id.accept);

        List<String> list = new ArrayList<String>();
        List<String> listCategorie = new ArrayList<String>();

        CategorieBDD categBdd = new CategorieBDD(this);
        categBdd.open();
        listCategorie=categBdd.getAllCategoriesName();
        categBdd.close();
        for (String categ:listCategorie) {
            list.add(categ);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        ((Button) findViewById(R.id.btGallery)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btGalleryClick(v);
            }
        });

        confirmer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String cat = spinner.getSelectedItem().toString();

                if (montant == null) {
                    Toast.makeText(FormulaireImg.this,"Un photo doit etre fourni", Toast.LENGTH_SHORT).show();
                } else {
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


                        intent.putExtra("NEWDEPENSE_IMAGE", montant);
                        intent.putExtra("CATEGORIE_IMAGE", cat);
                        intent.putExtra("DATE_IMAGE", d);
                        main.setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(FormulaireImg.this, "La date doit être inférieure ou égale à la date d'aujourd'hui", Toast.LENGTH_SHORT).show();
                    }


                    System.out.println("Date du calendrier2 : " + d);
                    System.out.println("Depense : " + Float.parseFloat(montant));
                    System.out.println("Catégorie : " + cat);
                    System.out.print("Date : " + d.toString());


                }
            }
        });


    }


    /**
     * Méthode appeller lorsque que l'on revient de la méthode startActivity
     * @param requestCode Code du retour pour connaitre l'intent appeller par la méthode startActivity
     * @param resultCode Code de retour qui determine si la méthode startActivity c'est bien dérouler
     * @param data L'intent pour récupérer les données sauvegarder dans la vue appeller par le startActivity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView = (ImageView) findViewById(R.id.imageView1);
                imageView.setImageBitmap(bitmap);
                String[] split;
                int pos;
                montant = null;

                ocr = (TextView) findViewById(R.id.texteOCR);
                String language = "eng";

                datapath = getFilesDir() + "/tesseract/";
                mTess = new TessBaseAPI();

                checkFile(new File(datapath + "tessdata/"));

                mTess.init(datapath, language);
                mTess.setImage(bitmap);
                contenu = mTess.getUTF8Text();
                contenu = contenu.replaceAll(",", ".");
                contenu = contenu.replaceAll("\n", " ");

                split = contenu.split(" ");
                pos = split.length-1;
                while(!soldeTrouver && pos >= 0){
                    System.out.println(split[pos]);
                    if(!estMotCle){
                        for(int i = 0; i < listeMotCle.length; i++){
                            if (split[pos].equals(listeMotCle[i])) { estMotCle = true;}
                        }
                        pos--;
                    } else {
                        if(estReel(split[pos])) {
                            soldeTrouver = true;
                        } else {
                            pos++;
                        }
                    }
                }

                //System.out.println(split[pos]);
                if(soldeTrouver) {
                    ocr.setText(split[pos]);
                    montant = split[pos];
                }


            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Méthode qui permet d'aller cercher une image dans la gallerie
     * @param v Une view pour définir dans quel vue a été appeller la méthode
     */
    public void btGalleryClick(View v) {
        //Création puis ouverture de la boite de dialogue
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);
    }

    /**
     * Méthode pour initialiser le chemin d'accés a la librairie Tesseract
     * @param dir le nom du tesseract
     */
    private void checkFile(File dir) {
        if (!dir.exists()&& dir.mkdirs()){
            copyFiles();
        }
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/eng.traineddata";
            File datafile = new File(datafilepath);

            if (!datafile.exists()) {
                copyFiles();
            }
        }
    }

    /**
     * Méthode pour copier le fichier
     */
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

    /**
     * Méthode pour déterminer si une chaine de caractère est un réel
     * @param nombre la chaine de caractère a vérifier
     * @return
     */
    public boolean estReel(String nombre) {
        try {
            Float.parseFloat(nombre);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


}
