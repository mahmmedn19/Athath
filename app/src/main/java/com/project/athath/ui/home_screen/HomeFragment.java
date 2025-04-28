package com.project.athath.ui.home_screen;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.athath.R;
import com.project.athath.data.model.CatalogItem;
import com.project.athath.data.model.Product;
import com.project.athath.data.model.ResponseModel;
import com.project.athath.data.utils.ImageUtils;
import com.project.athath.data.utils.Result;
import com.project.athath.databinding.FragmentHomeBinding;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.DialogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements
        HomeAdapter.HomeInteractionListener,
        ProductHomeAdapter.ProductInteractionListener,
        CatalogHomeAdapter.CatalogHomeInteractionListener {

    private CatalogHomeAdapter catalogAdapter;
    private ProductHomeAdapter productAdapter;
    private final List<CatalogItem> catalogItems = new ArrayList<>();
    private final List<Product> products = new ArrayList<>();
    private HomeViewModel viewModel;

    @Override
    protected String getTAG() {
        return "HomeFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_home;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Home");
        showBackButton(false);
        initRecyclerViews();
        setupListeners();
        observeCatalogItems();
        observeProducts();
        observeViewModel();
        viewModel.fetchCatalogItems();
        viewModel.fetchProducts();
    }


    private void initRecyclerViews() {
        catalogAdapter = new CatalogHomeAdapter(catalogItems, this);
        productAdapter = new ProductHomeAdapter(products, this);

        binding.rvCatalog.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCatalog.setAdapter(catalogAdapter);

        binding.productList.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.productList.setAdapter(productAdapter);
    }

    private void setupListeners() {
        binding.tvViewAllProducts.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_productsFragment)
        );

        binding.viewCatalogAllButton.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_catalogFragment)
        );

        binding.profileImage.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_userSelectionFragment)
        );
    }

    private void observeCatalogItems() {
        viewModel.getCatalogItems().observe(getViewLifecycleOwner(), result -> {
            handleState(
                    result,
                    binding.loadingProgressBar,         // Catalog loading bar
                    binding.rvCatalog,                  // Catalog RecyclerView
                    binding.imageNoDataFound,           // Catalog no data image
                    catalogItems,                       // Catalog data list
                    catalogAdapter                      // Catalog adapter
            );
        });
    }

    private void observeProducts() {
        viewModel.getProducts().observe(getViewLifecycleOwner(), result -> {
            handleState(
                    result,
                    binding.loadingProgressBarProduct,  // Product loading bar
                    binding.productList,                // Product RecyclerView
                    binding.imageNoDataFoundProduct,    // Product no data image
                    products,                           // Product data list
                    productAdapter                      // Product adapter
            );
        });
    }

    /**
     * Handles the loading, success, and error states for both products and catalogs.
     */

    private <T> void handleState(Result<List<T>> result, View progressBar, View recyclerView, View emptyView, List<T> dataList, androidx.recyclerview.widget.RecyclerView.Adapter<?> adapter) {
        if (result.getStatus() == Result.Status.LOADING) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);

        } else if (result.getStatus() == Result.Status.SUCCESS) {
            progressBar.setVisibility(View.GONE);
            dataList.clear();

            if (result.getData() != null && !result.getData().isEmpty()) {
                dataList.addAll(result.getData());
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }

            adapter.notifyDataSetChanged();

        } else if (result.getStatus() == Result.Status.ERROR) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    private void observeViewModel() {
        viewModel.getCustomerLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                if (result.getStatus() == Result.Status.SUCCESS) {
                    binding.profileName.setText(result.getData().getUsername() + "!");
                }
            }
        });
    }

    @Override
    public void onFavoriteClicked(Product product) {
    }

    @Override
    public LiveData<Boolean> checkIfProductIsFavorite(String productId) {
        return null;
    }


    @Override
    public void onProductClicked(Product product) {
        Bundle bundle = new Bundle();
        bundle.putString("productId", product.getId());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_productDetailsFragment, bundle);
    }

    @Override
    public void onShowDetailsClicked(CatalogItem catalogItem) {
        boolean isLoggedIn = viewModel.getIsCustomerLoggedIn().getValue() != null
                && viewModel.getIsCustomerLoggedIn().getValue();
        if (!isLoggedIn) {
            DialogUtils.showConfirmationDialog(
                    requireContext(),
                    "Login Required",
                    "You need to be logged in to access this feature.",
                    "Login", "Cancel",
                    (dialog, which) -> {
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_userSelectionFragment);
                    }
            );
            return;
        }
        if (catalogItem.getImageRes() == null || catalogItem.getImageRes().isEmpty()) {
            Toast.makeText(requireContext(), "No image found in this catalog item!", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Decode Base64 image
        Bitmap bitmap = ImageUtils.decodeBase64ToImage(catalogItem.getImageRes());
        if (bitmap == null) {
            Toast.makeText(requireContext(), "Failed to decode image.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Convert Bitmap to File
        File imageFile = bitmapToFile(bitmap);
        if (imageFile == null) {
            Toast.makeText(requireContext(), "Failed to process image file.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Show loading dialog
        DialogUtils.showLoadingDialog(requireContext(), "Uploading image...");

        // ✅ Upload Image
        viewModel.uploadImage(imageFile);

        // ✅ Observe Upload Result
        viewModel.getUploadResult().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                DialogUtils.showLoadingDialog(requireContext(), "Analyzing image...");
            } else {
                DialogUtils.hideLoadingDialog();

                if (result.getStatus() == Result.Status.SUCCESS) {
                    List<ResponseModel.DetectedObject> detectedObjects = result.getData();
                    if (detectedObjects != null && !detectedObjects.isEmpty()) {
                        // ✅ Show success dialog and navigate
                        DialogUtils.showConfirmationDialog(requireContext(),
                                "Upload Successful",
                                "Image Analyzing successfully!",
                                "View Catalog Details", "Cancel",
                                (dialog, which) -> {
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelableArrayList("detectedObjects", new ArrayList<>(detectedObjects));
                                    Navigation.findNavController(binding.getRoot())
                                            .navigate(R.id.action_homeFragment_to_catalogDetailsFragment, bundle);
                                });
                    } else {
                        DialogUtils.showConfirmationDialog(
                                requireContext(),
                                "Upload Successful",
                                "No objects detected in the image.",
                                "OK", null,
                                (dialog, which) -> dialog.dismiss()
                        );
                    }
                } else {
                    DialogUtils.showConfirmationDialog(
                            requireContext(),
                            "Upload Failed",
                            "Failed to analyzing image.",
                            "OK", null,
                            (dialog, which) -> dialog.dismiss()
                    );
                }
            }
        });
    }

    // ✅ Helper Method: Convert Bitmap to File
    private File bitmapToFile(Bitmap bitmap) {
        try {
            File file = new File(requireContext().getCacheDir(), "catalog_image_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);

            // ✅ Compress & Write the file
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
