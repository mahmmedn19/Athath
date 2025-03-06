package com.project.athath.ui.catelog_screen;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.athath.R;
import com.project.athath.data.model.CatalogItem;
import com.project.athath.data.model.ResponseModel;
import com.project.athath.data.utils.ImageUtils;
import com.project.athath.data.utils.Result;
import com.project.athath.databinding.FragmentCatalogBinding;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.DialogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CatalogFragment extends BaseFragment<FragmentCatalogBinding> implements CatalogAdapter.CatalogInteractionListener {

    private CatalogAdapter adapter;
    private CatalogViewModel viewModel;
    private final List<CatalogItem> catalogItems = new ArrayList<>();

    @Override
    protected String getTAG() {
        return "CatalogFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_catalog;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(CatalogViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Catalog");
        showBackButton(false);

        initRecyclerView();
        observeCatalogItems();

        // Fetch catalog items when fragment starts
        viewModel.fetchCatalogItems();
    }

    private void initRecyclerView() {
        binding.rvCatalogItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CatalogAdapter(catalogItems, this);
        binding.rvCatalogItems.setAdapter(adapter);
    }

    private void observeCatalogItems() {
        viewModel.getCatalogItems().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                binding.loadingProgressBar.setVisibility(View.VISIBLE);
                binding.rvCatalogItems.setVisibility(View.GONE);
                binding.imageNoDataFound.setVisibility(View.GONE);
            } else {
                binding.loadingProgressBar.setVisibility(View.GONE);

                if (result.getStatus() == Result.Status.SUCCESS) {
                    catalogItems.clear();
                    if (result.getData() != null && !result.getData().isEmpty()) {
                        catalogItems.addAll(result.getData());
                        binding.rvCatalogItems.setVisibility(View.VISIBLE);
                        binding.imageNoDataFound.setVisibility(View.GONE);
                    } else {
                        binding.rvCatalogItems.setVisibility(View.GONE);
                        binding.imageNoDataFound.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                } else if (result.getStatus() == Result.Status.ERROR) {
                    Toast.makeText(requireContext(), "Error: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    binding.rvCatalogItems.setVisibility(View.GONE);
                    binding.imageNoDataFound.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onShowProductsClicked(CatalogItem catalogItem) {
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
                                            .navigate(R.id.action_catalogFragment_to_catalogDetailsFragment, bundle);
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
