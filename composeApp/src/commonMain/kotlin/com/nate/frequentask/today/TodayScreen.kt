package com.nate.frequentask.today

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nate.frequentask.Navigation
import com.nate.frequentask.data.ThemeRepository
import com.nate.frequentask.data.isDue
import frequentaskmulti.composeapp.generated.resources.Res
import frequentaskmulti.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(navController: NavHostController, themeRepository: ThemeRepository) {
    val themeList by remember { themeRepository.themes }
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = stringResource(Res.string.app_name)) }, actions = {
            IconButton(onClick = { navController.navigate(Navigation.THEMELIST) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = stringResource(Res.string.theme_list)
                )
            }
        })
    }, content = { padding ->
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface).padding(
                top = padding.calculateTopPadding(), start = 4.dp, end = 4.dp, bottom = 4.dp
            )
        ) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(150.dp),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                (themeList.filter { theme -> theme.active && theme.tasks.any { it.isDue() } }
                    .sortedBy { it.created }.also { themes ->
                        items(themes) { theme ->
                            ElevatedCard(elevation = CardDefaults.cardElevation(
                                defaultElevation = 6.dp
                            ), colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            ), modifier = Modifier.padding(4.dp).fillMaxSize().clickable {
                                navController.navigate(Navigation.theme(theme.id))
                            }) {
                                Text(
                                    text = theme.name,
                                    modifier = Modifier.padding(
                                        top = 16.dp, start = 16.dp, end = 16.dp, bottom = 4.dp
                                    ),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Column(
                                    modifier = Modifier.padding(
                                        bottom = 8.dp, start = 3.dp, end = 3.dp
                                    )
                                ) {
                                    theme.tasks.filter { it.isDue() }.sortedBy { it.nextDueOn }
                                        .forEach(
                                        ) { task ->
                                            TodayListItem(task = task, onTaskClick = {
                                                navController.navigate(
                                                    Navigation.task(theme.id, it)
                                                )
                                            })
                                        }
                                }
                            }
                        }
                    })
            }
        }
    })
}