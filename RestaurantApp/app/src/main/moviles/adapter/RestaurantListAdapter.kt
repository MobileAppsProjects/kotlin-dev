package com.example.moviles.adapter

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviles.R
import com.example.moviles.databinding.RecyclerRestaurantListRowBinding
import com.example.moviles.models.Hours
import com.example.moviles.models.RestaurantModel
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RestaurantListAdapter(val restaurantList: List<RestaurantModel?>?) : RecyclerView.Adapter<RestaurantListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RecyclerRestaurantListRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val restaurant = restaurantList?.get(position)
        restaurant?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
        return restaurantList?.size ?: 0
    }

    class MyViewHolder(private val binding: RecyclerRestaurantListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurantModel: RestaurantModel?) {
            binding.tvRestaurantName.text = restaurantModel?.name
            binding.tvRestaurantAddress.text = "Address: " + restaurantModel?.address
            binding.tvRestaurantHours.text = "Today's Hours: " + getTodaysHours(restaurantModel?.hours!!)

            Glide.with(binding.root)
                .load(restaurantModel.image)
                .into(binding.thumbImage)
        }


        private fun getTodaysHours(hours: Hours): String? {
            val calendar: Calendar = Calendar.getInstance()
            val date: Date = calendar.time
            val day: String = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)
            return when (day) {
                "Sunday" -> hours.Sunday
                "Monday" -> hours.Monday
                "Tuesday" -> hours.Tuesday
                "Wednesday" -> hours.Wednesday
                "Thursday" -> hours.Thursday
                "Friday" -> hours.Friday
                "Saturday" -> hours.Saturday
                else -> hours.Sunday
            }
        }
    }
}
