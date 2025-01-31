package com.project.asas.ui.admin_screen.add_room_catalog;

import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.project.asas.R;
import com.project.asas.databinding.FragmentAddRoomCatalogBinding;
import com.project.asas.model.Furniture;
import com.project.asas.ui.base.BaseFragment;

import java.util.UUID;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddRoomCatalogFragment extends BaseFragment<FragmentAddRoomCatalogBinding> {
    //private AddRoomCatalogViewModel viewModel = new ViewModelProvider(this).get(AddRoomCatalogViewModel.class);

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
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Add Catalog");
        showBackButton(true);

        binding.btnSaveRoomConfig.setOnClickListener(v -> saveRoomConfiguration());
    }

    private void saveRoomConfiguration() {
        String name = binding.etRoomName.getText().toString().trim();
        String description = binding.etRoomDescription.getText().toString().trim();
        String category = binding.etCategory.getText().toString().trim();
        String color = binding.etRoomColor.getText().toString().trim();
        String roomType = binding.etRoomType.getText().toString().trim();
        String style = binding.etStyle.getText().toString().trim();
        double price = Double.parseDouble(binding.etRoomPrice.getText().toString().trim());
        double length = Double.parseDouble(binding.etRoomLength.getText().toString().trim());
        double width = Double.parseDouble(binding.etRoomWidth.getText().toString().trim());
        double height = Double.parseDouble(binding.etRoomHeight.getText().toString().trim());

        if (name.isEmpty() || description.isEmpty() || category.isEmpty() || color.isEmpty() || roomType.isEmpty() ||
                style.isEmpty() || price == 0 || length == 0 || width == 0 || height == 0) {
            Toast.makeText(requireContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        Furniture newFurniture = new Furniture(
                UUID.randomUUID().toString(), name, description, category, color, roomType,
                style, price, length, width, height, R.drawable.furniture_5);

        // viewModel.addFurniture(newFurniture);
        Toast.makeText(requireContext(), "Room Configuration Added!", Toast.LENGTH_SHORT).show();
        requireActivity().onBackPressed();
    }
}