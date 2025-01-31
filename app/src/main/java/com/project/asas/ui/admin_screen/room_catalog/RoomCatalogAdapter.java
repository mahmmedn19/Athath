package com.project.asas.ui.admin_screen.room_catalog;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.asas.R;
import com.project.asas.databinding.ItemRoomCatalogBinding;
import com.project.asas.model.Furniture;
import com.project.asas.ui.base.BaseAdapter;
import com.project.asas.ui.base.BaseInteractionListener;

import java.util.List;

public class RoomCatalogAdapter extends BaseAdapter<Furniture, ItemRoomCatalogBinding> {

    private final RoomConfigInteractionListener listener;

    public RoomCatalogAdapter(List<Furniture> furnitureList, RoomConfigInteractionListener listener) {
        super(furnitureList);
        this.listener = listener;
    }

    @Override
    public ItemRoomCatalogBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemRoomCatalogBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemRoomCatalogBinding> holder, int position, Furniture currentItem) {
        ItemRoomCatalogBinding binding = holder.binding;
        binding.setFurniture(currentItem);
        Glide.with(holder.itemView.getContext())
                .load(currentItem.getImageUrl())
                .placeholder(R.drawable.furniture_5)
                .into(binding.furnitureImage);

        // Edit Configuration
        binding.btnEdit.setOnClickListener(view -> listener.onEditFurniture(currentItem));

        // Delete Configuration
        binding.btnDelete.setOnClickListener(view -> listener.onDeleteFurniture(currentItem));

        binding.executePendingBindings();
    }

    public interface RoomConfigInteractionListener extends BaseInteractionListener {
        void onEditFurniture(Furniture furniture);
        void onDeleteFurniture(Furniture furniture);
    }
}
