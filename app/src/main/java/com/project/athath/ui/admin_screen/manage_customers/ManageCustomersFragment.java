package com.project.athath.ui.admin_screen.manage_customers;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.athath.R;
import com.project.athath.databinding.FragmentManageCustomersBinding;
import com.project.athath.model.Customer;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageCustomersFragment extends BaseFragment<FragmentManageCustomersBinding> implements CustomerAdapter.CustomerInteractionListener {

    private CustomerAdapter customerAdapter;
    private List<Customer> customerList;

    @Override
    protected String getTAG() {
        return "ManageCustomersFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_manage_customers;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Manage Customers");
        showBackButton(false);


        customerList = generateFakeCustomers();
        customerAdapter = new CustomerAdapter(customerList, this);

        binding.recyclerCustomers.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerCustomers.setAdapter(customerAdapter);
    }


    private List<Customer> generateFakeCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("John Doe", "john@example.com", "password123"));
        customers.add(new Customer("Alice Smith", "alice@example.com", "password456"));
        customers.add(new Customer("Michael Johnson", "michael@example.com", "password789"));
        customers.add(new Customer("Emma Williams", "emma@example.com", "password321"));
        customers.add(new Customer("David Brown", "david@example.com", "password654"));
        return customers;
    }

    @Override
    public void onBlockCustomer(Customer customer) {
        DialogUtils.showCustomDialog(requireContext(), "Blocked", "Customer " + customer.getUsername() + " has been blocked.");
    }
}
