package com.project.athath.ui.admin_screen.add_room_catalog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.athath.data.model.CatalogItem;
import com.project.athath.data.repository.app_repo.AthathRepository;
import com.project.athath.data.utils.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddRoomCatalogViewModel extends ViewModel {

    private final AthathRepository repository;
    private final MutableLiveData<Result<String>> uploadResult = new MutableLiveData<>();

    @Inject
    public AddRoomCatalogViewModel(AthathRepository repository) {
        this.repository = repository;
    }

    // ✅ Expose Upload Result LiveData
    public LiveData<Result<String>> getUploadResult() {
        return uploadResult;
    }

    // ✅ Upload New Catalog Item
    public void uploadCatalogItem(String base64Image) {
        uploadResult.setValue(Result.loading());
        repository.uploadCatalogItem(base64Image).observeForever(uploadResult::postValue);
    }

    // ✅ Update Existing Catalog Item
    public void updateCatalogItem(String itemId, String newBase64Image) {
        uploadResult.setValue(Result.loading());
        repository.updateCatalogItem(itemId, newBase64Image).observeForever(uploadResult::postValue);
    }

    // ✅ Fetch a single CatalogItem by its ID from Firestore
    public LiveData<Result<CatalogItem>> getCatalogItemById(String itemId) {
        MutableLiveData<Result<CatalogItem>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        repository.getCatalogItemById(itemId).observeForever(resultLiveData::setValue);

        return resultLiveData;
    }
}
