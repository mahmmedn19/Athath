package com.project.athath.ui.vendors_screen.profile;

import android.content.Intent;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.athath.MainActivity;
import com.project.athath.R;
import com.project.athath.databinding.FragmentProfileBinding;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.DialogUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends BaseFragment<FragmentProfileBinding> {

    private ProfileViewModel profileViewModel;

    @Override
    protected String getTAG() {
        return "ProfileFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_profile;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Profile");
        showBackButton(false);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.loadVendorData();

        binding.manageProfileLayout.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_vendor_profile_to_vendor_profile_details);
        });

        binding.manageChangePassLayout.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_vendor_profile_to_vendor_change_pass);
        });


        // Logout Action
        binding.btnLogout.setOnClickListener(v -> {
            DialogUtils.showConfirmationDialog(
                    requireContext(),
                    "Logout",
                    "Are you sure you want to log out?",
                    "Yes",
                    "Cancel",
                    (dialog, which) -> {
                        Intent intent = new Intent(requireContext(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    });
        });
    }
}
