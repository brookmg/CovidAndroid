package ethiopia.covid.android.ui.adapter;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import ethiopia.covid.android.R;
import ethiopia.covid.android.data.DetailItem;

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.adapter
 * inside the project CoVidEt .
 */
public class DetailRecyclerAdapter extends RecyclerView.Adapter<DetailRecyclerAdapter.ViewHolder> {

    List<DetailItem> details;

    public DetailRecyclerAdapter(List<DetailItem> details) {
        this.details = details;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_recycler_element , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(details.get(position).getTitle());
        holder.readMore.setVisibility(details.get(position).isHasMoreButton() ? View.VISIBLE : View.GONE);

        Glide.with(holder.imageView).asBitmap().load(
                details.get(position).getImageResource() != -1 ?
                        details.get(position).getImageResource():
                        details.get(position).getImageLink()
        ).addListener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @SuppressLint("RestrictedApi")
            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                Palette.from(resource).generate(palette -> {
                    Palette.Swatch mutedColor = palette.getLightMutedSwatch();
                    Palette.Swatch vibrantSwatch = palette.getDarkVibrantSwatch();

                    if (vibrantSwatch == null) vibrantSwatch = palette.getDominantSwatch();

                    if (mutedColor != null) holder.textView.setTextColor(mutedColor.getTitleTextColor());

                    if (palette.getLightMutedSwatch() != null)
                        holder.materialCardView.setCardBackgroundColor(palette.getLightMutedColor(Color.BLACK));

                    holder.readMore.setSupportBackgroundTintList(ColorStateList.valueOf(palette.getDarkVibrantColor(palette.getDominantColor(Color.BLUE))));
                    if (vibrantSwatch != null) holder.readMore.setTextColor(vibrantSwatch.getTitleTextColor());

                });
                return false;
            }
        }).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView textView;
        AppCompatImageView imageView;
        AppCompatButton readMore;
        MaterialCardView materialCardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.detail_text);
            imageView = itemView.findViewById(R.id.image_view);
            readMore = itemView.findViewById(R.id.read_more);
            materialCardView = itemView.findViewById(R.id.main_card_view);
        }
    }
}
