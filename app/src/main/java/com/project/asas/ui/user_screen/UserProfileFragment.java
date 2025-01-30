package com.project.asas.ui.user_screen;

import android.content.Intent;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.project.asas.MainActivity;
import com.project.asas.R;
import com.project.asas.databinding.FragmentUserProfileBinding;
import com.project.asas.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserProfileFragment extends BaseFragment<FragmentUserProfileBinding> {
    @Override
    protected String getTAG() {
        return "UserProfileFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_user_profile;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(false);
        binding.btnLogout.setOnClickListener(v -> {
            // Logout user
            ((MainActivity) requireActivity()).updateLoginState(false);
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        binding.manageProfileCard.setOnClickListener(v -> {
            // Navigate to Manage User Profile
            Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_manageProfileFragment);
        });
        binding.favoritesCard.setOnClickListener(v -> {
            // Navigate to View Orders
            Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_favFragment);
        });
    }
}