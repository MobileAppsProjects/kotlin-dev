import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.NetworkService
import com.example.restaurantapp.R
import com.example.restaurantapp.RestaurantMenuActivity
import com.example.restaurantapp.models.RestaurantModel
import com.example.restaurantapp.adapter.RestaurantListAdapter
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*

class MainActivity : AppCompatActivity(), RestaurantListAdapter.RestaurantListClickListener {

    private lateinit var networkService: NetworkService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        networkService = NetworkService(this)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = "Restaurant List"

        // Asynchronously check network status
        networkService.checkNetworkStatusAsync { isConnected ->
            if (isConnected) {
                fetchRestaurantData(true)
            } else {
                fetchRestaurantData(false)
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
            }
        }
        // Monitor connectivity changes
        networkService.registerNetworkReceiver { isConnected ->
            if (isConnected) {
                fetchRestaurantData(true)
            } else {
                Toast.makeText(this, "Connection lost", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun fetchRestaurantData(fetchFromNetwork: Boolean) {
        lifecycleScope.launch {
            val restaurantModel = withContext(Dispatchers.IO) {
                getRestaurantData(fetchFromNetwork)
            }
            initRecyclerView(restaurantModel)
        }
    }
    private fun getRestaurantData(fetchFromNetwork: Boolean): List<RestaurantModel?>? {
        val jsonStr: String?

        if (fetchFromNetwork) {
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
                e.printStackTrace()
            }
            jsonStr = writer.toString()
            saveDataToCache(jsonStr)
        } else {
            jsonStr = readDataFromCache() ?: return null
            Toast.makeText(this, "Loaded from cache", Toast.LENGTH_SHORT).show()
        }

        val gson = Gson()
        return gson.fromJson<Array<RestaurantModel>>(jsonStr, Array<RestaurantModel>::class.java).toList()
    }

    private fun saveDataToCache(data: String) {
        try {
            val file = File(cacheDir, "restaurant_cache.json")
            val writer = BufferedWriter(FileWriter(file))
            writer.use {
                it.write(data)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun readDataFromCache(): String? {
        return try {
            val file = File(cacheDir, "restaurant_cache.json")
            val reader = BufferedReader(FileReader(file))
            val stringBuilder = StringBuilder()
            reader.useLines { lines ->
                lines.forEach { stringBuilder.append(it) }
            }
            stringBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun initRecyclerView(restaurantList: List<RestaurantModel?>?) {
        val recyclerViewRestaurant = findViewById<RecyclerView>(R.id.recyclerViewRestaurant)
        recyclerViewRestaurant.layoutManager = LinearLayoutManager(this)
        val adapter = RestaurantListAdapter(restaurantList, this)
        recyclerViewRestaurant.adapter = adapter
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
