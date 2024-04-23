package com.example.moviles

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviles.adapter.RestaurantListAdapter
import com.example.moviles.models.RestaurantModel
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringWriter


class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val restaurantModel = getRestaurantData()
        initRecyclerView(restaurantModel)
    }

    private fun initRecyclerView(restaurantList: List<RestaurantModel?>?){
        val recyclerViewRestaurant = findViewById<RecyclerView>(R.id.recyclerViewRestaurant)
        recyclerViewRestaurant.layoutManager = LinearLayoutManager(this)
        val adapter = RestaurantListAdapter(restaurantList)
        recyclerViewRestaurant.adapter = adapter
    }



    private fun getRestaurantData(): List<RestaurantModel?>?{
        val InputStream: InputStream = resources.openRawResource(R.raw.restaurant)
        val writer = StringWriter()
        val buffer = CharArray(1024)
        try{
           val reader: Reader = BufferedReader(InputStreamReader(InputStream, "UTF-8"))
            var n: Int
                    while (reader.read(buffer).also {n = it} != -1) {
                        writer.write(buffer, 0, n)
                    }
        }catch (e: Exception){}
        val jsonStr: String = writer.toString()
        val gson = Gson()
        val restaurantModel = gson.fromJson<Array<RestaurantModel>>(jsonStr, Array<RestaurantModel>::class.java).toList()
        return restaurantModel;
    }
}