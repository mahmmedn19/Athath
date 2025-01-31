package com.project.asas.ui.vendors_screen.vendo_profile_details;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.project.asas.R;
import com.project.asas.databinding.FragmentVendorProfileDetailsBinding;
import com.project.asas.model.Vendor;
import com.project.asas.ui.base.BaseFragment;
import com.project.asas.ui.utils.DialogUtils;
import com.project.asas.ui.vendors_screen.profile.ProfileViewModel;

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
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Profile Details");
        showBackButton(true);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.loadVendorData();

        // Observe vendor data and populate UI
        profileViewModel.getVendorLiveData().observe(getViewLifecycleOwner(), vendor -> {
            binding.etVendorName.setText(vendor.getName());
            binding.etStoreName.setText(vendor.getStoreName());
            binding.etPhone.setText(vendor.getPhone());
            binding.etEmail.setText(vendor.getEmail());
            binding.etAddress.setText(vendor.getAddress());
        });

        // Save Profile Changes
        binding.btnSaveProfile.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        Vendor updatedVendor = new Vendor(
                binding.etVendorName.getText().toString(),
                binding.etStoreName.getText().toString(),
                binding.etPhone.getText().toString(),
                binding.etAddress.getText().toString(),
                binding.etEmail.getText().toString(),
                "Active", // Default status
                ""
        );

        profileViewModel.updateVendorProfile(updatedVendor);
        DialogUtils.showCustomDialog(requireContext(), "Success", "Profile updated successfully!");
    }

}