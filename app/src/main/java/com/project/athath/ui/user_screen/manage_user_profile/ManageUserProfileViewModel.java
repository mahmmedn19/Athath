package com.project.athath.ui.user_screen.manage_user_profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.athath.data.model.Customer;
import com.project.athath.data.repository.auth.AuthRepository;
import com.project.athath.data.utils.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageUserProfileViewModel extends ViewModel {

    private final AuthRepository authRepository;

    private final MutableLiveData<Result<Customer>> customerLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<String>> updateProfileResult = new MutableLiveData<>();
    private final MutableLiveData<Result<String>> passwordChangeResult = new MutableLiveData<>();

    @Inject
    public ManageUserProfileViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
        fetchCustomerProfile();
    }

    public LiveData<Result<Customer>> getCustomerLiveData() {
        return customerLiveData;
    }

    public LiveData<Result<String>> getUpdateProfileResult() {
        return updateProfileResult;
    }

    public LiveData<Result<String>> getPasswordChangeResult() {
        return passwordChangeResult;
    }

    public void fetchCustomerProfile() {
        customerLiveData.setValue(Result.loading());
        authRepository.getCustomerProfile().observeForever(customerLiveData::postValue);
    }

    public void updateCustomerProfile(Customer customer) {
        updateProfileResult.setValue(Result.loading());
        authRepository.updateCustomerProfile(customer).observeForever(updateProfileResult::postValue);
    }

    public void changePassword(String currentPassword, String newPassword) {
        passwordChangeResult.setValue(Result.loading());
        authRepository.changePassword(currentPassword, newPassword).observeForever(passwordChangeResult::postValue);
    }
}
