package com.project.athath.ui.admin_screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.project.athath.MainActivity;
import com.project.athath.R;
import com.project.athath.databinding.ActivityAdminMainBinding;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.DialogUtils;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AdminMainActivity extends AppCompatActivity implements BaseFragment.ToolbarHandler {
    private ActivityAdminMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);

        // Adjust insets for immersive UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.md_theme_surface));

        // Setup Toolbar
        setSupportActionBar(binding.adminToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // Setup Navigation Component
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_host_admin);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNavAdmin, navController);
        }

        // Hide Bottom Navigation on Specific Screens
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() != R.id.admin_manage_vendors &&
                    destination.getId() != R.id.admin_manage_customers &&
                    destination.getId() != R.id.admin_room_configurations &&
                    destination.getId() != R.id.admin_logout) {
                binding.bottomNavAdmin.setVisibility(View.GONE);
            } else {
                binding.bottomNavAdmin.setVisibility(View.VISIBLE);
            }
        });

        // Bottom Navigation Handling
        binding.bottomNavAdmin.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.admin_manage_vendors) {
                navController.navigate(R.id.admin_manage_vendors);
            } else if (item.getItemId() == R.id.admin_manage_customers) {
                navController.navigate(R.id.admin_manage_customers);
            } else if (item.getItemId() == R.id.admin_room_configurations) {
                navController.navigate(R.id.admin_room_configurations);
            } else if (item.getItemId() == R.id.admin_logout) {
                DialogUtils.showConfirmationDialog(
                        this,
                        "Logout",
                        "Are you sure you want to logout?",
                        "Yes", "Cancel",
                        (dialog, which) -> {
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        },
                        (dialog, which) -> {
                            navController.navigate(R.id.admin_manage_vendors);
                            dialog.dismiss();
                        });
            }
            return true;
        });
    }

    @Override
    public void setToolbarTitle(String title) {
        binding.adminToolbar.setTitle(title);
        binding.adminToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        binding.adminToolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        //navigationBar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
    }

    @Override
    public void showBackButton(boolean show) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back); // Set Custom Back Icon
            binding.adminToolbar.setNavigationOnClickListener(v -> {
                if (show) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void setToolbarVisibility(boolean isVisible) {
        if (isVisible) {
            binding.adminToolbar.setVisibility(View.VISIBLE);
        } else {
            binding.adminToolbar.setVisibility(View.GONE);
        }
    }
}
