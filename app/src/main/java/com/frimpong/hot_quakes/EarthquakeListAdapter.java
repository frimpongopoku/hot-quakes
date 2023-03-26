package com.frimpong.hot_quakes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeListAdapter extends RecyclerView.Adapter<EarthquakeListAdapter.ViewHolder> {

    private List<EarthquakeItem> earthquakes;
    private Context context;

    public EarthquakeListAdapter(Context context) {
        this.context = context;
        earthquakes = new ArrayList<>();
    }

    public void setEarthquakes(List<EarthquakeItem> earthquakes) {
        this.earthquakes = earthquakes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.recycler_view_earth_quake_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EarthquakeItem earthquakeItem = earthquakes.get(position);
        holder.binding.setVariable(BR.earthquake, earthquakeItem);
        holder.binding.executePendingBindings();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PRINTING", "I just clicked an item what do you think meerhn!");
                // TODO: when an item is clicked what should happen here
            }
        });
    }

    @Override
    public int getItemCount() {
        return earthquakes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding binding;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

