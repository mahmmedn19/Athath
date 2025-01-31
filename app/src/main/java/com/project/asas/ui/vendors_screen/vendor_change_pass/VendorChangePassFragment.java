package com.project.asas.ui.vendors_screen.vendor_change_pass;

import android.text.TextUtils;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.project.asas.R;
import com.project.asas.databinding.FragmentVendorChangePassBinding;
import com.project.asas.ui.base.BaseFragment;
import com.project.asas.ui.utils.DialogUtils;
import com.project.asas.ui.vendors_screen.profile.ProfileViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VendorChangePassFragment extends BaseFragment<FragmentVendorChangePassBinding> {
    private ProfileViewModel profileViewModel;

    @Override
    protected String getTAG() {
        return "VendorChangePassFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_vendor_change_pass;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Change Password");
        showBackButton(true);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Change Password
        binding.btnChangePassword.setOnClickListener(v -> changePassword());
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