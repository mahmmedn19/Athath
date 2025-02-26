package com.project.athath.ui.catelog_details_screen;

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
public class CatalogDetailsViewModel extends ViewModel {

    private final AthathRepository repository;
    private final MutableLiveData<Result<List<Product>>> productsLiveData = new MutableLiveData<>();

    @Inject
    public CatalogDetailsViewModel(AthathRepository repository) {
        this.repository = repository;
    }

    public LiveData<Result<List<Product>>> getProductsLiveData() {
        return productsLiveData;
    }

    public void fetchProducts() {
        productsLiveData.setValue(Result.loading());
        repository.getAllProducts().observeForever(productsLiveData::postValue);
    }
}
