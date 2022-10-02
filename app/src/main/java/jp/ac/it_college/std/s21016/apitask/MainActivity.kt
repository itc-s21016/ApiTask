package jp.ac.it_college.std.s21016.apitask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import jp.ac.it_college.std.s21016.apitask.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.sql.Types


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val apiKey = "318a1003a75ce344"
        val mainUrl = "http://webservice.recruit.co.jp/hotpepper/gourmet/v1/"
        val Coordinate = "lat=26.21086311681577&lng=127.68606472621535&range=2&order=1&format=json"

        val btnStore: Button = findViewById(R.id.btnStore)

        btnStore.setOnClickListener {
            val StoreUrl = "$mainUrl&$apiKey&$Coordinate"
            StoreTask(StoreUrl)
        }
    }

    private fun StoreTask(StoreUrl: String) {

        lifecycleScope.launch {
            val result = storeBackgroundTask(StoreUrl)

            storeJsonTask(result)
        }
    }

    private suspend fun storeBackgroundTask(storeUrl: String): String {
        val response = withContext(Dispatchers.IO) {
            var httpResult = ""

            try {
                val urlObj = URL(storeUrl)

                val br = BufferedReader(InputStreamReader(urlObj.openStream()))

                httpResult = br.readText()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return@withContext httpResult
        }

        return response
    }

    private fun storeJsonTask(result: String) {
        val tvStoreName: TextView = findViewById(R.id.tvStoreName)

        val jsonObj = JSONObject(result)

        val storeName = jsonObj.getString("name")
        tvStoreName.text = storeName
    }
}
