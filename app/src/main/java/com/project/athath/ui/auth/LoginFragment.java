package com.project.athath.ui.auth;

import static com.project.athath.ui.utils.InputValidator.clearErrorOnTextChange;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.athath.MainActivity;
import com.project.athath.R;
import com.project.athath.databinding.FragmentLoginBinding;
import com.project.athath.ui.admin_screen.AdminMainActivity;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.InputValidator;
import com.project.athath.ui.vendors_screen.VendorMainActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends BaseFragment<FragmentLoginBinding> {

    private AuthViewModels.LoginViewModel loginViewModel;
    private String userType;

    @Override
    protected String getTAG() {
        return "LoginFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_login;
    }

    @Override
    protected ViewModel getViewModel() {
        loginViewModel = new ViewModelProvider(this).get(AuthViewModels.LoginViewModel.class);
        return loginViewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Login");
        showBackButton(true);
        clearErrorOnTextChange(binding.emailLayout);
        clearErrorOnTextChange(binding.passwordLayout);

        binding.setViewModel(loginViewModel);
        binding.setLifecycleOwner(this);

        if (getArguments() != null && getArguments().containsKey("userType")) {
            userType = getArguments().getString("userType", "Customer");
            if ("Admin".equals(userType)) {
                binding.registerText.setVisibility(View.GONE);
            }
        }

        binding.loginButton.setOnClickListener(v -> loginUser());
        binding.registerText.setOnClickListener(v -> navigateToRegister());
        binding.forgotPasswordText.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_forgotPasswordFragment));

        observeLoginResult();
    }

    private void loginUser() {
        String email = binding.emailInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();

        if (!InputValidator.validateEmail(binding.emailLayout, email) ||
                !InputValidator.validateData(binding.passwordLayout, password)) {
            return;
        }

        loginViewModel.loginUser(email, password);
    }

    private void observeLoginResult() {
        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                switch (result.getStatus()) {
                    case SUCCESS:
                        binding.loadingProgressBar.setVisibility(View.GONE);
                        navigateToMainScreen(result.getData());
                        break;
                    case ERROR:
                        binding.loadingProgressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        break;
                    case LOADING:
                        binding.loadingProgressBar.setVisibility(View.VISIBLE);
                        binding.loginButton.setEnabled(false);
                        return;
                }
            }
            binding.loadingProgressBar.setVisibility(View.GONE);
            binding.loginButton.setEnabled(true);
        });

        // Observe isLoading LiveData
        loginViewModel.getLoadingState().observe(getViewLifecycleOwner(), isLoading -> {
            binding.loadingProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.loginButton.setEnabled(!isLoading);
        });
    }


    private void navigateToRegister() {
        if ("Customer".equals(userType)) {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registerFragment);
        } else {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_vendorRegisterFragment);
        }
    }

    private void navigateToMainScreen(String data) {
        Intent intent;
        if ("Admin".equals(data)) {
            intent = new Intent(requireContext(), AdminMainActivity.class);
            showToast("Admin Login...");
        } else if ("Vendor".equals(data)) {
            intent = new Intent(requireContext(), VendorMainActivity.class);
            showToast("Vendor Login...");
        } else {
            intent = new Intent(requireContext(), MainActivity.class);
            showToast("Customer Login...");
        }
        startActivity(intent);
        requireActivity().finish();
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
