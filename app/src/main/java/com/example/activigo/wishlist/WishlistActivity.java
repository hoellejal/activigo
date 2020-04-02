package com.example.activigo.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activigo.AccessToDB;
import com.example.activigo.Activite;
import com.example.activigo.Profil;
import com.example.activigo.R;
import com.example.activigo.accueil.AccueilActivity;
import com.example.activigo.add.AddActivity;
import com.example.activigo.profil.ShowProfil;
import com.example.activigo.recherche.RechercheActivity;
import com.example.activigo.resultat.ResultatClickListener;
import com.example.activigo.view.ViewActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity implements ResultatClickListener {
    private AccessToDB accessToDB = new AccessToDB();
    ArrayList<Activite> listActivites;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Profil p=new Profil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist); //sets layout

        accessToDB.getProfil(profil -> {
            p=profil;
            ArrayList<String> wish = p.getWishlist();
            if(wish.size()==0){
                Toast.makeText(getApplicationContext(),"Tu n'as pas de wishlist!",Toast.LENGTH_SHORT).show();
                Intent goToActivityIntent = new Intent(WishlistActivity.this, AccueilActivity.class);
                startActivity(goToActivityIntent);
            }
            else {
                accessToDB.getActivitiesById(wish, activity -> {
                    listActivites = activity;
                    recyclerView= findViewById(R.id.recycler_view_wishlist);
                    layoutManager = new LinearLayoutManager(this);
                    recyclerView.setLayoutManager(layoutManager);
                    mAdapter = new WishlistAdapter(getApplicationContext(), listActivites, this);
                    recyclerView.setAdapter(mAdapter);
                });
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_Home:
                    Intent intentHome = new Intent(WishlistActivity.this, AccueilActivity.class);
                    startActivity(intentHome);
                    break;
                case R.id.action_recherche:
                    Intent intentRecherche = new Intent(WishlistActivity.this, RechercheActivity.class);
                    startActivity(intentRecherche);
                    break;
                case R.id.action_ajouter:
                    Intent intentAdd = new Intent(WishlistActivity.this, AddActivity.class);
                    startActivity(intentAdd);
                    break;
                case R.id.action_profil:
                    Intent intentProfil = new Intent(WishlistActivity.this, ShowProfil.class);
                    startActivity(intentProfil);
                    break;
                case R.id.action_wishlist:
                    Intent intentWish = new Intent(WishlistActivity.this, WishlistActivity.class);
                    startActivity(intentWish);
                    break;
                default:
            }
            return true;
        });

    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        Activite activite_clicked = listActivites.get(position);
        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra("ActiviteSent",activite_clicked);
        startActivity(intent);
    }
}
