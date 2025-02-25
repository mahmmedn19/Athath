package com.project.athath.ui.user_screen.fav_screen;

import androidx.lifecycle.ViewModel;

import com.project.athath.R;
import com.project.athath.data.model.Product;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FavViewModel extends ViewModel {

    private List<Product> favoriteProducts;

    @Inject
    public FavViewModel() {
        favoriteProducts = new ArrayList<>();
    }

/*
    private void loadFakeFavorites() {
        favoriteProducts.add(new Product("Modern", "Living Room", 300.0, 4.5, 5.0, R.drawable.image_1));
        favoriteProducts.add(new Product("Minimalist", "Office", 250.0, 3.5, 4.5, R.drawable.image_4));
        favoriteProducts.add(new Product("Classic", "Bedroom", 450.0, 5.0, 6.0, R.drawable.image_2));
        favoriteProducts.add(new Product("Minimalist", "Office", 250.0, 3.5, 4.5, R.drawable.image_4));
        favoriteProducts.add(new Product("Minimalist", "Office", 250.0, 3.5, 4.5, R.drawable.image_4));
        favoriteProducts.add(new Product("Classic", "Bedroom", 450.0, 5.0, 6.0, R.drawable.image_2));
        favoriteProducts.add(new Product("Modern", "Living Room", 300.0, 4.5, 5.0, R.drawable.image_1));
        favoriteProducts.add(new Product("Minimalist", "Office", 250.0, 3.5, 4.5, R.drawable.image_4));
    }
*/

    public List<Product> getFavoriteProducts() {
        return favoriteProducts;
    }

    public void removeProductFromFavorites(Product product) {
        favoriteProducts.remove(product);
    }
}
