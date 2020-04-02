package com.example.activigo.accueil;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.activigo.Activite;
import com.example.activigo.MapManager;
import com.example.activigo.R;
import com.example.activigo.resultat.ResultatClickListener;

public class AccueilViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView mTitreView;
    private TextView mLieuView;
    private RatingBar mNoteView;
    private ResultatClickListener itemListener;
    private ImageView imageView ;

    AccueilViewHolder(View view, ResultatClickListener itemListener) {
        super(view);
        view.setOnClickListener(this);
        this.itemListener = itemListener;

        mTitreView = view.findViewById(R.id.titre_activite_accueil);
        mLieuView = view.findViewById(R.id.lieu_activite_accueil);
        mNoteView = view.findViewById(R.id.note_activite_accueil);
        imageView = view.findViewById(R.id.imageActiviteAccueil);
    }

    void bind(Activite activite, Context context) {
        mTitreView.setText(activite.getNomActivite());
        MapManager mapManager = new MapManager();
        String location = mapManager.getLocationFromLattitudeLongitude(activite.getLattActivite(),activite.getLngActivite(),context);
        mLieuView.setText(location);
        mNoteView.setRating(activite.getNoteActivite());

        Glide.with(context.getApplicationContext()).load(activite.getPhotoActivite()).into(imageView);
    }

    @Override
    public void onClick(View view) {
        itemListener.recyclerViewListClicked(view, this.getLayoutPosition());
    }

}
