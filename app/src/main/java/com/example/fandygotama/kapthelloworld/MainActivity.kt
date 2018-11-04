package com.example.fandygotama.kapthelloworld

import android.app.Activity
import android.os.Bundle
import com.example.annotation.GenName
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: Activity() {

    @GenName
    class Hello

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        go.setOnClickListener {

            Navigator.startAnotherActivity(this, 5, "value")

            /*
            val activity = AnotherActivity()

            val annos = activity.javaClass.annotations

            try {
                val fieldAnnotations = activity.javaClass.getDeclaredField("anotherId").annotations

                fieldAnnotations.forEach { annotation ->
                    Log.d("Kapt", "ANNOTATION: " + annotation)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            */
        }
    }
}