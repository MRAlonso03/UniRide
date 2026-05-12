package com.uniride.app.ui.trip;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uniride.app.R;
import com.uniride.app.databinding.ActivityPublishTripBinding;
import com.uniride.app.model.Trip;
import com.uniride.app.model.User;
import com.uniride.app.utils.Constants;
import com.uniride.app.viewmodel.TripViewModel;
import java.util.Calendar;

public class PublishTripActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private ActivityPublishTripBinding binding;
    private TripViewModel viewModel;
    private GoogleMap googleMap;
    private Marker originMarker;
    private Marker destinationMarker;
    private boolean selectingOrigin = true;
    private Calendar selectedDateTime = Calendar.getInstance();
    private String driverName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPublishTripBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(TripViewModel.class);

        // Inicializar mapa
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Cargar nombre del conductor desde Firestore
        loadDriverName();

        // Toolbar back
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Abrir TimePicker al tocar el campo hora
        binding.etTime.setOnClickListener(v -> pickDateTime());

        // Observar resultado de publicación
        viewModel.publishSuccess.observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnPublish.setEnabled(true);
                Toast.makeText(this,
                        "¡Viaje publicado exitosamente!",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewModel.errorMessage.observe(this, error -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnPublish.setEnabled(true);
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        });

        // Botón publicar
        binding.btnPublish.setOnClickListener(v -> publishTrip());
    }

    private void loadDriverName() {
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser == null) return;

        FirebaseFirestore.getInstance()
                .collection(Constants.USERS_COLLECTION)
                .document(fbUser.getUid())
                .get()
                .addOnSuccessListener(doc -> {
                    User user = doc.toObject(User.class);
                    if (user != null) {
                        driverName = user.getName();
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;

        // Centrar en Guadalajara por defecto
        LatLng guadalajara = new LatLng(20.6597, -103.3496);
        googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(guadalajara, 12f));

        // Tap en el mapa — primero origen, luego destino
        googleMap.setOnMapClickListener(latLng -> {
            if (selectingOrigin) {
                // Poner marcador de origen
                if (originMarker != null) originMarker.remove();
                originMarker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Origen")
                        .icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_GREEN)));

                binding.etOrigin.setText(String.format(
                        "%.5f, %.5f", latLng.latitude, latLng.longitude));

                selectingOrigin = false;
                binding.tvMapInstruction.setText(
                        "Ahora toca el mapa para marcar tu destino");

            } else {
                // Poner marcador de destino
                if (destinationMarker != null) destinationMarker.remove();
                destinationMarker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Destino")
                        .icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_RED)));

                binding.etDestination.setText(String.format(
                        "%.5f, %.5f", latLng.latitude, latLng.longitude));

                // Dibujar línea entre origen y destino
                if (originMarker != null) {
                    googleMap.addPolyline(new PolylineOptions()
                            .add(originMarker.getPosition(), latLng)
                            .color(0xFF2E6DB4)
                            .width(8f));

                    // Ajustar cámara para ver ambos puntos
                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(originMarker.getPosition())
                            .include(latLng)
                            .build();
                    googleMap.animateCamera(
                            CameraUpdateFactory.newLatLngBounds(bounds, 120));
                }

                binding.tvMapInstruction.setText(
                        "Ruta marcada. Completa los detalles abajo");
                binding.tvMapInstruction.setBackgroundColor(
                        getResources().getColor(R.color.green, null));
            }
        });
    }

    private void pickDateTime() {
        Calendar now = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            selectedDateTime.set(year, month, day);
            new TimePickerDialog(this, (timeView, hour, minute) -> {
                selectedDateTime.set(Calendar.HOUR_OF_DAY, hour);
                selectedDateTime.set(Calendar.MINUTE, minute);
                binding.etTime.setText(String.format(
                        "%02d/%02d/%d  %02d:%02d",
                        day, month + 1, year, hour, minute));
            }, now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE), true).show();
        }, now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void publishTrip() {
        String origin      = binding.etOrigin.getText().toString().trim();
        String destination = binding.etDestination.getText().toString().trim();
        String timeStr     = binding.etTime.getText().toString().trim();
        String seatsStr    = binding.etSeats.getText().toString().trim();

        // Validaciones
        if (originMarker == null) {
            Toast.makeText(this,
                    "Toca el mapa para marcar el origen",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (destinationMarker == null) {
            Toast.makeText(this,
                    "Toca el mapa para marcar el destino",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (timeStr.isEmpty()) {
            Toast.makeText(this,
                    "Selecciona la hora de salida",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (seatsStr.isEmpty()) {
            Toast.makeText(this,
                    "Indica cuántos lugares tienes disponibles",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser == null) return;

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnPublish.setEnabled(false);

        Trip trip = new Trip();
        trip.setDriverUid(fbUser.getUid());
        trip.setDriverName(driverName.isEmpty()
                ? fbUser.getEmail() : driverName);
        trip.setOriginName(origin);
        trip.setDestinationName(destination);
        trip.setOriginLat(originMarker.getPosition().latitude);
        trip.setOriginLng(originMarker.getPosition().longitude);
        trip.setDestinationLat(destinationMarker.getPosition().latitude);
        trip.setDestinationLng(destinationMarker.getPosition().longitude);
        trip.setDepartureTime(
                new Timestamp(selectedDateTime.getTime()));
        trip.setAvailableSeats(Integer.parseInt(seatsStr));

        viewModel.publishTrip(trip);
    }
}