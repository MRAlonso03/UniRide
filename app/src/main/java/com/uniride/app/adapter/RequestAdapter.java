package com.uniride.app.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.uniride.app.databinding.ItemRequestBinding;
import com.uniride.app.model.TripRequest;
import java.util.ArrayList;
import java.util.List;

public class RequestAdapter extends
        RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    public interface OnRequestActionListener {
        void onAction(TripRequest request);
    }

    private List<TripRequest> requests = new ArrayList<>();
    private final OnRequestActionListener onAccept;
    private final OnRequestActionListener onReject;

    public RequestAdapter(OnRequestActionListener onAccept,
                          OnRequestActionListener onReject) {
        this.onAccept = onAccept;
        this.onReject = onReject;
    }

    public void setRequests(List<TripRequest> requests) {
        this.requests = requests;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        ItemRequestBinding binding = ItemRequestBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new RequestViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder,
                                 int position) {
        holder.bind(requests.get(position));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {

        private final ItemRequestBinding b;

        RequestViewHolder(ItemRequestBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        void bind(TripRequest request) {
            b.tvPassengerName.setText(request.getPassengerName());

            b.btnAccept.setOnClickListener(v -> {
                onAccept.onAction(request);
                b.btnAccept.setEnabled(false);
                b.btnReject.setEnabled(false);
                b.tvRequestStatus.setText("Aceptado ✓");
            });

            b.btnReject.setOnClickListener(v -> {
                onReject.onAction(request);
                b.btnAccept.setEnabled(false);
                b.btnReject.setEnabled(false);
                b.tvRequestStatus.setText("Rechazado ✗");
            });
        }
    }
}