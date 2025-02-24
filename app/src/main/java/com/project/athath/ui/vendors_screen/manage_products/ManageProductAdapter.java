package com.project.athath.ui.vendors_screen.manage_products;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.athath.data.model.Product;
import com.project.athath.data.utils.ImageUtils;
import com.project.athath.databinding.ItemManageProductBinding;
import com.project.athath.ui.base.BaseAdapter;
import com.project.athath.ui.base.BaseInteractionListener;
import com.project.athath.ui.utils.DialogUtils;

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

        Bitmap bitmap = ImageUtils.decodeBase64ToImage(currentItem.getImageUrl());
        binding.productImage.setImageBitmap(bitmap);
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
