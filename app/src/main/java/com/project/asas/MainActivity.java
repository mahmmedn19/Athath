// Updated MainActivity with Bottom Navigation and NavHost
package com.project.asas;

import static com.project.asas.ui.utils.LocalLang.setLocale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.project.asas.databinding.ActivityMainBinding;
import com.project.asas.ui.base.BaseFragment;
import com.project.asas.ui.on_boarding.OnboardingActivity;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements BaseFragment.ToolbarHandler {
    private ActivityMainBinding binding;
    private NavController navController;
    private boolean isUserLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        setLocale("en", this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.md_theme_surface));

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        // Change back button icon dynamically
        if (navController != null) {
            navController.addOnDestinationChangedListener((@NonNull NavController controller, @NonNull androidx.navigation.NavDestination destination, Bundle arguments) -> {
                if (destination.getId() == R.id.homeFragment) {
                    binding.toolbar.setNavigationIcon(null); // Hide back button on home
                } else {
                    binding.toolbar.setNavigationIcon(R.drawable.ic_back);
                }
            });
        }
        // Load login state from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        isUserLoggedIn = prefs.getBoolean("isLoggedIn", false);

        // Setup Navigation Component
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_host);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNav, navController);
        }
        // Dynamically update bottom navigation menu
        updateBottomNavigationMenu();

        // Hide Bottom Navigation on Specific Screens
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() != R.id.homeFragment &&
                    destination.getId() != R.id.catalogFragment &&
                    destination.getId() != R.id.productsFragment &&
                    destination.getId() != R.id.aiFragment &&
                    destination.getId() != R.id.userSelectionFragment
                    && destination.getId() != R.id.userProfileFragment) {
                binding.bottomNav.setVisibility(View.GONE);
            } else {
                binding.bottomNav.setVisibility(View.VISIBLE);
            }
        });

        binding.bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                navController.navigate(R.id.homeFragment);
            } else if (item.getItemId() == R.id.catalog) {
                navController.navigate(R.id.catalogFragment);
            } else if (item.getItemId() == R.id.products) {
                navController.navigate(R.id.productsFragment);
            } else if (item.getItemId() == R.id.ai) {
                navController.navigate(R.id.aiFragment);
            } else if (item.getItemId() == R.id.userSelectionFragment) {
                navController.navigate(R.id.userSelectionFragment);
            } else if (item.getItemId() == R.id.userProfileFragment) {
                navController.navigate(R.id.userProfileFragment);
            }
            return true;
        });
    }
    // Update Bottom Navigation dynamically
    private void updateBottomNavigationMenu() {
        Menu menu = binding.bottomNav.getMenu();
        menu.clear(); // Clear existing menu

        getMenuInflater().inflate(R.menu.bottom_navigation_menu, menu); // Reinflate menu

        if (isUserLoggedIn) {
            // Remove login and add favorites
            menu.removeItem(R.id.userSelectionFragment);
            menu.add(Menu.NONE, R.id.userProfileFragment, Menu.NONE, "Profile")
                    .setIcon(R.drawable.ic_profile)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
    }
    // Call this method when user logs in or logs out
    public void updateLoginState(boolean loggedIn) {
        isUserLoggedIn = loggedIn;
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        prefs.edit().putBoolean("isLoggedIn", loggedIn).apply();
        updateBottomNavigationMenu();
    }
    @Override
    public void setToolbarTitle(String title) {
        binding.toolbar.setTitle(title);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        binding.toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        //navigationBar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
    }

    @Override
    public void showBackButton(boolean show) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back); // Set Custom Back Icon
            binding.toolbar.setNavigationOnClickListener(v -> {
                if (show) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void setToolbarVisibility(boolean isVisible) {
        if (isVisible) {
            binding.toolbar.setVisibility(View.VISIBLE);
        } else {
            binding.toolbar.setVisibility(View.GONE);
        }
    }
}