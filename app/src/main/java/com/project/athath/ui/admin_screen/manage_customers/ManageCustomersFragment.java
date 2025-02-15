package com.project.athath.ui.admin_screen.manage_customers;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.athath.R;
import com.project.athath.data.model.Customer;
import com.project.athath.data.utils.Result;
import com.project.athath.databinding.FragmentManageCustomersBinding;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageCustomersFragment extends BaseFragment<FragmentManageCustomersBinding> implements CustomerAdapter.CustomerInteractionListener {

    private CustomerAdapter customerAdapter;
    private CustomerViewModel viewModel;
    private final List<Customer> customerList = new ArrayList<>();

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
        viewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Manage Customers");
        showBackButton(false);

        binding.recyclerCustomers.setLayoutManager(new LinearLayoutManager(requireContext()));
        customerAdapter = new CustomerAdapter(customerList, this);
        binding.recyclerCustomers.setAdapter(customerAdapter);

        observeCustomers();
        viewModel.fetchCustomers();
    }

    private void observeCustomers() {
        viewModel.getCustomers().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                binding.loadingProgressBar.setVisibility(View.VISIBLE);
                binding.recyclerCustomers.setVisibility(View.GONE);
                binding.imageNoDataFound.setVisibility(View.GONE);
            } else {
                binding.loadingProgressBar.setVisibility(View.GONE);

                if (result.getStatus() == Result.Status.SUCCESS) {
                    customerList.clear();
                    if (result.getData() != null && !result.getData().isEmpty()) {
                        customerList.addAll(result.getData());
                        binding.recyclerCustomers.setVisibility(View.VISIBLE);
                        binding.imageNoDataFound.setVisibility(View.GONE);
                    } else {
                        binding.recyclerCustomers.setVisibility(View.GONE);
                        binding.imageNoDataFound.setVisibility(View.VISIBLE);
                    }
                    customerAdapter.notifyDataSetChanged();
                } else if (result.getStatus() == Result.Status.ERROR) {
                    Toast.makeText(requireContext(), "Error: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    binding.recyclerCustomers.setVisibility(View.GONE);
                    binding.imageNoDataFound.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onBlockCustomer(Customer customer) {
        viewModel.blockCustomer(customer.getId());
        customer.setStatus("Blocked");
        customerAdapter.notifyDataSetChanged();
        DialogUtils.showCustomDialog(requireContext(), "Blocked", "Customer " + customer.getUsername() + " has been blocked.");
    }
}
