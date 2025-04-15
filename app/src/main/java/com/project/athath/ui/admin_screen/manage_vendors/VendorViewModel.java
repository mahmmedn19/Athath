package com.project.athath.ui.admin_screen.manage_vendors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.project.athath.data.model.Vendor;
import com.project.athath.data.repository.app_repo.AthathRepository;
import com.project.athath.data.utils.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class VendorViewModel extends ViewModel {
    private final AthathRepository repository;
    private final MutableLiveData<Result<List<Vendor>>> vendors = new MutableLiveData<>();

    @Inject
    public VendorViewModel(AthathRepository repository) {
        this.repository = repository;
        fetchVendors();
    }

    public LiveData<Result<List<Vendor>>> getVendors() {
        return vendors;
    }

    public void fetchVendors() {
        repository.getAllVendors().observeForever(vendors::setValue);
    }

    public void updateVendorStatus(String vendorId, String status) {
        repository.updateUserStatus(vendorId, status, "Vendors").observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                fetchVendors();  // Refresh the vendor list after updating status
            }
        });
    }
}
