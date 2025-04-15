package com.project.athath.ui.admin_screen.manage_vendors;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.athath.data.model.Vendor;
import com.project.athath.databinding.ItemVendorBinding;
import com.project.athath.ui.base.BaseAdapter;
import com.project.athath.ui.base.BaseInteractionListener;
import com.project.athath.ui.utils.DialogUtils;

import java.util.List;

public class VendorAdapter extends BaseAdapter<Vendor, ItemVendorBinding> {

    private final VendorInteractionListener listener;

    public VendorAdapter(List<Vendor> vendors, VendorInteractionListener listener) {
        super(vendors);
        this.listener = listener;
    }

    @Override
    public ItemVendorBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemVendorBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemVendorBinding> holder, int position, Vendor currentItem) {
        ItemVendorBinding binding = holder.binding;
        binding.setVendor(currentItem);

        // Block Vendor
        binding.btnBlock.setOnClickListener(view ->
                DialogUtils.showConfirmationDialog(view.getContext(),
                        "Block Vendor", "Are you sure you want to block this vendor?",
                        "Yes", "Cancel",
                        (dialog, which) -> listener.onBlockVendor(currentItem))
        );

        // View Profile
        binding.btnViewProfile.setOnClickListener(view ->
                listener.onViewVendor(currentItem)
        );

        binding.executePendingBindings();
    }

    public interface VendorInteractionListener extends BaseInteractionListener {
        void onBlockVendor(Vendor vendor);

        void onViewVendor(Vendor vendor);
    }
}

