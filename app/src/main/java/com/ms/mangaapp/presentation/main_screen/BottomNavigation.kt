package com.ms.mangaapp.presentation.main_screen

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ms.mangaapp.presentation.HomeScreen
import com.ms.mangaapp.presentation.bottomBarScreens
import com.ms.mangaapp.presentation.getScreens

@Composable
fun BottomNavigationBar(
    //navController: NavHostController,
    selectedItemIndex: Int,
    onItemSelected: (index: Int) -> Unit,
    onNavigate: (destination: Any) -> Unit
) {
    BottomAppBar {
        bottomBarScreens.forEachIndexed { index, destinations ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    onItemSelected(index)
                    onNavigate(getScreens(bottomBarScreens[index]!!))
                    //onNavigate(destinations.route) // may merge both into onItemSelected and onNavigate into single (index,route)
                    //navController.navigateWithoutPopWithStateSingleTopTo(destinations.route)
                },
                label = { Text(text = bottomBarScreens[index]!!) },
                icon = {

                })
        }
    }
}