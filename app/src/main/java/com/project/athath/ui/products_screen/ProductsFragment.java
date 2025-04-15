package com.project.athath.ui.products_screen;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.chip.Chip;
import com.project.athath.R;
import com.project.athath.databinding.FragmentProductsBinding;
import com.project.athath.data.model.Product;
import com.project.athath.data.utils.Result;
import com.project.athath.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductsFragment extends BaseFragment<FragmentProductsBinding> implements ProductsAdapter.ProductsInteractionListener {

    private ProductsViewModel viewModel;
    private ProductsAdapter productAdapter;

    @Override
    protected String getTAG() {
        return "ProductsFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_products;
    }

    @Override
    protected ViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Products");
        showBackButton(false);

        viewModel = new ViewModelProvider(this).get(ProductsViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setupRecyclerView();
        setupCategoryChips();
        observeViewModel();
    }

    private void setupRecyclerView() {
        List<Product> initialProducts = viewModel.getFilteredProducts().getValue();
        productAdapter = new ProductsAdapter(initialProducts != null ? initialProducts : new ArrayList<>(), this);
        binding.rvProducts.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rvProducts.setAdapter(productAdapter);
    }



    private void setupCategoryChips() {
        viewModel.getProducts().observe(getViewLifecycleOwner(), productList -> {
            binding.chipGroupProductCategories.removeAllViews();

            if (productList != null && !productList.isEmpty()) {
                // Extract unique categories
                Set<String> categories = new HashSet<>();
                for (Product product : productList) {
                    if (product.getCategory() != null && !product.getCategory().trim().isEmpty()) {
                        categories.add(product.getCategory());
                    }
                }

                // Add "All" category to show all products by default
                Chip allChip = new Chip(requireContext());
                allChip.setText("All Products");
                allChip.setCheckable(true);
                allChip.setChecked(true);  // Set as selected by default
                allChip.setOnClickListener(v -> viewModel.filterProductsByCategory(null));  // Show all products
                binding.chipGroupProductCategories.addView(allChip);

                // Create chips for each unique category
                for (String category : categories) {
                    Chip chip = new Chip(requireContext());
                    chip.setText(category);
                    chip.setCheckable(true);
                    chip.setOnClickListener(v -> viewModel.filterProductsByCategory(category));
                    binding.chipGroupProductCategories.addView(chip);
                }

                // By default, show all products
                viewModel.filterProductsByCategory(null);
            }
        });
    }


    private void observeViewModel() {
        // Observe filtered products and update RecyclerView
        viewModel.getFilteredProducts().observe(getViewLifecycleOwner(), products -> {
            productAdapter.updateProducts(products);
            checkEmptyState(products);
        });

        // Observe the result of fetching products (with loading state)
        viewModel.getFetchProductsResult().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                showFullScreenLoading(true);  // Show loading during data fetch
            } else if (result.getStatus() == Result.Status.SUCCESS) {
                showFullScreenLoading(false); // Hide loading on success
            } else if (result.getStatus() == Result.Status.ERROR) {
                showFullScreenLoading(false); // Hide loading on error
                Toast.makeText(requireContext(), "Failed to load products: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void checkEmptyState(List<Product> products) {
        boolean isEmpty = products == null || products.isEmpty();
        binding.rvProducts.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    private void showFullScreenLoading(boolean isLoading) {
        binding.fullScreenLoader.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onProductClicked(Product product) {
        Bundle bundle = new Bundle();
        bundle.putString("productId", product.getId());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_productsFragment_to_productDetailsFragment, bundle);
    }
}
