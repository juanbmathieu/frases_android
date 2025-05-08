package com.tip.frasesdeprogramacionandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tip.frasesdeprogramacionandroid.R;
import com.tip.frasesdeprogramacionandroid.model.Frase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FraseAdapter extends RecyclerView.Adapter<FraseAdapter.FraseViewHolder> {

    private List<Frase> lista;

    public FraseAdapter(List<Frase> lista) {
        this.lista = lista;
    }

    private Gson gson = new Gson();

    @NonNull
    @Override
    public FraseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_frase_layout, parent, false);
        return new FraseViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull FraseViewHolder holder, int position) {
        Frase frase = lista.get(position);
        holder.textoFrase.setText("\"" + frase.getContenido() + "\"");
        holder.autorFrase.setText("- " + frase.getAutor());

        SharedPreferences shPrefs = holder.itemView.getContext().getSharedPreferences("favoritos", Context.MODE_PRIVATE);
        String key = "frase_" + frase.getId();

        boolean esFavorita = shPrefs.contains(key);
        frase.setFavorita(esFavorita);
        holder.btnFavorito.setImageResource(esFavorita ? R.drawable.ic_star : R.drawable.ic_star_null);

        holder.btnFavorito.setOnClickListener(v -> {
            boolean nuevaFavorita = !frase.esFavorita();
            frase.setFavorita(nuevaFavorita);
            SharedPreferences.Editor editor = shPrefs.edit();

            if (nuevaFavorita) {
                String fraseJson = new Gson().toJson(frase);
                editor.putString(key, fraseJson);
            } else {
                editor.remove(key);
            }

            editor.apply();
            holder.btnFavorito.setImageResource(nuevaFavorita ? R.drawable.ic_star : R.drawable.ic_star_null);
            notifyItemChanged(position);
        });

        holder.itemView.setOnClickListener(v1 -> {
            Intent compartirIntent = new Intent(Intent.ACTION_SEND);
            compartirIntent.setType("text/plain");
            String fraseACompartir = frase.getContenido() + "\" - " + frase.getAutor();
            compartirIntent.putExtra(Intent.EXTRA_TEXT, fraseACompartir);
            Log.d("COMPARTIR", "Frase clickeada:" + frase.getContenido());
            holder.itemView.getContext().startActivity(Intent.createChooser(compartirIntent, "Compartir via"));
        });

        holder.btnFavorito.setClickable(true);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class FraseViewHolder extends RecyclerView.ViewHolder {
        TextView textoFrase, autorFrase;
        ImageButton btnFavorito;
        public FraseViewHolder(@NonNull View itemView) {
            super(itemView);
            textoFrase = itemView.findViewById(R.id.textViewFrase);
            autorFrase = itemView.findViewById(R.id.textViewAutor);
            btnFavorito = itemView.findViewById(R.id.btnFavorito);
        }
    }
}
