package com.project.athath.ui.home_screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.athath.data.model.CatalogItem;
import com.project.athath.data.model.Customer;
import com.project.athath.data.model.Product;
import com.project.athath.data.model.ResponseModel;
import com.project.athath.data.repository.app_repo.AthathRepository;
import com.project.athath.data.repository.auth.AuthRepository;
import com.project.athath.data.utils.Result;

import java.io.File;
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
    private final MutableLiveData<Result<List<ResponseModel.DetectedObject>>> uploadResult = new MutableLiveData<>();
    private final MutableLiveData<Result<Customer>> customerProfileLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isCustomerLoggedIn = new MutableLiveData<>();
    private final FirebaseAuth firebaseAuth;

    @Inject
    public HomeViewModel(AthathRepository repository, AuthRepository authRepository, FirebaseAuth firebaseAuth) {
        this.repository = repository;
        this.authRepository = authRepository;
        this.firebaseAuth = firebaseAuth;
        fetchCustomerProfile();
        checkCustomerLoginState();

    }
    public LiveData<Boolean> getIsCustomerLoggedIn() {
        return isCustomerLoggedIn;
    }
    // ✅ Check if a customer is logged in and has a profile
    public void checkCustomerLoginState() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            authRepository.getCustomerProfile().observeForever(result -> {
                if (result.getStatus() == Result.Status.SUCCESS) {
                    isCustomerLoggedIn.setValue(true);
                    customerProfileLiveData.setValue(result);
                } else {
                    isCustomerLoggedIn.setValue(false);
                }
            });
        } else {
            isCustomerLoggedIn.setValue(false);
        }
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
    public void uploadImage(File file) {
        repository.uploadImage(file).observeForever(uploadResult::setValue);
    }

    public LiveData<Result<List<ResponseModel.DetectedObject>>> getUploadResult() {
        return uploadResult;
    }
}
