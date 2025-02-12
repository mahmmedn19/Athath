package com.project.athath.data.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.athath.data.model.Customer;
import com.project.athath.data.model.Vendor;
import com.project.athath.data.utils.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthRepositoryImpl implements AuthRepository {
    private final FirebaseAuth auth;
    private final FirebaseFirestore db;

    @Inject
    public AuthRepositoryImpl(FirebaseAuth auth, FirebaseFirestore db) {
        this.auth = auth;
        this.db = db;
    }

    // ✅ Login User and Determine User Type
    @Override
    public LiveData<Result<String>> loginUser(String email, String password) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    if (user != null) {
                        getUserType(user.getUid()).observeForever(resultLiveData::setValue);
                    }
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error(e.getMessage())));

        return resultLiveData;
    }

    // ✅ Fetch User Type Based on UID
    @Override
    public LiveData<Result<String>> getUserType(String userId) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        db.collection("Admins").document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                resultLiveData.setValue(Result.success("Admin"));
            } else {
                db.collection("Vendors").document(userId).get().addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful() && task2.getResult().exists()) {
                        resultLiveData.setValue(Result.success("Vendor"));
                    } else {
                        db.collection("Customers").document(userId).get().addOnCompleteListener(task3 -> {
                            if (task3.isSuccessful() && task3.getResult().exists()) {
                                resultLiveData.setValue(Result.success("Customer"));
                            } else {
                                resultLiveData.setValue(Result.error("User type not found"));
                            }
                        });
                    }
                });
            }
        });

        return resultLiveData;
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

    // ✅ Register Vendor
    @Override
    public LiveData<Result<String>> registerVendor(Vendor vendor) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        auth.createUserWithEmailAndPassword(vendor.getEmail(), vendor.getPassword())
                .addOnSuccessListener(authResult -> {
                    String userId = Objects.requireNonNull(authResult.getUser()).getUid();
                    vendor.setId(userId);
                    db.collection("Vendors").document(userId).set(vendor)
                            .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("Vendor Registered Successfully")))
                            .addOnFailureListener(e -> resultLiveData.setValue(Result.error(e.getMessage())));
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error(e.getMessage())));

        return resultLiveData;
    }

    // ✅ Register Customer
    @Override
    public LiveData<Result<String>> registerCustomer(Customer customer) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        auth.createUserWithEmailAndPassword(customer.getEmail(), customer.getPassword())
                .addOnSuccessListener(authResult -> {
                    String userId = Objects.requireNonNull(authResult.getUser()).getUid();
                    customer.setId(userId);
                    db.collection("Customers").document(userId).set(customer)
                            .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("Customer Registered Successfully")))
                            .addOnFailureListener(e -> resultLiveData.setValue(Result.error(e.getMessage())));
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error(e.getMessage())));

        return resultLiveData;
    }
}
