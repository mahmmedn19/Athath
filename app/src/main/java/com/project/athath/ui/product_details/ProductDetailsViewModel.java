package com.project.athath.ui.product_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.athath.data.model.Product;
import com.project.athath.data.model.Vendor;
import com.project.athath.data.repository.app_repo.AthathRepository;
import com.project.athath.data.repository.auth.AuthRepository;
import com.project.athath.data.utils.Result;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProductDetailsViewModel extends ViewModel {

    private final AthathRepository repository;
    private final AuthRepository authRepository;

    private final MutableLiveData<Result<Product>> productLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Product>>> recommendedProductsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<Vendor>> vendorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<String>> favoriteResultLiveData = new MutableLiveData<>();

    @Inject
    public ProductDetailsViewModel(AthathRepository repository, AuthRepository authRepository) {
        this.authRepository = authRepository;
        this.repository = repository;
    }

    public LiveData<Result<Product>> getProductLiveData() {
        return productLiveData;
    }

    public LiveData<Result<List<Product>>> getRecommendedProductsLiveData() {
        return recommendedProductsLiveData;
    }

    public LiveData<Result<Vendor>> getVendorLiveData() {
        return vendorLiveData;
    }

    public void fetchProductById(String productId) {
        productLiveData.postValue(Result.loading());  // Show loading state

        new android.os.Handler().postDelayed(() -> {
            repository.getAllProducts().observeForever(result -> {
                if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                    Product foundProduct = result.getData().stream()
                            .filter(product -> product.getId().equals(productId))
                            .findFirst()
                            .orElse(null);

                    if (foundProduct != null) {
                        productLiveData.postValue(Result.success(foundProduct));
                        fetchVendorById(foundProduct.getStoreId()); // Fetch vendor details
                    } else {
                        productLiveData.postValue(Result.error("Product not found"));
                    }
                } else {
                    productLiveData.postValue(Result.error("Failed to fetch product."));
                }
            });
        }, 2000); // 2 seconds delay
    }

    public void fetchRecommendedProducts(String productId) {
        recommendedProductsLiveData.setValue(Result.loading());
        repository.getAllProducts().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                List<Product> recommended = result.getData().stream()
                        .filter(product -> !product.getId().equals(productId))  // Exclude current product
                        .limit(6)
                        .collect(Collectors.toList());
                recommendedProductsLiveData.setValue(Result.success(recommended));
            } else {
                recommendedProductsLiveData.setValue(Result.error("Failed to fetch recommended products."));
            }
        });
    }

    public void fetchVendorById(String vendorId) {
        vendorLiveData.setValue(Result.loading());
        repository.getVendorById(vendorId).observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                vendorLiveData.setValue(Result.success(result.getData()));
            } else {
                vendorLiveData.setValue(Result.error("Failed to fetch vendor details."));
            }
        });
    }
    public void addToFavorites(Product product) {
        favoriteResultLiveData.setValue(Result.loading());
        repository.addProductToFavorites(product).observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                favoriteResultLiveData.setValue(Result.success("Added to favorites"));
            } else {
                favoriteResultLiveData.setValue(Result.error("Failed to add to favorites"));
            }
        });
    }
    public LiveData<Boolean> checkIfProductIsFavorite(String productId) {
        return repository.checkIfProductIsFavorite(productId);  // Delegate to repository
    }
    public LiveData<Boolean> toggleFavoriteStatus(Product product) {
        MutableLiveData<Boolean> favoriteStatusLiveData = new MutableLiveData<>();

        if (product.isFavorite()) {
            repository.removeProductFromFavorites(product).observeForever(result -> {
                if (result.getStatus() == Result.Status.SUCCESS) {
                    favoriteStatusLiveData.setValue(false);  // Removed from favorites
                }
            });
        } else {
            repository.addProductToFavorites(product).observeForever(result -> {
                if (result.getStatus() == Result.Status.SUCCESS) {
                    favoriteStatusLiveData.setValue(true);  // Added to favorites
                }
            });
        }

        return favoriteStatusLiveData;
    }

    public boolean isUserLoggedIn() {
        return authRepository.isUserLoggedIn();  // Implement this in your repository
    }


}
