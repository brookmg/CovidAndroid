package ethiopia.covid.android.ui.adapter

import android.widget.ImageView

/**
 * Created by BrookMG on 10/22/2019 in ethiopia.covid.android.ui.adapters
 * inside the project CoVidEt .
 */
interface OnImageItemClicked {
    fun onImageClicked(imageView: ImageView?, clickedImageUri: List<String?>?)
}