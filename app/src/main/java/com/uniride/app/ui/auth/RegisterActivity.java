package com.uniride.app.ui.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.uniride.app.databinding.ActivityRegisterBinding;
import com.uniride.app.viewmodel.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        viewModel.userLiveData.observe(this, user -> {
            if (user != null) {
                Toast.makeText(this, "¡Cuenta creada exitosamente!", Toast.LENGTH_SHORT).show();
                finish(); // Regresa a LoginActivity, que detecta la sesión
            }
        });

        viewModel.errorLiveData.observe(this, error -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnRegister.setEnabled(true);
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        });

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnRegister.setOnClickListener(v -> {
            String name      = binding.etName.getText().toString().trim();
            String studentId = binding.etStudentId.getText().toString().trim();
            String email     = binding.etEmail.getText().toString().trim();
            String password  = binding.etPassword.getText().toString().trim();

            if (name.isEmpty() || studentId.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(this, "La contraseña debe tener mínimo 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnRegister.setEnabled(false);
            viewModel.register(email, password, name, studentId);
        });
    }
}