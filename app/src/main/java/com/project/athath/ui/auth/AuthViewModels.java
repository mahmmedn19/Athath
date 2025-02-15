package com.project.athath.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.textfield.TextInputLayout;
import com.project.athath.data.model.Customer;
import com.project.athath.data.model.Vendor;
import com.project.athath.data.repository.auth.AuthRepository;
import com.project.athath.data.utils.Result;
import com.project.athath.ui.utils.InputValidator;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

public class AuthViewModels {

    // Login ViewModel
    @HiltViewModel
    public static class LoginViewModel extends ViewModel {
        private final AuthRepository authRepository;
        private final MutableLiveData<Result<String>> loginResult = new MutableLiveData<>();
        private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

        @Inject
        public LoginViewModel(AuthRepository authRepository) {
            this.authRepository = authRepository;
            isLoading.setValue(false); // Initialize to false
        }

        public LiveData<Result<String>> getLoginResult() {
            return loginResult;
        }

        public LiveData<Boolean> getLoadingState() {
            return isLoading;
        }

        public void loginUser(String email, String password) {
            if (!InputValidator.isValidEmailFormat(email) || password.length() < 6) {
                loginResult.setValue(Result.error("Invalid email or password"));
                return;
            }

            isLoading.setValue(true); // Show loading before request

            authRepository.loginUser(email, password).observeForever(result -> {
                loginResult.setValue(result);
                isLoading.postValue(false); // Use postValue to avoid UI threading issues
            });
        }
    }

    // Register ViewModel
// Register ViewModel
    @HiltViewModel
    public static class RegisterViewModel extends ViewModel {
        private final AuthRepository authRepository;
        private final MutableLiveData<Result<String>> registerResult = new MutableLiveData<>();
        private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

        @Inject
        public RegisterViewModel(AuthRepository authRepository) {
            this.authRepository = authRepository;
            isLoading.setValue(false); // Initialize to false
        }

        public LiveData<Result<String>> getRegisterResult() {
            return registerResult;
        }

        public LiveData<Boolean> getLoadingState() {
            return isLoading;
        }

        public void registerCustomer(Customer customer, TextInputLayout emailInputLayout, TextInputLayout passwordInputLayout) {
            if (!InputValidator.validateEmail(emailInputLayout, customer.getEmail()) ||
                    !InputValidator.validatePassword(passwordInputLayout, customer.getPassword())) {
                registerResult.setValue(Result.error("Invalid email or password"));
                return;
            }

            isLoading.setValue(true);
            authRepository.registerCustomer(customer).observeForever(result -> {
                registerResult.setValue(result);
                isLoading.setValue(false);
            });
        }

        public void registerVendor(Vendor vendor) {
            if (!InputValidator.validateEmail(null, vendor.getEmail()) || !InputValidator.validatePassword(null, vendor.getPassword()) || !InputValidator.validatePhone(null, vendor.getPhone())) {
                registerResult.setValue(Result.error("Invalid vendor details"));
                return;
            }

            isLoading.setValue(true);
            authRepository.registerVendor(vendor).observeForever(result -> {
                registerResult.setValue(result);
                isLoading.setValue(false);
            });
        }
    }
    @HiltViewModel
    public static class ForgotPasswordViewModel extends ViewModel {

        private final AuthRepository authRepository;
        private final MutableLiveData<Result<String>> resetPasswordResult = new MutableLiveData<>();

        @Inject
        public ForgotPasswordViewModel(AuthRepository authRepository) {
            this.authRepository = authRepository;
        }

        public LiveData<Result<String>> getResetPasswordResult() {
            return resetPasswordResult;
        }

        public void resetPassword(String email) {
            resetPasswordResult.setValue(Result.loading());
            authRepository.sendPasswordResetEmail(email).observeForever(resetPasswordResult::setValue);
        }
    }
}
