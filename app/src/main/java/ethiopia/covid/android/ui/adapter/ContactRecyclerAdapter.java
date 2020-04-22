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
import ethiopia.covid.android.data.ContactItem;
import ethiopia.covid.android.data.DetailItem;
import ethiopia.covid.android.util.Utils;

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.adapter
 * inside the project CoVidEt .
 */
public class ContactRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TEXT_TYPE = 0;
    private List<ContactItem> contactItems;

    public ContactRecyclerAdapter(List<ContactItem> contactItems) {
        this.contactItems = contactItems;
    }

    public void addContent(List<ContactItem> items) {
        int previousPosition = Math.max(0 , contactItems.size() - 1);
        contactItems.addAll(items);
        int nowPosition = Math.max(0 , contactItems.size() - 1);
        notifyItemRangeInserted(previousPosition , nowPosition);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TEXT_TYPE:        return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.regional_contact_recycler_element , parent , false));
            default:        return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.regional_contact_recycler_element , parent , false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TEXT_TYPE) ((ViewHolder) holder).bind(contactItems.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return TEXT_TYPE;
    }

    @Override
    public int getItemCount() {
        return contactItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView textView;
        AppCompatImageView imageView;
        MaterialCardView materialCardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.detail_text);
            imageView = itemView.findViewById(R.id.image_view);
            materialCardView = itemView.findViewById(R.id.main_card_view);
        }

        void bind(ContactItem item) {

            textView.setText(Html.fromHtml(item.getRegionPhoneNumber()));

            if (Utils.getCurrentTheme(itemView.getContext()) == 0) {
                // Light theme
                textView.setTextColor(ContextCompat.getColor(itemView.getContext() , R.color.black_1));
                materialCardView.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext() , R.color.white_0));
            } else {
                textView.setTextColor(ContextCompat.getColor(itemView.getContext() , R.color.white_1));
                materialCardView.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext() , R.color.black_2));
            }

            if (item.getRegionFlagImageLink() == -1) {
                imageView.setVisibility(View.GONE);
            } else {
                imageView.setVisibility(View.VISIBLE);
                Glide.with(imageView).asBitmap().load(item.getRegionFlagImageLink()).into(imageView);
            }
        }
    }
}
