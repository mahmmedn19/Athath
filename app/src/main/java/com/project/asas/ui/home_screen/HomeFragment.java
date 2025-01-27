package com.project.asas.ui.home_screen;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.asas.R;
import com.project.asas.databinding.FragmentHomeBinding;
import com.project.asas.model.Product;
import com.project.asas.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements HomeAdapter.HomeInteractionListener, ProductAdapter.ProductInteractionListener {

    private HomeAdapter adapter;
    private ProductAdapter productAdapter;
    private List<Product> products;
    private List<Product> recommendedProducts;
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
        setToolbarVisibility(false);
        initRecyclerView();
        binding.profileImage.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_userSelectionFragment);
        });
    }
    private void initRecyclerView() {
        binding.productList.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rvRecommended.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        products = new ArrayList<>();
        products = generateFakeProducts(4);

        recommendedProducts = new ArrayList<>();
        recommendedProducts = generateFakeProducts(10);

        adapter = new HomeAdapter(recommendedProducts, this);
        productAdapter = new ProductAdapter(products, this);
        binding.productList.setAdapter(productAdapter);
        binding.rvRecommended.setAdapter(adapter);
    }

    private List<Product> generateFakeProducts(int count) {
        List<Product> productList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            productList.add(new Product(
                    "Product " + i,
                    "Description of Product " + i,
                    "$" + (10 * i),
                    R.drawable.furniture1,
                    i % 2 == 0
            ));
        }
        return productList;
    }

    @Override
    public void onFavoriteClicked(Product product) {
        // Handle favorite icon click
        product.setFavorite(!product.isFavorite());
        adapter.notifyDataSetChanged(); // Update the RecyclerView
    }

    @Override
    public void onCartClicked(Product product) {
        //Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_productDetailsFragment);
    }

    @Override
    public void onProductClicked(Product product) {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_productDetailsFragment);
    }
}