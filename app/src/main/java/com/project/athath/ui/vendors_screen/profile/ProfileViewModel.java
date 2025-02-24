package com.project.athath.ui.vendors_screen.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.project.athath.data.model.Vendor;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<Vendor> vendorLiveData = new MutableLiveData<>();

    public LiveData<Vendor> getVendorLiveData() {
        return vendorLiveData;
    }

    public void loadVendorData() {
        // Simulated vendor data fetch

    }

    public void updateVendorProfile(Vendor updatedVendor) {
        vendorLiveData.setValue(updatedVendor);
    }

    public void updatePassword(String currentPassword, String newPassword) {
        Vendor vendor = vendorLiveData.getValue();
        if (vendor != null) {

        }
    }
}
