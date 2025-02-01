package com.project.athath.ui.home_screen;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.athath.databinding.ItemCatalogHomeBinding;
import com.project.athath.model.CatalogItem;
import com.project.athath.ui.base.BaseAdapter;
import com.project.athath.ui.base.BaseInteractionListener;

import java.util.List;

public class CatalogHomeAdapter extends BaseAdapter<CatalogItem, ItemCatalogHomeBinding> {

    private final CatalogHomeInteractionListener listener;

    public CatalogHomeAdapter(List<CatalogItem> items, CatalogHomeInteractionListener listener) {
        super(items);
        this.listener = listener;
    }

    @Override
    public ItemCatalogHomeBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemCatalogHomeBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemCatalogHomeBinding> holder, int position, CatalogItem currentItem) {
        ItemCatalogHomeBinding binding = holder.binding;
        binding.setItem(currentItem);
        Glide.with(holder.binding.getRoot().getContext())
                .load(currentItem.getImageRes())
                .into(holder.binding.ivCatalogImage);
        binding.btnShowDetails.setOnClickListener(view -> listener.onShowDetailsClicked(currentItem));
        binding.executePendingBindings();
    }

    public interface CatalogHomeInteractionListener extends BaseInteractionListener {
        void onShowDetailsClicked(CatalogItem catalogItem);
    }
}
