package com.example.activigo.wishlist;

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

public class WishlistAdapter extends RecyclerView.Adapter<WishlistViewHolder> {

    private ArrayList<Activite> listActivites;
    private LayoutInflater mInflater;
    private static ResultatClickListener itemListener;
    private Context context;

    WishlistAdapter(Context context, ArrayList<Activite> activites, ResultatClickListener itemListener) {
        mInflater = LayoutInflater.from(context);
        listActivites = activites;
        WishlistAdapter.itemListener = itemListener;
        this.context = context;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_resultat_recherche_row_item, parent, false);
        return new WishlistViewHolder(view, itemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        Activite activite = listActivites.get(position);
        holder.bind(activite,context);
    }

    @Override
    public int getItemCount() {
        return listActivites.size();
    }

}
