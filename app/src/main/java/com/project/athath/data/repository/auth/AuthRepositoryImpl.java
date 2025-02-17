package com.project.athath.data.repository.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.athath.data.model.Customer;
import com.project.athath.data.model.Vendor;
import com.project.athath.data.utils.Result;

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

    // ✅ Login User with Status Check
    @Override
    public LiveData<Result<String>> loginUser(String email, String password, String expectedUserType) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    if (user != null) {
                        checkUserStatus(user.getUid(), expectedUserType, resultLiveData);
                    }
                })
                .addOnFailureListener(e -> {
                    String errorMessage = getFirebaseAuthErrorMessage(Objects.requireNonNull(e.getMessage()));
                    resultLiveData.setValue(Result.error(errorMessage));
                });

        return resultLiveData;
    }



    // ✅ Check User Type and Status
    private void checkUserStatus(String userId, String expectedUserType, MutableLiveData<Result<String>> resultLiveData) {
        db.collection(expectedUserType).document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                if ("Admins".equalsIgnoreCase(expectedUserType)) {
                    resultLiveData.setValue(Result.success("Admins"));
                } else {
                    String status = task.getResult().getString("status");
                    if ("Blocked".equalsIgnoreCase(status)) {
                        resultLiveData.setValue(Result.error("Your account is blocked. Contact support."));
                    } else {
                        resultLiveData.setValue(Result.success(expectedUserType));
                    }
                }
            } else {
                resultLiveData.setValue(Result.error("No user found for this role."));
            }
        });
    }


    // ✅ Register Vendor with Initial Status (Pending)
    @Override
    public LiveData<Result<String>> registerVendor(Vendor vendor) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        auth.createUserWithEmailAndPassword(vendor.getEmail(), vendor.getPassword())
                .addOnSuccessListener(authResult -> {
                    String userId = Objects.requireNonNull(authResult.getUser()).getUid();
                    vendor.setId(userId);
                    vendor.setStatus("Pending"); // Vendor starts as Pending

                    db.collection("Vendors").document(userId).set(vendor)
                            .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("Vendor Registered Successfully")))
                            .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Database error: " + getFirebaseAuthErrorMessage(e.getMessage()))));
                })
                .addOnFailureListener(e -> {
                    String errorMessage = getFirebaseAuthErrorMessage(e.getMessage());
                    resultLiveData.setValue(Result.error(errorMessage));
                });

        return resultLiveData;
    }


    // ✅ Register Customer with Initial Status (Pending)
    @Override
    public LiveData<Result<String>> registerCustomer(Customer customer) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        auth.createUserWithEmailAndPassword(customer.getEmail(), customer.getPassword())
                .addOnSuccessListener(authResult -> {
                    String userId = Objects.requireNonNull(authResult.getUser()).getUid();
                    customer.setId(userId);
                    customer.setStatus("Pending"); // Customer starts as Pending
                    db.collection("Customers").document(userId).set(customer)
                            .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("Customer Registered Successfully")))
                            .addOnFailureListener(e -> resultLiveData.setValue(Result.error(getFirebaseAuthErrorMessage(e.getMessage()))));
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error(getFirebaseAuthErrorMessage(e.getMessage()))));

        return resultLiveData;
    }
    @Override
    public LiveData<Result<String>> sendPasswordResetEmail(String email) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("Reset link sent to your email.")))
                .addOnFailureListener(e -> {
                    String errorMessage = getFirebaseAuthErrorMessage(Objects.requireNonNull(e.getMessage()));
                    resultLiveData.setValue(Result.error(errorMessage));
                });
        return resultLiveData;
    }

    @Override
    public LiveData<Result<String>> getUserType(String userId) {
        return null;
    }
    private String getFirebaseAuthErrorMessage(String errorCode) {
        if (errorCode.contains("There is no user record")) {
            return "No account found with this email.";
        } else if (errorCode.contains("password is invalid")) {
            return "Incorrect password. Try again.";
        } else if (errorCode.contains("badly formatted")) {
            return "Invalid email format. Check your email.";
        } else if (errorCode.contains("blocked")) {
            return "Your account is blocked. Contact support.";
        } else {
            return "Please check your data.";
        }
    }

}
