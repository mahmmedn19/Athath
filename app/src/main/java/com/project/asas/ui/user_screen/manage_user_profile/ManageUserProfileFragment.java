package com.project.asas.ui.user_screen.manage_user_profile;

import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.project.asas.R;
import com.project.asas.databinding.FragmentManageUserProfileBinding;
import com.project.asas.ui.base.BaseFragment;
import com.project.asas.ui.utils.DialogUtils;

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
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(ManageUserProfileViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Manage User Profile");
        showBackButton(true);

        // Bind ViewModel Data
        binding.etUserName.setText(viewModel.getUserName());
        binding.etEmail.setText(viewModel.getEmail());

        // Save Profile Button
        binding.btnSaveProfile.setOnClickListener(v -> {
            String newUserName = binding.etUserName.getText().toString().trim();
            String newEmail = binding.etEmail.getText().toString().trim();

            viewModel.updateUserProfile(newUserName, newEmail);
            DialogUtils.showCustomDialog(requireContext(), "Success", "Profile updated successfully!");
        });

        // Change Password Button
        binding.btnChangePassword.setOnClickListener(v -> {
            String currentPassword = binding.etCurrentPassword.getText().toString().trim();
            String newPassword = binding.etNewPassword.getText().toString().trim();
            String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

            if (viewModel.changePassword(currentPassword, newPassword, confirmPassword)) {
                DialogUtils.showCustomDialog(requireContext(), "Success", "Password changed successfully!");
            } else {
                Toast.makeText(requireContext(), "Password Change Failed. Check Inputs", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
