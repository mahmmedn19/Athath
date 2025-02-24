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

        productAdapter = new ManageProductAdapter(viewModel.products.getValue(), this);
        binding.recyclerProducts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerProducts.setAdapter(productAdapter);

        observeViewModel();

        binding.fabAddProduct.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.action_vendor_products_to_add_product)
        );
        showLoading(true);
        viewModel.fetchAllProducts();
    }

    private void observeViewModel() {
        viewModel.products.observe(getViewLifecycleOwner(), products -> {
            productAdapter.notifyDataSetChanged();
            checkEmptyState(products);
            showLoading(false);
        });

        viewModel.getFetchProductsResult().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.ERROR) {
                showLoading(false);
                String errorMessage = result.getErrorMessage() != null ? result.getErrorMessage() : "Unknown error";
                Toast.makeText(requireContext(), "Failed to load products: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getDeleteProductResult().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                Toast.makeText(requireContext(), "Product deleted successfully!", Toast.LENGTH_SHORT).show();
            } else if (result.getStatus() == Result.Status.ERROR) {
                String errorMessage = result.getErrorMessage() != null ? result.getErrorMessage() : "Unknown error";
                Toast.makeText(requireContext(), "Failed to delete product: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkEmptyState(java.util.List<Product> products) {
        if (products == null || products.isEmpty()) {
            binding.imageNoDataFound.setVisibility(View.VISIBLE);
            binding.recyclerProducts.setVisibility(View.GONE);
        } else {
            binding.imageNoDataFound.setVisibility(View.GONE);
            binding.recyclerProducts.setVisibility(View.VISIBLE);
        }
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