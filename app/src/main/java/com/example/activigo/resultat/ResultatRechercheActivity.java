package com.example.activigo.resultat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activigo.Activite;
import com.example.activigo.R;
import com.example.activigo.accueil.AccueilActivity;
import com.example.activigo.add.AddActivity;
import com.example.activigo.profil.ShowProfil;
import com.example.activigo.recherche.RechercheActivity;
import com.example.activigo.view.ViewActivity;
import com.example.activigo.wishlist.WishlistActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class ResultatRechercheActivity extends AppCompatActivity implements ResultatClickListener {
    private ArrayList<Activite> listActivites;
    private String lieuRecherche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat_recherche);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_resultat_recherche);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        listActivites = (ArrayList<Activite>)getIntent().getSerializableExtra("ActivitiesListExtra");
        RecyclerView.Adapter mAdapter = new ResultatAdapter(getApplicationContext(), listActivites, this);
        recyclerView.setAdapter(mAdapter);

        lieuRecherche = (String)getIntent().getSerializableExtra("LieuRecherche");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_Home:
                    Intent intentHome = new Intent(ResultatRechercheActivity.this, AccueilActivity.class);
                    startActivity(intentHome);
                    break;
                case R.id.action_recherche:
                    Intent intentRecherche = new Intent(ResultatRechercheActivity.this, RechercheActivity.class);
                    startActivity(intentRecherche);
                    break;
                case R.id.action_ajouter:
                    Intent intentAdd = new Intent(ResultatRechercheActivity.this, AddActivity.class);
                    startActivity(intentAdd);
                    break;
                case R.id.action_profil:
                    Intent intentProfil = new Intent(ResultatRechercheActivity.this, ShowProfil.class);
                    startActivity(intentProfil);
                    break;
                case R.id.action_wishlist:
                    Intent intentWish = new Intent(ResultatRechercheActivity.this, WishlistActivity.class);
                    startActivity(intentWish);
                    break;
                default:
            }
            return true;
        });


    }

    public void goToMap(View v){
        Intent intent = new Intent(ResultatRechercheActivity.this, ResultatRechercheMapActivity.class);
        intent.putExtra("listActivities", listActivites);
        intent.putExtra("LieuRecherche", lieuRecherche);
        startActivity(intent);
    }


    public void goToRechercheActivity(View v) {
        Intent intent = new Intent(ResultatRechercheActivity.this, RechercheActivity.class);
        //intent.putExtra("nomRecherche", )
        startActivity(intent);
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        Log.d("CLICKITEM", "L'activite cliquée est en position :"+ position);
        Activite activite_clicked = listActivites.get(position);
        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra("ActiviteSent",activite_clicked);
        startActivity(intent);
    }
}

