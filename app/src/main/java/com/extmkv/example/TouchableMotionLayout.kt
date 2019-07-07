package com.extmkv.example

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene

class TouchableMotionLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0)
    : MotionLayout(context, attrs, defStyleAttr), MotionLayout.TransitionListener {

    private val viewRect = Rect()
    private var touchStarted = false

    init {
        initListener()
    }

    private fun initListener() {
        setTransitionListener(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                touchStarted = false
                return super.onTouchEvent(event)
            }
        }

        if (!touchStarted) {
            touchStarted = verifyIfChildHasTouched(event)
        }

        return touchStarted && super.onTouchEvent(event)
    }

    /**
     * Verify if touching one fo the children.
     *
     * @param event The motion event.
     * @return True if touch its in one of the children.
     */
    private fun verifyIfChildHasTouched(event: MotionEvent): Boolean {
        for (index in 0 until childCount) {
            val view = getChildAt(index)
            view.getHitRect(viewRect)
            if (viewRect.contains(event.x.toInt(), event.y.toInt())) {
                return true
            }
        }

        return false
    }

    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
        touchStarted = false
    }

    override fun allowsTransition(p0: MotionScene.Transition?) = true

    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
}