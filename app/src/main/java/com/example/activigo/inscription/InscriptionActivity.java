package com.example.activigo.inscription;

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
import com.example.activigo.connexion.ConnexionActivity;
import com.example.activigo.profil.ProfilActivity;


public class InscriptionActivity extends AppCompatActivity {
    public EditText username;
    public EditText password;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
    }

    /**
     * Creating a new user in the database, logging in, and then navigating to the profile activity
     * @param view
     */
    public void inscriptionSend(View view) {
        AccessToDB accessToDB = new AccessToDB();
        if(!(username.getText().toString().equals("")) && !(password.getText().toString().equals(""))) {
            accessToDB.addUserInDB(username.getText().toString(), password.getText().toString(), (isLoginSuccessful,id) -> {
                if (isLoginSuccessful) {
                    Session.setid(id,this);
                    Session.setLoggedIn(true,this);
                    Intent intent = new Intent(InscriptionActivity.this, ProfilActivity.class);
                    startActivity(intent);
                } else {
                    //user incorrect
                    AlertDialog.Builder popUpRate = new AlertDialog.Builder(InscriptionActivity.this);
                    popUpRate.setTitle("Echec de l'inscription");
                    popUpRate.setPositiveButton("Ok", (dialogInterface, i) -> Toast.makeText(getApplicationContext(), "Echec de l'inscription", Toast.LENGTH_SHORT).show());
                    popUpRate.show();
                    Intent intent = new Intent(InscriptionActivity.this, ConnexionActivity.class);
                    startActivity(intent);
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"Veuillez rentrer un nom d'utilisateur et un mot de passe",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Cancels registration and goes back to the connexion activity
     * @param view
     */
    public void inscriptionCancel(View view) {
        Intent intent = new Intent(InscriptionActivity.this, ConnexionActivity.class);
        startActivity(intent);
    }
}