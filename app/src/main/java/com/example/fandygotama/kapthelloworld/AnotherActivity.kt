package com.example.fandygotama.kapthelloworld

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.example.annotation.IntentParam
import com.example.annotation.NewIntent

@NewIntent
class AnotherActivity: Activity() {

    @IntentParam("ID")
    var anotherId: Int? = 0

    @IntentParam("NAME")
    var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_other)

        val bundle = intent.extras

        val id = bundle?.getInt("ID");
        val name = bundle?.getString("NAME");

        Log.d("KAPT", "ID: " + id + ", NAME: " + name)
    }
}