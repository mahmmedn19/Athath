package com.project.asas.ui.products_screen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.asas.R;
import com.project.asas.databinding.FragmentProductsBinding;
import com.project.asas.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductsFragment extends BaseFragment<FragmentProductsBinding> {


    @Override
    protected String getTAG() {
        return "ProductsFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_products;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(false);

    }
}