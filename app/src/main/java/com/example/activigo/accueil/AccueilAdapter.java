package com.example.activigo.accueil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activigo.Activite;
import com.example.activigo.R;
import com.example.activigo.resultat.ResultatClickListener;

import java.util.ArrayList;

public class AccueilAdapter extends RecyclerView.Adapter<AccueilViewHolder> {

    private ArrayList<Activite> listActivites;
    private LayoutInflater mInflater;
    private static ResultatClickListener itemListener;
    private Context context;

    AccueilAdapter(Context context, ArrayList<Activite> activites, ResultatClickListener itemListener) {
        mInflater = LayoutInflater.from(context);
        listActivites = activites;
        AccueilAdapter.itemListener = itemListener;
        this.context = context;
    }

    @NonNull
    @Override
    public AccueilViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_accueil_row_item, parent, false);
        return new AccueilViewHolder(view, itemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AccueilViewHolder holder, int position) {
        Activite activite = listActivites.get(position);
        holder.bind(activite,context);
    }

    @Override
    public int getItemCount() {
        return listActivites.size();
    }

}
