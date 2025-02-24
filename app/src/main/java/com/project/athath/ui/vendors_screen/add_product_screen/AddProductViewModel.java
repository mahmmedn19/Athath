package com.project.athath.ui.vendors_screen.add_product_screen;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.athath.data.model.Product;
import com.project.athath.data.repository.app_repo.AthathRepository;
import com.project.athath.data.utils.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddProductViewModel extends ViewModel {
    private final AthathRepository repository;

    @Inject
    public AddProductViewModel(AthathRepository repository) {
        this.repository = repository;
    }

    // Add Product
    public MutableLiveData<Result<String>> addProduct(Product product) {
        MutableLiveData<Result<String>> addProductResult = new MutableLiveData<>();
        repository.addProduct(product).observeForever(result -> {
            addProductResult.setValue(result);
        });
        return addProductResult;
    }

    // Update Product
    public MutableLiveData<Result<String>> updateProduct(Product product) {
        MutableLiveData<Result<String>> updateProductResult = new MutableLiveData<>();
        repository.updateProduct(product).observeForever(result -> {
            updateProductResult.setValue(result);
        });
        return updateProductResult;
    }

    //get Product By Id
    public MutableLiveData<Result<Product>> getProductById(String productId) {
        MutableLiveData<Result<Product>> getProductByIdResult = new MutableLiveData<>();
        repository.getProductById(productId).observeForever(result -> {
            getProductByIdResult.setValue(result);
        });
        return getProductByIdResult;
    }


}
