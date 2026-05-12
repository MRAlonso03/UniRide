package com.uniride.app.repository;

import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uniride.app.model.User;
import com.uniride.app.utils.Constants;
import com.uniride.app.utils.ValidationUtils;

public class AuthRepository {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Validar dominio institucional
    private boolean isInstitutionalEmail(String email) {
        return ValidationUtils.isInstitutionalEmail(email);
    }

    public void register(String email, String password,
                         String name, String studentId,
                         MutableLiveData<FirebaseUser> userLiveData,
                         MutableLiveData<String> errorLiveData) {

        if (!isInstitutionalEmail(email)) {
            errorLiveData.setValue("Debes usar tu correo institucional universitario.");
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    FirebaseUser fbUser = result.getUser();
                    if (fbUser == null) return;

                    // Guardar perfil en Firestore
                    User user = new User(
                            fbUser.getUid(), name, email,
                            studentId, "Universidad"
                    );
                    db.collection(Constants.USERS_COLLECTION)
                            .document(fbUser.getUid())
                            .set(user)
                            .addOnSuccessListener(v -> userLiveData.setValue(fbUser))
                            .addOnFailureListener(e -> errorLiveData.setValue(e.getMessage()));
                })
                .addOnFailureListener(e -> errorLiveData.setValue(e.getMessage()));
    }

    public void login(String email, String password,
                      MutableLiveData<FirebaseUser> userLiveData,
                      MutableLiveData<String> errorLiveData) {

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(r -> userLiveData.setValue(r.getUser()))
                .addOnFailureListener(e -> errorLiveData.setValue(e.getMessage()));
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public void logout() {
        auth.signOut();
    }
}