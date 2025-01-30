package com.project.asas.ui.admin_screen.room_configurations;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.asas.R;
import com.project.asas.databinding.FragmentManageRoomConfigBinding;
import com.project.asas.model.RoomConfig;
import com.project.asas.ui.base.BaseFragment;
import com.project.asas.ui.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageRoomConfigFragment extends BaseFragment<FragmentManageRoomConfigBinding> implements RoomConfigAdapter.RoomConfigInteractionListener {
    private RoomConfigAdapter roomConfigAdapter;
    private List<RoomConfig> roomConfigList;

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
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(false);
        roomConfigList = new ArrayList<>();
        roomConfigAdapter = new RoomConfigAdapter(roomConfigList, this);

        binding.recyclerRoomConfig.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerRoomConfig.setAdapter(roomConfigAdapter);

        fetchRoomConfigs();

        binding.fabAddConfig.setOnClickListener(v -> {
            DialogUtils.showCustomDialog(requireContext(), "Add Room Configuration", "Here you can add a new room configuration.");
        });
    }

    private void fetchRoomConfigs() {
        roomConfigList.add(new RoomConfig("Living Room", "4 Lights, 2 Fans", R.drawable.furniture_5));
        roomConfigList.add(new RoomConfig("Bedroom", "2 Lights, 1 Fan", R.drawable.furniture_6));
        roomConfigList.add(new RoomConfig("Kitchen", "3 Lights, 1 Fridge", R.drawable.furniture_7));
        roomConfigList.add(new RoomConfig("Bathroom", "1 Light, 1 Geyser", R.drawable.furniture_8));
        roomConfigAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEditConfig(RoomConfig roomConfig) {
        DialogUtils.showCustomDialog(requireContext(), "Edit Configuration", "Editing: " + roomConfig.getName());
    }

    @Override
    public void onDeleteConfig(RoomConfig roomConfig) {
        roomConfigList.remove(roomConfig);
        roomConfigAdapter.notifyDataSetChanged();
    }
}