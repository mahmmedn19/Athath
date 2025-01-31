package com.project.asas.ui.home_screen;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.asas.databinding.ItemProductBinding;
import com.project.asas.model.Product;
import com.project.asas.ui.base.BaseAdapter;
import com.project.asas.ui.base.BaseInteractionListener;

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