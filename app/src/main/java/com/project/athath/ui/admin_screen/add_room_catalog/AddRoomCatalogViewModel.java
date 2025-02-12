package com.project.athath.ui.admin_screen.add_room_catalog;


import androidx.lifecycle.ViewModel;

import com.project.athath.data.model.Furniture;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddRoomCatalogViewModel extends ViewModel {

    private List<Furniture> furnitureList;

    @Inject
    public AddRoomCatalogViewModel() {
        furnitureList = new ArrayList<>();
    }

    public void addFurniture(Furniture furniture) {
        furnitureList.add(furniture);
    }

    public List<Furniture> getFurnitureList() {
        return furnitureList;
    }
}
