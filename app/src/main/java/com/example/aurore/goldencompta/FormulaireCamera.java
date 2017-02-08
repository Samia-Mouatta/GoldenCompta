package com.example.aurore.goldencompta;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.aurore.goldencompta.R.id.date;

/**
 * Created by utilisateur on 08/02/2017.
 */

public class FormulaireCamera extends Activity {

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire_camera);
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


                 System.out.println("Date du calendrier2 : " + d);
                 System.out.println("Depense : " + montant);
                 System.out.println("Catégorie : " + cat);
                 System.out.print("Date : " + d.toString());


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
                        System.out.println("COOOOOOL");
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
                    System.out.println("Trouver");
                } else {
                    ocr.setText("0");
                    System.out.println("Pas trouver");
                }

            } catch (IOException e) {
                e.printStackTrace();
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
}