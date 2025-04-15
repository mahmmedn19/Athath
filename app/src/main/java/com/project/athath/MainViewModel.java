package com.project.athath;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.athath.data.model.Customer;
import com.project.athath.data.repository.app_repo.AthathRepository;
import com.project.athath.data.repository.auth.AuthRepository;
import com.project.athath.data.utils.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final AthathRepository aiRepository;
    private final FirebaseAuth firebaseAuth;
    private final MutableLiveData<Boolean> isCustomerLoggedIn = new MutableLiveData<>();
    private final MutableLiveData<Result<Customer>> customerProfileLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<String>> aiLinkLiveData = new MutableLiveData<>();

    @Inject
    public MainViewModel(AuthRepository authRepository, FirebaseAuth firebaseAuth , AthathRepository aiRepository) {
        this.authRepository = authRepository;
        this.aiRepository = aiRepository;
        this.firebaseAuth = firebaseAuth;
        fetchAILink(); // Auto-fetch on ViewModel creation
        checkCustomerLoginState();
    }

    public LiveData<Boolean> getIsCustomerLoggedIn() {
        return isCustomerLoggedIn;
    }

    public LiveData<Result<Customer>> getCustomerProfileLiveData() {
        return customerProfileLiveData;
    }

    // ✅ Check if a customer is logged in and has a profile
    public void checkCustomerLoginState() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            authRepository.getCustomerProfile().observeForever(result -> {
                if (result.getStatus() == Result.Status.SUCCESS) {
                    isCustomerLoggedIn.setValue(true);
                    customerProfileLiveData.setValue(result);
                } else {
                    isCustomerLoggedIn.setValue(false);
                }
            });
        } else {
            isCustomerLoggedIn.setValue(false);
        }
    }

    // ✅ Handle logout
    public void logout() {
        authRepository.logoutUser().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                isCustomerLoggedIn.setValue(false);
            }
        });
    }
    public void fetchAILink() {
        aiLinkLiveData.setValue(Result.loading());

        aiRepository.getSingleAILink().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                aiLinkLiveData.setValue(Result.success(result.getData()));
            } else {
                aiLinkLiveData.setValue(Result.error(result.getErrorMessage()));
            }
        });
    }

    public LiveData<Result<String>> getSingleAILink() {
        return aiLinkLiveData;
    }

}
