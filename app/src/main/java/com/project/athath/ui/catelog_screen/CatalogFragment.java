package com.project.athath.ui.catelog_screen;

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
import com.project.athath.databinding.FragmentCatalogBinding;
import com.project.athath.ui.base.BaseFragment;

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
        Bundle bundle = new Bundle();
        bundle.putString("catalogItemId", catalogItem.getId());
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_catalogFragment_to_catalogDetailsFragment, bundle);
    }
}
