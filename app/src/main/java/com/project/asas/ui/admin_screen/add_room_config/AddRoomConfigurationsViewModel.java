package com.project.asas.ui.admin_screen.add_room_config;


import androidx.lifecycle.ViewModel;

import com.project.asas.model.Furniture;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddRoomConfigurationsViewModel extends ViewModel {

    private List<Furniture> furnitureList;

    @Inject
    public AddRoomConfigurationsViewModel() {
        furnitureList = new ArrayList<>();
    }

    public void addFurniture(Furniture furniture) {
        furnitureList.add(furniture);
    }

    public List<Furniture> getFurnitureList() {
        return furnitureList;
    }
}
