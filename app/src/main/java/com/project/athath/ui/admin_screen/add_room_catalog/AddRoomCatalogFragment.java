package com.project.athath.ui.admin_screen.add_room_catalog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.project.athath.R;
import com.project.athath.data.model.CatalogItem;
import com.project.athath.data.utils.ImageUtils;
import com.project.athath.data.utils.Result;
import com.project.athath.databinding.FragmentAddRoomCatalogBinding;
import com.project.athath.ui.base.BaseFragment;

import java.io.IOException;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddRoomCatalogFragment extends BaseFragment<FragmentAddRoomCatalogBinding> {

    private AddRoomCatalogViewModel viewModel;
    private Bitmap selectedBitmap;
    private String catalogItemId = null;
    private String existingImageBase64 = null;  // Store existing image in case it's not changed

    @Override
    protected String getTAG() {
        return "AddRoomCatalogFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_add_room_catalog;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(AddRoomCatalogViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        showBackButton(true);

        // Check if navigating with an existing item (for editing)
        if (getArguments() != null && getArguments().containsKey("catalogItemId")) {
            catalogItemId = getArguments().getString("catalogItemId");
            setToolbarTitle("Edit Catalog");
            binding.btnUploadCatalog.setText("Update Catalog");  // Change button label
            loadCatalogItemDetails();  // Load existing image
        } else {
            setToolbarTitle("Add Catalog");
            binding.btnUploadCatalog.setText("Upload Catalog");
        }

        binding.btnSelectImage.setOnClickListener(v -> openGallery());
        binding.btnUploadCatalog.setOnClickListener(v -> saveCatalogItem());

        observeUploadResult();
    }

    private void loadCatalogItemDetails() {
        // Fetch the existing catalog item from Firestore
        viewModel.getCatalogItemById(catalogItemId).observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                CatalogItem item = result.getData();
                existingImageBase64 = item.getImageRes();  // Store existing image

                // Decode Base64 and set the image preview
                if (existingImageBase64 != null && !existingImageBase64.isEmpty()) {
                    Bitmap decodedBitmap = ImageUtils.decodeBase64ToImage(existingImageBase64);
                    binding.imagePreview.setImageBitmap(decodedBitmap);
                }
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        selectedBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                        binding.imagePreview.setImageBitmap(selectedBitmap);
                    } catch (IOException e) {
                        Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void saveCatalogItem() {
        binding.loadingProgressBar.setVisibility(View.VISIBLE);

        if (selectedBitmap == null) {
            if (catalogItemId == null) {
                Toast.makeText(requireContext(), "Please select an image first", Toast.LENGTH_SHORT).show();
                binding.loadingProgressBar.setVisibility(View.GONE);
                return;
            } else {
                // If editing and image wasn't changed, retain the existing one
                viewModel.updateCatalogItem(catalogItemId, existingImageBase64);
            }
        } else {
            // ✅ Convert Bitmap to Base64 before passing to ViewModel
            String newBase64Image = ImageUtils.encodeImageToBase64(selectedBitmap);

            if (catalogItemId == null) {
                viewModel.uploadCatalogItem(newBase64Image);
            } else {
                viewModel.updateCatalogItem(catalogItemId, newBase64Image);
            }
        }
    }

    private void observeUploadResult() {
        viewModel.getUploadResult().observe(getViewLifecycleOwner(), result -> {
            binding.loadingProgressBar.setVisibility(View.GONE);

            if (result != null) {
                switch (result.getStatus()) {
                    case SUCCESS:
                        Toast.makeText(requireContext(), "Operation Successful!", Toast.LENGTH_SHORT).show();
                        requireActivity().onBackPressed();
                        break;
                    case ERROR:
                        Toast.makeText(requireContext(), "Error: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        break;
                    case LOADING:
                        binding.loadingProgressBar.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }
}
