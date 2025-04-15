package com.project.athath.ui.vendors_screen.vendo_profile_details;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.project.athath.R;
import com.project.athath.data.model.Vendor;
import com.project.athath.data.utils.Result;
import com.project.athath.databinding.FragmentVendorProfileDetailsBinding;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.DialogUtils;
import com.project.athath.ui.vendors_screen.profile.ProfileViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VendorProfileDetailsFragment extends BaseFragment<FragmentVendorProfileDetailsBinding> {

    private ProfileViewModel profileViewModel;

    @Override
    protected String getTAG() {
        return "VendorProfileDetailsFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_vendor_profile_details;
    }

    @Override
    protected ViewModel getViewModel() {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        return profileViewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Profile Details");
        showBackButton(true);

        // Disable email field (not editable)
        binding.etEmail.setEnabled(false);

        profileViewModel.loadVendorData();

        // Observe vendor data and populate UI
        profileViewModel.getVendorLiveData().observe(getViewLifecycleOwner(), vendor -> {
            if (vendor != null) {
                populateVendorData(vendor);
            }
        });

        // Save Profile Changes
        binding.btnSaveProfile.setOnClickListener(v -> saveProfile());

        // Observe update result
        profileViewModel.getUpdateResult().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                binding.progressBar.setVisibility(View.VISIBLE);
            }
            else if (result.getStatus() == Result.Status.SUCCESS) {
                binding.progressBar.setVisibility(View.GONE);
                DialogUtils.showCustomDialog(requireContext(), "Success", "Profile updated successfully!");
            } else if (result.getStatus() == Result.Status.ERROR) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void populateVendorData(Vendor vendor) {
        binding.etVendorName.setText(vendor.getName());
        binding.etStoreName.setText(vendor.getStoreName());
        binding.etPhone.setText(vendor.getPhone());
        binding.etEmail.setText(vendor.getEmail());
        binding.etAddress.setText(vendor.getAddress());
    }

    private void saveProfile() {
        String name = binding.etVendorName.getText().toString().trim();
        String storeName = binding.etStoreName.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();
        String address = binding.etAddress.getText().toString().trim();

        if (name.isEmpty() || storeName.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        Vendor currentVendor = profileViewModel.getVendorLiveData().getValue();
        if (currentVendor != null) {
            currentVendor.setName(name);
            currentVendor.setStoreName(storeName);
            currentVendor.setPhone(phone);
            currentVendor.setAddress(address);

            profileViewModel.updateVendorData(currentVendor);  // Save changes
        }
    }
}
