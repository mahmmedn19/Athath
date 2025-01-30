package com.project.asas.ui.admin_screen.room_configurations;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.asas.R;
import com.project.asas.databinding.ItemRoomConfigBinding;
import com.project.asas.model.Furniture;
import com.project.asas.ui.base.BaseAdapter;
import com.project.asas.ui.base.BaseInteractionListener;

import java.util.List;

public class RoomConfigAdapter extends BaseAdapter<Furniture, ItemRoomConfigBinding> {

    private final RoomConfigInteractionListener listener;

    public RoomConfigAdapter(List<Furniture> furnitureList, RoomConfigInteractionListener listener) {
        super(furnitureList);
        this.listener = listener;
    }

    @Override
    public ItemRoomConfigBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemRoomConfigBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemRoomConfigBinding> holder, int position, Furniture currentItem) {
        ItemRoomConfigBinding binding = holder.binding;
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
