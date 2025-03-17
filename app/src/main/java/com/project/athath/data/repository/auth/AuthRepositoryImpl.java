package com.project.athath.data.repository.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
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
                    String errorMessage = getFirebaseAuthErrorMessage(e);
                    resultLiveData.setValue(Result.error(errorMessage));
                });

        return resultLiveData;
    }


    // ✅ Check User Type and Status
    private void checkUserStatus(String userId, String expectedUserType, MutableLiveData<Result<String>> resultLiveData) {
        resultLiveData.setValue(Result.loading());
        db.collection(expectedUserType).document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                if ("Admins".equalsIgnoreCase(expectedUserType)) {
                    resultLiveData.setValue(Result.success("Admins"));
                } else {
                        resultLiveData.setValue(Result.success(expectedUserType));
                }
            } else {
                resultLiveData.setValue(Result.error("No user found for this role."));
            }
        });
    }

    @Override
    public LiveData<Result<String>> registerVendor(Vendor vendor, String password) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        auth.createUserWithEmailAndPassword(vendor.getEmail(), password)
                .addOnSuccessListener(authResult -> {
                    String userId = Objects.requireNonNull(authResult.getUser()).getUid();
                    vendor.setId(userId);
                    vendor.setStatus("Pending");

                    db.collection("Vendors").document(userId).set(vendor)
                            .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("Vendor Registered Successfully")))
                            .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Database error: " + e.getMessage())));
                })
                .addOnFailureListener(e -> {
                    String errorMessage = getFirebaseAuthErrorMessage(e);
                    resultLiveData.setValue(Result.error(errorMessage));
                });

        return resultLiveData;
    }


    // ✅ Register Customer with Initial Status (Pending)
    @Override
    public LiveData<Result<String>> registerCustomer(Customer customer, String password) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        auth.createUserWithEmailAndPassword(customer.getEmail(), password)
                .addOnSuccessListener(authResult -> {
                    String userId = Objects.requireNonNull(authResult.getUser()).getUid();
                    customer.setId(userId);
                    customer.setStatus("Pending");

                    db.collection("Customers").document(userId).set(customer)
                            .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("Customer Registered Successfully")))
                            .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Database error: " + e.getMessage())));
                })
                .addOnFailureListener(e -> {
                    String errorMessage = getFirebaseAuthErrorMessage(e);
                    resultLiveData.setValue(Result.error(errorMessage));
                });

        return resultLiveData;
    }

    @Override
    public LiveData<Result<String>> sendPasswordResetEmail(String email) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("Reset link sent to your email.")))
                .addOnFailureListener(e -> {
                    String errorMessage = getFirebaseAuthErrorMessage(e);
                    resultLiveData.setValue(Result.error(errorMessage));
                });
        return resultLiveData;
    }

    @Override
    public LiveData<Result<Vendor>> getVendorProfile() {
        MutableLiveData<Result<Vendor>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
            resultLiveData.setValue(Result.error("User not logged in"));
            return resultLiveData;
        }

        db.collection("Vendors").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Vendor vendor = documentSnapshot.toObject(Vendor.class);
                    if (vendor != null) {
                        resultLiveData.setValue(Result.success(vendor));
                    } else {
                        resultLiveData.setValue(Result.error("Vendor not found"));
                    }
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to fetch vendor data")));

        return resultLiveData;
    }

    public LiveData<Result<String>> updateVendorProfile(Vendor vendor) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
            resultLiveData.setValue(Result.error("User not logged in"));
            return resultLiveData;
        }

        DocumentReference vendorRef = db.collection("Vendors").document(userId);
        vendorRef.set(vendor)
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("Profile updated successfully!")))
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to update profile")));

        return resultLiveData;
    }

    @Override
    public LiveData<Result<Customer>> getCustomerProfile() {
        MutableLiveData<Result<Customer>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            resultLiveData.setValue(Result.error("User not logged in."));
            return resultLiveData;
        }
        String userId = currentUser.getUid();

        db.collection("Customers").document(userId).get()
                .addOnSuccessListener(documentSnapshot  -> {
                    Customer customer = documentSnapshot.toObject(Customer.class);
                    if (customer != null) {
                        resultLiveData.setValue(Result.success(customer));
                    } else {
                        resultLiveData.setValue(Result.error("Customer not found."));
                    }
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to fetch profile")));

        return resultLiveData;
    }

    @Override
    public LiveData<Result<String>> updateCustomerProfile(Customer customer) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        String userId = auth.getCurrentUser().getUid();
        db.collection("Customers").document(userId).set(customer)
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("Profile updated successfully")))
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to update profile")));

        return resultLiveData;
    }

    @Override
    public LiveData<Result<String>> changePassword(String currentPassword, String newPassword) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());
        FirebaseUser user = auth.getCurrentUser();

        if (user == null || user.getEmail() == null) {
            resultLiveData.setValue(Result.error("User not logged in."));
            return resultLiveData;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        resultLiveData.setValue(Result.success("Password updated successfully!"));
                    } else {
                        resultLiveData.setValue(Result.error("Failed to update password: " + updateTask.getException().getMessage()));
                    }
                });
            } else {
                resultLiveData.setValue(Result.error("Re-authentication failed: Incorrect current password."));
            }
        });

        return resultLiveData;
    }



    @Override
    public LiveData<Result<String>> getUserType(String userId) {
        return null;
    }
    @Override
    public LiveData<Result<String>> logoutUser() {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        auth.signOut();
        resultLiveData.setValue(Result.success("Logged out successfully"));
        return resultLiveData;
    }
    @Override
    public boolean isUserLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    private String getFirebaseAuthErrorMessage(Exception e) {
        if (e instanceof FirebaseAuthUserCollisionException) {
            return "This email is already registered. Please use a different email.";
        } else if (e instanceof FirebaseAuthWeakPasswordException) {
            return "Password should be at least 6 characters.";
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            return "Invalid email or password.";
        } else {
            return "Registration failed: " + e.getMessage();
        }
    }


}
