package com.example.footbeat

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

import co.tryterra.terrartandroid.TerraRT
import co.tryterra.terrartandroid.enums.Connections
import co.tryterra.terrartandroid.enums.DataTypes
import java.io.InputStream


class FootBeatActivity : AppCompatActivity() {
    private lateinit var terraRT: TerraRT
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)

    private var heightView: EditText? = null
    private var paceMinView: EditText? = null
    private var paceSecView: EditText? = null

    private var errorMessageTextView: TextView? = null

    private lateinit var submitButton: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        heightView = findViewById(R.id.editTextHeight)
        paceMinView = findViewById(R.id.editTextMinutes)
        paceSecView = findViewById(R.id.editTextSeconds)

        errorMessageTextView = findViewById(R.id.errorMessageLabel)
        errorMessageTextView?.visibility = TextView.GONE

        terraRT = TerraRT(
            DEVID,
            this,
            "REFERENCE_ID"
        ){
            //Generate an SDK token: https://docs.tryterra.co/reference/generate-authentication-token
            terraRT.initConnection("250c68b9c21b78e40e7a3285a2d538d3bc24aabd3b4c76a782fb0a571ca4501d"){ }
        }

        submitButton = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            // Handle button click here
            val height = heightView?.text.toString().toIntOrNull()
            val minutes = paceMinView?.text.toString().toIntOrNull()
            val seconds = paceSecView?.text.toString().toIntOrNull()

            if (height == null || minutes == null || seconds == null) {
                errorMessageTextView?.visibility = TextView.VISIBLE
            } else {
                calculateCadence(height, minutes, seconds)
            }
            val stride_length = height * 0.415
            val pace = minutes + (seconds/60)
            val stride = 1 / (1000 * stride_length * pace)
        }
    }

    private fun calculateCadence(height:Int, minutes:Int, seconds:Int) {
        val stride_length = height * 0.45
        val pace = minutes + (seconds/60)
        val stride = 1 / (1000 * stride_length * pace)

        fun readCsv(inputStream: InputStream): List<Song> {
            val reader = inputStream.bufferedReader()
            val header = reader.readLine()
            return reader.lineSequence()
                .filter { it.isNotBlank() }
                .map {
                    val (track_id,
                        track_name,
                        track_artist,
                        track_album_id,
                        tempo,
                        duration_ms) = it.split(',', ignoreCase = false)
                    Song(track_id, track_name)
                }.toList()
        }
        val movies = readCsv(/*Open a stream to CSV file*/)

        val countAsText = "HI"
        message!!.text = String.format("You touched the droid %s", countAsText)
    }
}