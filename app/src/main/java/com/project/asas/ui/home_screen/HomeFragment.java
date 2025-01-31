package com.project.asas.ui.home_screen;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.asas.R;
import com.project.asas.databinding.FragmentHomeBinding;
import com.project.asas.model.CatalogItem;
import com.project.asas.model.Product;
import com.project.asas.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements HomeAdapter.HomeInteractionListener, ProductHomeAdapter.ProductInteractionListener , CatalogHomeAdapter.CatalogHomeInteractionListener {

    private CatalogHomeAdapter adapter;
    private ProductHomeAdapter productAdapter;
    private List<Product> products;
    private List<CatalogItem> catalogItems;
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
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Home");
        showBackButton(false);
        initRecyclerView();
        binding.tvViewAllProducts.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_productsFragment);
        });
        binding.viewCatalogAllButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_catalogFragment);
        });

        binding.profileImage.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_userSelectionFragment);
        });
    }
    private void initRecyclerView() {
        binding.productList.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rvCatalog.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        products = new ArrayList<>();
        products = generateFakeProducts(6);

        catalogItems = new ArrayList<>();
        catalogItems = generateFakeCatalogItems(10);

        adapter = new CatalogHomeAdapter(catalogItems, this);
        productAdapter = new ProductHomeAdapter(products, this);
        binding.productList.setAdapter(productAdapter);
        binding.rvCatalog.setAdapter(adapter);
    }

    private List<Product> generateFakeProducts(int count) {
        List<Product> productList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            productList.add(new Product(
                    "Product " + i,
                    "",
                    50 * i,
                    4.5f,
                    4.5f,
                    R.drawable.furniture1
            ));
        }
        return productList;
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
    public void onFavoriteClicked(Product product) {
        // Handle favorite icon click
        adapter.notifyDataSetChanged(); // Update the RecyclerView
    }

    @Override
    public void onCartClicked(Product product) {
        //Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_productDetailsFragment);
    }

    @Override
    public void onProductClicked(Product product) {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_productsFragment);
    }

    @Override
    public void onShowDetailsClicked(CatalogItem catalogItem) {
        // Navigate to products for this catalog item
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_catalogDetailsFragment);
    }
}