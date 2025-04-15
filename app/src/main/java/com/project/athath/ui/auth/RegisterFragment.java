package com.project.athath.ui.auth;

import static com.project.athath.ui.utils.InputValidator.clearErrorOnTextChange;
import static com.project.athath.ui.utils.InputValidator.setupFieldHelperText;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.athath.MainActivity;
import com.project.athath.R;
import com.project.athath.data.model.Customer;
import com.project.athath.data.utils.Result;
import com.project.athath.databinding.FragmentRegisterBinding;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.InputValidator;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterFragment extends BaseFragment<FragmentRegisterBinding> {

    private AuthViewModels.RegisterViewModel registerViewModel;

    @Override
    protected String getTAG() {
        return "RegisterFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_register;
    }

    @Override
    protected ViewModel getViewModel() {
        return registerViewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Customer Register");
        showBackButton(true);
        clearErrorOnTextChange(binding.customerNameLayout);
        clearErrorOnTextChange(binding.customerEmailLayout);
        clearErrorOnTextChange(binding.customerPasswordLayout);
        clearErrorOnTextChange(binding.customerConfirmPasswordLayout);

        // Example for name field
        setupFieldHelperText(binding.customerNameLayout, "Enter your full name (e.g., John Doe)");
        // Setting helper text for email example
        setupFieldHelperText(binding.customerEmailLayout, "Example: user@gmail.com");
        // Setting helper text for password format example
        setupFieldHelperText(binding.customerPasswordLayout, "Password must contain at least 1 uppercase letter, 1 number, and 1 special character");
        // Setting helper text for confirm password
        setupFieldHelperText(binding.customerConfirmPasswordLayout, "Must match the password above");

        registerViewModel = new ViewModelProvider(this).get(AuthViewModels.RegisterViewModel.class);
        binding.setViewModel(registerViewModel);
        binding.setLifecycleOwner(this);

        binding.registerButton.setOnClickListener(v -> registerCustomer());
        binding.loginText.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        observeRegisterResult();
    }

    private void registerCustomer() {
        String name = binding.customerNameInput.getText().toString().trim();
        String email = binding.customerEmailInput.getText().toString().trim();
        String password = binding.customerPasswordInput.getText().toString().trim();
        String confirmPassword = binding.customerConfirmPasswordInput.getText().toString().trim();

        if (!InputValidator.validateUsername(binding.customerNameLayout, name) ||
                !InputValidator.validateEmail(binding.customerEmailLayout, email) ||
                !InputValidator.validatePassword(binding.customerPasswordLayout, password) ||
                !InputValidator.validateConfirmPassword(binding.customerConfirmPasswordLayout, password, confirmPassword)) {
            return;
        }

        Customer customer = new Customer(name, email, "Active");
        registerViewModel.registerCustomer(customer, binding.customerEmailLayout, binding.customerPasswordLayout);
    }


    private void observeRegisterResult() {
        registerViewModel.getRegisterResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                if (result.getStatus() == Result.Status.SUCCESS) {
                    Toast.makeText(getContext(), "Registration Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                } else if (result.getStatus() == Result.Status.ERROR) {
                    Toast.makeText(getContext(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                } else if (result.getStatus() == Result.Status.LOADING) {
                    binding.loadingProgressBar.setVisibility(View.VISIBLE);
                    binding.registerButton.setEnabled(false);
                    return; // Early exit for loading state
                }
            }

            // Reset UI after SUCCESS or ERROR
            binding.loadingProgressBar.setVisibility(View.GONE);
            binding.registerButton.setEnabled(true);
        });
    }

}
