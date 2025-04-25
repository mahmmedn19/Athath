package com.project.athath.ui.auth;

import static com.project.athath.ui.utils.InputValidator.clearErrorOnTextChange;
import static com.project.athath.ui.utils.InputValidator.setupFieldHelperText;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.athath.R;
import com.project.athath.data.model.Vendor;
import com.project.athath.databinding.FragmentVendorRegisterBinding;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.InputValidator;
import com.project.athath.ui.vendors_screen.VendorMainActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VendorRegisterFragment extends BaseFragment<FragmentVendorRegisterBinding> {

    private AuthViewModels.RegisterViewModel registerViewModel;

    @Override
    protected String getTAG() {
        return "VendorRegisterFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_vendor_register;
    }

    @Override
    protected ViewModel getViewModel() {
        return registerViewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Vendor Register");
        showBackButton(true);
        // Clear error on text change
        clearErrorOnTextChange(binding.nameInputLayout);
        clearErrorOnTextChange(binding.storeNameInputLayout);
        clearErrorOnTextChange(binding.phoneInputLayout);
        clearErrorOnTextChange(binding.addressInputLayout);
        clearErrorOnTextChange(binding.vendorEmailInputLayout);
        clearErrorOnTextChange(binding.vendorPasswordInputLayout);
        clearErrorOnTextChange(binding.vendorConfirmPasswordInputLayout);

        // Example for name field
        setupFieldHelperText(binding.nameInputLayout, "Enter your full name (e.g., John Doe)");
        setupFieldHelperText(binding.storeNameInputLayout, "Enter your store name (e.g., Store ABC)");
        setupFieldHelperText(binding.phoneInputLayout, "Enter a valid phone number (e.g., +966503103249)");
        setupFieldHelperText(binding.addressInputLayout, "Enter your store address (e.g., Saudi Arabia, Riyadh)");
        setupFieldHelperText(binding.vendorEmailInputLayout, "Example: vendor@example.com");
        setupFieldHelperText(binding.vendorPasswordInputLayout, "Password must contain at least 1 uppercase letter, 1 number, and 1 special character");
        setupFieldHelperText(binding.vendorConfirmPasswordInputLayout, "Must match the password above");

        registerViewModel = new ViewModelProvider(this).get(AuthViewModels.RegisterViewModel.class);
        binding.setViewModel(registerViewModel);
        binding.setLifecycleOwner(this);

        binding.vendorRegisterButton.setOnClickListener(v -> registerVendor());
        binding.vendorLoginText.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        observeRegisterResult();
    }

    private void registerVendor() {
        String name = binding.nameInput.getText().toString().trim();
        String storeName = binding.storeNameInput.getText().toString().trim();
        String phone = binding.phoneInput.getText().toString().trim();
        String address = binding.addressInput.getText().toString().trim();
        String email = binding.vendorEmailInput.getText().toString().trim();
        String password = binding.vendorPasswordInput.getText().toString().trim();
        String confirmPassword = binding.vendorConfirmPasswordInput.getText().toString().trim();

        if (!InputValidator.validateUsername(binding.nameInputLayout, name) ||
                !InputValidator.validateData(binding.storeNameInputLayout, storeName) ||
                !InputValidator.validatePhone(binding.phoneInputLayout, phone) ||
                !InputValidator.validateStoreName(binding.addressInputLayout, address) ||
                !InputValidator.validateEmail(binding.vendorEmailInputLayout, email) ||
                !InputValidator.validatePassword(binding.vendorPasswordInputLayout, password) ||
                !InputValidator.validateConfirmPassword(binding.vendorConfirmPasswordInputLayout, password, confirmPassword)) {
            return;
        }

        Vendor vendor = new Vendor(name, storeName, phone, address, email, "Active");
        registerViewModel.registerVendor(vendor, binding.vendorEmailInputLayout, binding.vendorPasswordInputLayout, binding.phoneInputLayout);
    }

    private void observeRegisterResult() {
        registerViewModel.getRegisterResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                switch (result.getStatus()) {
                    case SUCCESS:
                        Toast.makeText(getContext(), "Vendor Registration Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(requireContext(), VendorMainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                        break;

                    case ERROR:
                        Toast.makeText(getContext(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        break;

                    case LOADING:
                        binding.loadingProgressBar.setVisibility(View.VISIBLE);
                        binding.vendorRegisterButton.setEnabled(false);
                        return;
                }
            }
            binding.loadingProgressBar.setVisibility(View.GONE);
            binding.vendorRegisterButton.setEnabled(true);
        });
        // Observe isLoading LiveData
        registerViewModel.getLoadingState().observe(getViewLifecycleOwner(), isLoading -> {
            binding.loadingProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }
}
