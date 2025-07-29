package vn.minh_nguyen.vkey.view_mover

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class ViewMover private constructor(private val viewMove: View) {

    private var viewA: View? = null
    private var viewB: View? = null
    private var animator: ValueAnimator? = null
    private var duration: Long = 1000L

    private var selfAnchor: Point = Point.CENTER    // điểm trên viewMove
    private var targetPoint: Point = Point.CENTER  // điểm trên target view

    enum class Point {
        CENTER,
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    companion object {
        fun move(viewMove: View): ViewMover = ViewMover(viewMove)
    }

    /**
     * Chỉ định view xuất phát (điểm bắt đầu). Nếu không có nó sẽ xuất phát từ vị trí hiện tại của viewMove
     * @param viewA View mà viewMove sẽ bắt đầu di chuyển từ đó.
     */
    fun from(viewA: View) = apply { this.viewA = viewA }

    /**
     * Chỉ định view đích (điểm kết thúc).
     * @param viewB View mà viewMove sẽ di chuyển tới.
     */
    fun to(viewB: View) = apply { this.viewB = viewB }

    /**
     * Đặt điểm neo (anchor) trên viewMove sẽ gắn vào targetPoint của view đích.
     * @param selfPoint Điểm trên viewMove (ví dụ: TOP_LEFT, CENTER...)
     */
    fun anchor(selfPoint: Point) = apply { this.selfAnchor = selfPoint }

    /**
     * Xác định điểm trên view đích mà viewMove sẽ gắn vào.
     * @param targetPoint Điểm trên view đích (ví dụ: CENTER, BOTTOM_RIGHT...)
     */
    fun attachTo(targetPoint: Point) = apply { this.targetPoint = targetPoint }
    fun duration(duration: Long) = apply { this.duration = duration }

    /**
     *  Di chuyển từ A đến B:
     *  - Nếu viewMove đang nằm trên A → bắt đầu từ vị trí hiện tại của viewMove
     *  - Nếu viewMove không nằm trên A → nhảy về A rồi mới đi đến B
     *  - Nếu không chỉ định A → bắt đầu từ vị trí hiện tại của viewMove
     */
    fun start() {
        val endView = viewB ?: return
        animator?.cancel()

        viewMove.post {
            val startPoint: Pair<Float, Float>

            if (viewA != null) {
                // Kiểm tra xem viewMove có đang đè lên viewA không
                if (isOverlapping(viewMove, viewA!!)) {
                    // Đang đè lên viewA → di chuyển từ vị trí hiện tại
                    startPoint = viewMove.x to viewMove.y
                } else {
                    // Không đè lên viewA → đặt viewMove về viewA trước khi bay
                    val start = getPosition(viewA!!, targetPoint, selfAnchor)
                    viewMove.x = start.first
                    viewMove.y = start.second
                    startPoint = start
                }
            } else {
                // Không có viewA → bay từ vị trí hiện tại
                startPoint = viewMove.x to viewMove.y
            }

            // Điểm kết thúc
            val endPoint = getPosition(endView, targetPoint, selfAnchor)
            runAnimation(startPoint, endPoint)
        }
    }

    /**
     * Di chuyển từ vị trí hiện tại của viewMove tới điểm mục tiêu
     */
    fun startSmooth() {
        val endView = viewB ?: return
        animator?.cancel()

        viewMove.post {
            val startPoint = viewMove.x to viewMove.y
            val endPoint = getPosition(endView, targetPoint, selfAnchor)
            runAnimation(startPoint, endPoint)
        }
    }

    private fun isOverlapping(view1: View, view2: View): Boolean {
        val rect1 = android.graphics.Rect().apply {
            view1.getHitRect(this)
        }
        val rect2 = android.graphics.Rect().apply {
            view2.getHitRect(this)
        }
        return rect1.intersect(rect2)
    }

    private fun runAnimation(startPoint: Pair<Float, Float>, endPoint: Pair<Float, Float>) {
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = this@ViewMover.duration
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { anim ->
                val fraction = anim.animatedValue as Float
                viewMove.x = startPoint.first + (endPoint.first - startPoint.first) * fraction
                viewMove.y = startPoint.second + (endPoint.second - startPoint.second) * fraction
            }
            start()
        }
    }


    private fun getPosition(
        targetView: View,
        targetPos: Point,
        selfPos: Point
    ): Pair<Float, Float> {
        // Xác định điểm trên target
        val targetPoint = when (targetPos) {
            Point.CENTER -> targetView.x + targetView.width / 2f to targetView.y + targetView.height / 2f
            Point.TOP -> targetView.x + targetView.width / 2f to targetView.y
            Point.BOTTOM -> targetView.x + targetView.width / 2f to targetView.y + targetView.height
            Point.LEFT -> targetView.x to targetView.y + targetView.height / 2f
            Point.RIGHT -> targetView.x + targetView.width to targetView.y + targetView.height / 2f
            Point.TOP_LEFT -> targetView.x to targetView.y
            Point.TOP_RIGHT -> targetView.x + targetView.width to targetView.y
            Point.BOTTOM_LEFT -> targetView.x to targetView.y + targetView.height
            Point.BOTTOM_RIGHT -> targetView.x + targetView.width to targetView.y + targetView.height
        }

        // Offset để “đặt” selfPosition của viewMove vào targetPoint
        val offsetX = when (selfPos) {
            Point.CENTER, Point.TOP, Point.BOTTOM -> viewMove.width / 2f
            Point.LEFT, Point.TOP_LEFT, Point.BOTTOM_LEFT -> 0f
            Point.RIGHT, Point.TOP_RIGHT, Point.BOTTOM_RIGHT -> viewMove.width.toFloat()
        }

        val offsetY = when (selfPos) {
            Point.CENTER, Point.LEFT, Point.RIGHT -> viewMove.height / 2f
            Point.TOP, Point.TOP_LEFT, Point.TOP_RIGHT -> 0f
            Point.BOTTOM, Point.BOTTOM_LEFT, Point.BOTTOM_RIGHT -> viewMove.height.toFloat()
        }

        return targetPoint.first - offsetX to targetPoint.second - offsetY
    }

    /**
     * Tạm dừng animation di chuyển hiện tại (nếu có).
     * - Trên Android 4.4 (API 19) trở lên: gọi `animator.pause()`.
     * - Trên Android thấp hơn: animation sẽ bị hủy (`cancel()`).
     */
    fun pause() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) animator?.pause() else animator?.cancel()
    }

    /**
     * Tiếp tục animation đã bị tạm dừng bằng [pause].
     * - Chỉ hoạt động trên Android 4.4 (API 19) trở lên.
     */
    fun resume() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) animator?.resume()
    }
}
