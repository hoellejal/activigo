package com.example.activigo.note;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.activigo.AccessToDB;
import com.example.activigo.Activite;
import com.example.activigo.R;
import com.example.activigo.view.ViewActivity;

public class NoteActivity extends AppCompatActivity {

    private AccessToDB accessToDB = new AccessToDB();
    private Activite activite;

    /* fields from view */
    private RatingBar note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note); //sets layout

        /* gets all fields from view */
        note = findViewById(R.id.activity_note);
        Button valider=findViewById(R.id.valider);
        Button annuler=findViewById(R.id.annuler);

        /* get extra from intent */
        activite=(Activite) getIntent().getSerializableExtra("activite");
        float newNote = (float) getIntent().getSerializableExtra("new_note");

        /* checking if we have the necessary fields */
        if(newNote == 0 || activite==null){
            throw new IllegalArgumentException("no note to give");
        }

        /* updating the view */
        note.setRating(newNote);
        note.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> ratingBar.setRating(rating));

        /* listeners for buttons */
        valider.setOnClickListener(view -> {
            /* update the activity in the database then go back to viewActivity */
            activite.setNoteActivite(activite.changeNote((long)note.getRating()));
            accessToDB.changeActivity(activite);
            Intent intent = new Intent(NoteActivity.this, ViewActivity.class);
            intent.putExtra("ActiviteSent", activite);
            startActivity(intent);
        });

        annuler.setOnClickListener(view -> {
            /* Discards the note and goes back to the view activity */
            Intent intent = new Intent(NoteActivity.this, ViewActivity.class);
            intent.putExtra("ActiviteSent",activite);
            startActivity(intent);
        });
    }
}
