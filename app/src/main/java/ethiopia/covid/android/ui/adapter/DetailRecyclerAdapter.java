package ethiopia.covid.android.ui.adapter;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.card.MaterialCardView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import ethiopia.covid.android.R;
import ethiopia.covid.android.data.DetailItem;
import ethiopia.covid.android.util.Utils;

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.adapter
 * inside the project CoVidEt .
 */
public class DetailRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TEXT_TYPE = 0;
    private static final int Q_ATYPE = 1;

    private List<DetailItem> details;

    public DetailRecyclerAdapter(List<DetailItem> details) {
        this.details = details;
    }

    public void addContent(List<DetailItem> items) {
        int previousPosition = Math.max(0 , details.size() - 1);
        details.addAll(items);
        int nowPosition = Math.max(0 , details.size() - 1);
        notifyItemRangeInserted(previousPosition , nowPosition);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TEXT_TYPE:        return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.detail_recycler_element , parent , false));
            case Q_ATYPE:        return new FAQViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.detail_faq_recycler_element , parent , false));
            default:        return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.detail_recycler_element , parent , false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TEXT_TYPE) ((ViewHolder) holder).bind(details.get(position));
        else if (getItemViewType(position) == Q_ATYPE) ((FAQViewHolder) holder).bind(details.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return details.get(position).getFaqItem() == null ? TEXT_TYPE : Q_ATYPE;
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

        void bind(DetailItem item) {

            textView.setText(Html.fromHtml(item.getTitle()));
            readMore.setVisibility(item.isHasMoreButton() ? View.VISIBLE : View.GONE);

            if (Utils.getCurrentTheme(itemView.getContext()) == 0) {
                // Light theme
                textView.setTextColor(ContextCompat.getColor(itemView.getContext() , R.color.black_1));
                materialCardView.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext() , R.color.white_0));
            } else {
                textView.setTextColor(ContextCompat.getColor(itemView.getContext() , R.color.white_1));
                materialCardView.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext() , R.color.black_2));
            }

            if (item.getImageLink().equals("") &&
                    item.getImageResource() == -1) {
                imageView.setVisibility(View.GONE);
            } else {
                imageView.setVisibility(View.VISIBLE);

                Glide.with(imageView).asBitmap().load(
                        item.getImageResource() != -1 ?
                                item.getImageResource() :
                                item.getImageLink()
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

                            if (mutedColor != null)
                                textView.setTextColor(mutedColor.getTitleTextColor());

                            if (palette.getLightMutedSwatch() != null)
                                materialCardView.setCardBackgroundColor(palette.getLightMutedColor(Color.BLACK));

                            readMore.setSupportBackgroundTintList(ColorStateList.valueOf(palette.getDarkVibrantColor(palette.getDominantColor(Color.BLUE))));
                            if (vibrantSwatch != null)
                                readMore.setTextColor(vibrantSwatch.getTitleTextColor());

                        });
                        return false;
                    }
                }).into(imageView);
            }
        }
    }

    static class FAQViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView questionText;
        AppCompatTextView answerText;
        MaterialCardView materialCardView;
        ExpandableLayout expandableLayout;

        FAQViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.question_text);
            answerText = itemView.findViewById(R.id.answer_text);
            expandableLayout = itemView.findViewById(R.id.expandable_container);
            materialCardView = itemView.findViewById(R.id.main_card_view);
        }

        void bind(DetailItem item) {

            materialCardView.setOnClickListener(v -> expandableLayout.toggle(true));
            questionText.setText(Html.fromHtml(item.getFaqItem().getQuestion()));
            answerText.setText(Html.fromHtml(item.getFaqItem().getAnswer()));

            if (Utils.getCurrentTheme(itemView.getContext()) == 0) {
                // Light theme
                questionText.setTextColor(ContextCompat.getColor(itemView.getContext() , R.color.black_1));
                answerText.setTextColor(ContextCompat.getColor(itemView.getContext() , R.color.black_1));
                materialCardView.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext() , R.color.white_0));
            } else {
                questionText.setTextColor(ContextCompat.getColor(itemView.getContext() , R.color.white_1));
                answerText.setTextColor(ContextCompat.getColor(itemView.getContext() , R.color.white_1));
                materialCardView.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext() , R.color.black_2));
            }

        }
    }
}
