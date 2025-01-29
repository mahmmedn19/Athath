package com.project.asas.ui.product_details;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.asas.R;
import com.project.asas.databinding.FragmentProductDetailsBinding;
import com.project.asas.model.Product;
import com.project.asas.ui.base.BaseFragment;
import com.project.asas.ui.home_screen.HomeAdapter;
import com.project.asas.ui.home_screen.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductDetailsFragment extends BaseFragment<FragmentProductDetailsBinding> implements HomeAdapter.HomeInteractionListener{

    private HomeAdapter adapter;
    private ProductAdapter productAdapter;
    private List<Product> products;
    private List<Product> recommendedProducts;

    @Override
    protected String getTAG() {
        return "ProductDetailsFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_product_details;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Product Details");
        showBackButton(true);

        binding.rvSuggestionItems.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        recommendedProducts = new ArrayList<>();
        recommendedProducts = generateFakeProducts(10);

        adapter = new HomeAdapter(recommendedProducts, this);
        binding.rvSuggestionItems.setAdapter(adapter);
    }
    private List<Product> generateFakeProducts(int count) {
        List<Product> productList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            productList.add(new Product(
                    i,
                    "Description " + i,
                    "",
                    50 * i,
                    4.5f,
                    4.5f,
                    R.drawable.furniture1
            ));
        }
        return productList;
    }
    @Override
    public void onFavoriteClicked(Product product) {

    }

    @Override
    public void onCartClicked(Product product) {

    }
}