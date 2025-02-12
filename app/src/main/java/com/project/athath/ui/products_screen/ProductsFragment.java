package com.project.athath.ui.products_screen;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.chip.Chip;
import com.project.athath.R;
import com.project.athath.databinding.FragmentProductsBinding;
import com.project.athath.data.model.Product;
import com.project.athath.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductsFragment extends BaseFragment<FragmentProductsBinding> implements ProductsAdapter.ProductsInteractionListener {

    private ProductsAdapter productAdapter;
    private List<Product> products;

    @Override
    protected String getTAG() {
        return "ProductsFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_products;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Products");
        showBackButton(false);

        binding.rvProducts.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        setupCategoryChips();
        products = new ArrayList<>();
        products = generateFakeProducts(8);
        productAdapter = new ProductsAdapter(products, this);
        binding.rvProducts.setAdapter(productAdapter);
    }
    private void setupCategoryChips() {
        List<Product> productList = generateFakeProducts(8);

        // Ensure ChipGroup starts empty
        binding.chipGroupProductCategories.removeAllViews();

        for (Product product : productList) {
            if (product.getStyle() != null && !product.getStyle().trim().isEmpty()) { // Prevent empty chips
                Chip chip = new Chip(requireContext());
                chip.setText(product.getStyle());
                chip.setCheckable(true);
                // chip.setOnClickListener(v -> filterProductsByCategory(category));
                binding.chipGroupProductCategories.addView(chip);
            }
        }
    }


    private List<Product> generateFakeProducts(int count) {
        List<Product> productList = new ArrayList<>();
        String[] styles = {"Modern", "Classic", "Luxury", "Minimalist", "Vintage"};

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
                    styles[i % styles.length],  // Assign different styles
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
        // Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_productsFragment_to_productDetailsFragment);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_productsFragment_to_productDetailsFragment);
    }
}