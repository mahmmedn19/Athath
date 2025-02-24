package com.project.athath.ui.vendors_screen.manage_products;

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
public class ManageProductsViewModel extends ViewModel {

    private final AthathRepository repository;

    public MutableLiveData<List<Product>> products = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<Result<List<Product>>> fetchProductsResult = new MutableLiveData<>();
    private final MutableLiveData<Result<String>> deleteProductResult = new MutableLiveData<>();

    @Inject
    public ManageProductsViewModel(AthathRepository repository) {
        this.repository = repository;
        fetchAllProducts();
    }

    public LiveData<Result<List<Product>>> getFetchProductsResult() {
        return fetchProductsResult;
    }

    public LiveData<Result<String>> getDeleteProductResult() {
        return deleteProductResult;
    }

    public void fetchAllProducts() {
        repository.getAllProducts().observeForever(result -> {
            fetchProductsResult.setValue(result);
            if (result.getStatus() == Result.Status.SUCCESS) {
                products.setValue(result.getData());
            }
        });
    }

    public void refreshProducts() {
        fetchAllProducts(); // Fetch again when refreshing
    }

    public void deleteProduct(Product product) {
        repository.deleteProduct(product.getId()).observeForever(result -> {
            deleteProductResult.setValue(result);
            if (result.getStatus() == Result.Status.SUCCESS) {
                refreshProducts(); // Refresh after deletion
            }
        });
    }
}
