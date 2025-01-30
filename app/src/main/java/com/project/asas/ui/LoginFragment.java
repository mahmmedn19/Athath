package com.project.asas.ui;

import android.content.Intent;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import android.view.View;

import com.project.asas.MainActivity;
import com.project.asas.R;
import com.project.asas.databinding.FragmentLoginBinding;
import com.project.asas.ui.admin_screen.AdminMainActivity;
import com.project.asas.ui.vendors_screen.*;
import com.project.asas.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends BaseFragment<FragmentLoginBinding> {
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
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Login");
        showBackButton(true);

        // Handle Login Button Click
        binding.loginButton.setOnClickListener(v -> {
            // TODO: Add login logic here
            if ("Admin".equals(userType)) {
                Intent intent = new Intent(requireContext(), AdminMainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            } else if ("Vendor".equals(userType)) {
                Intent intent = new Intent(requireContext(), VendorMainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            } else {
               // Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_homeFragment);
                ((MainActivity) requireActivity()).updateLoginState(true);
                Intent intent = new Intent(requireContext(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();

            }
        });
        //hide register text if userType is Admin
        if (getArguments() != null && getArguments().containsKey("userType")) {
            userType = getArguments().getString("userType", "Customer");
            if ("Admin".equals(userType)) {
                binding.registerText.setVisibility(View.GONE);
            }
        }

        // Handle Register Text Click
        binding.registerText.setOnClickListener(v -> {
            String userType = "Customer"; // Default user type
            if (getArguments() != null && getArguments().containsKey("userType")) {
                userType = getArguments().getString("userType", "Customer");
            }

            // Navigate to the appropriate registration fragment
            if ("Customer".equals(userType)) {
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment);
            } else {
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_vendorRegisterFragment);
            }
        });
    }
}