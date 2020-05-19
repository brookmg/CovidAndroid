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
package ethiopia.covid.android.ui.adapter

import android.content.Context
import android.graphics.PointF
import androidx.recyclerview.widget.LinearSmoothScroller

/**
 * Created by BrookMG on 3/7/2019 in ethiopia.covid.android.ui.adapters
 * inside the project CovidEt .
 */
class SmoothScroller(
        context: Context?,
        private val distanceInPixel: Int,
        private val duration: Int)
    : LinearSmoothScroller(context) {

    override fun calculateTimeForScrolling(dx: Int): Int {
        val propagation = dx / distanceInPixel.toFloat()
        return (duration * propagation).toInt()
    }

}