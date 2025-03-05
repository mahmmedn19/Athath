package com.project.athath.ui.catelog_details_screen;


import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.athath.data.model.ResponseModel;
import com.project.athath.data.utils.ImageUtils;
import com.project.athath.databinding.ItemCatelogComponentBinding;
import com.project.athath.ui.base.BaseAdapter;

import java.util.List;

public class CategoryDetailsAdapter extends BaseAdapter<ResponseModel.DetectedObject, ItemCatelogComponentBinding> {


    public CategoryDetailsAdapter(List<ResponseModel.DetectedObject> componentList) {
        super(componentList);
    }

    @Override
    public ItemCatelogComponentBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemCatelogComponentBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemCatelogComponentBinding> holder, int position, ResponseModel.DetectedObject currentItem) {
        ItemCatelogComponentBinding binding = holder.binding;
        binding.setComponent(currentItem);
        // Convert Base64 to Bitmap before displaying
        Bitmap decodedBitmap = ImageUtils.decodeBase64ToImage(currentItem.getImage());
        binding.componentImage.setImageBitmap(decodedBitmap);
        binding.executePendingBindings();
    }

}
