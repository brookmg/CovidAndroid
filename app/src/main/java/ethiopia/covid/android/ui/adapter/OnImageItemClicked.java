package ethiopia.covid.android.ui.adapter;

import android.widget.ImageView;

import java.util.List;

/**
 * Created by BrookMG on 10/22/2019 in ethiopia.covid.android.ui.adapters
 * inside the project CoVidEt .
 */
public interface OnImageItemClicked {
    void OnImageClicked(ImageView imageView , List<String> clickedImageUri);
}
