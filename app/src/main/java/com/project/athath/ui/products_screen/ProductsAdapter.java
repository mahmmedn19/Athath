package com.project.athath.ui.products_screen;


import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.athath.data.utils.ImageUtils;
import com.project.athath.databinding.ItemProductBinding;
import com.project.athath.data.model.Product;
import com.project.athath.ui.base.BaseAdapter;
import com.project.athath.ui.base.BaseInteractionListener;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends BaseAdapter<Product, ItemProductBinding> {

    private final ProductsInteractionListener listener;

    public ProductsAdapter(List<Product> productList, ProductsInteractionListener listener) {
        super(productList != null ? productList : new ArrayList<>());  // Prevent null
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

        binding.btnShowDetails.setOnClickListener(view -> listener.onProductClicked(currentItem));
        binding.productImage.setOnClickListener(view -> listener.onProductClicked(currentItem));
        binding.executePendingBindings();
    }

    public void updateProducts(List<Product> products) {
        this.items = products;
        notifyDataSetChanged();
    }

    public interface ProductsInteractionListener extends BaseInteractionListener {
        void onProductClicked(Product product);
    }
}