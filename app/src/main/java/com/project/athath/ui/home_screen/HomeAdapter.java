package com.project.athath.ui.home_screen;


import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;


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
        binding.favIcon.setOnClickListener(view -> listener.onFavoriteClicked(currentItem));
        binding.executePendingBindings();
    }

    public void updateProducts(List<Product> products) {
        this.items = products;
        notifyDataSetChanged();
    }



    public interface HomeInteractionListener extends BaseInteractionListener {
        void onFavoriteClicked(Product product);
    }
}