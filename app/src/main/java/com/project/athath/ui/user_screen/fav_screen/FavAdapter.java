package com.project.athath.ui.user_screen.fav_screen;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.athath.databinding.ItemFavProductBinding;
import com.project.athath.data.model.Product;
import com.project.athath.ui.base.BaseAdapter;
import com.project.athath.ui.base.BaseInteractionListener;

import java.util.List;

public class FavAdapter extends BaseAdapter<Product, ItemFavProductBinding> {

    private final FavInteractionListener listener;

    public FavAdapter(List<Product> products, FavInteractionListener listener) {
        super(products);
        this.listener = listener;
    }

    @Override
    public ItemFavProductBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemFavProductBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemFavProductBinding> holder, int position, Product currentItem) {
        ItemFavProductBinding binding = holder.binding;
        binding.setProduct(currentItem);
        Glide.with(binding.getRoot().getContext())
                .load(currentItem.getImageUrl())
                .into(binding.productImage);

        // Remove Favorite
        binding.btnRemoveFav.setOnClickListener(view -> listener.onRemoveFavorite(currentItem));
        binding.cardProduct.setOnClickListener(view -> listener.onProductClick(currentItem));


        binding.executePendingBindings();
    }

    public interface FavInteractionListener extends BaseInteractionListener {
        void onRemoveFavorite(Product product);
        void onProductClick(Product product);
    }
}
