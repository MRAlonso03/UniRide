package com.uniride.app.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.uniride.app.repository.AuthRepository;

public class AuthViewModel extends ViewModel {
    private final AuthRepository repo = new AuthRepository();

    public final MutableLiveData<FirebaseUser> userLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public void login(String email, String password) {
        repo.login(email, password, userLiveData, errorLiveData);
    }

    public void register(String email, String password,
                         String name, String studentId) {
        repo.register(email, password, name, studentId, userLiveData, errorLiveData);
    }

    public FirebaseUser getCurrentUser() {
        return repo.getCurrentUser();
    }

    public void logout() {
        repo.logout();
    }
}