package com.example.pokedex;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.models.Result;

import java.util.ArrayList;
import java.util.List;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder> implements Filterable {

    public List<Result> results;
    public static class PokedexViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public LinearLayout containerView;

        public PokedexViewHolder(@NonNull View view) {
            super(view);
            containerView = view.findViewById(R.id.pokedex_row);
            textView = view.findViewById(R.id.pokedex_row_text_view);
        }
    }
    private class PokemonFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Result> equalsList = new ArrayList<>();
            List<Result> startsList = new ArrayList<>();
            List<Result> containsList = new ArrayList<>();

            for (int i = 0 ; i < results.size() ; i++) {
                Result next = results.get(i);

                if (next.getName().toLowerCase().equals(constraint.toString().toLowerCase())) {
                    equalsList.add(next);
                } else if (next.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                    startsList.add(next);
                } else if (next.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                    containsList.add(next);
                }
            }

            List<Result> pokemonList = new ArrayList<>();
            pokemonList.clear();
            pokemonList.addAll(equalsList);
            pokemonList.addAll(startsList);
            pokemonList.addAll(containsList);

            FilterResults res = new FilterResults();
            res.values = pokemonList; // you need to create this variable!
            res.count = pokemonList.size();
            Log.v("Process","performFiltering calls");
            return res;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults r) {
            filtered = (List<Result>) r.values;
            Log.v("Process","publishResults calls");
            notifyDataSetChanged();
        }
    }

    private List<Result> filtered = new ArrayList<>();
    public Context context;
    public PokedexAdapter(List<Result> results, Context context) {
        this.results = results;
        this.context = context;
    }
    @NonNull
    @Override
    public PokedexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new PokedexViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PokedexViewHolder holder, int position) {
        Result model = null;
        if (!filtered.isEmpty() && position < filtered.size() ) {
            model = filtered.get(position);
            holder.textView.setText(model.getName().substring(0, 1).toUpperCase() + model.getName().substring(1));
        }
        else if (filtered.isEmpty()) {
            model = results.get(position);
            holder.textView.setText(model.getName().substring(0, 1).toUpperCase() + model.getName().substring(1));
            holder.containerView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), PokemonIntent.class);
                intent.putExtra("name",holder.textView.getText().toString());
                v.getContext().startActivity(intent);
            });
        }

        if (model != null) {
            holder.textView.setText(model.getName().substring(0, 1).toUpperCase() + model.getName().substring(1));
            holder.containerView.setTag(model);
            String url = model.getUrl();
            holder.containerView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), PokemonIntent.class);
                intent.putExtra("name",holder.textView.getText().toString());
                intent.putExtra("id",position+1);
                intent.putExtra("url",url);
                v.getContext().startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    @Override
    public Filter getFilter() {
        return new PokemonFilter();
    }

}
