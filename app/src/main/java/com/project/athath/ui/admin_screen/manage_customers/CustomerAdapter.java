package com.project.athath.ui.admin_screen.manage_customers;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.project.athath.databinding.ItemCustomerBinding;
import com.project.athath.model.Customer;
import com.project.athath.ui.base.BaseAdapter;
import com.project.athath.ui.base.BaseInteractionListener;
import com.project.athath.ui.utils.DialogUtils;
import java.util.List;

public class CustomerAdapter extends BaseAdapter<Customer, ItemCustomerBinding> {

    private final CustomerInteractionListener listener;

    public CustomerAdapter(List<Customer> customers, CustomerInteractionListener listener) {
        super(customers);
        this.listener = listener;
    }

    @Override
    public ItemCustomerBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemCustomerBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemCustomerBinding> holder, int position, Customer currentItem) {
        ItemCustomerBinding binding = holder.binding;
        binding.setCustomer(currentItem);

        // Block Customer
        binding.btnBlockCustomer.setOnClickListener(view ->
                DialogUtils.showConfirmationDialog(view.getContext(),
                        "Block Customer", "Are you sure you want to block this customer?",
                        "Yes", "Cancel",
                        (dialog, which) -> listener.onBlockCustomer(currentItem))
        );

        binding.executePendingBindings();
    }

    public interface CustomerInteractionListener extends BaseInteractionListener {
        void onBlockCustomer(Customer customer);
    }
}
