package com.project.athath.ui.user_screen.manage_user_profile;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.athath.R;
import com.project.athath.data.model.Customer;
import com.project.athath.data.utils.Result;
import com.project.athath.databinding.FragmentManageUserProfileBinding;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.DialogUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageUserProfileFragment extends BaseFragment<FragmentManageUserProfileBinding> {

    private ManageUserProfileViewModel viewModel;

    @Override
    protected String getTAG() {
        return "ManageUserProfileFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_manage_user_profile;
    }

    @Override
    protected ManageUserProfileViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(ManageUserProfileViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Manage User Profile");
        showBackButton(true);
        binding.etEmail.setEnabled(false);

        observeViewModel();

        binding.btnSaveProfile.setOnClickListener(v -> saveProfile());
        binding.btnChangePassword.setOnClickListener(v -> changePassword());
    }

    private void observeViewModel() {
        viewModel.getCustomerLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) showLoading(true);
            else {
                showLoading(false);
                if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                    populateUserProfile(result.getData());
                }
            }
        });

        viewModel.getUpdateProfileResult().observe(getViewLifecycleOwner(), result -> {
            showLoading(false);
        });

        viewModel.getPasswordChangeResult().observe(getViewLifecycleOwner(), result -> {
            showLoading(false);
        });
    }

    private void populateUserProfile(Customer customer) {
        binding.etUserName.setText(customer.getUsername());
        binding.etEmail.setText(customer.getEmail());
    }

    private void saveProfile() {
        Customer customer = new Customer(
                binding.etUserName.getText().toString().trim(),
                binding.etEmail.getText().toString().trim()
        );
        viewModel.updateCustomerProfile(customer);
        Navigation.findNavController(requireView()).popBackStack();
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

        viewModel.changePassword(currentPassword, newPassword);
        Navigation.findNavController(requireView()).popBackStack();
    }

    private void showLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
