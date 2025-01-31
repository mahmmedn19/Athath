package com.project.asas.ui.vendors_screen;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.project.asas.R;
import dagger.hilt.android.AndroidEntryPoint;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.asas.databinding.ActivityVendorMainBinding;
import com.project.asas.ui.base.BaseFragment;

import java.util.Objects;

@AndroidEntryPoint
public class VendorMainActivity extends AppCompatActivity implements BaseFragment.ToolbarHandler {
    private ActivityVendorMainBinding  binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVendorMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });
        setSupportActionBar(binding.vendorToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        // Setup Navigation Component
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_host_vendor);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_vendor);
            NavigationUI.setupWithNavController(bottomNav, navController);
        }
        // Hide Bottom Navigation on Specific Screens
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() != R.id.vendor_profile &&
                    destination.getId() != R.id.vendor_products ) {
                binding.bottomNavVendor.setVisibility(View.GONE);
            } else {
                binding.bottomNavVendor.setVisibility(View.VISIBLE);
            }
        });

        // Bottom Navigation Handling
        binding.bottomNavVendor.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.vendor_profile) {
                navController.navigate(R.id.vendor_profile);
            } else if (item.getItemId() == R.id.vendor_products) {
                navController.navigate(R.id.vendor_products);
            }
            return true;
        });
    }

    @Override
    public void setToolbarTitle(String title) {
        binding.vendorToolbar.setTitle(title);
        binding.vendorToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        binding.vendorToolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        //navigationBar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
    }

    @Override
    public void showBackButton(boolean show) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back); // Set Custom Back Icon
            binding.vendorToolbar.setNavigationOnClickListener(v -> {
                if (show) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void setToolbarVisibility(boolean isVisible) {
        if (isVisible) {
            binding.vendorToolbar.setVisibility(View.VISIBLE);
        } else {
            binding.vendorToolbar.setVisibility(View.GONE);
        }
    }
}
