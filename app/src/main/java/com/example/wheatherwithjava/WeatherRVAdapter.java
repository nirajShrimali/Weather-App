package com.example.wheatherwithjava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<forecastRVModal> arrayList;

    public WeatherRVAdapter(Context context, ArrayList<forecastRVModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public WeatherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.ViewHolder holder, int position) {
        forecastRVModal forecastRVModal = arrayList.get(position);
        SimpleDateFormat input = new SimpleDateFormat("yyyy-mm-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
        try {
            Date date = input.parse(arrayList.get(position).getTime());
            holder.windSpeed.setText(output.format(date));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        holder.temperature.setText(arrayList.get(position).getTemperature().concat("°C"));
        Picasso.get().load("https:".concat(arrayList.get(position).getIcon())).into(holder.icon);
        holder.windSpeed.setText(arrayList.get(position).getWindSpeed());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time,temperature,windSpeed;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.timeRV);
            temperature = itemView.findViewById(R.id.temperatureRV);
            windSpeed = itemView.findViewById(R.id.windSpeed);
            icon = itemView.findViewById(R.id.iconRV);
        }
    }
}
