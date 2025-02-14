package com.project.athath.ui.auth;

import static com.project.athath.ui.utils.InputValidator.clearErrorOnTextChange;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
        setToolbarTitle("Register");
        showBackButton(true);
        clearErrorOnTextChange(binding.customerNameLayout);
        clearErrorOnTextChange(binding.customerEmailLayout);
        clearErrorOnTextChange(binding.customerPasswordLayout);
        clearErrorOnTextChange(binding.customerConfirmPasswordLayout);


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

        Customer customer = new Customer(name, email, "Active", password);
        registerViewModel.registerCustomer(customer, binding.customerEmailLayout, binding.customerPasswordLayout);
    }


    private void observeRegisterResult() {
        registerViewModel.getRegisterResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                switch (result.getStatus()) {
                    case SUCCESS:
                        Toast.makeText(getContext(), "Registration Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(requireContext(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                        break;

                    case ERROR:
                        Toast.makeText(getContext(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        break;

                    case LOADING:
                        binding.loadingProgressBar.setVisibility(View.VISIBLE);
                        binding.registerButton.setEnabled(false);
                        return;
                }
            }
            binding.loadingProgressBar.setVisibility(View.GONE);
            binding.registerButton.setEnabled(true);
        });
    }
}
