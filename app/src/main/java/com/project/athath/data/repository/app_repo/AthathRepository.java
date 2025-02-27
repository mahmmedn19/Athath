package com.project.athath.data.repository.app_repo;

import androidx.lifecycle.LiveData;

import com.project.athath.data.model.CatalogItem;
import com.project.athath.data.model.Customer;
import com.project.athath.data.model.Product;
import com.project.athath.data.model.Vendor;
import com.project.athath.data.utils.Result;

import java.util.List;

public interface AthathRepository {
    LiveData<Result<List<Vendor>>> getAllVendors();

    LiveData<Result<List<Customer>>> getAllCustomers();

    LiveData<Result<String>> updateUserStatus(String userId, String status, String role);

    LiveData<Result<List<CatalogItem>>> getAllCatalogItems();

    LiveData<Result<String>> uploadCatalogItem(String base64Image);

    LiveData<Result<String>> deleteCatalogItem(String itemId);

    LiveData<Result<String>> updateCatalogItem(String itemId, String newBase64Image);

    LiveData<Result<CatalogItem>> getCatalogItemById(String itemId);

    LiveData<Result<String>> addProduct(Product product);

    LiveData<Result<String>> updateProduct(Product product);

    LiveData<Result<String>> deleteProduct(String productId);

    LiveData<Result<Product>> getProductById(String productId);

    LiveData<Result<List<Product>>> getAllProducts();

    LiveData<Result<Vendor>> getVendorById(String vendorId);

    LiveData<Result<List<Product>>> getFavoriteProducts();

    LiveData<Result<String>> addProductToFavorites(Product product);

    LiveData<Result<String>> removeProductFromFavorites(Product product);

    LiveData<Boolean> checkIfProductIsFavorite(String productId);

}