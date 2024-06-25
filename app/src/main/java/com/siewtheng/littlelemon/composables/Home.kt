package com.siewtheng.littlelemon.composables

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.siewtheng.littlelemon.MenuModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.siewtheng.littlelemon.MenuItemEntity
import com.siewtheng.littlelemon.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.siewtheng.littlelemon.ui.theme.highlightBlack
import com.siewtheng.littlelemon.ui.theme.highlightWhite
import com.siewtheng.littlelemon.ui.theme.primaryGreen
import com.siewtheng.littlelemon.ui.theme.primaryYellow
import com.siewtheng.littlelemon.ui.theme.secondaryOrange


class Home : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "homeScreen") {
                composable("homeScreen") {
                    HomeScreen(navController)
                }
                composable(ProfileDestination.route) {
                    Profile(navController)
                }
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {

    val menuModel: MenuModel = viewModel()
    val menuItems by menuModel.getAllDatabaseMenuItems().observeAsState(emptyList())
    val searchQuery = remember { mutableStateOf("") }
    val categories = remember {
        mutableStateListOf("All", "Starters", "Mains", "Desserts")
    }
    val filteredMenuItems = remember { mutableStateOf(menuItems) }
    val selectedCategory = remember { mutableStateOf("All") }

    LaunchedEffect(Unit) {
        menuModel.fetchMenuDataIfRequired()
    }

    // Set filteredMenuItems initially when menuItems changes or when screen loads
    DisposableEffect(menuItems) {
        filteredMenuItems.value = menuItems
        onDispose { }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            ,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .align(Alignment.Center)
                    .height(40.dp)
                    .fillMaxWidth()
            )


            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = "Profile",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
                    .clickable {
                        navController.navigate(ProfileDestination.route)
                    }
            )
        }


        Spacer(modifier = Modifier.height(2.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(primaryGreen)
        ) {
            HeroSection(searchQuery) {
                filterBySearchQuery(searchQuery.value, menuItems, filteredMenuItems)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Order For Delivery",
            style = MaterialTheme.typography.displayMedium.copy(
                color = highlightBlack,
                fontSize = 24.sp
            )
            )
        Spacer(modifier = Modifier.height(8.dp))

        MenuBreakdown(categories, selectedCategory.value, filteredMenuItems.value) { category ->
            selectedCategory.value = category
            filterByCategory(category, menuItems, searchQuery, filteredMenuItems)
        }

        if (filteredMenuItems.value.isNotEmpty()) {
            MenuItems(filteredMenuItems.value)
        } else {
            Text("No items found", modifier = Modifier.padding(16.dp))
        }

    }

}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HeroSection(searchQuery: MutableState<String>, onSearch: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Text(
            text = "Little Lemon",
            style = MaterialTheme.typography.displayLarge.copy(
                color = secondaryOrange
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {


                Text(
                    text = "Japan",
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = primaryYellow
                    ),
                    modifier = Modifier.padding(bottom = 0.dp)
                )

                Text(
                    text = "We are a family-owned Japanese restaurant, focused on traditional recipes served with a modern twist.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 2.dp)
                )

            }

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = painterResource(id = R.drawable.hero),
                contentDescription = "Hero",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )
        }

    }

    Spacer(modifier = Modifier.height(8.dp))

    // Search Bar
    OutlinedTextField(
        value = searchQuery.value,
        onValueChange = { searchQuery.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 14.dp),
        placeholder = { Text("Enter Search Phrase") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = primaryGreen,
            focusedLabelColor = primaryGreen,
            unfocusedLabelColor = primaryGreen,
            focusedIndicatorColor = primaryGreen,
            unfocusedIndicatorColor = primaryGreen
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = {
            keyboardController?.hide()
            onSearch.invoke()
        }),
        visualTransformation = VisualTransformation.None,
        singleLine = true
    )

}

@Composable
fun MenuItems(menuItems: List<MenuItemEntity>) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            items(menuItems) { menuItem ->
                MenuItem(menuItem)
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuItem(menuItem: MenuItemEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f)
        ) {
            Text(text = menuItem.title, style = MaterialTheme.typography.headlineSmall.copy(
                color = primaryGreen,

            ))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = menuItem.description, style = MaterialTheme.typography.bodyMedium.copy(
                color = highlightBlack
            ))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "$${menuItem.price}", style = MaterialTheme.typography.bodyMedium.copy(
                color = highlightBlack
            ))
            Spacer(modifier = Modifier.height(4.dp))

        }

        GlideImage(
            model = menuItem.image,
            contentDescription = "Menu Item Image",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray)
                .align(Alignment.CenterVertically),
            contentScale = ContentScale.Crop
        )
    }

}

@Composable
fun MenuBreakdown(
    categories: List<String>,
    selectedCategory: String,
    menuItems: List<MenuItemEntity>,
    onCategorySelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            categories.forEach { category ->
                CategoryButton(
                    category = category,
                    isSelected = category == selectedCategory,
                    onClick = { onCategorySelected(category) }
                )
            }
        }
    }
}

@Composable
fun CategoryButton(category: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(start = 8.dp)
            .background(if (isSelected) primaryGreen else highlightWhite)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = category,
            color = if (isSelected) highlightWhite else highlightBlack,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 16.sp
            )
        )
    }
}

// Function to filter menu items by category
fun filterByCategory(
    category: String,
    menuItems: List<MenuItemEntity>,
    searchQuery: MutableState<String>,
    filteredMenuItems: MutableState<List<MenuItemEntity>>
) {
    val query = searchQuery.value.lowercase()

    val filteredItems = if (category == "All") {
        menuItems.filter {
            it.title.lowercase().contains(query)
        }
    } else {
        menuItems.filter {
            it.category.equals(category, ignoreCase = true) && it.title.lowercase().contains(query)
        }
    }

    filteredMenuItems.value = filteredItems
}

// Function to filter menu items by search query
fun filterBySearchQuery(
    query: String,
    menuItems: List<MenuItemEntity>,
    filteredMenuItems: MutableState<List<MenuItemEntity>>
) {
    val filteredItems = if (query.isNotBlank()) {
        menuItems.filter {
            it.title.contains(query, ignoreCase = true)
        }
    } else {
        menuItems
    }

    filteredMenuItems.value = filteredItems
}