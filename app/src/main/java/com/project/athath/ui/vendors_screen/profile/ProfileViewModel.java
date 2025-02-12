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
        Vendor vendor = new Vendor(
                "John Doe",
                "Luxury Furniture",
                "+1234567890",
                "1234 Street, City",
                "vendor@example.com",
                "Active",
                "password123" // Simulated stored password
        );
        vendorLiveData.setValue(vendor);
    }

    public void updateVendorProfile(Vendor updatedVendor) {
        vendorLiveData.setValue(updatedVendor);
    }

    public void updatePassword(String currentPassword, String newPassword) {
        Vendor vendor = vendorLiveData.getValue();
        if (vendor != null) {
            if (!vendor.getPassword().equals(currentPassword)) {
                vendorLiveData.postValue(vendor);
            } else {
                vendor.setPassword(newPassword);
                vendorLiveData.postValue(vendor);
            }
        }
    }
}
