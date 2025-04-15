package com.project.athath.ui.catelog_screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.athath.data.model.CatalogItem;
import com.project.athath.data.model.ResponseModel;
import com.project.athath.data.repository.app_repo.AthathRepository;
import com.project.athath.data.utils.Result;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CatalogViewModel extends ViewModel {

    private final AthathRepository repository;
    private final MutableLiveData<Result<List<CatalogItem>>> catalogItemsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<List<ResponseModel.DetectedObject>>> uploadResult = new MutableLiveData<>();

    @Inject
    public CatalogViewModel(AthathRepository repository) {
        this.repository = repository;
    }

    // ✅ Expose LiveData for Catalog Items
    public LiveData<Result<List<CatalogItem>>> getCatalogItems() {
        return catalogItemsLiveData;
    }

    // ✅ Fetch Catalog Items from Repository
    public void fetchCatalogItems() {
        catalogItemsLiveData.setValue(Result.loading());

        repository.getAllCatalogItems().observeForever(catalogItemsLiveData::postValue);
    }
    public void uploadImage(File file) {
        repository.uploadImage(file).observeForever(uploadResult::setValue);
    }

    public LiveData<Result<List<ResponseModel.DetectedObject>>> getUploadResult() {
        return uploadResult;
    }
}
