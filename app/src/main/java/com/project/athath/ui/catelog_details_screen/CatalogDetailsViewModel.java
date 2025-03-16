package com.project.athath.ui.catelog_details_screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.project.athath.data.model.Product;
import com.project.athath.data.repository.app_repo.AthathRepository;
import com.project.athath.data.utils.Result;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CatalogDetailsViewModel extends ViewModel {

    private final AthathRepository repository;
    private final MutableLiveData<Result<List<Product>>> allProductsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<String>> detectedLabelsLiveData = new MutableLiveData<>();

    @Inject
    public CatalogDetailsViewModel(AthathRepository repository) {
        this.repository = repository;
    }

    public LiveData<Result<List<Product>>> getFilteredProductsLiveData() {
        return Transformations.map(allProductsLiveData, result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                List<Product> allProducts = result.getData();
                List<String> detectedLabels = detectedLabelsLiveData.getValue();

                if (allProducts != null && detectedLabels != null) {
                    // ✅ Filter products that contain any detected label in their name
                    List<Product> filteredProducts = new ArrayList<>();
                    for (Product product : allProducts) {
                        for (String label : detectedLabels) {
                            if (product.getName().toLowerCase().contains(label.toLowerCase())) {
                                filteredProducts.add(product);
                                break; // Avoid adding duplicates
                            }
                        }
                    }
                    return Result.success(filteredProducts);
                }
            }
            return result;
        });
    }

    public void fetchProducts() {
        allProductsLiveData.setValue(Result.loading());
        repository.getAllProducts().observeForever(allProductsLiveData::postValue);
    }

    public void setDetectedLabels(List<String> labels) {
        detectedLabelsLiveData.setValue(labels);
    }
}