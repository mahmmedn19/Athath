package com.project.asas.ui.vendors_screen.add_product_screen;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.asas.R;
import com.project.asas.databinding.FragmentAddProductBinding;
import com.project.asas.model.Product;
import com.project.asas.ui.base.BaseFragment;
import com.project.asas.ui.vendors_screen.manage_products.ManageProductsViewModel;

import java.util.UUID;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddProductFragment extends BaseFragment<FragmentAddProductBinding> {

    private ManageProductsViewModel viewModel;
    private Uri selectedImageUri = null; // Holds the selected image URI

    @Override
    protected String getTAG() {
        return "AddProductFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_add_product;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(ManageProductsViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Add Product");
        showBackButton(true);

        // Handle Image Selection
        binding.productImage.setOnClickListener(view -> openImagePicker());

        // Save Product Button
        binding.btnSaveProduct.setOnClickListener(view -> saveProduct());

        // Cancel Button
        binding.btnCancel.setOnClickListener(view -> Navigation.findNavController(view)
                .navigateUp());
    }

    private void saveProduct() {
        String name = binding.etProductName.getText().toString().trim();
        String priceStr = binding.etProductPrice.getText().toString().trim();
        String color = binding.etProductColor.getText().toString().trim();
        String style = binding.etProductStyle.getText().toString().trim();
        String roomType = binding.etProductRoomType.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || color.isEmpty() || style.isEmpty() || roomType.isEmpty() || selectedImageUri == null) {
            Toast.makeText(requireContext(), "Please fill all fields and select an image!", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);

        Product newProduct = new Product(
                UUID.randomUUID().toString(),
                style,
                roomType,
                price,
                5.0,
                4.0,
                R.drawable.furniture_3
        );

        viewModel.addProduct(newProduct);
        Toast.makeText(requireContext(), "Product Added Successfully!", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(requireView()).navigateUp();
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    binding.productImage.setImageURI(selectedImageUri);
                }
            });

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }
}
