package com.project.athath.ui.user_screen;

import android.content.Intent;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.project.athath.MainActivity;
import com.project.athath.R;
import com.project.athath.data.utils.Result;
import com.project.athath.databinding.FragmentUserProfileBinding;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.user_screen.manage_user_profile.ManageUserProfileViewModel;
import com.project.athath.ui.utils.DialogUtils;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserProfileFragment extends BaseFragment<FragmentUserProfileBinding> {

    private ManageUserProfileViewModel viewModel;

    @Override
    protected String getTAG() {
        return "UserProfileFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_user_profile;
    }

    @Override
    protected ManageUserProfileViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(ManageUserProfileViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(false);
        setToolbarTitle("User Profile");
        showBackButton(false);

        observeViewModel();  // Observe changes
        setupClickListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.fetchCustomerProfile();  // ✅ Refresh profile on entering the screen
    }

    private void observeViewModel() {
        viewModel.getCustomerLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                binding.userName.setText(result.getData().getUsername());  // ✅ Update name
            }
        });
    }

    private void setupClickListeners() {
        binding.btnLogout.setOnClickListener(v -> {
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
                    }
            );
        });

        binding.manageProfileCard.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_manageProfileFragment)
        );

        binding.favoritesCard.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_favFragment)
        );
    }
}
