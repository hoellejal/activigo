package com.example.activigo.resultat;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import com.example.activigo.Activite;
import com.example.activigo.MapManager;
import com.example.activigo.R;
import com.example.activigo.accueil.AccueilActivity;
import com.example.activigo.add.AddActivity;
import com.example.activigo.profil.ShowProfil;
import com.example.activigo.recherche.RechercheActivity;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class ResultatRechercheMapActivity extends FragmentActivity implements OnMapReadyCallback{

    ArrayList<Activite> listActivites;
    String lieuRecherche;
    //FusedLocationProviderClient fusedLocationProviderClient;

    private final static int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat_recherche_map);


        /* get extra from intent */
        listActivites =(ArrayList<Activite>) getIntent().getSerializableExtra("listActivities");
        lieuRecherche =(String) getIntent().getSerializableExtra("LieuRecherche");

        // map
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(ResultatRechercheMapActivity.this);


        // bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_Home:
                    Intent intentHome = new Intent(ResultatRechercheMapActivity.this, AccueilActivity.class);
                    startActivity(intentHome);
                    break;
                case R.id.action_recherche:
                    Intent intentRecherche = new Intent(ResultatRechercheMapActivity.this, RechercheActivity.class);
                    startActivity(intentRecherche);
                    break;
                case R.id.action_ajouter:
                    Intent intentAdd = new Intent(ResultatRechercheMapActivity.this, AddActivity.class);
                    startActivity(intentAdd);
                    break;
                case R.id.action_profil:
                    Intent intentProfil = new Intent(ResultatRechercheMapActivity.this, ShowProfil.class);
                    startActivity(intentProfil);
                    break;
                default:
            }
            return true;
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapManager mapManager = new MapManager();
        if(lieuRecherche!=null){
            GeoPoint positionRecherche = mapManager.getLatitudeLongitudeFromLocation(lieuRecherche, this.getApplicationContext());
            LatLng latlng = new LatLng(positionRecherche.getLatitude(),positionRecherche.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latlng).title(lieuRecherche);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,10));
            googleMap.addMarker(markerOptions);

        }else {
            Task<Location> task = LocationServices.getFusedLocationProviderClient(this).getLastLocation();
            task.addOnSuccessListener(location -> {
                LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(latlng).title("Vous êtes ici.");
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,10));
                googleMap.addMarker(markerOptions);
            });
        }

        for (Activite activite:listActivites) {
            LatLng latlng = new LatLng(activite.getLattActivite(),activite.getLngActivite());
            String lieu = mapManager.getLocationFromLattitudeLongitude(activite.getLattActivite(), activite.getLngActivite(), this.getApplicationContext());
            MarkerOptions markerOptions = new MarkerOptions().position(latlng).title(activite.getNomActivite()).snippet(lieu).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
            googleMap.addMarker(markerOptions);
        }
    }

    public void goBack(View v){
        finish();
    }
}
