package com.skb.duhui.simpledrawviewsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import com.skb.duhui.simpledrawview.SimpleDrawView

class MainActivity : AppCompatActivity() {

    lateinit var simpleDrawView: SimpleDrawView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        simpleDrawView = findViewById(R.id.simple_draw_view)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        simpleDrawView.drawEvent(event!!)
        return super.onTouchEvent(event)
    }
}
