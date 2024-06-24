package com.siewtheng.littlelemon

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuModel(application: Application) : AndroidViewModel(application) {
    private val database: MenuDatabase

    init {
        database = Room.databaseBuilder(
            application,
            MenuDatabase::class.java,
            "database"
        ).build()
    }

    fun getAllDatabaseMenuItems(): LiveData<List<MenuItemEntity>> {
        return database.menuDao().getAllMenuItems()
    }

    private suspend fun fetchMenu(url: String): List<MenuItemNetwork> {
        val httpClient = HttpClient(Android){
            install(ContentNegotiation){
                json(contentType = ContentType("text", "plain"))
            }
        }
        val  httpResponse: MenuNetwork = httpClient.get(url).body()
        return httpResponse.menu
    }

    fun fetchMenuDataIfRequired() {
        viewModelScope.launch(Dispatchers.IO) {
            println("Checking if menu data is required")

            if (database.menuDao().isEmpty()) {
                saveMenuToDatabase(
                    database,
                    fetchMenu("https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json")
                )
            } else {
                println("Menu data already exists in database")
            }
        }
    }

    private fun saveMenuToDatabase(database: MenuDatabase, menuItemsNetwork: List<MenuItemNetwork>) {
        println("Saving menu items to database")
        val menuItemsEntity = menuItemsNetwork.map { it.toMenuItemEntity() }
        database.menuDao().insertAll(menuItemsEntity)
    }
}