package com.example.restaurantapp;
/**
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.adapter.CategoryAdapter
import com.example.restaurantapp.adapter.FeaturedRestaurantAdapter
import com.example.restaurantapp.databinding.ActivityHomeBinding
import com.example.restaurantapp.models.Category
import com.example.restaurantapp.models.FeaturedRestaurant

 class HomeActivity : AppCompatActivity() {

 private lateinit var binding: ActivityHomeBinding
 private lateinit var categoryAdapter: CategoryAdapter
 private lateinit var featuredRestaurantAdapter: FeaturedRestaurantAdapter

 override fun onCreate(savedInstanceState: Bundle?) {
 super.onCreate(savedInstanceState)
 binding = ActivityHomeBinding.inflate(layoutInflater)
 setContentView(binding.root)

 setupPromotionSection()
 setupSearchBar()
 setupCategoryRecyclerView()
 setupFeaturedRestaurantRecyclerView()
 setupNavigationBar()
 }

 private fun setupPromotionSection() {
 // Configura la sección de promoción
 binding.promotionText.text = "50% off on your first order"
 binding.promotionButton.text = "See restaurant"
 // Agrega lógica adicional si es necesario
 }

 private fun setupSearchBar() {
 // Configura la barra de búsqueda
 binding.searchEditText.hint = "Search for restaurants or food"
 // Agrega lógica de búsqueda si es necesario
 }

 private fun setupCategoryRecyclerView() {
 binding.categoryRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
 categoryAdapter = CategoryAdapter(getCategoryList())
 binding.categoryRecyclerView.adapter = categoryAdapter
 }

 private fun setupFeaturedRestaurantRecyclerView() {
 binding.featuredRestaurantRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
 featuredRestaurantAdapter = FeaturedRestaurantAdapter(getFeaturedRestaurantList())
 binding.featuredRestaurantRecyclerView.adapter = featuredRestaurantAdapter
 }

 private fun setupNavigationBar() {
 // Configura la barra de navegación
 binding.homeButton.setOnClickListener { /* Lógica para el botón Home */
/**}
        binding.mapButton.setOnClickListener { /* Lógica para el botón Map *//** }/**
/**}

private fun getCategoryList(): List<Category> {
    // Retorna una lista de categorías
    return listOf(
            Category("Fast Food"),
            Category("Lunch & Others"),
            Category("Coffee Shops")
    )
}

private fun getFeaturedRestaurantList(): List<FeaturedRestaurant> {
    // Retorna una lista de restaurantes destacados
    return listOf(
            FeaturedRestaurant("One Burrito", "Mexican", true),
            FeaturedRestaurant("El pico de la cabra", "Lunch & Others", true),
            // Agrega más restaurantes destacados según sea necesario
    )
}
 /**}**/
