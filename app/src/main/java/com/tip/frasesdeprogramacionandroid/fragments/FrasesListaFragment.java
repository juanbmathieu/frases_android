package com.tip.frasesdeprogramacionandroid.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tip.frasesdeprogramacionandroid.R;
import com.tip.frasesdeprogramacionandroid.adapter.FraseAdapter;
import com.tip.frasesdeprogramacionandroid.model.Frase;
import com.tip.frasesdeprogramacionandroid.network.QuoteApi;
import com.tip.frasesdeprogramacionandroid.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FrasesListaFragment extends Fragment {
    private RecyclerView recyclerView;
    private FraseAdapter adapter;
    private List<Frase> frases = new ArrayList<>();
    private static final int CANTIDAD_FRASES = 10; // Número de frases que deseas cargar

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frases_lista, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFrases);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new FraseAdapter(frases);
        recyclerView.setAdapter(adapter);

        cargarFrases();

        return view;
    }

    private void cargarFrases() {
        QuoteApi api = RetrofitClient.getClient().create(QuoteApi.class);

        Call<List<Frase>> call = api.getListQuote();
        call.enqueue(new Callback<List<Frase>>() {
            @Override
            public void onResponse(Call<List<Frase>> call, Response<List<Frase>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Frase> listaFrases = response.body();
                    int limite = Math.min(listaFrases.size(), 10);
                    List<Frase> frasesLimitadas = listaFrases.subList(0, limite);


                    SharedPreferences shPrefs = getContext().getSharedPreferences("idFrases", Context.MODE_PRIVATE);
                    int idActual = shPrefs.getInt("idActual", 1);

                    for (Frase frase : frasesLimitadas) {
                        frase.setId(String.valueOf(idActual));
                        idActual++;
                    }

                    shPrefs.edit().putInt("idActual", idActual).apply();

                    frases.addAll(frasesLimitadas);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Frase>> call, Throwable t) {
                Log.e("FrasesListFragment", "Error al cargar frases", t);
            }
        });
    }

}