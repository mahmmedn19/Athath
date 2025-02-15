package com.project.athath.ui.catelog_screen;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.athath.data.utils.ImageUtils;
import com.project.athath.databinding.ItemCatalogBinding;
import com.project.athath.data.model.CatalogItem;
import com.project.athath.ui.base.BaseAdapter;
import com.project.athath.ui.base.BaseInteractionListener;

import java.util.List;

public class CatalogAdapter extends BaseAdapter<CatalogItem, ItemCatalogBinding> {

    private final CatalogInteractionListener listener;

    public CatalogAdapter(List<CatalogItem> items, CatalogInteractionListener listener) {
        super(items);
        this.listener = listener;
    }

    @Override
    public ItemCatalogBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemCatalogBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemCatalogBinding> holder, int position, CatalogItem currentItem) {
        ItemCatalogBinding binding = holder.binding;
        binding.setItem(currentItem);
        // Convert Base64 to Bitmap before displaying
        Bitmap decodedBitmap = ImageUtils.decodeBase64ToImage(currentItem.getImageRes());
        binding.ivCatalogImage.setImageBitmap(decodedBitmap);

        binding.btnShowProducts.setOnClickListener(view -> listener.onShowProductsClicked(currentItem));
        binding.executePendingBindings();
    }

    public interface CatalogInteractionListener extends BaseInteractionListener {
        void onShowProductsClicked(CatalogItem catalogItem);
    }
}
