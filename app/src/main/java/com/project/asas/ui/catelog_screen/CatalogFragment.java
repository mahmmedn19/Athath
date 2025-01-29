package com.project.asas.ui.catelog_screen;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.asas.R;
import com.project.asas.databinding.FragmentCatalogBinding;
import com.project.asas.model.CatalogItem;
import com.project.asas.ui.base.BaseFragment;
import com.project.asas.ui.catalog_screen.CatalogAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CatalogFragment extends BaseFragment<FragmentCatalogBinding> implements CatalogAdapter.CatalogInteractionListener {

    private CatalogAdapter adapter;
    private List<CatalogItem> catalogItems;

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
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(false);
        initRecyclerView();
    }

    private void initRecyclerView() {
        binding.rvCatalogItems.setLayoutManager(new LinearLayoutManager(requireContext()));

        catalogItems = new ArrayList<>();
        catalogItems = generateFakeCatalogItems(5);

        adapter = new CatalogAdapter(catalogItems, this);
        binding.rvCatalogItems.setAdapter(adapter);
    }

    private List<CatalogItem> generateFakeCatalogItems(int count) {
        List<CatalogItem> items = new ArrayList<>();
        items.add(new CatalogItem(R.drawable.furniture_5));
        items.add(new CatalogItem(R.drawable.furniture_6));
        items.add(new CatalogItem(R.drawable.furniture_7));
        items.add(new CatalogItem(R.drawable.furniture_8));
        items.add(new CatalogItem(R.drawable.furniture_9));
        return items;
    }

    @Override
    public void onShowProductsClicked(CatalogItem catalogItem) {
        // Navigate to products for this catalog item
       Navigation.findNavController(binding.getRoot()).navigate(R.id.action_catalogFragment_to_catalogDetailsFragment);
    }
}
