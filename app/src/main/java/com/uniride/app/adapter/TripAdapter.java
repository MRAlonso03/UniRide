package com.uniride.app.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.uniride.app.R;
import com.uniride.app.databinding.ItemTripBinding;
import com.uniride.app.model.Trip;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    public interface OnTripClickListener {
        void onTripClick(Trip trip);
    }

    private List<Trip> trips = new ArrayList<>();
    private final OnTripClickListener listener;

    public TripAdapter(OnTripClickListener listener) {
        this.listener = listener;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
        ItemTripBinding binding = ItemTripBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new TripViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        holder.bind(trips.get(position));
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    class TripViewHolder extends RecyclerView.ViewHolder {

        private final ItemTripBinding b;

        TripViewHolder(ItemTripBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        void bind(Trip trip) {
            b.tvOrigin.setText(trip.getOriginName());
            b.tvDestination.setText(trip.getDestinationName());
            b.tvDriverName.setText(trip.getDriverName());
            b.tvSeats.setText(trip.getAvailableSeats() + " lugares");

            if (trip.getDepartureTime() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "EEE d MMM, HH:mm", Locale.getDefault());
                b.tvTime.setText(
                        sdf.format(trip.getDepartureTime().toDate()));
            }

            if (trip.getDriverPhotoUrl() != null
                    && !trip.getDriverPhotoUrl().isEmpty()) {
                Glide.with(b.getRoot())
                        .load(trip.getDriverPhotoUrl())
                        .circleCrop()
                        .placeholder(R.drawable.ic_person)
                        .into(b.ivDriverPhoto);
            }

            b.getRoot().setOnClickListener(v -> listener.onTripClick(trip));
        }
    }
}