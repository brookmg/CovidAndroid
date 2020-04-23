/*
 *           Copyright (c) 2019 Brook Mezgebu
 *           Permission is hereby granted, free of charge, to any person obtaining
 *           a copy of this software and associated documentation files (the
 *           "Software"), to deal in the Software without restriction, including
 *           without limitation the rights to use, copy, modify, merge, publish,
 *           distribute, sublicense, and/or sell copies of the Software, and to
 *           permit persons to whom the Software is furnished to do so, subject to
 *           the following conditions:
 *
 *           The above copyright notice and this permission notice shall be
 *           included in all copies or substantial portions of the Software.
 *
 *           THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *           EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *           MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *           NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *           LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *           OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *           WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ethiopia.covid.android.ui.adapter;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by BrookMG on 3/7/2019 in ethiopia.covid.android.ui.adapters
 * inside the project CovidEt .
 */
public class ScrollingLinearLayoutManager extends LinearLayoutManager {

    private int duration;

    public ScrollingLinearLayoutManager(Context context, int orientation, boolean reverseLayout, int duration) {
        super(context, orientation, reverseLayout);
        this.duration = duration;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        View firstVisibleChild = recyclerView.getChildAt(0);
        int itemWidth = firstVisibleChild.getWidth();
        int currentPosition = recyclerView.getChildLayoutPosition(firstVisibleChild);
        int distanceInPx = Math.abs((currentPosition - position) * itemWidth);

        distanceInPx = (distanceInPx == 0) ? (int) Math.abs(firstVisibleChild.getY()) : distanceInPx;
        SmoothScroller smoothScroller = new SmoothScroller(recyclerView.getContext(), distanceInPx, duration);
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }
}
