package com.project.athath.ui.admin_screen.room_catalog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.project.athath.data.model.CatalogItem;
import com.project.athath.data.repository.app_repo.AthathRepository;
import com.project.athath.data.utils.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageRoomCatalogViewModel extends ViewModel {
    private final AthathRepository repository;
    private final MutableLiveData<Result<List<CatalogItem>>> catalogItems = new MutableLiveData<>();

    @Inject
    public ManageRoomCatalogViewModel(AthathRepository repository) {
        this.repository = repository;
        fetchCatalogItems();
    }

    public LiveData<Result<List<CatalogItem>>> getCatalogItems() {
        return catalogItems;
    }

    public void fetchCatalogItems() {
        repository.getAllCatalogItems().observeForever(catalogItems::setValue);
    }

    public LiveData<Result<String>> deleteCatalogItem(String itemId) {
        return repository.deleteCatalogItem(itemId);
    }
}
