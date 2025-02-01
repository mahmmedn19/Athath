package com.project.athath.ui.vendors_screen.manage_products;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.athath.R;
import com.project.athath.model.Product;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class ManageProductsViewModel extends ViewModel {

    private final MutableLiveData<List<Product>> _products = new MutableLiveData<>();
    public LiveData<List<Product>> products = _products;

    @Inject
    public ManageProductsViewModel() {
        loadFakeProducts(); // Load initial data
    }

    private void loadFakeProducts() {
        List<Product> productList = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            productList.add(new Product(
                    "Modern Style " + i,
                    "Living Room",
                    250.0 * i,
                    5.0,
                    4.0,
                    getImageResource(i)
            ));
        }
        _products.setValue(productList);
    }

    public void addProduct(Product product) {
        List<Product> currentList = _products.getValue();
        if (currentList != null) {
            currentList.add(product);
            _products.setValue(currentList);
        }
    }

    public void updateProduct(Product updatedProduct) {
        List<Product> currentList = _products.getValue();
        if (currentList != null) {
            for (int i = 0; i < currentList.size(); i++) {
                if (currentList.get(i).getId().equals(updatedProduct.getId())) {
                    currentList.set(i, updatedProduct);
                    break;
                }
            }
            _products.setValue(currentList);
        }
    }

    public void deleteProduct(Product product) {
        List<Product> currentList = _products.getValue();
        if (currentList != null) {
            currentList.remove(product);
            _products.setValue(currentList);
        }
    }

    private int getImageResource(int index) {
        return switch (index % 5) {
            case 0 -> R.drawable.furniture_5;
            case 1 -> R.drawable.furniture_6;
            case 2 -> R.drawable.furniture_8;
            case 3 -> R.drawable.furniture_7;
            default -> R.drawable.furniture_5;
        };
    }
}
