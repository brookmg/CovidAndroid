package ethiopia.covid.android.data

import android.view.View

/**
 * Created by BrookMG on 3/24/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
class DetailItem {
    var title: String? = null
    var imageLink: String? = null
    var isHasMoreButton = false
    var text: String? = null
    var imageResource = -1
    var moreButtonClickListener: View.OnClickListener? = null
    var faqItem: FAQ.QuestionItem? = null

    @JvmOverloads
    constructor(title: String?, imageResource: Int, hasMoreButton: Boolean = false, text: String? = "", moreButtonClickListener: View.OnClickListener? = null) :
            this(title, "", hasMoreButton, text, imageResource, moreButtonClickListener)

    @JvmOverloads
    constructor(title: String?, imageLink: String?, hasMoreButton: Boolean = false, text: String? = "", moreButtonClickListener: View.OnClickListener? = null) :
            this(title, imageLink, hasMoreButton, text, -1, moreButtonClickListener)

    constructor(title: String?, imageLink: String?, hasMoreButton: Boolean, text: String?, imageResource: Int, moreButtonClickListener: View.OnClickListener?) {
        this.title = title
        this.imageLink = imageLink
        isHasMoreButton = hasMoreButton
        this.text = text
        this.imageResource = imageResource
        this.moreButtonClickListener = moreButtonClickListener
    }

    constructor(faqItem: FAQ.QuestionItem?) {
        this.faqItem = faqItem
    }

}