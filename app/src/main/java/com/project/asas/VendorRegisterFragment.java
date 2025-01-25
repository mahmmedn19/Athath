package com.project.asas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.asas.databinding.FragmentRegisterBinding;
import com.project.asas.databinding.FragmentVendorRegisterBinding;
import com.project.asas.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VendorRegisterFragment extends BaseFragment<FragmentVendorRegisterBinding> {


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
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        //navigation up
        binding.vendorLoginText.setOnClickListener(v -> {
            // Handle register button click
            Navigation.findNavController(v).navigateUp();
        });
    }
}