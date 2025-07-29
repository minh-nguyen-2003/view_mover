package vn.minh_nguyen.vkey.view_mover

import android.animation.ValueAnimator
import android.view.View

class ViewMover private constructor(private val viewX: View) {

    private var viewA: View? = null
    private var viewB: View? = null
    private var animator: ValueAnimator? = null
    private var duration: Long = 1000L

    companion object {
        fun move(viewX: View): ViewMover {
            return ViewMover(viewX)
        }
    }

    fun from(viewA: View): ViewMover {
        this.viewA = viewA
        return this
    }

    fun to(viewB: View): ViewMover {
        this.viewB = viewB
        return this
    }

    fun duration(duration: Long): ViewMover {
        this.duration = duration
        return this
    }

    // Bắt đầu di chuyển viewX từ viewA tới viewB
    fun start() {
        val startView = viewA ?: return
        val endView = viewB ?: return

        animator?.cancel()

        // Tính vị trí bắt đầu và kết thúc
        val startX = startView.x + startView.width / 2 - viewX.width / 2
        val startY = startView.y + startView.height / 2 - viewX.height / 2
        val endX = endView.x + endView.width / 2 - viewX.width / 2
        val endY = endView.y + endView.height / 2 - viewX.height / 2

        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            this.duration = this@ViewMover.duration
            addUpdateListener { valueAnimator ->
                val fraction = valueAnimator.animatedValue as Float

                viewX.x = startX + (endX - startX) * fraction
                viewX.y = startY + (endY - startY) * fraction
            }
            start()
        }
    }

    // Dừng animation
    fun stop() {
        animator?.cancel()
    }

    // Ẩn view đang di chuyển
    fun hide() {
        viewX.visibility = View.GONE
    }

    // Hiện view đang di chuyển
    fun show() {
        viewX.visibility = View.VISIBLE
    }
}