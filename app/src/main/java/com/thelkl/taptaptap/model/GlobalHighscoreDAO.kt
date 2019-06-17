package com.thelkl.taptaptap.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.thelkl.taptaptap.Record
import com.thelkl.taptaptap.utils.LOG_TAG
import com.thelkl.taptaptap.utils.SERVER_URL
import org.json.JSONArray
import org.json.JSONObject

private const val REQUEST_TAG = "GlobalHighscore"

class GlobalHighscoreDAO(context: Context) {
    private val highscoreRecordArray = ArrayList<Record>()
    private val globalHighscores = MutableLiveData<ArrayList<Record>>()
    private val requestQueue = Volley.newRequestQueue(context)

    init {
        globalHighscores.value = highscoreRecordArray
    }

    fun cancelRequests() {
        requestQueue.cancelAll(REQUEST_TAG)
    }

    // Sends a GET request for global highscores
    fun refreshData() {
        val url = SERVER_URL

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                Log.d(LOG_TAG, "Global highscores fetched successfully")
                val array = JSONArray(response)
                highscoreRecordArray.clear()
                (0 until array.length()).forEach { i ->
                    val taps = (array[i] as JSONObject).get("taps").toString()
                    val timestamp = (array[i] as JSONObject).get("timestamp").toString()
                    highscoreRecordArray.add(Record(timestampText = timestamp, scoreText =  taps))
                }
                globalHighscores.value = highscoreRecordArray
            },
            Response.ErrorListener { res ->
                Log.e(LOG_TAG, "Failed to fetch global highscores: $res")
            }).apply { this.tag = REQUEST_TAG }

        requestQueue.add(stringRequest)
    }

    // Sends a POST request to the server, with a given highscore
    fun addHighscore(record: Record) {
        val json = JSONObject()
        json.put("taps", record.scoreText)
        json.put("timestamp", record.timestampText)
        json.put("nickname", "PLACEHOLDER")
        val url = "$SERVER_URL/addRecord"

        val jsonRequest = JsonObjectRequest(
            Request.Method.POST, url, json,
            Response.Listener<JSONObject> {
                Log.d(LOG_TAG, "Highscore sent successfully")
                refreshData()
            },
            Response.ErrorListener { res ->
                Log.e(LOG_TAG, "Failed to send a highscore: $res")
            }).apply { this.tag = REQUEST_TAG }

        requestQueue.add(jsonRequest)
    }

    fun getHighscores() = globalHighscores as LiveData<ArrayList<Record>>
}