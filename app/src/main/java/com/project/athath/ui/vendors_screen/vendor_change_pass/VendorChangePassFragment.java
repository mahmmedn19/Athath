package com.project.athath.ui.vendors_screen.vendor_change_pass;

import android.text.TextUtils;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.athath.R;
import com.project.athath.data.utils.Result;
import com.project.athath.databinding.FragmentVendorChangePassBinding;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.DialogUtils;
import com.project.athath.ui.vendors_screen.profile.ProfileViewModel;

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
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        return profileViewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Change Password");
        showBackButton(true);

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

        if (newPassword.length() < 6) {
            DialogUtils.showCustomDialog(requireContext(), "Error", "Password must be at least 6 characters.");
            return;
        }

        binding.progressBar.setVisibility(android.view.View.VISIBLE);
        profileViewModel.changePassword(currentPassword, newPassword).observe(getViewLifecycleOwner(), result -> {
            binding.progressBar.setVisibility(android.view.View.GONE);
            if (result.getStatus() == Result.Status.SUCCESS) {
                DialogUtils.showCustomDialog(requireContext(), "Success", result.getData());
                Navigation.findNavController(requireView()).popBackStack();
            }
        });
    }
}
