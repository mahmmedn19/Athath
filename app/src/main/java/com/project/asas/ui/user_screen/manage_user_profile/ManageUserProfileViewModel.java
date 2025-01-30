package com.project.asas.ui.user_screen.manage_user_profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageUserProfileViewModel extends ViewModel {

    private MutableLiveData<String> userName = new MutableLiveData<>("John Doe");
    private MutableLiveData<String> email = new MutableLiveData<>("johndoe@example.com");

    @Inject
    public ManageUserProfileViewModel() {
        // Fake Data for Initial State
        userName.setValue("John Doe");
        email.setValue("johndoe@example.com");
    }

    public String getUserName() {
        return userName.getValue();
    }

    public String getEmail() {
        return email.getValue();
    }

    public void updateUserProfile(String newUserName, String newEmail) {
        // In a real scenario, update Firebase or API backend
        userName.setValue(newUserName);
        email.setValue(newEmail);
    }

    public boolean changePassword(String currentPassword, String newPassword, String confirmPassword) {
        // Fake validation logic
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            return false;
        }

        // Simulate password update
        return true;
    }
}
