package com.project.asas.ui.home_screen;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.project.asas.R;
import com.project.asas.databinding.FragmentHomeBinding;
import com.project.asas.model.Product;
import com.project.asas.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements HomeAdapter.HomeInteractionListener {

    private HomeAdapter adapter;
    private List<Product> products;
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
        initRecyclerView();
        binding.profileImage.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_userSelectionFragment);
        });
    }
    private void initRecyclerView() {
        binding.productList.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        products = new ArrayList<>();
        products = generateFakeProducts(10);
        adapter = new HomeAdapter(products, this);
        binding.productList.setAdapter(adapter);
    }
    private List<Product> generateFakeProducts(int count) {
        List<Product> productList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            productList.add(new Product(
                    "Product " + i,
                    "Description of Product " + i,
                    "$" + (10 * i), // Example price: $10, $20, $30, etc.
                    R.drawable.furniture1, // Replace with a real drawable resource
                    i % 2 == 0 // Even-indexed items are favorited by default
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

    }
}