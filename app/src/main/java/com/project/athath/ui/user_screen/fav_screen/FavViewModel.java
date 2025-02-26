// FavViewModel.java
package com.project.athath.ui.user_screen.fav_screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.athath.data.model.Product;
import com.project.athath.data.repository.app_repo.AthathRepository;
import com.project.athath.data.utils.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FavViewModel extends ViewModel {

    private final AthathRepository repository;
    private final MutableLiveData<Result<List<Product>>> favoriteProductsLiveData = new MutableLiveData<>();

    @Inject
    public FavViewModel(AthathRepository repository) {
        this.repository = repository;
        fetchFavoriteProducts();  // Fetch favorites on ViewModel init
    }

    public LiveData<Result<List<Product>>> getFavoriteProductsLiveData() {
        return favoriteProductsLiveData;
    }

    public void fetchFavoriteProducts() {
        repository.getFavoriteProducts().observeForever(favoriteProductsLiveData::setValue);
    }

    public void addProductToFavorites(Product product) {
        repository.addProductToFavorites(product).observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) fetchFavoriteProducts();
        });
    }

    public void removeProductFromFavorites(Product product) {
        repository.removeProductFromFavorites(product).observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) fetchFavoriteProducts();
        });
    }
}
