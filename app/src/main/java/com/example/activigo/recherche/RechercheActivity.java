package com.example.activigo.recherche;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.activigo.AccessToDB;
import com.example.activigo.MapManager;
import com.example.activigo.R;
import com.example.activigo.connexion.ConnexionActivity;
import com.example.activigo.resultat.ResultatRechercheActivity;
import com.google.firebase.firestore.GeoPoint;
import java.util.HashMap;

public class RechercheActivity extends AppCompatActivity {
    private String lieu;
    private EditText searchview_lieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        /* get field from view */
        searchview_lieu = findViewById(R.id.searchview_lieu);

        /* get field from intent */
        lieu = (String) getIntent().getSerializableExtra("lieu");

        /* update view */
        searchview_lieu.setText(lieu);
    }

    /**
     * Gets all activities in a sorted list then goes to result activity
     * @param v
     */
    public void goToResultatRecherche(View v) {
        HashMap<String, Integer> seekBarMap = getSeekBarResult();
        HashMap<String, Boolean> checkBoxMap = getCheckBoxResult();
        GeoPoint location;
        float lat,lng;
        boolean success = false;
        MapManager mapManager = new MapManager();
        lieu = searchview_lieu.getText().toString();
        if(!lieu.matches("")) {
            location = mapManager.getLatitudeLongitudeFromLocation(lieu, this.getApplicationContext());
            if(location == null) {
                lat = 0;
                lng = 0;
            } else {
                lat = (float) location.getLatitude();
                lng = (float) location.getLongitude();
                success = true;
            }
        }
        else {
            lat= 0;
            lng= 0;
        }
        AccessToDB accessToDB = new AccessToDB();
        if(success){
            /* gets a sorted list of activities and goes to result activity */
            accessToDB.getActivityFiltered(lat, lng, seekBarMap, checkBoxMap, listActivite -> {
                Intent intent = new Intent(RechercheActivity.this, ResultatRechercheActivity.class);
                intent.putExtra("ActivitiesListExtra", listActivite);
                intent.putExtra("LieuRecherche", lieu);
                startActivity(intent);
            });
        } else {

            AlertDialog.Builder popUpRate = new AlertDialog.Builder(RechercheActivity.this);
            popUpRate.setTitle("Ville incorrecte");
            popUpRate.setMessage("Le nom de la ville est incorrect.");
            popUpRate.setPositiveButton("Ok", (dialogInterface, i) -> Toast.makeText(this.getApplicationContext(),"Nom de ville incorrect.",Toast.LENGTH_SHORT).show());
            popUpRate.show();
        }
    }

    /**
     * Gets information from seekbars in view
     * @return a hashmap of the values from the seekbars
     */
    private HashMap<String, Integer> getSeekBarResult(){
        HashMap<String, Integer> sbMap = new HashMap<>();

        SeekBar seekBarPopularite = findViewById(R.id.seek_bar_popularite);
        sbMap.put("popularite", seekBarPopularite.getProgress());

        SeekBar seekBarPrix = findViewById(R.id.seek_bar_prix);
        sbMap.put("prix", seekBarPrix.getProgress());

        SeekBar seekBarNature = findViewById(R.id.seek_bar_nature);
        sbMap.put("nature", seekBarNature.getProgress());

        return sbMap;
    }

    /**
     * Gets information from checkboxes in view
     * @return a hashmap of the values from the checkboxes
     */
    private HashMap<String, Boolean> getCheckBoxResult(){
        HashMap<String, Boolean> cbMap = new HashMap<>();

        CheckBox checkboxCulture = findViewById(R.id.checkbox_culture);
        cbMap.put("culture", checkboxCulture.isChecked());

        CheckBox checkboxGroupe = findViewById(R.id.checkbox_groupe);
        cbMap.put("groupe", checkboxGroupe.isChecked());

        CheckBox checkboxDivertissement = findViewById(R.id.checkbox_divertissement);
        cbMap.put("divertissement", checkboxDivertissement.isChecked());

        CheckBox checkboxEndroits = findViewById(R.id.checkbox_endroits);
        cbMap.put("endroits", checkboxEndroits.isChecked());

        CheckBox checkboxGastronomie = findViewById(R.id.checkbox_gastronomie);
        cbMap.put("gastronomie", checkboxGastronomie.isChecked());

        CheckBox checkboxSport = findViewById(R.id.checkbox_sport);
        cbMap.put("sport", checkboxSport.isChecked());

        return cbMap;
    }
}
