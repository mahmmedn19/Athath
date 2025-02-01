// ManageRoomCatalogFragment.java
package com.project.athath.ui.admin_screen.room_catalog;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.athath.R;
import com.project.athath.databinding.FragmentManageRoomCatalogBinding;
import com.project.athath.model.Furniture;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.DialogUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageRoomCatalogFragment extends BaseFragment<FragmentManageRoomCatalogBinding> implements RoomCatalogAdapter.RoomConfigInteractionListener {

    private RoomCatalogAdapter adapter;
    private ManageRoomCatalogViewModel viewModel;

    @Override
    protected String getTAG() {
        return "ManageRoomConfigFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_manage_room_catalog;
    }

    @Override
    protected ViewModel getViewModel() {
        return new ViewModelProvider(this).get(ManageRoomCatalogViewModel.class);
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Catalog Management");
        showBackButton(false);

        viewModel = new ViewModelProvider(this).get(ManageRoomCatalogViewModel.class);

        binding.recyclerRoomConfig.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RoomCatalogAdapter(viewModel.getFurnitureList(), this);
        binding.recyclerRoomConfig.setAdapter(adapter);

        binding.fabAddConfig.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_admin_room_configurations_to_admin_add_room_configurations)
        );
    }

    @Override
    public void onEditFurniture(Furniture furniture) {
        DialogUtils.showCustomDialog(requireContext(), "Edit Furniture", "Here you can edit the furniture.");
    }

    @Override
    public void onDeleteFurniture(Furniture furniture) {
        viewModel.removeFurniture(furniture);
        adapter.notifyDataSetChanged();
        DialogUtils.showCustomDialog(requireContext(), "Delete Furniture", "Furniture deleted successfully.");
    }
}
