package com.project.athath.data.repository.app_repo;

import androidx.lifecycle.LiveData;

import com.project.athath.data.model.Customer;
import com.project.athath.data.model.Vendor;
import com.project.athath.data.utils.Result;

import java.util.List;

public interface AthathRepository {
    LiveData<Result<List<Vendor>>> getAllVendors();
    LiveData<Result<List<Customer>>> getAllCustomers();
    LiveData<Result<String>> updateUserStatus(String userId, String status , String role);
}