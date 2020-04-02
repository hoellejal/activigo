package com.example.activigo.profil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.activigo.AccessToDB;
import com.example.activigo.Profil;
import com.example.activigo.R;
import com.example.activigo.Session;
import com.example.activigo.accueil.AccueilActivity;
import com.example.activigo.add.AddActivity;
import com.example.activigo.connexion.ConnexionActivity;
import com.example.activigo.recherche.RechercheActivity;
import com.example.activigo.wishlist.WishlistActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class ShowProfil extends AppCompatActivity {
    private Profil profil;
    private AccessToDB accessToDB=new AccessToDB();
    private HashMap<Integer, Integer> scoreInverse=new HashMap<>();

    /* score value */
    int pas_du_tout=-100;
    int pas_vraiment = -10;
    int sans_preference = 0;
    int plutot = 5;
    int beaucoup = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profil);

        /* linking seekbar values and score */
        scoreInverse.put(pas_du_tout, 0);
        scoreInverse.put(pas_vraiment, 1);
        scoreInverse.put(sans_preference, 2);
        scoreInverse.put(plutot, 3);
        scoreInverse.put(beaucoup, 4);

        HashMap<Integer,Integer> score=new HashMap<>();
        score.put(0,pas_du_tout);
        score.put(1, pas_vraiment);
        score.put(2, sans_preference);
        score.put(3, plutot);
        score.put(4, beaucoup);

        SeekBar seekBarSportif = findViewById(R.id.profilSeekBarSportif);
        SeekBar seekBarConnu = findViewById(R.id.profilSeekBarConnu);
        SeekBar seekBarPopulaire = findViewById(R.id.profilSeekBarPopulaire);
        SeekBar seekBarNature = findViewById(R.id.profilSeekBarNature);
        SeekBar seekBarUrbain = findViewById(R.id.profilSeekBarUrbain);
        SeekBar seekBarCulturel = findViewById(R.id.profilSeekBarCulturel);
        SeekBar seekBarGastro = findViewById(R.id.profilSeekBarGastro);
        SeekBar seekBarCollectif = findViewById(R.id.profilSeekBarCollectif);
        SeekBar seekBarDivertissement = findViewById(R.id.profilSeekBarDivertissement);
        SeekBar seekBarBudget = findViewById(R.id.profilSeekBarBudget);
        Button valider=findViewById(R.id.valider);

        accessToDB.getProfil(p -> {
            profil=p;
            // récupérer les préférence de l'utilisateur et modifier les seekbar et checkbox en fonction

            seekBarSportif.setProgress(scoreInverse.get(profil.getSport()));
            seekBarConnu.setProgress(scoreInverse.get(profil.getPeu_connu()));
            seekBarPopulaire.setProgress(scoreInverse.get(profil.getPopulaire()));
            seekBarNature.setProgress(scoreInverse.get(profil.getNature()));
            seekBarUrbain.setProgress(scoreInverse.get(profil.getUrbain()));
            seekBarCulturel.setProgress(scoreInverse.get(profil.getCulture()));
            seekBarGastro.setProgress(scoreInverse.get(profil.getGastronomie()));
            seekBarCollectif.setProgress(scoreInverse.get(profil.getGroupe()));
            seekBarDivertissement.setProgress(scoreInverse.get(profil.getDivertissement()));
            seekBarBudget.setProgress(scoreInverse.get(profil.getBudget()));

            valider.setOnClickListener(click -> {
                profil.setNature(score.get(seekBarNature.getProgress()));
                profil.setUrbain(score.get(seekBarUrbain.getProgress()));
                profil.setPopulaire(score.get(seekBarPopulaire.getProgress()));
                profil.setPeu_connu(score.get(seekBarConnu.getProgress()));
                profil.setBudget(score.get(seekBarBudget.getProgress()));
                profil.setCulture(score.get(seekBarCulturel.getProgress()));
                profil.setGroupe(score.get(seekBarCollectif.getProgress()));
                profil.setDivertissement(score.get(seekBarDivertissement.getProgress()));
                profil.setGastronomie(score.get(seekBarGastro.getProgress()));
                profil.setSport(score.get(seekBarSportif.getProgress()));
                accessToDB.changeProfil(profil);
                Toast.makeText(getApplicationContext(), "Profil mis à jour", Toast.LENGTH_SHORT).show();
            });
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_Home:
                    Intent intentHome = new Intent(ShowProfil.this, AccueilActivity.class);
                    startActivity(intentHome);
                    break;
                case R.id.action_recherche:
                    Intent intentRecherche = new Intent(ShowProfil.this, RechercheActivity.class);
                    startActivity(intentRecherche);
                    break;
                case R.id.action_ajouter:
                    Intent intentAdd = new Intent(ShowProfil.this, AddActivity.class);
                    startActivity(intentAdd);
                    break;
                case R.id.action_profil:
                    break;
                case R.id.action_wishlist:
                    Intent intentWish = new Intent(ShowProfil.this, WishlistActivity.class);
                    startActivity(intentWish);
                    break;
                default:
            }
            return true;
        });

    }

    public void logout(View v){
        Session.setLoggedIn(false,this);
        Session.setid("",this);
        Intent it = new Intent(ShowProfil.this, ConnexionActivity.class);
        startActivity(it);
    }

}
