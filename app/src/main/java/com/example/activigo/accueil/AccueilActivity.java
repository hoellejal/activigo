package com.example.activigo.accueil;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activigo.AccessToDB;
import com.example.activigo.Activite;
import com.example.activigo.MapManager;
import com.example.activigo.R;
import com.example.activigo.Session;
import com.example.activigo.add.AddActivity;
import com.example.activigo.connexion.ConnexionActivity;
import com.example.activigo.profil.ShowProfil;
import com.example.activigo.recherche.RechercheActivity;
import com.example.activigo.resultat.ResultatClickListener;
import com.example.activigo.view.ViewActivity;
import com.example.activigo.wishlist.WishlistActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class AccueilActivity extends FragmentActivity implements OnMapReadyCallback, ResultatClickListener{

    private GoogleMap myGoogleMap;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String CHANNEL_ID = "Channel1";
    int notificationID = 100;

    ArrayList<Activite> listActivites;

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 101;
    public EditText searchview_lieu_accueil;
    String lieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        createNotificationChannel();

        // maps
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this); 
        fetchLastLocation();

        // bottom navigation
        searchview_lieu_accueil = findViewById(R.id.searchview_lieu_accueil);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_Home:
                    break;
                case R.id.action_recherche:
                    Intent intentRecherche = new Intent(AccueilActivity.this, RechercheActivity.class);
                    startActivity(intentRecherche);
                    break;
                case R.id.action_ajouter:
                    Intent intentAdd = new Intent(AccueilActivity.this, AddActivity.class);
                    startActivity(intentAdd);
                    break;
                case R.id.action_profil:
                    Intent intentProfil = new Intent(AccueilActivity.this, ShowProfil.class);
                    startActivity(intentProfil);
                    break;
                case R.id.action_wishlist:
                    Intent intentWish = new Intent(AccueilActivity.this, WishlistActivity.class);
                    startActivity(intentWish);
                    break;
                default:
            }
            return true;
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                SupportMapFragment supportMapFragment = (SupportMapFragment) AccueilActivity.this.getSupportFragmentManager().findFragmentById(R.id.map);
                supportMapFragment.getMapAsync(AccueilActivity.this);


                // recycler view
                recyclerView = AccueilActivity.this.findViewById(R.id.recycler_view_accueil);
                layoutManager = new LinearLayoutManager(AccueilActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                AccessToDB accessToDB = new AccessToDB();
                accessToDB.getActivityFiltered((float)currentLocation.getLatitude(),(float)currentLocation.getLongitude(),null,null,listActivity -> {
                    for (Activite activite:listActivity) {
                        LatLng latlng = new LatLng(activite.getLattActivite(),activite.getLngActivite());
                        MapManager mapManager = new MapManager();
                        String lieu = mapManager.getLocationFromLattitudeLongitude(activite.getLattActivite(),activite.getLngActivite(),this.getApplicationContext());
                        MarkerOptions markerOptions = new MarkerOptions().position(latlng).title(activite.getNomActivite()).snippet(lieu).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
                        myGoogleMap.addMarker(markerOptions);
                    }
                    listActivites = listActivity;
                    mAdapter = new AccueilAdapter(getApplicationContext(), listActivites, this);
                    recyclerView.setAdapter(mAdapter);
                });
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myGoogleMap = googleMap;
        LatLng latlng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latlng).title("Vous êtes ici.");
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,10));
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLastLocation();
            }
        }
    }

    public void goToRechercheFromAccueil(View view) {
        lieu = searchview_lieu_accueil.getText().toString();
        Intent intentRechercheActivite = new Intent(AccueilActivity.this, RechercheActivity.class);
        intentRechercheActivite.putExtra("lieu",lieu);
        startActivity(intentRechercheActivite);
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
