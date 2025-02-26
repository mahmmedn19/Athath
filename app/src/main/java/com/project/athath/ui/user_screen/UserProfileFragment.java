package com.project.athath.ui.user_screen;

import android.content.Intent;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.project.athath.MainActivity;
import com.project.athath.R;
import com.project.athath.databinding.FragmentUserProfileBinding;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.DialogUtils;

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
                DialogUtils.showConfirmationDialog(
                        requireContext(),
                        "Logout",
                        "Are you sure you want to log out?",
                        "Yes",
                        "Cancel",
                        (dialog, which) -> {
                            ((MainActivity) requireActivity()).logout();
                            Intent intent = new Intent(requireContext(), MainActivity.class);
                            startActivity(intent);
                            requireActivity().finish();
                        });
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