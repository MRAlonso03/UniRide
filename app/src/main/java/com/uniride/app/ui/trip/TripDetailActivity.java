package com.uniride.app.ui.trip;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uniride.app.R;
import com.uniride.app.databinding.ActivityTripDetailBinding;
import com.uniride.app.model.Trip;
import com.uniride.app.model.TripRequest;
import com.uniride.app.utils.Constants;
import com.uniride.app.viewmodel.TripViewModel;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TripDetailActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private ActivityTripDetailBinding binding;
    private TripViewModel viewModel;
    private GoogleMap googleMap;
    private Trip currentTrip;
    private String tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTripDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(TripViewModel.class);

        // Obtener tripId del Intent
        tripId = getIntent().getStringExtra("tripId");
        if (tripId == null) {
            Toast.makeText(this, "Error al cargar el viaje",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar mapa
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Toolbar back
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Cargar datos del viaje desde Firestore
        loadTripData();

        // Observar resultado de solicitud
        viewModel.requestSuccess.observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnRequestJoin.setEnabled(false);
                binding.btnRequestJoin.setText("Solicitud enviada ✓");
                Toast.makeText(this,
                        "¡Solicitud enviada! El conductor te responderá pronto.",
                        Toast.LENGTH_LONG).show();
            }
        });

        viewModel.errorMessage.observe(this, error -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnRequestJoin.setEnabled(true);
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        });

        // Botón solicitar unirse
        binding.btnRequestJoin.setOnClickListener(v -> sendJoinRequest());
    }

    private void loadTripData() {
        FirebaseFirestore.getInstance()
                .collection(Constants.TRIPS_COLLECTION)
                .document(tripId)
                .get()
                .addOnSuccessListener(doc -> {
                    currentTrip = doc.toObject(Trip.class);
                    if (currentTrip == null) {
                        Toast.makeText(this,
                                "Viaje no encontrado", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    bindTripData();

                    // Si el mapa ya estaba listo, dibuja la ruta
                    if (googleMap != null) {
                        drawRouteOnMap();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Error al cargar el viaje: " + e.getMessage(),
                                Toast.LENGTH_LONG).show()
                );
    }

    private void bindTripData() {
        binding.tvDriverName.setText(currentTrip.getDriverName());
        binding.tvOrigin.setText(currentTrip.getOriginName());
        binding.tvDestination.setText(currentTrip.getDestinationName());
        binding.chipSeats.setText(
                currentTrip.getAvailableSeats() + " lugares");

        if (currentTrip.getDepartureTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "EEE d MMM, HH:mm", Locale.getDefault());
            binding.tvTime.setText(
                    sdf.format(currentTrip.getDepartureTime().toDate()));
        }

        // Ocultar botón si el usuario actual es el conductor
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null
                && user.getUid().equals(currentTrip.getDriverUid())) {
            binding.btnRequestJoin.setVisibility(View.GONE);
        }

        // Verificar si ya envió solicitud antes
        checkExistingRequest();
    }

    private void checkExistingRequest() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        FirebaseFirestore.getInstance()
                .collection(Constants.REQUESTS_COLLECTION)
                .whereEqualTo("tripId", tripId)
                .whereEqualTo("passengerUid", user.getUid())
                .get()
                .addOnSuccessListener(snapshots -> {
                    if (!snapshots.isEmpty()) {
                        binding.btnRequestJoin.setEnabled(false);
                        binding.btnRequestJoin.setText("Solicitud ya enviada ✓");
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        if (currentTrip != null) {
            drawRouteOnMap();
        }
    }

    private void drawRouteOnMap() {
        LatLng origin = new LatLng(
                currentTrip.getOriginLat(),
                currentTrip.getOriginLng());
        LatLng destination = new LatLng(
                currentTrip.getDestinationLat(),
                currentTrip.getDestinationLng());

        // Marcador origen verde
        googleMap.addMarker(new MarkerOptions()
                .position(origin)
                .title("Origen")
                .icon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN)));

        // Marcador destino rojo
        googleMap.addMarker(new MarkerOptions()
                .position(destination)
                .title("Destino")
                .icon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_RED)));

        // Línea de ruta
        googleMap.addPolyline(new PolylineOptions()
                .add(origin, destination)
                .color(0xFF2E6DB4)
                .width(8f));

        // Ajustar cámara para ver ambos puntos
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination)
                .build();
        googleMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(bounds, 150));
    }

    private void sendJoinRequest() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnRequestJoin.setEnabled(false);

        // Obtener nombre del pasajero desde Firestore
        FirebaseFirestore.getInstance()
                .collection(Constants.USERS_COLLECTION)
                .document(user.getUid())
                .get()
                .addOnSuccessListener(doc -> {
                    String passengerName = user.getEmail();
                    if (doc.exists() && doc.getString("name") != null) {
                        passengerName = doc.getString("name");
                    }

                    TripRequest request = new TripRequest();
                    request.setTripId(tripId);
                    request.setPassengerUid(user.getUid());
                    request.setPassengerName(passengerName);

                    viewModel.requestJoinTrip(request);
                })
                .addOnFailureListener(e -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnRequestJoin.setEnabled(true);
                    Toast.makeText(this,
                            "Error al obtener tu perfil",
                            Toast.LENGTH_SHORT).show();
                });
    }
}