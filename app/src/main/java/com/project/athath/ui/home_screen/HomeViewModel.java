package com.project.athath.ui.home_screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.athath.data.model.CatalogItem;
import com.project.athath.data.model.Customer;
import com.project.athath.data.model.Product;
import com.project.athath.data.repository.app_repo.AthathRepository;
import com.project.athath.data.repository.auth.AuthRepository;
import com.project.athath.data.utils.Result;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {

    private final AthathRepository repository;
    private final AuthRepository authRepository;

    private final MutableLiveData<Result<Customer>> customerLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<List<CatalogItem>>> catalogItemsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<List<Product>>> productsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<String>> aiLinkLiveData = new MutableLiveData<>();

    @Inject
    public HomeViewModel(AthathRepository repository, AuthRepository authRepository) {
        this.repository = repository;
        this.authRepository = authRepository;
        fetchCustomerProfile();
        fetchAILink(); // Auto-fetch on ViewModel creation
    }

    public LiveData<Result<List<CatalogItem>>> getCatalogItems() {
        return catalogItemsLiveData;
    }

    public LiveData<Result<List<Product>>> getProducts() {
        return productsLiveData;
    }

    public void fetchCustomerProfile() {
        customerLiveData.setValue(Result.loading());
        authRepository.getCustomerProfile().observeForever(customerLiveData::postValue);
    }

    public LiveData<Result<Customer>> getCustomerLiveData() {
        return customerLiveData;
    }

    public void fetchCatalogItems() {
        catalogItemsLiveData.setValue(Result.loading());

        repository.getAllCatalogItems().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                List<CatalogItem> limitedCatalogItems = result.getData().stream()
                        .limit(6)
                        .collect(Collectors.toList());
                catalogItemsLiveData.setValue(Result.success(limitedCatalogItems));
            } else {
                catalogItemsLiveData.setValue(Result.error(result.getErrorMessage()));
            }
        });
    }

    public void fetchProducts() {
        productsLiveData.setValue(Result.loading());

        repository.getAllProducts().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                List<Product> limitedProducts = result.getData().stream()
                        .limit(6)
                        .collect(Collectors.toList());
                productsLiveData.setValue(Result.success(limitedProducts));
            } else {
                productsLiveData.setValue(Result.error(result.getErrorMessage()));
            }
        });
    }

    public void fetchAILink() {
        aiLinkLiveData.setValue(Result.loading());

        repository.getSingleAILink().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                aiLinkLiveData.setValue(Result.success(result.getData()));
            } else {
                aiLinkLiveData.setValue(Result.error(result.getErrorMessage()));
            }
        });
    }

    public LiveData<Result<String>> getSingleAILink() {
        return aiLinkLiveData;
    }
}
