package com.project.athath.ui.auth;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.project.athath.R;
import com.project.athath.data.utils.Result;
import com.project.athath.databinding.FragmentForgotPasswordBinding;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.InputValidator;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ForgotPasswordFragment extends BaseFragment<FragmentForgotPasswordBinding> {

    private AuthViewModels.ForgotPasswordViewModel viewModel;

    @Override
    protected String getTAG() {
        return "ForgotPasswordFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_forgot_password;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(AuthViewModels.ForgotPasswordViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Forgot Password");
        showBackButton(true);

        // Clear error on text change
        InputValidator.clearErrorOnTextChange(binding.emailInputLayout);

        binding.btnResetPassword.setOnClickListener(v -> sendPasswordReset());
        observeResetPasswordResult();
    }

    private void sendPasswordReset() {
        String email = binding.etEmail.getText().toString().trim();
        TextInputLayout emailInputLayout = binding.emailInputLayout;

        if (!InputValidator.validateEmail(emailInputLayout, email)) {
            return; // Stop execution if validation fails
        }

        binding.loadingProgressBar.setVisibility(View.VISIBLE);
        viewModel.resetPassword(email);
    }

    private void observeResetPasswordResult() {
        viewModel.getResetPasswordResult().observe(getViewLifecycleOwner(), result -> {
            binding.loadingProgressBar.setVisibility(View.GONE);

            if (result.getStatus() == Result.Status.SUCCESS) {
                Toast.makeText(requireContext(), result.getData(), Toast.LENGTH_LONG).show();
                requireActivity().onBackPressed();
            } else if (result.getStatus() == Result.Status.ERROR) {
                Toast.makeText(requireContext(), "Error: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
