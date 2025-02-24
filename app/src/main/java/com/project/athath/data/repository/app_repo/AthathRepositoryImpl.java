package com.project.athath.data.repository.app_repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.athath.data.model.CatalogItem;
import com.project.athath.data.model.Customer;
import com.project.athath.data.model.Product;
import com.project.athath.data.model.Vendor;
import com.project.athath.data.utils.Result;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AthathRepositoryImpl implements AthathRepository {

    private final FirebaseAuth auth;
    private final FirebaseFirestore db;
    private static final String COLLECTION_NAME_CATALOG = "CatalogItems";
    private final String COLLECTION_NAME_PRODUCTS = "products";


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
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Vendor vendor = doc.toObject(Vendor.class);
                        if (vendor != null) {
                            vendor.setId(doc.getId());  // ✅ Assign Firestore document ID manually
                            vendorList.add(vendor);
                        }
                    }
                    resultLiveData.setValue(Result.success(vendorList));
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error(e.getMessage())));

        return resultLiveData;
    }

    @Override
    public LiveData<Result<List<Customer>>> getAllCustomers() {
        MutableLiveData<Result<List<Customer>>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        db.collection("Customers").get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Customer> customerList = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Customer customer = doc.toObject(Customer.class);
                        if (customer != null) {
                            customer.setId(doc.getId());  // ✅ Assign Firestore document ID manually
                            customerList.add(customer);
                        }
                    }
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

    // ✅ Upload a Catalog Item with Auto-ID
    @Override
    public LiveData<Result<String>> uploadCatalogItem(String base64Image) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        CatalogItem newItem = new CatalogItem(base64Image);

        db.collection(COLLECTION_NAME_CATALOG)
                .add(newItem)  // Firestore will generate an auto ID
                .addOnSuccessListener(documentReference -> {
                    String generatedId = documentReference.getId();

                    // Update document with the generated ID
                    db.collection(COLLECTION_NAME_CATALOG).document(generatedId)
                            .update("id", generatedId)  // Store the ID in Firestore
                            .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success(generatedId)))
                            .addOnFailureListener(e -> resultLiveData.setValue(Result.error(e.getMessage())));
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error(e.getMessage())));

        return resultLiveData;
    }


    // ✅ Retrieve All Catalog Items
    @Override
    public LiveData<Result<List<CatalogItem>>> getAllCatalogItems() {
        MutableLiveData<Result<List<CatalogItem>>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        db.collection(COLLECTION_NAME_CATALOG).get()
                .addOnSuccessListener(querySnapshot -> {
                    List<CatalogItem> catalogList = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        CatalogItem item = doc.toObject(CatalogItem.class);
                        if (item != null) {
                            item.setId(doc.getId());  // Assign Firestore document ID manually
                            catalogList.add(item);
                        }
                    }
                    resultLiveData.setValue(Result.success(catalogList));
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error(e.getMessage())));

        return resultLiveData;
    }


    // ✅ Update Catalog Item Image
    @Override
    public LiveData<Result<String>> updateCatalogItem(String itemId, String newBase64Image) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        db.collection(COLLECTION_NAME_CATALOG).document(itemId)
                .update("imageRes", newBase64Image)
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("Item updated successfully")))
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error(e.getMessage())));

        return resultLiveData;
    }

    // ✅ Delete a Catalog Item
    @Override
    public LiveData<Result<String>> deleteCatalogItem(String itemId) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        db.collection(COLLECTION_NAME_CATALOG).document(itemId)
                .delete()
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("Item deleted successfully")))
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error(e.getMessage())));

        return resultLiveData;
    }

    @Override
    public LiveData<Result<CatalogItem>> getCatalogItemById(String itemId) {
        MutableLiveData<Result<CatalogItem>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        db.collection("CatalogItems").document(itemId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    CatalogItem item = documentSnapshot.toObject(CatalogItem.class);
                    if (item != null) {
                        item.setId(documentSnapshot.getId()); // Ensure ID is assigned
                        resultLiveData.setValue(Result.success(item));
                    } else {
                        resultLiveData.setValue(Result.error("Item not found"));
                    }
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error(e.getMessage())));

        return resultLiveData;
    }

    public LiveData<Result<String>> addProduct(Product product) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        String currentUserId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (currentUserId == null) {
            resultLiveData.setValue(Result.error("User not authenticated!"));
            return resultLiveData;
        }

        product.setStoreId(currentUserId);

        db.collection(COLLECTION_NAME_PRODUCTS).add(product)
                .addOnSuccessListener(documentReference -> {
                    product.setId(documentReference.getId());
                    db.collection(COLLECTION_NAME_PRODUCTS).document(product.getId()).set(product)
                            .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("Product added successfully!")))
                            .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to update product with ID.")));
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to add product.")));

        return resultLiveData;
    }

    @Override
    public LiveData<Result<String>> updateProduct(Product product) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        db.collection(COLLECTION_NAME_PRODUCTS).document(product.getId()).set(product)
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("Product updated successfully!")))
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to update product.")));

        return resultLiveData;
    }

    @Override
    public LiveData<Result<Product>> getProductById(String productId) {
        MutableLiveData<Result<Product>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        db.collection(COLLECTION_NAME_PRODUCTS).document(productId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Product product = documentSnapshot.toObject(Product.class);
                    if (product != null) {
                        resultLiveData.setValue(Result.success(product));
                    } else {
                        resultLiveData.setValue(Result.error("Product not found."));
                    }
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to fetch product.")));

        return resultLiveData;
    }

    @Override
    public LiveData<Result<String>> deleteProduct(String productId) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        db.collection(COLLECTION_NAME_PRODUCTS).document(productId).delete()
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("Product deleted successfully!")))
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to delete product")));

        return resultLiveData;
    }


    @Override
    public LiveData<Result<List<Product>>> getAllProducts() {
        MutableLiveData<Result<List<Product>>> resultLiveData = new MutableLiveData<>();

        db.collection(COLLECTION_NAME_PRODUCTS).get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Product> productList = querySnapshot.toObjects(Product.class);
                    resultLiveData.setValue(Result.success(productList));
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to fetch products")));

        return resultLiveData;
    }

}
