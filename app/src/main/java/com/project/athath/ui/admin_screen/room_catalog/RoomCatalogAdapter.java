package com.project.athath.ui.admin_screen.room_catalog;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.athath.R;
import com.project.athath.data.model.CatalogItem;
import com.project.athath.data.utils.ImageUtils;
import com.project.athath.databinding.ItemRoomCatalogBinding;
import com.project.athath.ui.base.BaseAdapter;
import com.project.athath.ui.base.BaseInteractionListener;

import java.util.List;

public class RoomCatalogAdapter extends BaseAdapter<CatalogItem, ItemRoomCatalogBinding> {

    private final RoomCatalogInteractionListener listener;

    public RoomCatalogAdapter(List<CatalogItem> itemList, RoomCatalogInteractionListener listener) {
        super(itemList);
        this.listener = listener;
    }

    @Override
    public ItemRoomCatalogBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemRoomCatalogBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemRoomCatalogBinding> holder, int position, CatalogItem currentItem) {
        ItemRoomCatalogBinding binding = holder.binding;
        binding.setItems(currentItem);

        // Convert Base64 to Bitmap before displaying
        Bitmap decodedBitmap = ImageUtils.decodeBase64ToImage(currentItem.getImageRes());
        binding.catalogImage.setImageBitmap(decodedBitmap);

        // Edit Configuration
        binding.btnEdit.setOnClickListener(view -> listener.onEditCatalog(currentItem));

        // Delete Configuration
        binding.btnDelete.setOnClickListener(view -> listener.onDeleteCatalog(currentItem));

        binding.executePendingBindings();
    }


    public interface RoomCatalogInteractionListener extends BaseInteractionListener {
        void onEditCatalog(CatalogItem item);

        void onDeleteCatalog(CatalogItem item);
    }
}
