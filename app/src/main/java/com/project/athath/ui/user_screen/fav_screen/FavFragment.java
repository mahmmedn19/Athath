package com.project.athath.ui.user_screen.fav_screen;

import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.athath.R;
import com.project.athath.databinding.FragmentFavBinding;
import com.project.athath.model.Product;
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
    protected ViewModel getViewModel() {
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
        favAdapter = new FavAdapter(viewModel.getFavoriteProducts(), this);
        binding.recyclerFav.setAdapter(favAdapter);
    }

    @Override
    public void onRemoveFavorite(Product product) {
        viewModel.removeProductFromFavorites(product);
        favAdapter.notifyDataSetChanged();
        Toast.makeText(requireContext(), product.getStyle() + " removed from favorites", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProductClick(Product product) {
        Navigation.findNavController(requireView()).navigate(R.id.action_favFragment_to_productDetailsFragment);
    }
}
