package com.example.activigo.resultat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activigo.Activite;
import com.example.activigo.R;

import java.util.ArrayList;

public class ResultatAdapter extends RecyclerView.Adapter<ResultatViewHolder> {

    private ArrayList<Activite> listActivites;
    private LayoutInflater mInflater;
    private static ResultatClickListener itemListener;
    private Context context;

    /**
     * Constructor for the resultatAdapter
     * @param context the context of the application
     * @param activites a list of activities
     * @param itemListener a listener for the activity's list items
     */
    ResultatAdapter(Context context, ArrayList<Activite> activites, ResultatClickListener itemListener) {
        mInflater = LayoutInflater.from(context);
        listActivites = activites;
        ResultatAdapter.itemListener = itemListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ResultatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_resultat_recherche_row_item, parent, false);
        return new ResultatViewHolder(view, itemListener);

    }

    @Override
    public void onBindViewHolder(@NonNull ResultatViewHolder holder, int position) {
        Activite activite = listActivites.get(position);
        holder.bind(activite,context);
    }

    @Override
    public int getItemCount() {
        return listActivites.size();
    }
}
