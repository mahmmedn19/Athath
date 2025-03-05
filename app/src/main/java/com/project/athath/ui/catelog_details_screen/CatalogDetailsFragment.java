package com.project.athath.ui.catelog_details_screen;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.athath.R;
import com.project.athath.data.model.Product;
import com.project.athath.data.model.ResponseModel;
import com.project.athath.data.utils.Result;
import com.project.athath.databinding.FragmentCatalogDetailsBinding;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.products_screen.ProductsAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CatalogDetailsFragment extends BaseFragment<FragmentCatalogDetailsBinding> implements ProductsAdapter.ProductsInteractionListener {

    private ProductsAdapter productAdapter;
    private List<Product> products = new ArrayList<>();
    private CatalogDetailsViewModel viewModel;
    private CategoryDetailsAdapter detectedObjectsAdapter;
    private List<ResponseModel.DetectedObject> detectedObjects = new ArrayList<>();

    @Override
    protected String getTAG() {
        return "CatalogDetailsFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_catalog_details;
    }

    @Override
    protected CatalogDetailsViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(CatalogDetailsViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Catalog Details");
        showBackButton(true);

        // ✅ Retrieve detected objects from arguments
        if (getArguments() != null) {
            List<ResponseModel.DetectedObject> receivedObjects = getArguments().getParcelableArrayList("detectedObjects");
            if (receivedObjects != null) {
                detectedObjects.clear();
                detectedObjects.addAll(receivedObjects);
            }
        }

        // ✅ Set up detected objects RecyclerView (replaces fake components)
        setupDetectedObjectsRecyclerView();

        // ✅ Set up products RecyclerView
        setupRecyclerView();
        observeProducts();

        // Fetch products
        viewModel.fetchProducts();
    }

    private void setupDetectedObjectsRecyclerView() {
        binding.recyclerComponents.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        detectedObjectsAdapter = new CategoryDetailsAdapter(detectedObjects);
        binding.recyclerComponents.setAdapter(detectedObjectsAdapter);
    }

    private void setupRecyclerView() {
        productAdapter = new ProductsAdapter(products, this);
        binding.rvProducts.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rvProducts.setAdapter(productAdapter);
    }

    private void observeProducts() {
        viewModel.getProductsLiveData().observe(getViewLifecycleOwner(), result -> {
            handleLoadingState(result.getStatus());

            if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                products.clear();
                products.addAll(result.getData());
                productAdapter.notifyDataSetChanged();

                boolean hasProducts = !products.isEmpty();
                binding.rvProducts.setVisibility(hasProducts ? View.VISIBLE : View.GONE);
                binding.imageNoDataFoundProduct.setVisibility(hasProducts ? View.GONE : View.VISIBLE);
            } else if (result.getStatus() == Result.Status.ERROR) {
                binding.rvProducts.setVisibility(View.GONE);
                binding.imageNoDataFoundProduct.setVisibility(View.VISIBLE);
                Toast.makeText(requireContext(), "Error: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLoadingState(Result.Status status) {
        binding.loadingProgressBarProduct.setVisibility(status == Result.Status.LOADING ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onProductClicked(Product product) {
        Bundle bundle = new Bundle();
        bundle.putString("productId", product.getId());
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_catalogDetailsFragment_to_productDetailsFragment, bundle);
    }
}
