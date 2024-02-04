package com.example.footbeat

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

import java.io.InputStream
import kotlin.math.abs


class FootBeatActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)

    private var heightView: EditText? = null
    private var paceMinView: EditText? = null
    private var paceSecView: EditText? = null

    private var songTitleTextView: TextView? = null
    private var bpmTextView: TextView? = null

    private var errorMessageTextView: TextView? = null

    private lateinit var submitButton: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        heightView = findViewById(R.id.editTextHeight)
        paceMinView = findViewById(R.id.editTextMinutes)
        paceSecView = findViewById(R.id.editTextSeconds)

        songTitleTextView = findViewById(R.id.songTitleTextView)
        bpmTextView = findViewById(R.id.bpmTextView)

        errorMessageTextView = findViewById(R.id.errorMessageLabel)
        errorMessageTextView?.visibility = TextView.GONE


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

        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculateCadence(height:Int, minutes:Int, seconds:Int) {

        val stride_length = height * 0.45
        val pace = minutes + (seconds/60)
        val stride = 1 / (1000 * stride_length * pace)

        val csvResourceId = R.raw.music
        val inputStream: InputStream = resources.openRawResource(csvResourceId)

        val recommendation = findClosestTrack(readCsv(inputStream), stride/2)

        if (songTitleTextView == null || bpmTextView == null){
            songTitleTextView!!.text = "Not found"
            bpmTextView!!.text = "---"
        } else {
            songTitleTextView!!.text = String.format(recommendation!!.track_name)
            bpmTextView!!.text = String.format(recommendation.tempo.toString())
        }

    }

    fun readCsv(inputStream: InputStream): List<Song> {
        val reader = inputStream.bufferedReader()
        val header = reader.readLine()

        val trackList = mutableListOf<Song>()
        var line: String?

        while (reader.readLine().also { line = it } != null) {
            val columns = line!!.split(",")

            val track = Song(
                track_id = columns[1],
                track_name = columns[2],
                track_artist = columns[3],
                track_album_id = columns[4],
                tempo = columns[5].toDouble(),
                duration_ms = columns[6].toInt()
            )

            trackList.add(track)
        }
        return trackList
    }

    fun findClosestTrack(trackList: List<Song>, targetTempo: Double): Song? {
        return trackList.minByOrNull { abs(it.tempo - targetTempo) }
    }
}