package com.project.athath.data.repository.app_repo;

import static com.project.athath.data.utils.ImageUtils.encodeImageToBase64;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.athath.R;
import com.project.athath.data.model.AiRecommendationResponse;
import com.project.athath.data.model.CatalogItem;
import com.project.athath.data.model.Customer;
import com.project.athath.data.model.NextRecommendationResponse;
import com.project.athath.data.model.Product;
import com.project.athath.data.model.ResponseModel;
import com.project.athath.data.model.Vendor;
import com.project.athath.data.network.ApiService;
import com.project.athath.data.utils.Result;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private  static Context context;

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
    public LiveData<Result<String>> addAILink(String link) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        result.setValue(Result.loading());

        db.collection("AiLink").document("single_ai_link") // Use a fixed document ID
                .set(Collections.singletonMap("link", link)) // Store as a key-value pair
                .addOnSuccessListener(aVoid -> result.setValue(Result.success("AI link updated successfully.")))
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to update AI link: " + e.getMessage())));

        return result;
    }

    @Override
    public LiveData<Result<String>> getSingleAILink() {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        result.setValue(Result.loading());

        db.collection("AiLink").document("single_ai_link").get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("link")) {
                        String link = documentSnapshot.getString("link");
                        result.setValue(Result.success(link));
                    } else {
                        result.setValue(Result.error("No AI link found."));
                    }
                })
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to fetch AI link: " + e.getMessage())));

        return result;
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
    private static final String STORE_ID = "GZKZyt21BFeuAewSIJjPgzM3Awn2"; // 🔥 Fixed Store ID
    // ✅ Convert Image to Base64
    private String encodeImageToBase64(int imageResource) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageResource);
        if (bitmap == null) {
            Log.e("Base64", "Failed to decode image");
            return null;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream); // Compressing image
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    public void addFakeProductsToFirestore() {
        List<Product> fakeProducts = Arrays.asList(
                // 🔵 Chair - 5 Items
                new Product(UUID.randomUUID().toString(), "Modern Chair", "Chair",
                        "A stylish and ergonomic modern chair that blends comfort and contemporary design. " +
                                "Perfect for offices, lounges, or reading corners.",
                        120.0, "Blue", "Modern", "Living Room", 50, 50,
                        encodeImageToBase64(R.drawable.chair1), STORE_ID),

                new Product(UUID.randomUUID().toString(), "Wooden Chair", "Chair",
                        "A handcrafted wooden chair with a classic finish. This chair provides " +
                                "sturdy support and timeless aesthetics for any dining space.",
                        95.0, "Brown", "Classic", "Dining Room", 55, 55,
                        encodeImageToBase64(R.drawable.chair2), STORE_ID),

                new Product(UUID.randomUUID().toString(), "Office Chair", "Chair",
                        "Designed for maximum back support, this office chair features " +
                                "adjustable armrests, lumbar support, and a breathable mesh backrest.",
                        150.0, "Black", "Contemporary", "Office", 60, 60,
                        encodeImageToBase64(R.drawable.chair3), STORE_ID),

                new Product(UUID.randomUUID().toString(), "Gaming Chair", "Chair",
                        "An ergonomic gaming chair with adjustable height, headrest, and lumbar pillow. " +
                                "Built for long hours of comfort during gaming sessions.",
                        200.0, "Red", "Modern", "Gaming Room", 70, 70,
                        encodeImageToBase64(R.drawable.chair4), STORE_ID),

                new Product(UUID.randomUUID().toString(), "Rocking Chair", "Chair",
                        "A cozy and relaxing wooden rocking chair, ideal for enjoying a good book or " +
                                "rocking a baby to sleep in a nursery.",
                        180.0, "White", "Rustic", "Patio", 65, 65,
                        encodeImageToBase64(R.drawable.chair5), STORE_ID),

                // 🟡 Lamp - 5 Items
                new Product(UUID.randomUUID().toString(), "Table Lamp", "Lamp",
                        "An elegant bedside table lamp with soft lighting, " +
                                "ideal for night reading and creating a cozy atmosphere.",
                        50.0, "Gold", "Modern", "Bedroom", 20, 20,
                        encodeImageToBase64(R.drawable.lamp1), STORE_ID),

                new Product(UUID.randomUUID().toString(), "Floor Lamp", "Lamp",
                        "A sleek and tall floor lamp with an adjustable head, providing " +
                                "ample lighting for living rooms and reading corners.",
                        85.0, "Black", "Minimalist", "Living Room", 30, 30,
                        encodeImageToBase64(R.drawable.lamp2), STORE_ID),

                new Product(UUID.randomUUID().toString(), "LED Desk Lamp", "Lamp",
                        "A compact LED desk lamp with adjustable brightness levels. " +
                                "Perfect for office desks, study areas, or bedside tables.",
                        40.0, "Silver", "Tech", "Office", 25, 25,
                        encodeImageToBase64(R.drawable.lamp3), STORE_ID),

                new Product(UUID.randomUUID().toString(), "Antique Lamp", "Lamp",
                        "A vintage-style lamp featuring a warm glow and intricate detailing. " +
                                "Adds a touch of nostalgia to any room decor.",
                        90.0, "Bronze", "Vintage", "Study Room", 22, 22,
                        encodeImageToBase64(R.drawable.lamp4), STORE_ID),

                new Product(UUID.randomUUID().toString(), "Bedside Lamp", "Lamp",
                        "A small and stylish bedside lamp with a soft fabric shade, " +
                                "creating a relaxing ambiance for night-time use.",
                        35.0, "White", "Scandinavian", "Bedroom", 18, 18,
                        encodeImageToBase64(R.drawable.lamp5), STORE_ID),

                // 🔴 Sofa - 5 Items
                new Product(UUID.randomUUID().toString(), "Modern Sofa", "Sofa",
                        "A spacious and elegant modern sofa with premium upholstery. " +
                                "Designed for comfort and sophistication in any living space.",
                        500.0, "Gray", "Modern", "Living Room", 200, 90,
                        encodeImageToBase64(R.drawable.sofa1), STORE_ID),

                new Product(UUID.randomUUID().toString(), "Leather Sofa", "Sofa",
                        "A luxurious leather sofa with a timeless design. " +
                                "Perfect for lounges and high-end living rooms.",
                        800.0, "Brown", "Classic", "Lounge", 210, 95,
                        encodeImageToBase64(R.drawable.sofa2), STORE_ID),

                new Product(UUID.randomUUID().toString(), "Convertible Sofa", "Sofa",
                        "A multi-functional sofa that easily converts into a bed. " +
                                "Ideal for small apartments or guest rooms.",
                        450.0, "Blue", "Functional", "Guest Room", 190, 85,
                        encodeImageToBase64(R.drawable.sofa3), STORE_ID),

                new Product(UUID.randomUUID().toString(), "L-Shaped Sofa", "Sofa",
                        "A spacious L-shaped sofa, providing both comfort and style. " +
                                "Great for large families and entertaining guests.",
                        750.0, "Beige", "Contemporary", "Living Room", 250, 100,
                        encodeImageToBase64(R.drawable.sofa4), STORE_ID),

                new Product(UUID.randomUUID().toString(), "Recliner Sofa", "Sofa",
                        "A luxurious recliner sofa with built-in cup holders and a " +
                                "mechanized recline function for ultimate relaxation.",
                        900.0, "Black", "Luxury", "Home Theater", 220, 110,
                        encodeImageToBase64(R.drawable.sofa5), STORE_ID),

                // 🟢 Table - 5 Items
                new Product(UUID.randomUUID().toString(), "Dining Table", "Table",
                        "A large wooden dining table designed for family gatherings. " +
                                "Seats up to six people comfortably.",
                        300.0, "Brown", "Classic", "Dining Room", 150, 80,
                        encodeImageToBase64(R.drawable.table1), STORE_ID),

                new Product(UUID.randomUUID().toString(), "Coffee Table", "Table",
                        "A modern coffee table with a minimalist design, " +
                                "perfect for contemporary living rooms.",
                        120.0, "White", "Minimalist", "Living Room", 90, 50,
                        encodeImageToBase64(R.drawable.table2), STORE_ID),

                new Product(UUID.randomUUID().toString(), "Office Desk", "Table",
                        "A professional-grade office desk with spacious compartments " +
                                "for storage and organization.",
                        250.0, "Black", "Professional", "Office", 160, 80,
                        encodeImageToBase64(R.drawable.table3), STORE_ID),

                new Product(UUID.randomUUID().toString(), "Side Table", "Table",
                        "A stylish and compact side table, ideal for placing next to " +
                                "sofas or beds for convenience.",
                        75.0, "Beige", "Scandinavian", "Bedroom", 50, 50,
                        encodeImageToBase64(R.drawable.table4), STORE_ID),

                new Product(UUID.randomUUID().toString(), "Outdoor Table", "Table",
                        "A weather-resistant outdoor table, perfect for patios and garden setups.",
                        200.0, "Gray", "Rustic", "Patio", 140, 70,
                        encodeImageToBase64(R.drawable.table5), STORE_ID)
        );

            for (Product product : fakeProducts) {
                if (product.getId() == null || product.getId().isEmpty()) {
                    product.setId(UUID.randomUUID().toString()); // Assign a new random ID
                }

                db.collection("products").document(product.getId()) // Use the generated ID
                        .set(product)
                        .addOnSuccessListener(aVoid -> Log.d("Firestore", "Product added: " + product.getId()))
                        .addOnFailureListener(e -> Log.e("Firestore", "Error adding product", e));
            }
    }
}
