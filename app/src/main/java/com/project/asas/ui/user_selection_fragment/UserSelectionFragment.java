package com.project.asas.ui.user_selection_fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.asas.R;
import com.project.asas.databinding.FragmentUserSelectionBinding;
import com.project.asas.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserSelectionFragment extends BaseFragment<FragmentUserSelectionBinding> {


    @Override
    protected String getTAG() {
        return "UserSelectionFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_user_selection;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Select User Type");
        showBackButton(true);
        binding.adminOption.setOnClickListener(v -> {
            // Navigate to LoginFragment with "Admin" user type
            Bundle bundle = new Bundle();
            bundle.putString("userType", "Admin");
            Navigation.findNavController(v).navigate(R.id.action_userSelectionFragment_to_loginFragment, bundle);
        });

        binding.vendorOption.setOnClickListener(v -> {
            // Navigate to LoginFragment with "Vendor" user type
            Bundle bundle = new Bundle();
            bundle.putString("userType", "Vendor");
            Navigation.findNavController(v).navigate(R.id.action_userSelectionFragment_to_loginFragment, bundle);
        });

        binding.customerOption.setOnClickListener(v -> {
            // Navigate to LoginFragment with "Customer" user type
            Bundle bundle = new Bundle();
            bundle.putString("userType", "Customer");
            Navigation.findNavController(v).navigate(R.id.action_userSelectionFragment_to_loginFragment, bundle);
        });
    }
}