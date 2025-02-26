package com.project.athath.ui.products_screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.athath.data.model.Product;
import com.project.athath.data.repository.app_repo.AthathRepository;
import com.project.athath.data.utils.Result;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProductsViewModel extends ViewModel {

    private final AthathRepository repository;
    private final MutableLiveData<List<Product>> products = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Product>> filteredProducts = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Product>>> fetchProductsResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public ProductsViewModel(AthathRepository repository) {
        this.repository = repository;
        fetchProducts();
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    public LiveData<List<Product>> getFilteredProducts() {
        return filteredProducts;
    }

    public LiveData<Result<List<Product>>> getFetchProductsResult() {
        return fetchProductsResult;
    }

    public LiveData<Boolean> getLoadingState() {
        return isLoading;
    }

    public void fetchProducts() {
        fetchProductsResult.setValue(Result.loading());  // Emit loading before fetching

        repository.getAllProducts().observeForever(result -> {
            fetchProductsResult.setValue(result);  // Emit SUCCESS or ERROR state
            if (result.getStatus() == Result.Status.SUCCESS) {
                products.setValue(result.getData());
                filteredProducts.setValue(result.getData());
            }
        });
    }


    public void filterProductsByCategory(String category) {
        List<Product> allProducts = products.getValue();
        if (allProducts == null) return;

        if (category == null || category.isEmpty()) {
            filteredProducts.setValue(allProducts);
        } else {
            List<Product> filteredList = new ArrayList<>();
            for (Product product : allProducts) {
                if (product.getCategory() != null && product.getCategory().equalsIgnoreCase(category)) {
                    filteredList.add(product);
                }
            }
            filteredProducts.setValue(filteredList);
        }
    }
}
