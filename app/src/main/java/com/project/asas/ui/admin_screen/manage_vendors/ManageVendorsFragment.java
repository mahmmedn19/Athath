package com.project.asas.ui.admin_screen.manage_vendors;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.asas.R;
import com.project.asas.databinding.FragmentManageVendorsBinding;
import com.project.asas.model.Vendor;
import com.project.asas.ui.base.BaseFragment;
import com.project.asas.ui.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageVendorsFragment extends BaseFragment<FragmentManageVendorsBinding> implements VendorAdapter.VendorInteractionListener {

    private VendorAdapter vendorAdapter;
    private List<Vendor> vendorList;

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
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(false);

        if (binding == null) {
            return; // Prevents further execution if binding is null
        }

        setToolbarVisibility(false);

        binding.recyclerVendors.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        vendorList = generateFakeVendors();
        vendorAdapter = new VendorAdapter(vendorList, this);

        binding.recyclerVendors.setAdapter(vendorAdapter);
    }


    private List<Vendor> generateFakeVendors() {
        List<Vendor> vendors = new ArrayList<>();
        vendors.add(new Vendor("John Doe", "Pending"));
        vendors.add(new Vendor("Alice Smith", "Approved"));
        vendors.add(new Vendor("Michael Johnson", "Pending"));
        vendors.add(new Vendor("Emma Williams", "Blocked"));
        vendors.add(new Vendor("David Brown", "Approved"));
        return vendors;
    }

    @Override
    public void onAcceptVendor(Vendor vendor) {
        vendor.setStatus("Approved");
        vendorAdapter.notifyDataSetChanged();
        DialogUtils.showCustomDialog(requireContext(), "Success", "Vendor has been approved.");
    }

    @Override
    public void onBlockVendor(Vendor vendor) {
        vendor.setStatus("Blocked");
        vendorAdapter.notifyDataSetChanged();
        DialogUtils.showCustomDialog(requireContext(), "Blocked", "Vendor has been blocked.");
    }

    @Override
    public void onViewVendor(Vendor vendor) {
        String vendorDetails = "ID: " + vendor.getId() + "\n" +
                "Name: " + vendor.getName() + "\n" +
                "Store Name: " + vendor.getStoreName() + "\n" +
                "Phone: " + vendor.getPhone() + "\n" +
                "Address: " + vendor.getAddress() + "\n" +
                "Email: " + vendor.getEmail();

        DialogUtils.showCustomDialog(requireContext(), "Vendor Details", vendorDetails);
    }

}
