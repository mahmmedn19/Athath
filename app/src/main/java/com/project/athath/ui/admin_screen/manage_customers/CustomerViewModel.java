package com.project.athath.ui.admin_screen.manage_customers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.athath.data.model.Customer;
import com.project.athath.data.repository.app_repo.AthathRepository;
import com.project.athath.data.utils.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CustomerViewModel extends ViewModel {
    private final AthathRepository repository;
    private final MutableLiveData<Result<List<Customer>>> customers = new MutableLiveData<>();

    @Inject
    public CustomerViewModel(AthathRepository repository) {
        this.repository = repository;
        fetchCustomers();
    }

    public LiveData<Result<List<Customer>>> getCustomers() {
        return customers;
    }

    public void fetchCustomers() {
        repository.getAllCustomers().observeForever(customers::setValue);
    }

    public void blockCustomer(String customerId) {
        repository.updateUserStatus(customerId, "Blocked", "Customers").observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                fetchCustomers();  // Refresh list after updating status
            }
        });
    }
}
