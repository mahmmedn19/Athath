package com.project.athath.ui.admin_screen.room_catalog;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.athath.R;
import com.project.athath.data.model.CatalogItem;
import com.project.athath.data.utils.Result;
import com.project.athath.databinding.FragmentManageRoomCatalogBinding;
import com.project.athath.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageRoomCatalogFragment extends BaseFragment<FragmentManageRoomCatalogBinding> implements RoomCatalogAdapter.RoomCatalogInteractionListener {

    private RoomCatalogAdapter adapter;
    private ManageRoomCatalogViewModel viewModel;
    private final List<CatalogItem> catalogItems = new ArrayList<>();

    @Override
    protected String getTAG() {
        return "ManageRoomCatalogFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_manage_room_catalog;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(ManageRoomCatalogViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Catalog Management");
        showBackButton(false);

        binding.recyclerRoomConfig.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RoomCatalogAdapter(catalogItems, this);
        binding.recyclerRoomConfig.setAdapter(adapter);

        binding.fabAddConfig.setOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_admin_room_configurations_to_admin_add_room_configurations));

        observeCatalogItems();
    }

    private void observeCatalogItems() {
        viewModel.getCatalogItems().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                binding.loadingProgressBar.setVisibility(View.VISIBLE);
                binding.recyclerRoomConfig.setVisibility(View.GONE);
                binding.imageNoDataFound.setVisibility(View.GONE);
            } else {
                binding.loadingProgressBar.setVisibility(View.GONE);

                if (result.getStatus() == Result.Status.SUCCESS) {
                    catalogItems.clear();
                    if (result.getData() != null && !result.getData().isEmpty()) {
                        catalogItems.addAll(result.getData());
                        binding.recyclerRoomConfig.setVisibility(View.VISIBLE);
                        binding.imageNoDataFound.setVisibility(View.GONE);
                    } else {
                        binding.recyclerRoomConfig.setVisibility(View.GONE);
                        binding.imageNoDataFound.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                } else if (result.getStatus() == Result.Status.ERROR) {
                    Toast.makeText(requireContext(), "Error: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    binding.recyclerRoomConfig.setVisibility(View.GONE);
                    binding.imageNoDataFound.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public void onEditCatalog(CatalogItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("catalogItemId", item.getId());
        Navigation.findNavController(requireView())
                .navigate(R.id.action_admin_room_configurations_to_admin_add_room_configurations, bundle);
    }

    @Override
    public void onDeleteCatalog(CatalogItem item) {
        // Show loading
        binding.loadingProgressBar.setVisibility(View.VISIBLE);
        binding.recyclerRoomConfig.setVisibility(View.GONE);

        viewModel.deleteCatalogItem(item.getId()).observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                Toast.makeText(requireContext(), "Item deleted successfully", Toast.LENGTH_SHORT).show();
                viewModel.fetchCatalogItems();  // Refresh the list after deletion
            } else if (result.getStatus() == Result.Status.ERROR) {
                Toast.makeText(requireContext(), "Error deleting item: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
