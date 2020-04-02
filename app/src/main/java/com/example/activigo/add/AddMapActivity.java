package com.example.activigo.add;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.activigo.AccessToDB;
import com.example.activigo.Activite;
import com.example.activigo.MapManager;
import com.example.activigo.R;
import com.example.activigo.accueil.AccueilActivity;
import com.example.activigo.profil.ShowProfil;
import com.example.activigo.recherche.RechercheActivity;
import com.example.activigo.view.ViewActivity;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AddMapActivity extends FragmentActivity implements OnMapReadyCallback{

    Location currentLocation;
    MarkerOptions markerOptions;

    private AccessToDB accessToDB = new AccessToDB();
    private final static int REQUEST_CODE = 101;
    private Activite activite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_choose_position);

        getFromIntent();

        // map
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(AddMapActivity.this);


        // bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_Home:
                    Intent intentHome = new Intent(AddMapActivity.this, AccueilActivity.class);
                    startActivity(intentHome);
                    break;
                case R.id.action_recherche:
                    Intent intentRecherche = new Intent(AddMapActivity.this, RechercheActivity.class);
                    startActivity(intentRecherche);
                    break;
                case R.id.action_ajouter:
                    Intent intentAdd = new Intent(AddMapActivity.this, AddActivity.class);
                    startActivity(intentAdd);
                    break;
                case R.id.action_profil:
                    Intent intentProfil = new Intent(AddMapActivity.this, ShowProfil.class);
                    startActivity(intentProfil);
                    break;
                default:
            }
            return true;
        });


    }

    private void getFromIntent() {
        /* gets all extras from intent */
        activite = (Activite)getIntent().getSerializableExtra("ActiviteSent");

        if(activite == null){
            throw new IllegalArgumentException("no activity");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapManager mapManager = new MapManager();
        Task<Location> task = LocationServices.getFusedLocationProviderClient(this).getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
            }
            LatLng latlng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
            String lieu = mapManager.getLocationFromLattitudeLongitude((float)currentLocation.getLatitude(), (float)currentLocation.getLongitude(), this.getApplicationContext());
            markerOptions = new MarkerOptions().position(latlng).draggable(true);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,10));
            googleMap.addMarker(markerOptions);
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                String lieu = mapManager.getLocationFromLattitudeLongitude((float)latLng.latitude, (float)latLng.longitude, AddMapActivity.this.getApplicationContext());
                markerOptions.position(latLng).title(lieu);
                Toast.makeText(AddMapActivity.this.getApplicationContext(), lieu, Toast.LENGTH_SHORT).show();
                googleMap.clear();
                googleMap.addMarker(markerOptions);
            }
        });




        /*googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener(){
            @Override
            public void onMarkerDragStart(Marker marker){

            }

            @Override
            public void onMarkerDragEnd(Marker marker){

            }

            @Override
            public void onMarkerDrag(Marker marker){

            }
        });*/
    }

    public void validerPosition(View v){
        //getPosition of marker
        double latitude = markerOptions.getPosition().latitude;
        double longitude = markerOptions.getPosition().longitude;
        //add to the activity
        activite.setLattActivite((float) latitude);
        activite.setLngActivite((float) longitude);
        accessToDB.addActivityInDB(activite, id -> {
            activite.setIdActivite(id);
            //send new activity to DB
            Intent intent = new Intent(AddMapActivity.this, ViewActivity.class);
            intent.putExtra("ActiviteSent",activite);
            startActivity(intent);
        });

    }

    public void annulerPosition(View v){
        Intent it= new Intent(AddMapActivity.this, AddActivity.class);
        it.putExtra("ActiviteSent",activite);
        startActivity(it);
    }
}
