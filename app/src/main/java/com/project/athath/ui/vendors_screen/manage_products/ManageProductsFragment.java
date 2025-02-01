package com.project.athath.ui.vendors_screen.manage_products;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.athath.R;
import com.project.athath.databinding.FragmentManageProductsBinding;
import com.project.athath.model.Product;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.DialogUtils;
import java.util.ArrayList;
import java.util.List;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageProductsFragment extends BaseFragment<FragmentManageProductsBinding>
        implements ManageProductAdapter.ManageProductInteractionListener {

    private ManageProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected String getTAG() {
        return "ManageProductsFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_manage_products;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Manage Products");
        showBackButton(false);

        // Initialize RecyclerView
        productList = generateFakeProducts(8);
        productAdapter = new ManageProductAdapter(productList, this);

        binding.recyclerProducts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerProducts.setAdapter(productAdapter);

        // Floating Button to Add New Product
        binding.fabAddProduct.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.action_vendor_products_to_add_product)
        );
    }

    private List<Product> generateFakeProducts(int count) {
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            products.add(new Product(
                    "Modern Style",
                    "Living Room",
                    250.0 * i,
                    5.0,
                    4.0,
                    R.drawable.furniture1
            ));
        }
        return products;
    }

    @Override
    public void onEditProduct(Product product) {
        DialogUtils.showCustomDialog(requireContext(), "Edit Product", "Editing: " + product.getStyle());
    }

    @Override
    public void onDeleteProduct(Product product) {
        productList.remove(product);
        productAdapter.notifyDataSetChanged();
        DialogUtils.showCustomDialog(requireContext(), "Product Deleted", "The product has been removed.");
    }
}
