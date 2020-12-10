package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.api.APIClient;
import com.example.models.Pokemon;
import com.example.models.RecyclerAPI;
import com.example.models.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    PokedexAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Result> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        APIClient.BASE_URL ="https://pokeapi.co/api/v2/";
        RecyclerAPI service =  APIClient.getInstance().create(RecyclerAPI.class);
        Call<Pokemon> call = service.getResults(150);
        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if(response.isSuccessful() ){
                    if(!results.isEmpty()){
                        results.clear();
                    }
                    results = response.body().getResults();
                    adapter = new PokedexAdapter(results,  MainActivity.this);

                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                Log.v("onResponse:","Works");
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Doesn't work",Toast.LENGTH_SHORT).show();

                Log.v("onFailure:",t.getMessage());
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        adapter.getFilter().filter(query);

        Log.v("Process","onQueryTextChange calls");
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.getFilter().filter(query);
        Log.v("Process","onQueryTextSubmit calls");
        return false;
    }
}