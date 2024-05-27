package com.example.restaurantapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.GridLayoutManager
import com.example.restaurantapp.databinding.ActivityRestaurantMenuBinding
import com.example.restaurantapp.models.Menus
import com.example.restaurantapp.models.RestaurantModel
import com.example.restaurantapp.adapter.MenuListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RestaurantMenuActivity : AppCompatActivity(), MenuListAdapter.MenuListClickListener {

    private var itemsInTheCartList: MutableList<Menus?>? = null
    private var totalItemInCartCount = 0
    private var menuList: List<Menus?>? = null
    private var menuListAdapter: MenuListAdapter? = null
    private lateinit var binding: ActivityRestaurantMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val restaurantModel = intent?.getParcelableExtra<RestaurantModel>("RestaurantModel")

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setTitle(restaurantModel?.name)
        actionBar?.setSubtitle(restaurantModel?.address)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        menuList = restaurantModel?.menus

        // CoroutineScope utiliza para iniciar una nueva corrutina en el contexto principal (main)
        // del hilo de la interfaz de usuario (UI).
        // La función lambda pasada al método se ejecuta de forma asincrónica, pero en el hilo principal,
        // lo que significa que puede interactuar con la interfaz de usuario sin causar bloqueos o problemas de concurrencia.
        CoroutineScope(Dispatchers.Main).launch {
            initRecyclerView(menuList)
            binding.checkoutButton.setOnClickListener {
                if (itemsInTheCartList != null && itemsInTheCartList!!.size <= 0) {
                    Toast.makeText(
                        this@RestaurantMenuActivity,
                        "",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    restaurantModel?.menus = itemsInTheCartList
                    val intent =
                        Intent(this@RestaurantMenuActivity, PlaceYourOrderActivity::class.java)
                    intent.putExtra("RestaurantModel", restaurantModel)
                    startActivityForResult(intent, 1000)
                }
            }
        }

    }
    private suspend fun initRecyclerView(menus: List<Menus?>?) = withContext(Dispatchers.Main) {
        binding.menuRecyclerVuew.layoutManager = GridLayoutManager(this@RestaurantMenuActivity, 2)
        menuListAdapter = MenuListAdapter(menus, this@RestaurantMenuActivity)
        binding.menuRecyclerVuew.adapter = menuListAdapter
    }

    override fun addToCartClickListener(menu: Menus) {
        if(itemsInTheCartList == null) {
            itemsInTheCartList = ArrayList()
        }
        itemsInTheCartList?.add(menu)
        totalItemInCartCount = 0
        for(menu in itemsInTheCartList!!) {
            totalItemInCartCount = totalItemInCartCount + menu?.totalInCart!!
        }
        binding.checkoutButton.text = "Checkout (" + totalItemInCartCount +") Items"

    }

    override fun updateCartClickListener(menu: Menus) {
        val index = itemsInTheCartList!!.indexOf(menu)
        itemsInTheCartList?.removeAt(index)
        itemsInTheCartList?.add(menu)
        totalItemInCartCount = 0
        for(menu in itemsInTheCartList!!) {
            totalItemInCartCount = totalItemInCartCount + menu?.totalInCart!!
        }
        binding.checkoutButton.text = "Checkout (" + totalItemInCartCount +") Items"
    }

    override fun removeFromCartClickListener(menu: Menus) {
        if(itemsInTheCartList!!.contains(menu)) {
            itemsInTheCartList?.remove(menu)
            totalItemInCartCount = 0
            for(menu in itemsInTheCartList!!) {
                totalItemInCartCount = totalItemInCartCount + menu?.totalInCart!!
            }
            binding.checkoutButton.text = "Checkout (" + totalItemInCartCount +") Items"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1000 && resultCode == RESULT_OK) {
            finish()
        }
    }
}