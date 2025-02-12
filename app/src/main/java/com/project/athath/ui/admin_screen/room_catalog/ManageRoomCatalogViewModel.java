// ManageRoomCatalogViewModel.java
package com.project.athath.ui.admin_screen.room_catalog;

import androidx.lifecycle.ViewModel;
import com.project.athath.R;
import com.project.athath.data.model.Furniture;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageRoomCatalogViewModel extends ViewModel {

    private final List<Furniture> furnitureList = new ArrayList<>();

    @Inject
    public ManageRoomCatalogViewModel() {
        initializeFurnitureList();
    }

    public List<Furniture> getFurnitureList() {
        return new ArrayList<>(furnitureList); // Return a copy to avoid unintended modification
    }

    private void initializeFurnitureList() {
        if (!furnitureList.isEmpty()) return; // Prevent duplicate initialization

        furnitureList.add(new Furniture("Sofa Set", "Comfortable sofa set", "Living Room", "Blue", "Modern",
                "Minimalist", 999.99, 2.5, 1.5, 1.2, R.drawable.furniture_5));
        furnitureList.add(new Furniture("Dining Table", "Elegant wooden dining table", "Dining Room", "Brown", "Classic",
                "Wooden", 499.99, 2.0, 1.0, 1.0, R.drawable.furniture_9));
        furnitureList.add(new Furniture("Study Desk", "Compact study desk", "Office", "White", "Contemporary",
                "Space-Saving", 299.99, 1.5, 0.7, 0.8, R.drawable.furniture_3));
    }

    public void removeFurniture(Furniture furniture) {
        furnitureList.remove(furniture);
    }
}
