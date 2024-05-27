package com.example.restaurantapp.localstorage

import Menu
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.Gson

class SQLiteHelperRestaurant(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        private const val DATABASE_NAME = "Database.db"
        private const val TABLE_NAME_RESTAURANTS = "Restaurants"
        private const val TABLE_NAME_MENUS = "Menus"
        private const val ID = "ID"
        private const val NAME = "Name"
        private const val ADDRESS = "Address"
        private const val DELIVERY_CHARGE = "DeliveryCharge"
        private const val HOURS = "Hours"
        private const val MENU_ID = "MenuID"
        private const val PRICE = "Price"
        private const val URL = "URL"
        private const val RESTAURANT_ID = "RestaurantID"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQueryRestaurants =
            "CREATE TABLE $TABLE_NAME_RESTAURANTS ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $NAME TEXT, $ADDRESS TEXT, $DELIVERY_CHARGE INTEGER, $HOURS TEXT)"
        val createTableQueryMenus =
            "CREATE TABLE $TABLE_NAME_MENUS ($MENU_ID INTEGER PRIMARY KEY AUTOINCREMENT, $NAME TEXT, $PRICE REAL, $URL TEXT, $RESTAURANT_ID INTEGER, FOREIGN KEY($RESTAURANT_ID) REFERENCES $TABLE_NAME_RESTAURANTS($ID))"
        db.execSQL(createTableQueryRestaurants)
        db.execSQL(createTableQueryMenus)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_RESTAURANTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_MENUS")
        onCreate(db)
    }

    fun insertRestaurant(name: String, address: String, deliveryCharge: Int, hours: Map<String, String>): Int {
        val db = this.writableDatabase
        val hoursJson = Gson().toJson(hours)
        val values = ContentValues().apply {
            put(NAME, name)
            put(ADDRESS, address)
            put(DELIVERY_CHARGE, deliveryCharge)
            put(HOURS, hoursJson)
        }
        val restaurantId = db.insert(TABLE_NAME_RESTAURANTS, null, values).toInt()
        db.close()
        return restaurantId
    }

    fun insertMenu(restaurantId: Int, menu: Menu) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(NAME, menu.name)
            put(PRICE, menu.price)
            put(URL, menu.url)
            put(RESTAURANT_ID, restaurantId)
        }
        db.insert(TABLE_NAME_MENUS, null, values)
        db.close()
    }
}