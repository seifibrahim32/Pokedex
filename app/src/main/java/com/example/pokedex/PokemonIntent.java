package com.example.pokedex;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.api.APIClient;
import com.example.models.FlavorTextEntries;
import com.example.models.IDImgTypes;
import com.example.models.RecyclerAPI;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PokemonIntent extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ImageView pokemonPic;
    private TextView nameTextView, numberTextView;
    private TextView type1TextView;
    private TextView type2TextView;
    private TextView catchButtonText;
    private TextView pokemonDesc;
    private Boolean catched;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);
        sharedPreferences = getApplicationContext().getSharedPreferences("PokemonPull", 0);
        editor = sharedPreferences.edit();
        catched = false;
        pokemonPic = findViewById(R.id.imageView);
        nameTextView = findViewById(R.id.pokemon_name);
        numberTextView = findViewById(R.id.pokemon_number);
        type1TextView = findViewById(R.id.pokemon_type1);
        type2TextView = findViewById(R.id.pokemon_type2);
        catchButtonText = findViewById(R.id.button);
        pokemonDesc = findViewById(R.id.Description);
        APIClient.BASE_URL = "https://pokeapi.co/api/v2/" ;
        RecyclerAPI service =  APIClient.getInstance().create(RecyclerAPI.class);
        Call<IDImgTypes> call = service.getId(getIntent().getIntExtra("id", 0));
        call.enqueue(new Callback<IDImgTypes>() {
            @Override
            public void onResponse(Call<IDImgTypes> call, Response<IDImgTypes> response) {
                numberTextView.setText(String.format("#%03d", response.body().getId()));
                nameTextView.setText(response.body().getName());
                if(response.body().getTypes().size() == 2) {
                    type1TextView.setText(response.body().getTypes().get(0).getType().getName());
                    type2TextView.setText(response.body().getTypes().get(1).getType().getName());
                }
                else if(response.body().getTypes().size() == 1)
                {
                    type1TextView.setText(response.body().getTypes().get(0).getType().getName());
                }

                if (sharedPreferences.getBoolean(response.body().getName(),false)) {
                    catchPokemon();
                } else {
                    releasePokemon();
                }
                ImageView ivBasicImage = (ImageView) findViewById(R.id.imageView);
                Picasso.get().load(response.body().getSprites().getFrontDefault()).into(ivBasicImage);
            }

            @Override
            public void onFailure(Call<IDImgTypes> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"JSON parsing failed",Toast.LENGTH_LONG).show();
            }
        });
        Call<FlavorTextEntries> call_2 = service.getFlav(getIntent().getIntExtra("id", 0));
        call_2.enqueue(new Callback<FlavorTextEntries>() {
            @Override
            public void onResponse(Call<FlavorTextEntries> call, Response<FlavorTextEntries> response) {
                for(int i = 0 ; i < response.body().getFlavorTextEntries().size() ; i++){
                    if(response.body().getFlavorTextEntries().get(i).getLanguage().getName().equals("en"))
                    pokemonDesc.setText(response.body().getFlavorTextEntries().get(i).getFlavorText());
                }
            }

            @Override
            public void onFailure(Call<FlavorTextEntries> call, Throwable t) {

            }
        });
    }

    public void toggleCatch(View view) {

        if (catched) {
            releasePokemon();
        } else {
            catchPokemon();
        }
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void catchPokemon() {
        catched = true;
        catchButtonText.setText("Release");
        editor.putBoolean(numberTextView.getText().toString(), true);
        editor.putBoolean(nameTextView.getText().toString(), true);
        editor.commit();
    }

    private void releasePokemon() {
        catched = false;
        catchButtonText.setText("Catch");
        editor.remove(nameTextView.getText().toString());
        editor.remove(numberTextView.getText().toString());
        editor.commit();
    }
}
