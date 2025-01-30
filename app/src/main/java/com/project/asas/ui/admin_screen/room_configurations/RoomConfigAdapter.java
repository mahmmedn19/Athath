package com.project.asas.ui.admin_screen.room_configurations;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.project.asas.R;
import com.project.asas.databinding.ItemRoomConfigBinding;
import com.project.asas.model.RoomConfig;
import com.project.asas.ui.base.BaseAdapter;
import com.project.asas.ui.base.BaseInteractionListener;

import java.util.List;

public class RoomConfigAdapter extends BaseAdapter<RoomConfig, ItemRoomConfigBinding> {

    private final RoomConfigInteractionListener listener;

    public RoomConfigAdapter(List<RoomConfig> roomConfigs, RoomConfigInteractionListener listener) {
        super(roomConfigs);
        this.listener = listener;
    }

    @Override
    public ItemRoomConfigBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemRoomConfigBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemRoomConfigBinding> holder, int position, RoomConfig currentItem) {
        ItemRoomConfigBinding binding = holder.binding;
        binding.setRoomConfig(currentItem);

        // Load Image from Firebase Storage
        Glide.with(holder.itemView.getContext())
                .load(currentItem.getImageUrl())
                .placeholder(R.drawable.furniture_5)
                .into(binding.roomImage);

        // Menu Button Click
        binding.topRightIcon.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), binding.topRightIcon);
            popupMenu.inflate(R.menu.room_config_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_edit) {
                    listener.onEditConfig(currentItem);
                    return true;
                } else if (item.getItemId() == R.id.menu_delete) {
                    listener.onDeleteConfig(currentItem);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });

        binding.executePendingBindings();
    }

    public interface RoomConfigInteractionListener extends BaseInteractionListener {
        void onEditConfig(RoomConfig roomConfig);

        void onDeleteConfig(RoomConfig roomConfig);
    }
}
