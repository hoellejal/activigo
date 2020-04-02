package com.example.activigo.profil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.activigo.AccessToDB;
import com.example.activigo.Profil;
import com.example.activigo.R;
import com.example.activigo.accueil.AccueilActivity;
import com.example.activigo.connexion.ConnexionActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfilActivity extends AppCompatActivity {
    private HashMap<Integer, Integer> score=new HashMap<>();
    private AccessToDB accessToDB = new AccessToDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        /* score value */
        int pas_du_tout=-100;
        int pas_vraiment = -10;
        int sans_preference = 0;
        int plutot = 5;
        int beaucoup = 10;

        /* linking seekbar values and score */
        score.put(0,pas_du_tout);
        score.put(1, pas_vraiment);
        score.put(2, sans_preference);
        score.put(3, plutot);
        score.put(4, beaucoup);

        /* get fields from view */
        Button valider=findViewById(R.id.valider);
        Button annuler=findViewById(R.id.annuler);

        /* button listeners */
        valider.setOnClickListener(view -> {
            /* gets all editable fields from view */
            SeekBar sportif=findViewById(R.id.sportif);
            SeekBar peu_connu=findViewById(R.id.peu_connu);
            SeekBar populaire=findViewById(R.id.populaire);
            SeekBar nature=findViewById(R.id.nature);
            SeekBar urbain=findViewById(R.id.urbain);
            SeekBar culturel=findViewById(R.id.culturel);
            SeekBar groupe=findViewById(R.id.groupe);
            SeekBar gastronomique=findViewById(R.id.gastronomique);
            SeekBar divertissement=findViewById(R.id.divertissement);
            SeekBar budget=findViewById(R.id.budget);

            /* additional fields for profile creation */
            final int profil_sportif=score.get(sportif.getProgress());
            final int profil_peu_connu=score.get(peu_connu.getProgress());
            final int profil_populaire=score.get(populaire.getProgress());
            final int profil_nature=score.get(nature.getProgress());
            final int profil_urbain=score.get(urbain.getProgress());
            final int profil_culturel=score.get(culturel.getProgress());
            final int profil_groupe=score.get(groupe.getProgress());
            final int profil_gastronomique=score.get(gastronomique.getProgress());
            final int profil_divertissement=score.get(divertissement.getProgress());
            final int profil_budget=score.get(budget.getProgress());

            /* Make a profile will all the fields */
            final Profil profil=new Profil(null, new ArrayList<>(),profil_nature,profil_urbain,profil_populaire,profil_peu_connu,profil_budget,profil_culturel,profil_groupe,profil_divertissement,profil_gastronomique,profil_sportif);

            /* add the profile in the database then go to connexionActivity */
            accessToDB.addProfilInDB(profil);
            Intent intent=new Intent(ProfilActivity.this, AccueilActivity.class);
            startActivity(intent);
        });
        annuler.setOnClickListener(view -> {
            /* Discards the profile and goes back to the connexion activity */

            Intent intent = new Intent(ProfilActivity.this, ConnexionActivity.class);
            startActivity(intent);
        });
    }
}