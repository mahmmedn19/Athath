package com.project.athath.ui.catelog_details_screen;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.project.athath.databinding.ItemCatelogComponentBinding;
import com.project.athath.data.model.Component;
import com.project.athath.ui.base.BaseAdapter;

import java.util.List;

public class CategoryDetailsAdapter extends BaseAdapter<Component, ItemCatelogComponentBinding> {


    public CategoryDetailsAdapter(List<Component> componentList) {
        super(componentList);
    }

    @Override
    public ItemCatelogComponentBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemCatelogComponentBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemCatelogComponentBinding> holder, int position, Component currentItem) {
        ItemCatelogComponentBinding binding = holder.binding;
        binding.setComponent(currentItem);

        Glide.with(holder.binding.getRoot().getContext())
                .load(currentItem.getImageResId())
                .into(holder.binding.componentImage);

        binding.executePendingBindings();
    }

}
