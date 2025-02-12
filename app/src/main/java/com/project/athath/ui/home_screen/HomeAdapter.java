package com.project.athath.ui.home_screen;


import android.view.LayoutInflater;
import android.view.ViewGroup;


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
        binding.favIcon.setOnClickListener(view -> listener.onFavoriteClicked(currentItem));
        binding.productImage.setOnClickListener(view -> listener.onCartClicked(currentItem));
        binding.executePendingBindings();
    }



    public interface HomeInteractionListener extends BaseInteractionListener {
        void onFavoriteClicked(Product product);
        void onCartClicked(Product product);
    }
}