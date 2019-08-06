package com.skb.duhui.simpledrawview

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class SimpleDrawView(context: Context, attr: AttributeSet): View(context, attr) {

    private var strokeWidth = 12f

    private var prevPathBitmap: Bitmap? = null
    private var prevPathCanvas: Canvas? = null
    private val prevPathPaint: Paint = Paint(Paint.DITHER_FLAG)
    private val currentPath: Path = Path()
    private val currentPaint: Paint = Paint()

    private var prevX: Float = 0f
    private var prevY: Float = 0f


    init {
        currentPaint.isAntiAlias = true
        currentPaint.isDither = true
        currentPaint.color = Color.GREEN
        currentPaint.style = Paint.Style.STROKE
        currentPaint.strokeJoin = Paint.Join.ROUND
        currentPaint.strokeCap = Paint.Cap.ROUND
        currentPaint.strokeWidth = strokeWidth
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initDraw()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas!!.drawBitmap(prevPathBitmap!!, 0f, 0f, prevPathPaint)
        canvas.drawPath(currentPath, currentPaint)
    }

    fun drawEvent(event: MotionEvent) {
        val x = coordinateX(event.x)
        val y = coordinateY(event.y)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startDraw(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                if (isTolerant(x, y)) {
                    recordDraw(x, y)
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                commitDraw()
                invalidate()
            }
        }
    }

    private fun coordinateX(x: Float): Float {
        val location = IntArray(2)
        getLocationInWindow(location)
        Log.d("Loc", "x : " + x + "LOC X : " + location[0])
        return x - location[0].toFloat()
    }

    private fun coordinateY(y: Float): Float {
        val location = IntArray(2)
        getLocationInWindow(location)
        Log.d("Loc", "y : " + y + "LOC Y : " + location[1])
        return y - location[1].toFloat()
    }



    private fun startDraw(x: Float, y: Float) {
        currentPath.moveTo(x, y)
        prevX = x
        prevY = y
    }

    private fun recordDraw(x: Float, y: Float) {
        currentPath.quadTo(prevX, prevY, x, y)
        prevX = x
        prevY = y
    }

    private fun isTolerant(x: Float, y: Float): Boolean {
        return Math.abs(x-prevX) >= strokeWidth || Math.abs(y-prevY) >= strokeWidth
    }

    private fun commitDraw() {
        currentPath.lineTo(prevX, prevY)
        prevPathCanvas!!.drawPath(currentPath, currentPaint)
        currentPath.reset()
    }

    private fun initDraw() {
        prevPathBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        prevPathCanvas = Canvas(prevPathBitmap!!)
    }

    fun erase() {
        initDraw()
        invalidate()
    }

    fun setColor(color: Int) {
        currentPaint.color = color
    }

    @TargetApi(26)
    fun setColor(red: Float, green: Float, blue: Float) {
        currentPaint.color = Color.rgb(red, green, blue)
    }

    @TargetApi(26)
    fun setColor(red: Int, green: Int, blue: Int) {
        currentPaint.color = Color.rgb(red, green, blue)
    }

    @TargetApi(26)
    fun setColor(alpha: Float, red: Float, green: Float, blue: Float) {
        currentPaint.color = Color.argb(alpha, red, green, blue)
    }

    @TargetApi(26)
    fun setColor(alpha: Int, red: Int, green: Int, blue: Int) {
        currentPaint.color = Color.argb(alpha, red, green, blue)
    }

    fun setStrokeWidth(strokeWidth: Float) {
        this.strokeWidth = strokeWidth
        currentPaint.strokeWidth = strokeWidth
    }
}