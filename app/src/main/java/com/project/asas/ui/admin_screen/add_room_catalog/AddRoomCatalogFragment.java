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

        binding.btnUploadCatalog.setOnClickListener(v -> saveRoomConfiguration());
    }

    private void saveRoomConfiguration() {

        // viewModel.addFurniture(newFurniture);
        Toast.makeText(requireContext(), "Room Configuration Added!", Toast.LENGTH_SHORT).show();
        requireActivity().onBackPressed();
    }
}