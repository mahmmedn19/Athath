package com.project.athath.ui.admin_screen.manage_vendors;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.athath.R;
import com.project.athath.data.model.Vendor;
import com.project.athath.data.utils.Result;
import com.project.athath.databinding.FragmentManageVendorsBinding;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageVendorsFragment extends BaseFragment<FragmentManageVendorsBinding> implements VendorAdapter.VendorInteractionListener {

    private VendorAdapter vendorAdapter;
    private VendorViewModel viewModel;
    private final List<Vendor> vendorList = new ArrayList<>();

    @Override
    protected String getTAG() {
        return "ManageVendorsFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_manage_vendors;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(VendorViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Manage Vendors");
        showBackButton(false);

        binding.recyclerVendors.setLayoutManager(new LinearLayoutManager(requireContext()));
        vendorAdapter = new VendorAdapter(vendorList, this);
        binding.recyclerVendors.setAdapter(vendorAdapter);

        observeVendors();
        viewModel.fetchVendors();
    }

    private void observeVendors() {
        viewModel.getVendors().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                binding.loadingProgressBar.setVisibility(View.VISIBLE);
                binding.recyclerVendors.setVisibility(View.GONE);
                binding.imageNoDataFound.setVisibility(View.GONE);
            } else {
                binding.loadingProgressBar.setVisibility(View.GONE);

                if (result.getStatus() == Result.Status.SUCCESS) {
                    vendorList.clear();
                    if (result.getData() != null && !result.getData().isEmpty()) {
                        vendorList.addAll(result.getData());
                        binding.recyclerVendors.setVisibility(View.VISIBLE);
                        binding.imageNoDataFound.setVisibility(View.GONE);
                    } else {
                        binding.recyclerVendors.setVisibility(View.GONE);
                        binding.imageNoDataFound.setVisibility(View.VISIBLE);
                    }
                    vendorAdapter.notifyDataSetChanged();
                } else if (result.getStatus() == Result.Status.ERROR) {
                    Toast.makeText(requireContext(), "Error: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    binding.recyclerVendors.setVisibility(View.GONE);
                    binding.imageNoDataFound.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onAcceptVendor(Vendor vendor) {
        viewModel.updateVendorStatus(vendor.getId(), "Approved");
        vendor.setStatus("Approved");
        vendorAdapter.notifyDataSetChanged();
        DialogUtils.showCustomDialog(requireContext(), "Success", "Vendor has been approved.");
    }

    @Override
    public void onBlockVendor(Vendor vendor) {
        viewModel.updateVendorStatus(vendor.getId(), "Blocked");
        vendor.setStatus("Blocked");
        vendorAdapter.notifyDataSetChanged();
        DialogUtils.showCustomDialog(requireContext(), "Blocked", "Vendor has been blocked.");
    }

    @Override
    public void onViewVendor(Vendor vendor) {
        String vendorDetails =
                "Name: " + vendor.getName() + "\n" +
                "Store Name: " + vendor.getStoreName() + "\n" +
                "Phone: " + vendor.getPhone() + "\n" +
                "Address: " + vendor.getAddress() + "\n" +
                "Email: " + vendor.getEmail() + "\n" +
                "Status: " + vendor.getStatus();

        DialogUtils.showCustomDialog(requireContext(), "Vendor Details", vendorDetails);
    }
}
