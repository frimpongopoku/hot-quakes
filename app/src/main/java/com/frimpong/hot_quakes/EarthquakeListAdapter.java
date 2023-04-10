package com.frimpong.hot_quakes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
        int color = earthquakeItem.getColorRepresentation();
        color = ContextCompat.getColor(context,color);
        holder.title.setTextColor(color);
//        holder.magnitudeText.setTextColor(color);
        holder.binding.setVariable(BR.earthquake, earthquakeItem);
        holder.binding.executePendingBindings();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(Constants.TITLE,earthquakeItem.getTitle());
                intent.putExtra(Constants.MAGNITUDE,earthquakeItem.getMagnitude());
                intent.putExtra(Constants.DEPTH,earthquakeItem.getDepth());
                intent.putExtra(Constants.LONG,earthquakeItem.getLongitude());
                intent.putExtra(Constants.LAT,earthquakeItem.getLatitude());
                intent.putExtra(Constants.DESC,earthquakeItem.getDescription());
                intent.putExtra(Constants.PUB_DATE,earthquakeItem.getPubDate());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return earthquakes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding binding;
        TextView magnitudeText, title;
        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.title = binding.getRoot().findViewById(R.id.country_name_text);
//            this.magnitudeText = binding.getRoot().findViewById(R.id.mag_and_depth_text);
        }
    }
}

