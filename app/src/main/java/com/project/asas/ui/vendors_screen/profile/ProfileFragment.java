package com.project.asas.ui.vendors_screen.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.asas.MainActivity;
import com.project.asas.R;
import com.project.asas.databinding.FragmentProfileBinding;
import com.project.asas.model.Vendor;
import com.project.asas.ui.base.BaseFragment;
import com.project.asas.ui.utils.DialogUtils;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends BaseFragment<FragmentProfileBinding> {

    private ProfileViewModel profileViewModel;

    @Override
    protected String getTAG() {
        return "ProfileFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_profile;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(false);

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

        // Change Password
        binding.btnChangePassword.setOnClickListener(v -> changePassword());

        // Logout Action
        binding.btnLogout.setOnClickListener(v -> {
            DialogUtils.showConfirmationDialog(
                    requireContext(),
                    "Logout",
                    "Are you sure you want to log out?",
                    "Yes",
                    "Cancel",
                    (dialog, which) -> {
                        Intent intent = new Intent(requireContext(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    });
        });
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

    private void changePassword() {
        String currentPassword = binding.etCurrentPassword.getText().toString().trim();
        String newPassword = binding.etNewPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            DialogUtils.showCustomDialog(requireContext(), "Error", "All password fields are required.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            DialogUtils.showCustomDialog(requireContext(), "Error", "New password and confirm password do not match.");
            return;
        }

        profileViewModel.updatePassword(currentPassword, newPassword);
        DialogUtils.showCustomDialog(requireContext(), "Success", "Password changed successfully!");
    }
}
