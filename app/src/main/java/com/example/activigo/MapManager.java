package com.example.activigo;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapManager {

    public GeoPoint getLatitudeLongitudeFromLocation(String lieu, Context context) {
        GeoPoint geoPoint = null;
        if(Geocoder.isPresent()){
            Log.d("GEOLOG", "Geocoder is present");
            try {
                Geocoder gc = new Geocoder(context);
                List<Address> addresses= gc.getFromLocationName(lieu, 5);

                List<GeoPoint> geoPointList = new ArrayList<>(addresses.size());
                for(Address a : addresses){
                    if(a.hasLatitude() && a.hasLongitude()){
                        geoPointList.add(new GeoPoint(a.getLatitude(),a.getLongitude()));
                        Log.d("adresse", a.getLatitude() + "," + a.getLongitude());
                    }
                }
                if(geoPointList.size()>0) {
                    geoPoint = geoPointList.get(0);
                }

            } catch (IOException e) {
                // handle the exception
            }
        }
        return geoPoint;
    }

    public String getLocationFromLattitudeLongitude(float lattitude, float longitude, Context context){
        String location = null;
        if(Geocoder.isPresent()){
            Log.d("GEOLOG", "Geocoder is present");
            try {
                Geocoder gc = new Geocoder(context);
                List<Address> addresses= gc.getFromLocation((double)lattitude,(double)longitude,5);

                List<String> cityList = new ArrayList<>(addresses.size());
                for(Address a : addresses){
                    if(a.hasLatitude() && a.hasLongitude()){
                        cityList.add(a.getLocality());
                        Log.d("adresse", a.getLatitude() + "," + a.getLongitude());
                    }
                }
                if(cityList.size()>0) {
                    location = cityList.get(0);
                }

            } catch (IOException e) {
                // handle the exception
            }
        }

        if (location==null) {

            location = "Ville inconnue";

        }
        return location;
    }
}
