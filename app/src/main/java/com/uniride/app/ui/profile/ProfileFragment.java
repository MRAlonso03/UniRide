package com.uniride.app.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uniride.app.databinding.FragmentProfileBinding;
import com.uniride.app.model.User;
import com.uniride.app.ui.auth.LoginActivity;
import com.uniride.app.utils.Constants;
import com.uniride.app.viewmodel.AuthViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private AuthViewModel authViewModel;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        loadUserProfile();

        // Botón cerrar sesión
        binding.btnLogout.setOnClickListener(v -> {
            authViewModel.logout();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void loadUserProfile() {
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser == null) return;

        db.collection(Constants.USERS_COLLECTION)
                .document(fbUser.getUid())
                .get()
                .addOnSuccessListener(doc -> {
                    User user = doc.toObject(User.class);
                    if (user == null) return;

                    binding.tvName.setText(user.getName());
                    binding.tvEmail.setText(user.getEmail());
                    binding.tvStudentId.setText("Matrícula: " + user.getStudentId());
                    binding.tvUniversity.setText(user.getUniversity());
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(),
                                "Error al cargar perfil", Toast.LENGTH_SHORT).show()
                );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}