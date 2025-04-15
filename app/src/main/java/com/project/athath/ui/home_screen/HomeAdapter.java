package com.project.athath.ui.home_screen;


import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import androidx.lifecycle.LiveData;

import com.project.athath.R;
import com.project.athath.data.utils.ImageUtils;
import com.project.athath.databinding.HomeItemBinding;
import com.project.athath.data.model.Product;
import com.project.athath.ui.base.BaseAdapter;
import com.project.athath.ui.base.BaseInteractionListener;
import java.util.List;

public class HomeAdapter extends BaseAdapter<Product, HomeItemBinding> {

    private final HomeInteractionListener listener;

    public HomeAdapter(List<Product> reports, HomeInteractionListener listener) {
        super(reports);
        this.listener = listener;
    }

    @Override
    public HomeItemBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return HomeItemBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<HomeItemBinding> holder, int position, Product currentItem) {
        HomeItemBinding binding = holder.binding;
        binding.setItem(currentItem);
        Bitmap bitmap = ImageUtils.decodeBase64ToImage(currentItem.getImageUrl());
        binding.productImage.setImageBitmap(bitmap);
        // ✅ Observe favorite status and update icon
        listener.checkIfProductIsFavorite(currentItem.getId()).observeForever(isFavorite -> {
            updateFavoriteIcon(binding, isFavorite);
        });

        binding.favIcon.setOnClickListener(view -> listener.onFavoriteClicked(currentItem));
        binding.executePendingBindings();
    }

    // ✅ Update icon dynamically
    private void updateFavoriteIcon(HomeItemBinding binding, boolean isFavorite) {
        if (isFavorite) {
            binding.favIcon.setColorFilter(binding.getRoot().getContext().getResources().getColor(R.color.md_theme_errorContainer_mediumContrast));
        } else {
            binding.favIcon.setColorFilter(binding.getRoot().getContext().getResources().getColor(R.color.white));
        }
    }

    public void updateProducts(List<Product> products) {
        this.items = products;
        notifyDataSetChanged();
    }



    public interface HomeInteractionListener extends BaseInteractionListener {
        void onFavoriteClicked(Product product);
        LiveData<Boolean> checkIfProductIsFavorite(String productId); // ✅ Added method
    }
}