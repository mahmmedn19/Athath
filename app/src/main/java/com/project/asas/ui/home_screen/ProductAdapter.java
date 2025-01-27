package com.project.asas.ui.home_screen;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.asas.databinding.HomeItemBinding;
import com.project.asas.databinding.ItemProductBinding;
import com.project.asas.model.Product;
import com.project.asas.ui.base.BaseAdapter;
import com.project.asas.ui.base.BaseInteractionListener;

import java.util.List;

public class ProductAdapter extends BaseAdapter<Product, ItemProductBinding> {

    private final ProductInteractionListener listener;

    public ProductAdapter(List<Product> productList, ProductInteractionListener listener) {
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
        binding.productImage.setOnClickListener(view -> listener.onProductClicked(currentItem));
        binding.executePendingBindings();
    }



    public interface ProductInteractionListener extends BaseInteractionListener {
        void onProductClicked(Product product);
    }
}