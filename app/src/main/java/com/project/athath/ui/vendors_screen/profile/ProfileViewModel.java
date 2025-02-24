package com.project.athath.ui.vendors_screen.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.athath.data.model.Vendor;
import com.project.athath.data.repository.auth.AuthRepository;
import com.project.athath.data.utils.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProfileViewModel extends ViewModel {

    private final AuthRepository vendorRepository;

    private final MutableLiveData<Vendor> vendorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<String>> updateResult = new MutableLiveData<>();
    private final MutableLiveData<Result<String>> passwordUpdateResult = new MutableLiveData<>();

    @Inject
    public ProfileViewModel(AuthRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
        loadVendorData();
    }

    public LiveData<Vendor> getVendorLiveData() {
        return vendorLiveData;
    }

    public LiveData<Result<String>> getUpdateResult() {
        return updateResult;
    }

    public void loadVendorData() {
        vendorRepository.getVendorProfile().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                vendorLiveData.setValue(result.getData());
            }
        });
    }

    public void updateVendorData(Vendor vendor) {
        vendorRepository.updateVendorProfile(vendor).observeForever(result -> {
            updateResult.setValue(result);
            if (result.getStatus() == Result.Status.SUCCESS) {
                vendorLiveData.setValue(vendor);  // Update local data
            }
        });
    }

    public LiveData<Result<String>> getPasswordUpdateResult() {
        return passwordUpdateResult;
    }

    public LiveData<Result<String>> changePassword(String currentPassword, String newPassword) {
        vendorRepository.changePassword(currentPassword, newPassword).observeForever(result -> {
            passwordUpdateResult.setValue(result);
        });
        return passwordUpdateResult;
    }

}
