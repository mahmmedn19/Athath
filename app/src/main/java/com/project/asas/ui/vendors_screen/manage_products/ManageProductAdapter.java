package com.project.asas.ui.vendors_screen.manage_products;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.asas.databinding.ItemManageProductBinding;
import com.project.asas.model.Product;
import com.project.asas.ui.base.BaseAdapter;
import com.project.asas.ui.base.BaseInteractionListener;
import com.project.asas.ui.utils.DialogUtils;

import java.util.List;

public class ManageProductAdapter extends BaseAdapter<Product, ItemManageProductBinding> {

    private final ManageProductInteractionListener listener;

    public ManageProductAdapter(List<Product> products, ManageProductInteractionListener listener) {
        super(products);
        this.listener = listener;
    }

    @Override
    public ItemManageProductBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemManageProductBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemManageProductBinding> holder, int position, Product currentItem) {
        ItemManageProductBinding binding = holder.binding;
        binding.setProduct(currentItem);
        Glide
                .with(binding.getRoot())
                .load(currentItem.getImageUrl())
                .centerCrop()
                .into(binding.productImage);

        // Edit Product
        binding.btnEdit.setOnClickListener(view ->
                listener.onEditProduct(currentItem)
        );

        // Delete Product
        binding.btnDelete.setOnClickListener(view ->
                DialogUtils.showConfirmationDialog(view.getContext(),
                        "Delete Product", "Are you sure you want to delete this product?",
                        "Yes", "Cancel",
                        (dialog, which) -> listener.onDeleteProduct(currentItem))
        );

        binding.executePendingBindings();
    }

    public interface ManageProductInteractionListener extends BaseInteractionListener {
        void onEditProduct(Product product);

        void onDeleteProduct(Product product);
    }
}
