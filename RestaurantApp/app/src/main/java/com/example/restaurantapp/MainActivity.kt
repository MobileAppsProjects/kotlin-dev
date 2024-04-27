package com.example.restaurantapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.adapter.RestaurantListAdapter
import com.example.restaurantapp.models.RestaurantModel
import com.google.gson.Gson
import java.io.*

class MainActivity : AppCompatActivity(), RestaurantListAdapter.RestaurantListClickListener {

    private lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkService = NetworkService(this)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setTitle("Restaurant List")

        if (networkService.isConnected()) {
            val restaurantModel = getRestaurantData()
            initRecyclerView(restaurantModel)
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
        }

        // Monitorear cambios en la conectividad
        networkService.registerNetworkReceiver { isConnected ->
            if (isConnected) {
                val restaurantModel = getRestaurantData()
                initRecyclerView(restaurantModel)
            } else {
                Toast.makeText(this, "Connection lost", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initRecyclerView(restaurantList: List<RestaurantModel?>?) {
        val recyclerViewRestaurant = findViewById<RecyclerView>(R.id.recyclerViewRestaurant)
        recyclerViewRestaurant.layoutManager = LinearLayoutManager(this)
        val adapter = RestaurantListAdapter(restaurantList, this)
        recyclerViewRestaurant.adapter = adapter
    }

    private fun getRestaurantData(): List<RestaurantModel?>? {
        val inputStream: InputStream = resources.openRawResource(R.raw.restaurant)
        val writer: Writer = StringWriter()
        val buffer = CharArray(1024)
        try {
            val reader: Reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var n: Int
            while (reader.read(buffer).also { n = it } != -1) {
                writer.write(buffer, 0, n)
            }
        } catch (e: Exception) {
        }
        val jsonStr: String = writer.toString()
        val gson = Gson()
        val restaurantModel = gson.fromJson<Array<RestaurantModel>>(jsonStr, Array<RestaurantModel>::class.java).toList()
        return restaurantModel
    }

    override fun onItemClick(restaurantModel: RestaurantModel) {
        val intent = Intent(this@MainActivity, RestaurantMenuActivity::class.java)
        intent.putExtra("RestaurantModel", restaurantModel)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        networkService.unregisterNetworkReceiver()
    }
}
