// FavFragment.java
package com.project.athath.ui.user_screen.fav_screen;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.athath.R;
import com.project.athath.data.model.Product;
import com.project.athath.data.utils.Result;
import com.project.athath.databinding.FragmentFavBinding;
import com.project.athath.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavFragment extends BaseFragment<FragmentFavBinding> implements FavAdapter.FavInteractionListener {

    private FavViewModel viewModel;
    private FavAdapter favAdapter;

    @Override
    protected String getTAG() {
        return "FavFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_fav;
    }

    @Override
    protected FavViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(FavViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Favorites");
        showBackButton(true);

        binding.recyclerFav.setLayoutManager(new LinearLayoutManager(requireContext()));
        favAdapter = new FavAdapter(new java.util.ArrayList<>(), this);
        binding.recyclerFav.setAdapter(favAdapter);

        observeFavoriteProducts();
    }

    private void observeFavoriteProducts() {
        viewModel.getFavoriteProductsLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.recyclerFav.setVisibility(View.GONE);
                binding.imageNoDataFound.setVisibility(View.GONE);
            } else if (result.getStatus() == Result.Status.SUCCESS) {
                binding.progressBar.setVisibility(View.GONE);
                if (result.getData() != null && !result.getData().isEmpty()) {
                    binding.recyclerFav.setVisibility(View.VISIBLE);
                    binding.imageNoDataFound.setVisibility(View.GONE);
                    favAdapter.updateProducts(result.getData());
                } else {
                    binding.recyclerFav.setVisibility(View.GONE);
                    binding.imageNoDataFound.setVisibility(View.VISIBLE);
                }
            } else if (result.getStatus() == Result.Status.ERROR) {
                binding.progressBar.setVisibility(View.GONE);
                binding.recyclerFav.setVisibility(View.GONE);
                binding.imageNoDataFound.setVisibility(View.VISIBLE);
                Toast.makeText(requireContext(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onRemoveFavorite(Product product) {
        viewModel.removeProductFromFavorites(product);
        Toast.makeText(requireContext(), product.getName() + " removed from favorites", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProductClick(Product product) {
        var bundle = new android.os.Bundle();
        bundle.putString("productId", product.getId());
        Navigation.findNavController(requireView()).navigate(R.id.action_favFragment_to_productDetailsFragment, bundle);
    }
}
