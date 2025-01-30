package com.project.asas.ui.admin_screen.room_configurations;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.asas.R;
import com.project.asas.databinding.FragmentManageRoomConfigBinding;
import com.project.asas.model.Furniture;
import com.project.asas.ui.base.BaseFragment;
import com.project.asas.ui.utils.DialogUtils;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageRoomConfigFragment extends BaseFragment<FragmentManageRoomConfigBinding> implements RoomConfigAdapter.RoomConfigInteractionListener {

    private final ManageRoomConfigViewModel viewModel = new ViewModelProvider(this).get(ManageRoomConfigViewModel.class);
    private RoomConfigAdapter adapter;

    @Override
    protected String getTAG() {
        return "ManageRoomConfigFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_manage_room_config;
    }

    @Override
    protected ViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(false);

        if (viewModel.getFurnitureList().isEmpty()) {
            viewModel.initializeFurnitureList(); // Ensure the list is populated
        }

        binding.recyclerRoomConfig.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RoomConfigAdapter(viewModel.getFurnitureList(), this);
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
