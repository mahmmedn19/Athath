package com.project.asas.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.asas.R;
import com.project.asas.databinding.FragmentLoginBinding;
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
        });

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