package com.project.athath.ui.home_screen;


import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.athath.data.utils.ImageUtils;
import com.project.athath.databinding.ItemProductBinding;
import com.project.athath.data.model.Product;
import com.project.athath.ui.base.BaseAdapter;
import com.project.athath.ui.base.BaseInteractionListener;

import java.util.List;

public class ProductHomeAdapter extends BaseAdapter<Product, ItemProductBinding> {

    private final ProductInteractionListener listener;

    public ProductHomeAdapter(List<Product> productList, ProductInteractionListener listener) {
        super(productList);
        this.listener = listener;
    }

    @Override
    public ItemProductBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemProductBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemProductBinding> holder, int position, Product currentItem) {
        ItemProductBinding binding = holder.binding;
        binding.setItem(currentItem);
        Bitmap bitmap = ImageUtils.decodeBase64ToImage(currentItem.getImageUrl());
        binding.productImage.setImageBitmap(bitmap);
        binding.btnShowDetails.setVisibility(View.GONE);
        binding.productName.setGravity(View.TEXT_ALIGNMENT_CENTER);
        binding.productName.setForegroundGravity(View.TEXT_ALIGNMENT_CENTER);
        binding.productPrice.setVisibility(View.GONE);
        binding.productStore.setVisibility(View.GONE);
        binding.productImage.setOnClickListener(view -> listener.onProductClicked(currentItem));
        binding.executePendingBindings();
    }



    public interface ProductInteractionListener extends BaseInteractionListener {
        void onProductClicked(Product product);
    }
}