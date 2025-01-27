package com.project.asas.ui;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.project.asas.R;
import com.project.asas.databinding.FragmentRegisterBinding;
import com.project.asas.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterFragment extends BaseFragment<FragmentRegisterBinding> {


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
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Register");
        showBackButton(true);

        //navigation up
        binding.loginText.setOnClickListener(v -> {
            // Handle register button click
            Navigation.findNavController(v).navigateUp();
        });
    }
}