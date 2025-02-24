package com.project.athath.ui.vendors_screen.add_product_screen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.athath.R;
import com.project.athath.data.model.Product;
import com.project.athath.data.utils.ImageUtils;
import com.project.athath.databinding.FragmentAddProductBinding;
import com.project.athath.ui.base.BaseFragment;

import java.io.IOException;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddProductFragment extends BaseFragment<FragmentAddProductBinding> {

    private AddProductViewModel viewModel;
    private Uri selectedImageUri = null;
    private String encodedImage = null;
    private String productId = null;  // Holds the product ID if editing

    @Override
    protected String getTAG() {
        return "AddProductFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_add_product;
    }

    @Override
    protected AddProductViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(AddProductViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);

        Bundle args = getArguments();
        if (args != null && args.containsKey("productId")) {
            productId = args.getString("productId");
            setToolbarTitle("Update Product");
            loadProductData(productId);
        } else {
            setToolbarTitle("Add Product");
        }

        binding.productImage.setOnClickListener(view -> openImagePicker());
        binding.btnSelectImage.setOnClickListener(view -> openImagePicker());
        binding.btnSaveProduct.setOnClickListener(view -> saveOrUpdateProduct());
    }

    private void loadProductData(String productId) {
        viewModel.getProductById(productId).observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == com.project.athath.data.utils.Result.Status.SUCCESS) {
                Product product = result.getData();
                if (product != null) {
                    fillProductFields(product);
                }
            } else {
                Toast.makeText(requireContext(), "Failed to load product data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillProductFields(Product product) {
        binding.etProductName.setText(product.getName());
        binding.etProductCategory.setText(product.getCategory());
        binding.etProductDescription.setText(product.getDescription());
        binding.etProductPrice.setText(String.valueOf(product.getPrice()));
        binding.etProductColor.setText(product.getColor());
        binding.etProductStyle.setText(product.getStyle());
        binding.etProductRoomType.setText(product.getRoomType());
        binding.etProductWidth.setText(String.valueOf(product.getProductWidth()));
        binding.etProductLength.setText(String.valueOf(product.getProductLength()));

        encodedImage = product.getImageUrl();
        if (encodedImage != null) {
            binding.productImage.setImageBitmap(ImageUtils.decodeBase64ToImage(encodedImage));
        }
    }

    private void saveOrUpdateProduct() {
        String name = binding.etProductName.getText().toString().trim();
        String category = binding.etProductCategory.getText().toString().trim();
        String description = binding.etProductDescription.getText().toString().trim();
        String priceStr = binding.etProductPrice.getText().toString().trim();
        String color = binding.etProductColor.getText().toString().trim();
        String style = binding.etProductStyle.getText().toString().trim();
        String roomType = binding.etProductRoomType.getText().toString().trim();
        String widthStr = binding.etProductWidth.getText().toString().trim();
        String lengthStr = binding.etProductLength.getText().toString().trim();

        if (name.isEmpty() || category.isEmpty() || description.isEmpty() || priceStr.isEmpty()
                || color.isEmpty() || style.isEmpty() || roomType.isEmpty()
                || widthStr.isEmpty() || lengthStr.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        double width = Double.parseDouble(widthStr);
        double length = Double.parseDouble(lengthStr);

        // Use existing encoded image if no new image is selected
        if (encodedImage == null) {
            Toast.makeText(requireContext(), "Please select an image!", Toast.LENGTH_SHORT).show();
            return;
        }

        Product product = new Product(
                productId, name, category, description, price, color, style,
                roomType, width, length, encodedImage, null, null
        );

        if (productId != null) {
            viewModel.updateProduct(product).observe(getViewLifecycleOwner(), result -> {
                if (result.getStatus() == com.project.athath.data.utils.Result.Status.SUCCESS) {
                    Toast.makeText(requireContext(), "Product updated successfully!", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireView()).navigateUp();
                } else {
                    Toast.makeText(requireContext(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            viewModel.addProduct(product).observe(getViewLifecycleOwner(), result -> {
                if (result.getStatus() == com.project.athath.data.utils.Result.Status.SUCCESS) {
                    Toast.makeText(requireContext(), "Product added successfully!", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireView()).navigateUp();
                } else {
                    Toast.makeText(requireContext(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    binding.productImage.setImageURI(selectedImageUri);
                    encodeSelectedImage();
                }
            });

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void encodeSelectedImage() {
        if (selectedImageUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri);
                encodedImage = ImageUtils.encodeImageToBase64(bitmap);
            } catch (IOException e) {
                Toast.makeText(requireContext(), "Failed to process image.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
