package com.project.athath.ui.product_details;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.athath.R;
import com.project.athath.data.model.Product;
import com.project.athath.data.model.Vendor;
import com.project.athath.data.utils.ImageUtils;
import com.project.athath.data.utils.Result;
import com.project.athath.databinding.FragmentProductDetailsBinding;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.home_screen.HomeAdapter;
import com.project.athath.ui.utils.DialogUtils;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductDetailsFragment extends BaseFragment<FragmentProductDetailsBinding> implements HomeAdapter.HomeInteractionListener {

    private ProductDetailsViewModel viewModel;
    private HomeAdapter adapter;
    private Product currentProduct;

    @Override
    protected String getTAG() {
        return "ProductDetailsFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_product_details;
    }

    @Override
    protected ProductDetailsViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(ProductDetailsViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Product Details");
        showBackButton(true);

        // Get productId from arguments
        String productId = getArguments() != null ? getArguments().getString("productId") : null;
        if (productId == null) {
            Toast.makeText(requireContext(), "Product ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        setupRecyclerView();

        // Observe product details and recommended products
        observeProductDetails();
        observeVendorDetails();
        observeRecommendedProducts();

        // Fetch product details and recommended products
        viewModel.fetchProductById(productId);
        viewModel.fetchRecommendedProducts(productId);
        binding.ivFavoriteIcon.setOnClickListener(v -> {
            if (!viewModel.isUserLoggedIn()) {
                showLoginRequiredDialog();  // Show login dialog if not logged in
                return;
            }

            if (currentProduct != null) {
                viewModel.toggleFavoriteStatus(currentProduct).observe(getViewLifecycleOwner(), isFavorite -> {
                    currentProduct.setFavorite(isFavorite);     // Update product state
                    updateFavoriteIcon(isFavorite);            // Update icon color

                    String message = isFavorite ? "Added to favorites!" : "Removed from favorites!";
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new HomeAdapter(new ArrayList<>(), this);
        binding.rvSuggestionItems.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvSuggestionItems.setAdapter(adapter);
    }

    private void observeProductDetails() {
        viewModel.getProductLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                showFullScreenLoading(true);
            } else if (result.getStatus() == Result.Status.SUCCESS) {
                showFullScreenLoading(false);
                if (result.getData() != null) {
                    currentProduct = result.getData();
                    updateProductDetails(currentProduct);
                    // Check initial favorite status
                    viewModel.checkIfProductIsFavorite(currentProduct.getId()).observe(getViewLifecycleOwner(), isFavorite -> {
                        currentProduct.setFavorite(isFavorite);
                        updateFavoriteIcon(isFavorite);
                    });
                } else {
                    Toast.makeText(requireContext(), "Product not found", Toast.LENGTH_SHORT).show();
                }
            } else if (result.getStatus() == Result.Status.ERROR) {
                showFullScreenLoading(false);
            }
        });
    }

    private void observeVendorDetails() {
        viewModel.getVendorLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                showFullScreenLoading(true);
            } else if (result.getStatus() == Result.Status.SUCCESS) {
                showFullScreenLoading(false);
                if (result.getData() != null) {
                    updateVendorDetails(result.getData());
                }
            } else if (result.getStatus() == Result.Status.ERROR) {
                showFullScreenLoading(false);
            }
        });
    }

    private void updateVendorDetails(Vendor vendor) {
        binding.tvStoreName.setText(vendor.getStoreName());
        binding.tvStoreAddress.setText(vendor.getAddress());
    }


    private void observeRecommendedProducts() {
        viewModel.getRecommendedProductsLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                showFullScreenLoading(true);
                binding.rvSuggestionItems.setVisibility(View.GONE);
                binding.imageNoDataFoundProduct.setVisibility(View.GONE);

            } else if (result.getStatus() == Result.Status.SUCCESS) {
                showFullScreenLoading(false);

                if (result.getData() != null && !result.getData().isEmpty()) {
                    binding.rvSuggestionItems.setVisibility(View.VISIBLE);
                    binding.imageNoDataFoundProduct.setVisibility(View.GONE);
                    adapter.updateProducts(result.getData());
                } else {
                    binding.rvSuggestionItems.setVisibility(View.GONE);
                    binding.imageNoDataFoundProduct.setVisibility(View.VISIBLE);
                }

            } else if (result.getStatus() == Result.Status.ERROR) {
                showFullScreenLoading(false);
                binding.rvSuggestionItems.setVisibility(View.GONE);
                binding.imageNoDataFoundProduct.setVisibility(View.VISIBLE);
            }
        });
    }



    private void updateProductDetails(Product product) {
        binding.tvProductName.setText(product.getName());
        binding.tvRoomType.setText(product.getRoomType());
        binding.tvStyle.setText(product.getStyle());
        binding.tvColor.setText(product.getColor());
        binding.tvPrice.setText(String.format("$%.2f", product.getPrice()));
        // CONCAT DIMENSIONS
        int width = (int) product.getProductWidth();
        int length = (int) product.getProductLength();
        String dimensions = width + " x " + length;
        binding.tvDimensions.setText(dimensions);
        binding.tvDescriptionDetails.setText(product.getDescription());

        Bitmap bitmap = ImageUtils.decodeBase64ToImage(product.getImageUrl());
        binding.ivProductImage.setImageBitmap(bitmap);
    }

    private void showFullScreenLoading(boolean isLoading) {
        binding.fullScreenLoader.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
    private void updateFavoriteIcon(boolean isFavorite) {
        if (isFavorite) {
            binding.ivFavoriteIcon.setColorFilter(getResources().getColor(R.color.md_theme_errorContainer_mediumContrast)); // Primary color
        } else {
            binding.ivFavoriteIcon.setColorFilter(getResources().getColor(R.color.white));
        }
    }

    private void showLoginRequiredDialog() {
        DialogUtils.showConfirmationDialog(
                requireContext(),
                "Login Required",
                "Please login to add this product to favorites.",
                "Login",
                "Cancel",
                (dialog, which) -> {
                    Navigation.findNavController(requireView()).navigate(R.id.action_productDetailsFragment_to_userSelectionFragment);
                }
        );
    }

    @Override
    public void onFavoriteClicked(Product product) {
        if (product != null) {
            if (!viewModel.isUserLoggedIn()) {
                showLoginRequiredDialog();  // Show login dialog if not logged in
                return;
            }
            viewModel.toggleFavoriteStatus(product).observe(getViewLifecycleOwner(), isFavorite -> {
                product.setFavorite(isFavorite);     // Update product state
                updateFavoriteIcon(isFavorite);            // Update icon color
                String message = isFavorite ? "Added to favorites!" : "Removed from favorites!";
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            });
        }
    }
}
