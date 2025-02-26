package com.project.athath.ui.vendors_screen.manage_products;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.athath.R;
import com.project.athath.data.model.Product;
import com.project.athath.data.utils.Result;
import com.project.athath.databinding.FragmentManageProductsBinding;
import com.project.athath.ui.base.BaseFragment;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageProductsFragment extends BaseFragment<FragmentManageProductsBinding>
        implements ManageProductAdapter.ManageProductInteractionListener {

    private ManageProductsViewModel viewModel;
    private ManageProductAdapter productAdapter;

    @Override
    protected String getTAG() {
        return "ManageProductsFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_manage_products;
    }

    @Override
    protected ViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Manage Products");
        showBackButton(false);

        viewModel = new ViewModelProvider(this).get(ManageProductsViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setupRecyclerView();
        observeViewModel();

        showLoading(true);  // Show loading initially
        viewModel.fetchAllProducts();  // Auto-fetch products on fragment load

        binding.fabAddProduct.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.action_vendor_products_to_add_product)
        );
    }
    private void setupRecyclerView() {
        productAdapter = new ManageProductAdapter(viewModel.products.getValue(), this);
        binding.recyclerProducts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerProducts.setAdapter(productAdapter);
    }

    private void observeViewModel() {
        // Observe products list changes
        viewModel.products.observe(getViewLifecycleOwner(), products -> {
            productAdapter.updateProducts(products);  // Update the adapter with the new list
            checkEmptyState(products);                // Check if the list is empty
            showLoading(false);                       // Hide loading indicator
        });

        // Observe the result of fetching products
        viewModel.getFetchProductsResult().observe(getViewLifecycleOwner(), result -> {
            showLoading(false);  // Stop showing loading when data fetch is complete
            if (result.getStatus() == Result.Status.ERROR) {
                String errorMessage = result.getErrorMessage() != null ? result.getErrorMessage() : "Failed to load products.";
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Observe the result of deleting a product
        viewModel.getDeleteProductResult().observe(getViewLifecycleOwner(), result -> {
            showLoading(false);  // Stop loading after delete operation
            if (result.getStatus() == Result.Status.SUCCESS) {
                Toast.makeText(requireContext(), "Product deleted successfully!", Toast.LENGTH_SHORT).show();
                viewModel.fetchAllProducts(); // Refresh products after deletion
            } else if (result.getStatus() == Result.Status.ERROR) {
                String errorMessage = result.getErrorMessage() != null ? result.getErrorMessage() : "Failed to delete product.";
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkEmptyState(List<Product> products) {
        boolean isEmpty = products == null || products.isEmpty();
        binding.imageNoDataFound.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        binding.recyclerProducts.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    private void showLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onEditProduct(Product product) {
        Bundle bundle = new Bundle();
        bundle.putString("productId", product.getId());
        Navigation.findNavController(requireView()).navigate(R.id.action_vendor_products_to_add_product, bundle);
    }

    @Override
    public void onDeleteProduct(Product product) {
        viewModel.deleteProduct(product);
    }
}