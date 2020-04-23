package ethiopia.covid.android.ui.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ethiopia.covid.android.R;

/**
 * Created by BrookMG on 3/7/2019 in ethiopia.covid.android.ui.adapters
 * inside the project CovidEt .
 */
public class ImageListRecyclerAdapter extends RecyclerView.Adapter<ImageListRecyclerAdapter.ViewHolder> {

    private List<String> images;
    private OnImageItemClicked onImageItemClicked;

    public ImageListRecyclerAdapter(List<String> images) {
        this(images, null);
    }

    public ImageListRecyclerAdapter(List<String> images, OnImageItemClicked onImageItemClicked) {
        this.onImageItemClicked = onImageItemClicked;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.imageView).load(images.get(position)).into(holder.imageView);
        holder.itemView.setOnClickListener(v -> {
            if (onImageItemClicked != null) {
                onImageItemClicked.OnImageClicked(
                        Uri.parse(images.get(position))
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_item);
        }

    }

}
