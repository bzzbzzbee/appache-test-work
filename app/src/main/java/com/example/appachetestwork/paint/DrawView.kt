package com.example.appachetestwork.paint

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.appachetestwork.DrawViewHelper
import kotlin.math.abs

class DrawView(context: Context?, attrs: AttributeSet? = null) : View(context, attrs) {
    private var mX = 0f
    private var mY = 0f

    private val mPaint: Paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        alpha = 0xff
    }

    private var mPath = Path()
    private lateinit var mBitmap: Bitmap
    private lateinit var mCanvas: Canvas
    private lateinit var helper: DrawViewHelper

    private val mBitmapPaint = Paint(Paint.DITHER_FLAG)

    fun setWidth(width: Int) {
        helper.setWidth(width)
    }

    fun setColor(color: Int) {
        helper.setColor(color)
    }

    fun init(height: Int, width: Int, helper: DrawViewHelper) {
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)
        this.helper = helper
    }

    fun init(bitmap: Bitmap, helper: DrawViewHelper) {
        mCanvas = Canvas(bitmap)
        this.helper = helper
    }

    fun undo() {
        if (helper.paths.isNotEmpty()) {
            val next = helper.removeLastFromPath()
            helper.addToStash(next)
            invalidate()
        }
    }

    fun repeat() {
        if (helper.stashPaths.isNotEmpty()) {
            val last = helper.removeLastFromStash()
            helper.addToPath(last)
            invalidate()
        }
    }

    fun save(): Bitmap = mBitmap

    override fun onDraw(canvas: Canvas) {
        // save the current state of the canvas before,
        // to draw the background of the canvas
        canvas.save()

        mCanvas.drawColor(Color.WHITE)

        // now, we iterate over the list of paths
        // and draw each path on the canvas
        helper.paths.forEach { fp ->
            mPaint.color = fp.color
            mPaint.strokeWidth = fp.strokeWidth.toFloat()
            mCanvas.drawPath(fp.path, mPaint)
        }
        canvas.drawBitmap(mBitmap, 0f, 0f, mBitmapPaint)
        canvas.restore()
    }

    // the below methods manages the touch
    // response of the user on the screen
    // firstly, we create a new Stroke
    // and add it to the paths list
    private fun touchStart(x: Float, y: Float) {
        mPath = Path()

        val fp = Stroke(helper.currentColor, helper.strokeWidth, mPath)
        helper.addToPath(fp)
        helper.clearStash()

        // finally remove any curve
        // or line from the path
        mPath.reset()

        // this methods sets the starting
        // point of the line being drawn
        mPath.moveTo(x, y)

        // we save the current
        // coordinates of the finger
        mX = x
        mY = y
    }

    // in this method we check
    // if the move of finger on the
    // screen is greater than the
    // Tolerance we have previously defined,
    // then we call the quadTo() method which
    // actually smooths the turns we create,
    // by calculating the mean position between
    // the previous position and current position
    private fun touchMove(x: Float, y: Float) {
        val dx = abs(x - mX)
        val dy = abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    // at the end, we call the lineTo method
    // which simply draws the line until
    // the end position
    private fun touchUp() = mPath.lineTo(mX, mY)

    // the onTouchEvent() method provides us with
    // the information about the type of motion
    // which has been taken place, and according
    // to that we call our desired methods
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }
        return true
    }

    fun clear() {
        helper.setColor(Color.WHITE)
        helper.setWidth(50)
    }

    companion object {
        private const val TOUCH_TOLERANCE = 4f
    }
}