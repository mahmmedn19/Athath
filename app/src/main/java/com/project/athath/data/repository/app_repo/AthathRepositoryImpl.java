package com.project.athath.data.repository.app_repo;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.athath.data.model.AiRecommendationResponse;
import com.project.athath.data.model.CatalogItem;
import com.project.athath.data.model.Customer;
import com.project.athath.data.model.NextRecommendationResponse;
import com.project.athath.data.model.Product;
import com.project.athath.data.model.ResponseModel;
import com.project.athath.data.model.Vendor;
import com.project.athath.data.network.ApiService;
import com.project.athath.data.utils.Result;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AthathRepositoryImpl implements AthathRepository {

    private final FirebaseAuth auth;
    private final FirebaseFirestore db;
    private static final String COLLECTION_NAME_CATALOG = "CatalogItems";
    private final String COLLECTION_NAME_PRODUCTS = "products";
    private static final String COLLECTION_NAME_USERS = "Customers";
    private final ApiService apiService;
    private static AthathRepositoryImpl instance;
    private static Context context;

    @Inject
    public AthathRepositoryImpl(Context context, FirebaseAuth auth, FirebaseFirestore db, ApiService apiService) {
        this.auth = auth;
        this.db = db;
        this.context = context;
        this.apiService = apiService;
    }

    public static synchronized AthathRepositoryImpl getInstance(ApiService apiService) {
        if (instance == null) {
            instance = new AthathRepositoryImpl(
                    context,
                    FirebaseAuth.getInstance(),
                    FirebaseFirestore.getInstance(),
                    apiService
            );
        }
        return instance;
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

    @Override
    public LiveData<Result<String>> addProduct(Product product) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        String currentUserId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        if (currentUserId == null) {
            resultLiveData.setValue(Result.error("User not authenticated!"));
            return resultLiveData;
        }

        product.setStoreId(currentUserId);

        // 🔑 Generate ID BEFORE adding to Firestore
        String productId = db.collection(COLLECTION_NAME_PRODUCTS).document().getId();
        product.setId(productId);

        db.collection(COLLECTION_NAME_PRODUCTS).document(productId).set(product)
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success(productId)))
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to add product: " + e.getMessage())));

        return resultLiveData;
    }


    public LiveData<Result<String>> updateProduct(Product product) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        if (product.getStoreId() == null || product.getStoreId().isEmpty()) {
            String currentUserId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
            if (currentUserId != null) {
                product.setStoreId(currentUserId);  // Reassign storeId
            }
        }

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
        resultLiveData.setValue(Result.loading());

        db.collection(COLLECTION_NAME_PRODUCTS).get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Product> productList = querySnapshot.toObjects(Product.class);
                    resultLiveData.setValue(Result.success(productList));
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to fetch products")));

        return resultLiveData;
    }

    @Override
    public LiveData<Result<Vendor>> getVendorById(String vendorId) {
        MutableLiveData<Result<Vendor>> vendorLiveData = new MutableLiveData<>();
        vendorLiveData.setValue(Result.loading());

        db.collection("Vendors").document(vendorId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Vendor vendor = documentSnapshot.toObject(Vendor.class);
                        vendorLiveData.setValue(Result.success(vendor));
                    } else {
                        vendorLiveData.setValue(Result.error("Vendor not found."));
                    }
                })
                .addOnFailureListener(e -> vendorLiveData.setValue(Result.error(e.getMessage())));

        return vendorLiveData;
    }

    @Override
    public LiveData<Result<List<Product>>> getFavoriteProducts() {
        MutableLiveData<Result<List<Product>>> resultLiveData = new MutableLiveData<>();
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
            resultLiveData.setValue(Result.error("User not authenticated!"));
            return resultLiveData;
        }

        resultLiveData.setValue(Result.loading());

        db.collection(COLLECTION_NAME_USERS).document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Customer customer = documentSnapshot.toObject(Customer.class);
                        if (customer != null && customer.getFavoriteProductIds() != null) {
                            List<String> productIds = customer.getFavoriteProductIds();
                            fetchProductsByIds(productIds, resultLiveData);
                        } else {
                            resultLiveData.setValue(Result.success(new ArrayList<>()));
                        }
                    } else {
                        resultLiveData.setValue(Result.success(new ArrayList<>()));
                    }
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to fetch favorites: " + e.getMessage())));

        return resultLiveData;
    }

    // ✅ Helper method to fetch product details using IDs
    private void fetchProductsByIds(List<String> productIds, MutableLiveData<Result<List<Product>>> resultLiveData) {
        if (productIds.isEmpty()) {
            resultLiveData.setValue(Result.success(new ArrayList<>()));
            return;
        }

        db.collection(COLLECTION_NAME_PRODUCTS)
                .whereIn("id", productIds)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Product> products = new ArrayList<>();
                    for (var document : querySnapshot.getDocuments()) {
                        Product product = document.toObject(Product.class);
                        if (product != null) products.add(product);
                    }
                    resultLiveData.setValue(Result.success(products));
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to fetch product details: " + e.getMessage())));
    }

    @Override
    public LiveData<Result<String>> addProductToFavorites(Product product) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
            resultLiveData.setValue(Result.error("User not authenticated!"));
            return resultLiveData;
        }

        db.collection(COLLECTION_NAME_USERS).document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Customer customer = documentSnapshot.toObject(Customer.class);
                        if (customer != null) {
                            List<String> favoriteProductIds = customer.getFavoriteProductIds();
                            if (!favoriteProductIds.contains(product.getId())) {
                                favoriteProductIds.add(product.getId());
                                db.collection(COLLECTION_NAME_USERS).document(userId)
                                        .update("favoriteProductIds", favoriteProductIds)
                                        .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("Product added to favorites.")))
                                        .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to add to favorites: " + e.getMessage())));
                            } else {
                                resultLiveData.setValue(Result.success("Product already in favorites."));
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to fetch user data: " + e.getMessage())));

        return resultLiveData;
    }

    // ✅ Remove Product from Favorites
    @Override
    public LiveData<Result<String>> removeProductFromFavorites(Product product) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
            resultLiveData.setValue(Result.error("User not authenticated!"));
            return resultLiveData;
        }

        db.collection(COLLECTION_NAME_USERS).document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Customer customer = documentSnapshot.toObject(Customer.class);
                        if (customer != null) {
                            List<String> favoriteProductIds = customer.getFavoriteProductIds();
                            if (favoriteProductIds.contains(product.getId())) {
                                favoriteProductIds.remove(product.getId());
                                db.collection(COLLECTION_NAME_USERS).document(userId)
                                        .update("favoriteProductIds", favoriteProductIds)
                                        .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("Product removed from favorites.")))
                                        .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to remove favorite: " + e.getMessage())));
                            } else {
                                resultLiveData.setValue(Result.success("Product was not in favorites."));
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to fetch user data: " + e.getMessage())));

        return resultLiveData;
    }

    @Override
    public LiveData<Boolean> checkIfProductIsFavorite(String productId) {
        MutableLiveData<Boolean> isFavoriteLiveData = new MutableLiveData<>();
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
            isFavoriteLiveData.setValue(false);
            return isFavoriteLiveData;
        }

        db.collection(COLLECTION_NAME_USERS).document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Customer customer = documentSnapshot.toObject(Customer.class);
                        isFavoriteLiveData.setValue(customer != null && customer.getFavoriteProductIds().contains(productId));
                    } else {
                        isFavoriteLiveData.setValue(false);
                    }
                })
                .addOnFailureListener(e -> isFavoriteLiveData.setValue(false));

        return isFavoriteLiveData;
    }


    @Override
    public LiveData<Result<List<ResponseModel.DetectedObject>>> uploadImage(File file) {
        MutableLiveData<Result<List<ResponseModel.DetectedObject>>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        // ✅ Convert File to RequestBody
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // ✅ Call the API
        apiService.uploadImage(body).enqueue(new retrofit2.Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, retrofit2.Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ResponseModel.DetectedObject> objects = response.body().getObjects();
                    resultLiveData.setValue(Result.success(objects)); // ✅ Return the detected objects list
                } else {
                    resultLiveData.setValue(Result.error("Failed to upload image."));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                resultLiveData.setValue(Result.error("Error uploading image: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    @Override
    public LiveData<Result<AiRecommendationResponse>> getRecommendations(Map<String, String> userPreferences) {
        MutableLiveData<Result<AiRecommendationResponse>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        apiService.getRecommendations(userPreferences).enqueue(new Callback<AiRecommendationResponse>() {
            @Override
            public void onResponse(Call<AiRecommendationResponse> call, Response<AiRecommendationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.setValue(Result.success(response.body()));
                } else {
                    resultLiveData.setValue(Result.error("Failed to fetch recommendations"));
                }
            }

            @Override
            public void onFailure(Call<AiRecommendationResponse> call, Throwable t) {
                resultLiveData.setValue(Result.error("Error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }

    @Override
    public LiveData<Result<NextRecommendationResponse>> getNextRecommendation() {
        MutableLiveData<Result<NextRecommendationResponse>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        apiService.getNextRecommendation().enqueue(new Callback<NextRecommendationResponse>() {
            @Override
            public void onResponse(Call<NextRecommendationResponse> call, Response<NextRecommendationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.setValue(Result.success(response.body()));
                } else {
                    resultLiveData.setValue(Result.error("No more recommendations available"));
                }
            }

            @Override
            public void onFailure(Call<NextRecommendationResponse> call, Throwable t) {
                resultLiveData.setValue(Result.error("Error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }
}
