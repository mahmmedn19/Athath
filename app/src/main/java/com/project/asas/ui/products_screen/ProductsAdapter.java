package com.project.asas.ui.products_screen;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.asas.databinding.ItemProductBinding;
import com.project.asas.model.Product;
import com.project.asas.ui.base.BaseAdapter;
import com.project.asas.ui.base.BaseInteractionListener;

import java.util.List;

public class ProductsAdapter extends BaseAdapter<Product, ItemProductBinding> {

    private final ProductsInteractionListener listener;

    public ProductsAdapter(List<Product> productList, ProductsInteractionListener listener) {
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
        binding.btnShowDetails.setOnClickListener(view -> listener.onProductClicked(currentItem));
        binding.productName.setVisibility(View.GONE);
        binding.productImage.setOnClickListener(view -> listener.onProductClicked(currentItem));
        Glide.with(holder.binding.getRoot().getContext())
                .load(currentItem.getImageUrl())
                .centerCrop()
                .into(holder.binding.productImage);
        binding.executePendingBindings();
    }



    public interface ProductsInteractionListener extends BaseInteractionListener {
        void onProductClicked(Product product);
    }
}