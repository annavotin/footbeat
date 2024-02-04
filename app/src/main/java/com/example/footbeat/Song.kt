package com.example.footbeat

class Song(val track_id:String,
           val track_name:String,
           val track_artist:String,
           val track_album_id:String,
           val tempo:Double,
           val duration_ms:Int) {

    override fun toString(): String {
        return "Title: $track_name, Tempo: $tempo"
    }

}