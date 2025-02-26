package com.project.athath.data.repository.auth;

import androidx.lifecycle.LiveData;
import com.project.athath.data.model.Customer;
import com.project.athath.data.model.Vendor;
import com.project.athath.data.utils.Result;
import java.util.List;

public interface AuthRepository {
    LiveData<Result<String>> loginUser(String email, String password, String expectedUserType);
    LiveData<Result<String>> registerVendor(Vendor vendor,String password);
    LiveData<Result<String>> registerCustomer(Customer customer,String password);
    LiveData<Result<String>> getUserType(String userId);
    LiveData<Result<String>> sendPasswordResetEmail(String email);
    LiveData<Result<Vendor>> getVendorProfile();
    LiveData<Result<String>> updateVendorProfile(Vendor vendor);
    LiveData<Result<Customer>> getCustomerProfile();
    LiveData<Result<String>> updateCustomerProfile(Customer customer);
    LiveData<Result<String>> changePassword(String currentPassword, String newPassword);
    LiveData<Result<String>> logoutUser();
}
