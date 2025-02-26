package com.project.athath.ui.product_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.athath.data.model.Product;
import com.project.athath.data.model.Vendor;
import com.project.athath.data.repository.app_repo.AthathRepository;
import com.project.athath.data.utils.Result;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProductDetailsViewModel extends ViewModel {

    private final AthathRepository repository;

    private final MutableLiveData<Result<Product>> productLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Product>>> recommendedProductsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<Vendor>> vendorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<String>> favoriteResultLiveData = new MutableLiveData<>();

    @Inject
    public ProductDetailsViewModel(AthathRepository repository) {
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
        productLiveData.setValue(Result.loading());
        repository.getAllProducts().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                Product foundProduct = result.getData().stream()
                        .filter(product -> product.getId().equals(productId))
                        .findFirst()
                        .orElse(null);

                if (foundProduct != null) {
                    productLiveData.setValue(Result.success(foundProduct));
                    fetchVendorById(foundProduct.getStoreId()); // Fetch vendor details using storeId
                } else {
                    productLiveData.setValue(Result.error("Product not found"));
                }
            } else {
                productLiveData.setValue(Result.error("Failed to fetch product."));
            }
        });
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
}
