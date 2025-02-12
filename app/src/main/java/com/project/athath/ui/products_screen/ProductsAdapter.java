package com.project.athath.ui.products_screen;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.athath.databinding.ItemProductBinding;
import com.project.athath.data.model.Product;
import com.project.athath.ui.base.BaseAdapter;
import com.project.athath.ui.base.BaseInteractionListener;

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