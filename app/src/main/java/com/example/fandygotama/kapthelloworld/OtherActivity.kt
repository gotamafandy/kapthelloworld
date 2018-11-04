package com.example.fandygotama.kapthelloworld

import android.app.Activity
import android.os.Bundle
import com.example.annotation.NewIntent

@NewIntent
class OtherActivity: Activity() {

    @Suppress
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_other)
    }


}