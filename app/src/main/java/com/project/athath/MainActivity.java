// Updated MainActivity with Bottom Navigation and NavHost
package com.project.athath;

import static com.project.athath.ui.utils.LocalLang.setLocale;

import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.project.athath.data.utils.Result;
import com.project.athath.databinding.ActivityMainBinding;
import com.project.athath.di.NetworkModule;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.SharedPrefUtils;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements BaseFragment.ToolbarHandler {
    private ActivityMainBinding binding;
    private NavController navController;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        setLocale("en", this);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });
        // ✅ Fetch AI link and observe the LiveData
        mainViewModel.getSingleAILink().observe(this, result -> {
            if (Objects.requireNonNull(result.getStatus()) == Result.Status.SUCCESS) {
                String aiLink = result.getData();
                Log.d("AI_LINK", aiLink);
                if (aiLink != null) {
                    SharedPrefUtils.saveAiLink(this, aiLink); // ✅ Save AI link to SharedPreferences
                }
            }
        });
        // Dynamically update bottom navigation menu
        observeLoginState();

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

        // Setup Navigation Component
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_host);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNav, navController);
        }


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

    private void observeLoginState() {
        mainViewModel.getIsCustomerLoggedIn().observe(this, isLoggedIn -> {
            updateBottomNavigationMenu(isLoggedIn);
        });
    }

    // Update Bottom Navigation dynamically
    private void updateBottomNavigationMenu(boolean isLoggedIn) {
        Menu menu = binding.bottomNav.getMenu();

        // Remove existing login/profile items (if any)
        menu.removeItem(R.id.userSelectionFragment);
        menu.removeItem(R.id.userProfileFragment);

        if (isLoggedIn) {
            menu.add(Menu.NONE, R.id.userProfileFragment, Menu.NONE, "Profile")
                    .setIcon(R.drawable.ic_profile)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else {
            menu.add(Menu.NONE, R.id.userSelectionFragment, Menu.NONE, "Login")
                    .setIcon(R.drawable.ic_profile)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
    }

    public void logout() {
        mainViewModel.logout();
        updateBottomNavigationMenu(false);  // Hide profile and show login after logout
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

    @Override
    protected void onStart() {
        super.onStart();
        mainViewModel.fetchAILink(); // ✅ Always fetch AI link on start
    }
}