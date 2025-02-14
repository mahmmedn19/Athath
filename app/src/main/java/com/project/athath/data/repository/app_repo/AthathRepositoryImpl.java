package com.project.athath.data.repository.app_repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.athath.data.model.Customer;
import com.project.athath.data.model.Vendor;
import com.project.athath.data.utils.Result;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AthathRepositoryImpl implements AthathRepository {

    private final FirebaseAuth auth;
    private final FirebaseFirestore db;

    @Inject
    public AthathRepositoryImpl(FirebaseAuth auth, FirebaseFirestore db) {
        this.auth = auth;
        this.db = db;
    }

    @Override
    public LiveData<Result<List<Vendor>>> getAllVendors() {
        MutableLiveData<Result<List<Vendor>>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        db.collection("Vendors").get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Vendor> vendorList = new ArrayList<>();
                    querySnapshot.forEach(doc -> vendorList.add(doc.toObject(Vendor.class)));
                    resultLiveData.setValue(Result.success(vendorList));
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error(e.getMessage())));

        return resultLiveData;
    }

    // get all customers
    @Override
    public LiveData<Result<List<Customer>>> getAllCustomers() {
        MutableLiveData<Result<List<Customer>>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        db.collection("Customers").get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Customer> customerList = new ArrayList<>();
                    querySnapshot.forEach(doc -> customerList.add(doc.toObject(Customer.class)));
                    resultLiveData.setValue(Result.success(customerList));
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error(e.getMessage())));

        return resultLiveData;
    }

    @Override
    public LiveData<Result<String>> updateUserStatus(String userId, String status, String role) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        db.collection(role).document(userId)
                .update("status", status)
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("User status updated")))
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error(e.getMessage())));

        return resultLiveData;
    }
}
