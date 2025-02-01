package com.project.athath.ui.catelog_details_screen;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.project.athath.R;
import com.project.athath.databinding.FragmentCatalogDetailsBinding;
import com.project.athath.model.Component;
import com.project.athath.model.Product;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.products_screen.ProductsAdapter;
import java.util.ArrayList;
import java.util.List;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CatalogDetailsFragment extends BaseFragment<FragmentCatalogDetailsBinding> implements ProductsAdapter.ProductsInteractionListener {
    private ProductsAdapter productAdapter;
    private CategoryDetailsAdapter componentAdapter;
    private List<Product> products;
    private List<Component> components;

    @Override
    protected String getTAG() {
        return "CatalogDetailsFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_catalog_details;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Catalog Details");
        showBackButton(true);

        // Set up catalog components list (horizontal)
        binding.recyclerComponents.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        components = generateFakeComponents(3);
        componentAdapter = new CategoryDetailsAdapter(components);
        binding.recyclerComponents.setAdapter(componentAdapter);

        // Set up product list (grid)
        binding.rvProducts.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        products = generateFakeProducts(8);
        productAdapter = new ProductsAdapter(products, this);
        binding.rvProducts.setAdapter(productAdapter);
    }

    // Generate Fake Components Data
    private List<Component> generateFakeComponents(int count) {
        List<Component> componentList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            int imageRes = switch (i % 5) {
                case 0 -> R.drawable.image_1;
                case 1 -> R.drawable.image_2;
                default -> R.drawable.image_4;
            };

            componentList.add(new Component("Component " + i, imageRes));
        }
        return componentList;
    }

    // Generate Fake Products Data
    private List<Product> generateFakeProducts(int count) {
        List<Product> productList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            int imageRes = switch (i % 5) {
                case 0 -> R.drawable.image_1;
                case 1 -> R.drawable.image_2;
                case 2 -> R.drawable.image_3;
                case 3 -> R.drawable.image_4;
                default -> R.drawable.image_5;
            };

            productList.add(new Product(
                    "Product " + i,
                    "",
                    50 * i,
                    4.5f,
                    4.5f,
                    imageRes
            ));
        }
        return productList;
    }

    @Override
    public void onProductClicked(Product product) {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_catalogDetailsFragment_to_productDetailsFragment);
    }
}
