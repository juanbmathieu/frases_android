package com.tip.frasesdeprogramacionandroid.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tip.frasesdeprogramacionandroid.R;
import com.tip.frasesdeprogramacionandroid.model.Frase;
import com.tip.frasesdeprogramacionandroid.network.QuoteApi;
import com.tip.frasesdeprogramacionandroid.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FraseFragment extends Fragment {

    private TextView fraseTextView;
    private TextView autorTextView;

    private Button fraseButton;
    private Button verListaButton;

    private Button verFavoritosButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frase, container, false);

        fraseTextView = view.findViewById(R.id.fraseTextView);
        autorTextView = view.findViewById(R.id.autorTextView);
        fraseButton = view.findViewById(R.id.fraseButton);
        verListaButton = view.findViewById(R.id.verListaButton);
        verFavoritosButton = view.findViewById(R.id.verFavoritosButton);

        fraseButton.setOnClickListener(v -> fetchQuote());

        verListaButton.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new FrasesListaFragment())
                .addToBackStack(null)
                .commit());

        verFavoritosButton.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new FavoritosFragment())
                .addToBackStack(null)
                .commit());

        fetchQuote();

        return view;
    }


    private void fetchQuote() {
        QuoteApi api = RetrofitClient.getClient().create(QuoteApi.class);
        Call<List<Frase>> call = api.getRandomQuote();

        call.enqueue(new Callback<List<Frase>>() {
            @Override
            public void onResponse(Call<List<Frase>> call, Response<List<Frase>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Frase frase = response.body().get(0);
                    fraseTextView.setText("\"" + frase.getContenido() + "\"");
                    autorTextView.setText("- " + frase.getAutor());
                } else {
                    fraseTextView.setText("Error al cargar la frase.");
                    autorTextView.setText("");
                }
            }

            @Override
            public void onFailure(Call<List<Frase>> call, Throwable t) {
                fraseTextView.setText("Error de red.");
                autorTextView.setText("");
            }
        });
    }
}
