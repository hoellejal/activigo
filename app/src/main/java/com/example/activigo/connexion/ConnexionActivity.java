package com.example.activigo.connexion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.activigo.AccessToDB;
import com.example.activigo.R;
import com.example.activigo.Session;
import com.example.activigo.accueil.AccueilActivity;
import com.example.activigo.inscription.InscriptionActivity;

public class ConnexionActivity extends AppCompatActivity {
    private EditText username ;
    private EditText pass;
    private AccessToDB accessToDB = new AccessToDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        username = findViewById(R.id.username);
        pass = findViewById(R.id.password);

        if((Session.getid(this).length())!=0){
            accessToDB.gen_id_user=Session.getid(this);
            Session.setLoggedIn(true,this);
            Intent goToActivityIntent = new Intent(ConnexionActivity.this, AccueilActivity.class);
            startActivity(goToActivityIntent);
        }
    }

    /**
     * Function making the user authentification by checking if the username and password exist in database as a pair
     * @param view
     */
    public void connectToApp(View view) {
        //checking the fields are filled
        if(!(username.getText().toString().equals("")) && !(pass.getText().toString().equals(""))){
            accessToDB.connectionUserDB(username.getText().toString(), pass.getText().toString(), (isLoginSuccessful, id) -> {
                if (isLoginSuccessful){
                    Session.setid(id,this);
                    Session.setLoggedIn(true,this);

                    Intent goToActivityIntent = new Intent(ConnexionActivity.this, AccueilActivity.class);
                    startActivity(goToActivityIntent);
                } else {
                    //user incorrect
                    AlertDialog.Builder popUpRate = new AlertDialog.Builder(ConnexionActivity.this);
                    popUpRate.setTitle("Authentification échouée");
                    popUpRate.setMessage("Nom de compte ou mot de passe incorrect.");
                    popUpRate.setPositiveButton("Ok", (dialogInterface, i) -> Toast.makeText(getApplicationContext(),"Nom de compte ou mot de passe incorrect.",Toast.LENGTH_SHORT).show());
                    popUpRate.show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(),"Veuillez rentrer un nom d'utilisateur et un mot de passe",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Navigates to the inscription activity
     * @param view
     */
    public void inscriptionPage(View view) {
        Intent goToProfileIntent = new Intent(ConnexionActivity.this, InscriptionActivity.class);
        startActivity(goToProfileIntent);
    }
}
