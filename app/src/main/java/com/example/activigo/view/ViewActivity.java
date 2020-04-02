package com.example.activigo.view;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.example.activigo.AccessToDB;
import com.example.activigo.Activite;
import com.example.activigo.MapManager;
import com.example.activigo.Profil;
import com.example.activigo.R;
import com.example.activigo.accueil.AccueilActivity;
import com.example.activigo.add.AddActivity;
import com.example.activigo.note.NoteActivity;
import com.example.activigo.onProfilComplete;
import com.example.activigo.profil.ShowProfil;
import com.example.activigo.recherche.RechercheActivity;
import com.example.activigo.wishlist.WishlistActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class ViewActivity extends AppCompatActivity {

    private String CHANNEL_ID = "Channel1";
    int notificationID = 100;

    private AccessToDB accessToDB = new AccessToDB();
    private Activite activite;
    /* fields from intent */

    private String activityId;
    private String activityName;
    private String activityLocation;
    private String activityDescription;
    private String activityPhoto;
    private float activityNote;
    private int activityReputation;
    private int activityPrice;
    private int activityPerif;
    private boolean activityCulturel;
    private boolean activityGroupe;
    private boolean activityDivert;
    private boolean activityToDiscover;
    private boolean activityGastronomique;
    private boolean activitySportif;

    /* fields from view */

    private TextView nom ;
    private TextView location;
    private TextView description;
    private ImageView photo;
    private RatingBar note;
    private ProgressBar reputation;
    private ProgressBar prix;
    private ProgressBar nature;
    private RadioButton culturel;
    private RadioButton adecouvrir;
    private RadioButton sportif;
    private RadioButton groupe;
    private RadioButton gastronomique;
    private RadioButton divertissement;
    private RatingBar new_note;
    private ImageButton wishlist;
    private boolean inWishlist=false;
    private Profil profil=new Profil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details); //sets layout

        /* gets all editable fields */

        nom = findViewById(R.id.activity_name);
        location = findViewById(R.id.activity_location);
        description = findViewById(R.id.activity_description);
        photo = findViewById(R.id.activity_photo);
        note = findViewById(R.id.activity_note);
        reputation = findViewById(R.id.activity_progressbar_reputation);
        prix = findViewById(R.id.activity_progressbar_prix);
        nature = findViewById(R.id.activity_progressbar_nature);
        culturel = findViewById(R.id.culturel);
        adecouvrir = findViewById(R.id.adecouvrir);
        sportif = findViewById(R.id.sportif);
        groupe = findViewById(R.id.groupe);
        gastronomique = findViewById(R.id.gastronomique);
        divertissement = findViewById(R.id.divertissement);
        new_note = findViewById(R.id.noter_activity);
        wishlist=findViewById(R.id.wishlist);

        getFromIntent(); //gets information from the intent
        updateView(); //updates the view


        accessToDB.getProfil(profil1 -> {
            profil=profil1;
            ArrayList<String> wish = profil.getWishlist();
            if(wish.contains(activityId)){
                inWishlist=true;
                wishlist.setImageResource(R.drawable.coeur_plein);
            }

            /* wishlist button listener */
            wishlist.setOnClickListener(v -> {
                /* adds the activity to the user's wishlist if it isn't in it already */
                if (!inWishlist) {
                    wishlist.setImageResource(R.drawable.coeur_plein);
                    inWishlist=true;
                    profil.addTowishlist(activityId);
                    accessToDB.changeProfil(profil);
                    Toast.makeText(getApplicationContext(), "Activité ajoutée dans ta wishlist", Toast.LENGTH_SHORT).show();
                }
                /* otherwise removes the activity from the user's wishlist */
                else {
                    wishlist.setImageResource(R.drawable.coeur);
                    inWishlist=false;
                    profil.removeFromWishlist(activityId);
                    accessToDB.changeProfil(profil);
                    Toast.makeText(getApplicationContext(), "Activité retirée de ta wishlist", Toast.LENGTH_SHORT).show();
                }
            });
        });

        /* note ratingbar listener */
        new_note.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            /* starts the note activity with the value of the ratingbar */
            Intent it = new Intent(ViewActivity.this, NoteActivity.class);
            it.putExtra("activite",activite);
            it.putExtra("new_note",new_note.getRating());
            startActivity(it);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_Home:
                    Intent intentHome = new Intent(ViewActivity.this, AccueilActivity.class);
                    startActivity(intentHome);
                    break;
                case R.id.action_recherche:
                    Intent intentRecherche = new Intent(ViewActivity.this, RechercheActivity.class);
                    startActivity(intentRecherche);
                    break;
                case R.id.action_ajouter:
                    Intent intentAdd = new Intent(ViewActivity.this, AddActivity.class);
                    startActivity(intentAdd);
                    break;
                case R.id.action_profil:
                    Intent intentProfil = new Intent(ViewActivity.this, ShowProfil.class);
                    startActivity(intentProfil);
                    break;
                case R.id.action_wishlist:
                    Intent intentWish = new Intent(ViewActivity.this, WishlistActivity.class);
                    startActivity(intentWish);
                    break;
                default:
            }
            return true;
        });
    }

    /**
     * Updates the view with the fields gotten from the intent
     */
    private void updateView() {

        /* fixes the values given by the intent to the fields */
        nom.setText(activityName);
        location.setText(activityLocation);
        Glide.with(getApplicationContext()).load(activite.getPhotoActivite()).into(photo);
        System.out.println(Uri.parse(activityPhoto));
        description.setText(activityDescription);
        note.setRating(activityNote);
        reputation.setProgress(activityReputation);
        prix.setProgress(activityPrice);
        nature.setProgress(activityPerif);
        if(activityCulturel) culturel.setVisibility(View.VISIBLE);
        if(activityGroupe) groupe.setVisibility(View.VISIBLE);
        if(activityDivert) divertissement.setVisibility(View.VISIBLE);
        if(activityToDiscover) adecouvrir.setVisibility(View.VISIBLE);
        if(activityGastronomique) gastronomique.setVisibility(View.VISIBLE);
        if(activitySportif) sportif.setVisibility(View.VISIBLE);
    }

    /**
     * Gets all the fields from the intent and checks if they are correct
     */
    private void getFromIntent() {
        /* gets all extras from intent */
        activite = (Activite)getIntent().getSerializableExtra("ActiviteSent");

        if(activite == null){
            throw new IllegalArgumentException("no activity to display");
        }

        activityId=activite.getIdActivite();
        activityName = activite.getNomActivite();
        MapManager mapManager = new MapManager();
        activityLocation= mapManager.getLocationFromLattitudeLongitude(activite.getLattActivite(),activite.getLngActivite(), this.getApplicationContext());
        activityDescription=activite.getDescription();
        activityPhoto= activite.getPhotoActivite();
        activityNote=activite.getNoteActivite();
        activityReputation=activite.getPopularite();
        activityPrice=activite.getPrix();
        activityPerif=activite.getNature();
        activityCulturel=activite.isCulture();
        activityGroupe=activite.isGroupe();
        activityDivert=activite.isDivertissement();
        activityToDiscover=activite.isEndroits();
        activityGastronomique=activite.isGastronomie();
        activitySportif=activite.isSport();

        /* stops if the intent was incomplete or incorrect */

        if (activityName == null || activityLocation==null || activityDescription==null ||
                activityNote<0 ||activityNote>5||activityReputation<0||activityPrice<0||activityPerif<0) {
            throw new IllegalArgumentException("Incomplete/Incorrect activity");
        }
    }

    public void goCalendar(View view) {

    }

    public void createNotification() {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, AccueilActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icone_activigo)
                .setContentTitle("Bonne activité :)")
                .setContentText("Vous allez à "+activityName+". N'oubliez pas de noter l'activité quand vous l'aurez faite!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(notificationID, builder.build());
        notificationID += 1;
    }
    public void onClickGo(View v)
    {
        createNotification();

        Intent intentHome = new Intent(ViewActivity.this, AccueilActivity.class);
        startActivity(intentHome);
    }

}
