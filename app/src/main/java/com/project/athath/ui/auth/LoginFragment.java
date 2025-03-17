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
import com.project.athath.data.utils.Result;
import com.project.athath.databinding.FragmentLoginBinding;
import com.project.athath.ui.admin_screen.AdminMainActivity;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.InputValidator;
import com.project.athath.ui.vendors_screen.VendorMainActivity;

import java.util.Objects;

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
        configureToolbar();
        setupBindings();
        extractUserType();
        setupClickListeners();
        observeLoginResult();
    }

    private void configureToolbar() {
        setToolbarVisibility(true);
        setToolbarTitle("Login" + (userType != null ? " as " + userType : ""));
        showBackButton(true);
    }

    private void setupBindings() {
        clearErrorOnTextChange(binding.emailLayout);
        clearErrorOnTextChange(binding.passwordLayout);
        binding.setViewModel(loginViewModel);
        binding.setLifecycleOwner(this);
    }

    private void extractUserType() {
        if (getArguments() != null && getArguments().containsKey("userType")) {
            userType = getArguments().getString("userType", "Customers");
            if ("Admins".equals(userType)) {
                binding.registerText.setVisibility(View.GONE);
            }
        }
    }

    private void setupClickListeners() {
        binding.loginButton.setOnClickListener(v -> loginUser());
        binding.registerText.setOnClickListener(v -> navigateToRegister());
        binding.forgotPasswordText.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_forgotPasswordFragment));
    }

    private void loginUser() {
        String email = Objects.requireNonNull(binding.emailInput.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.passwordInput.getText()).toString().trim();

        if (!InputValidator.validateEmail(binding.emailLayout, email) ||
                !InputValidator.validateData(binding.passwordLayout, password)) {
            return;
        }

        loginViewModel.loginUser(email, password, userType);
    }

    private void observeLoginResult() {
        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                handleLoginResult(result);
            }
        });

        loginViewModel.getLoadingState().observe(getViewLifecycleOwner(), isLoading -> {
            binding.loadingProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.loginButton.setEnabled(!isLoading);
        });
    }

    private void handleLoginResult(Result<String> result) {
        if (result.getStatus() == Result.Status.LOADING) {
            binding.loadingProgressBar.setVisibility(View.VISIBLE);
            binding.loginButton.setEnabled(false);
            binding.tvInvalidEmail.setVisibility(View.GONE);
        }
        else if (result.getStatus() == Result.Status.SUCCESS) {
            binding.loadingProgressBar.setVisibility(View.GONE);
            navigateToMainScreen(result.getData());
            binding.tvInvalidEmail.setVisibility(View.GONE);

        } else if (result.getStatus() == Result.Status.ERROR) {
            binding.loadingProgressBar.setVisibility(View.GONE);
            binding.tvInvalidEmail.setVisibility(View.VISIBLE);
            binding.tvInvalidEmail.setText(result.getErrorMessage());
            showToast(result.getErrorMessage());
        }
    }


    private void navigateToRegister() {
        int actionId = "Customers".equals(userType) ?
                R.id.action_loginFragment_to_registerFragment :
                R.id.action_loginFragment_to_vendorRegisterFragment;
        Navigation.findNavController(requireView()).navigate(actionId);
    }

    private void navigateToMainScreen(String role) {
        Intent intent = switch (role) {
            case "Admins" -> new Intent(requireContext(), AdminMainActivity.class);
            case "Vendors" -> new Intent(requireContext(), VendorMainActivity.class);
            default -> new Intent(requireContext(), MainActivity.class);
        };
        showToast(role + " Login...");
        startActivity(intent);
        requireActivity().finish();
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
