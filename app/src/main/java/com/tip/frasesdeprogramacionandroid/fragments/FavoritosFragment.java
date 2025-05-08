package com.tip.frasesdeprogramacionandroid.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tip.frasesdeprogramacionandroid.R;
import com.tip.frasesdeprogramacionandroid.adapter.FraseAdapter;
import com.tip.frasesdeprogramacionandroid.model.Frase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavoritosFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_favoritos, container, false);

        SharedPreferences shPrefs = requireContext().getSharedPreferences("favoritos", Context.MODE_PRIVATE);
        Map<String, ?> favoritas = shPrefs.getAll();

        List<Frase> frasesFavoritas = new ArrayList<>();

        for (Map.Entry<String, ?> entry : favoritas.entrySet()) {
            if (entry.getKey().startsWith("frase_")) {
                String json = (String) entry.getValue();
                Frase frase = new Gson().fromJson(json, Frase.class);
                frasesFavoritas.add(frase);
            }
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFavoritos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FraseAdapter adapter = new FraseAdapter(frasesFavoritas);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
