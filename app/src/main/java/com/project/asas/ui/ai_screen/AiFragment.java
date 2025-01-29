package com.project.asas.ui.ai_screen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.asas.R;
import com.project.asas.databinding.FragmentAiBinding;
import com.project.asas.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AiFragment extends BaseFragment<FragmentAiBinding> {


    @Override
    protected String getTAG() {
        return "AiFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_ai;
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