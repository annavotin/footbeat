package com.example.footbeat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class FootBeatActivity : AppCompatActivity() {

    private var heightView: EditText? = null
    private var paceMinView: EditText? = null
    private var paceSecView: EditText? = null

    private lateinit var submitButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        heightView = findViewById(R.id.editTextHeight)
        paceMinView = findViewById(R.id.editTextMinutes)
        paceSecView = findViewById(R.id.editTextSeconds)

        submitButton = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            // Handle button click here
            val height = heightView.text.toString()
            val minutes = paceMinView.text.toString()
            val seconds = paceSecView.text.toString()

            // Perform any actions you want with the entered data
            // For example, you can print the values to the log
            println("Height: $height, Minutes: $minutes, Seconds: $seconds")
        }
    }

    private fun tapDroid() {
        counter++
        val countAsText = when (counter) {
            1 -> "once"
            2 -> "twice"
            else -> String.format("%d times", counter)
        }
        message!!.text = String.format("You touched the droid %s", countAsText)
    }
}