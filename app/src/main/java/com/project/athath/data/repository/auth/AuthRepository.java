package com.project.athath.data.repository.auth;

import androidx.lifecycle.LiveData;
import com.project.athath.data.model.Customer;
import com.project.athath.data.model.Vendor;
import com.project.athath.data.utils.Result;
import java.util.List;

public interface AuthRepository {
    LiveData<Result<String>> loginUser(String email, String password);
    LiveData<Result<String>> registerVendor(Vendor vendor);
    LiveData<Result<String>> registerCustomer(Customer customer);
    LiveData<Result<String>> getUserType(String userId);
    LiveData<Result<String>> sendPasswordResetEmail(String email);
}
