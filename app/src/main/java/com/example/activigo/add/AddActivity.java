package com.example.activigo.add;

import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.app.Activity;

import com.bumptech.glide.Glide;
import com.example.activigo.Activite;
import android.widget.EditText;
import android.content.Intent;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import com.example.activigo.AccessToDB;
import com.example.activigo.R;
import com.example.activigo.accueil.AccueilActivity;



public class AddActivity extends AppCompatActivity {
    public static final int GALLERY_REQUEST_CODE = 1;
    private AccessToDB accessToDB = new AccessToDB();

    /* fields from view */
    private ImageView photo;
    private Button chooser;
    private static String activityPic=null;
    private Activite activite;
    private EditText nom;
    private EditText description;
    private SeekBar reputation;
    private SeekBar prix;
    private SeekBar nature;
    private CheckBox culturel;
    private CheckBox adecouvrir;
    private CheckBox sportif;
    private CheckBox groupe;
    private CheckBox gastronomique;
    private CheckBox divertissement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        /* gets all fields from view */
        photo=findViewById(R.id.photo);
        Button valider=findViewById(R.id.valider);
        Button annuler=findViewById(R.id.annuler);
        chooser=findViewById(R.id.choosePhoto);

        getFromIntent();
        updateView();

        /* Pick a photo on gallery by clicking the chooser button */
        chooser.setOnClickListener(this::pickFromGallery);

        /* Additional fields for activity creation */
        final int note=0;
        valider.setOnClickListener(view -> {
            /* gets editable fields */
            nom=findViewById(R.id.name);
            description=findViewById(R.id.description);
            reputation=findViewById(R.id.activity_progressbar_reputation);
            prix=findViewById(R.id.activity_progressbar_prix);
            nature=findViewById(R.id.activity_progressbar_nature);
            culturel=findViewById(R.id.culturel);
            adecouvrir=findViewById(R.id.adecouvrir);
            sportif=findViewById(R.id.sportif);
            groupe=findViewById(R.id.groupe);
            gastronomique=findViewById(R.id.gastronomique);
            divertissement=findViewById(R.id.divertissement);


            /* Correcting the location field if necessary
            String lieu_corrected=lieu.getText().toString();
            lieu_corrected=lieu_corrected.toUpperCase();
            lieu_corrected=Normalizer.normalize(lieu_corrected, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
*/
            /* Additional fields for activity creation */
            final String activityName=nom.getText().toString();
            final String activityDesc=description.getText().toString();
            final int activityRep=reputation.getProgress();
            final int activityPrice=prix.getProgress();
            final int activityPerif=nature.getProgress();
            final boolean activityCult=culturel.isChecked();
            final boolean activityGroup=groupe.isChecked();
            final boolean activityDivert=divertissement.isChecked();
            final boolean activityPlace=adecouvrir.isChecked();
            final boolean activityGastro=gastronomique.isChecked();
            final boolean activitySport=sportif.isChecked();

            /* uploads the picture on firebase storage and gets back the download uri */
            accessToDB.sendPicture(Uri.parse(activityPic), picstored -> {
                /* Make an activity will all the fields */
                final Activite activity=new Activite(null,activityName,picstored.toString(),activityDesc,0,0,note,activityRep,activityPrice,activityPerif,activityCult,activityGroup,activityDivert,activityPlace,activityGastro,activitySport,0);

                /* add the activity in the database then go to viewActivity */
                //activity.setIdActivite(id);
                Intent intent = new Intent(AddActivity.this, AddMapActivity.class);
                intent.putExtra("ActiviteSent",activity);
                startActivity(intent);
            });
        });
        annuler.setOnClickListener(view -> {
            /* Discards the activity and goes back to the main activity */

            Intent intent = new Intent(AddActivity.this, AccueilActivity.class);
            startActivity(intent);
        });
    }

    private void updateView() {
        if(activite!=null){
            nom=findViewById(R.id.name);
            description=findViewById(R.id.description);
            reputation=findViewById(R.id.activity_progressbar_reputation);
            prix=findViewById(R.id.activity_progressbar_prix);
            nature=findViewById(R.id.activity_progressbar_nature);
            culturel=findViewById(R.id.culturel);
            adecouvrir=findViewById(R.id.adecouvrir);
            sportif=findViewById(R.id.sportif);
            groupe=findViewById(R.id.groupe);
            gastronomique=findViewById(R.id.gastronomique);
            divertissement=findViewById(R.id.divertissement);

            nom.setText(activite.getNomActivite());
            description.setText(activite.getDescription());
            reputation.setProgress(activite.getPopularite());
            prix.setProgress(activite.getPrix());
            nature.setProgress(activite.getNature());
            culturel.setChecked(activite.isCulture());
            adecouvrir.setChecked(activite.isEndroits());
            sportif.setChecked(activite.isSport());
            groupe.setChecked(activite.isGroupe());
            gastronomique.setChecked(activite.isGastronomie());
            divertissement.setChecked(activite.isDivertissement());
            photo.setImageURI(Uri.parse (activite.getPhotoActivite()));
            photo.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).load(Uri.parse (activite.getPhotoActivite())).into(photo);
        }
    }

    private void getFromIntent() {
        /* gets all extras from intent */
        activite = (Activite)getIntent().getSerializableExtra("ActiviteSent");
    }

    /**
     * Makes an intent for the phone gallery to pick a picture
     * @param v the view
     */
    private void pickFromGallery(View v){
        /* Starts an intent to pick a png or jpeg picture */
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    /**
     * Gets result from intent and computes it
     * @param requestCode int to identify the request
     * @param resultCode result of user input (picking an image or not)
     * @param data data saved in intent
     */
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        /* Result code is RESULT_OK only if the user selects an Image */
        if (resultCode == Activity.RESULT_OK){
            if(requestCode == GALLERY_REQUEST_CODE){
                /* data.getData returns the URI for the selected Image */
                Uri selectedImage = data.getData(); //uri for photo
                photo.setImageURI(selectedImage);
                photo.setVisibility(View.VISIBLE);
                activityPic= selectedImage.toString();
                /* Updates the button text to indicate a picture was chosen */
                chooser.setText("Changer la photo");
            }
        }
    }

    /*public void goToMap(View v){
        Intent intent = new Intent(AddActivity.this, ResultatRechercheMapActivity.class);
        startActivity(intent);
    }*/
}
