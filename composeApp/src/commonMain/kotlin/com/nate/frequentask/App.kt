package com.nate.frequentask

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nate.frequentask.data.ThemeRepository
import com.nate.frequentask.task.TaskDetailScreen
import com.nate.frequentask.theme.ThemeDetailScreen
import com.nate.frequentask.themelist.ThemeListScreen
import com.nate.frequentask.today.TodayScreen
import com.nate.frequentask.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

private val themeRepository by lazy { ThemeRepository() }


@Composable
@Preview
fun App() {
    val themesList by remember { themeRepository.themes }
    AppTheme {
        val navController = rememberNavController()
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primaryContainer
        ) {
            NavHost(navController = navController, startDestination = Navigation.TODAY) {
                composable(Navigation.TODAY) {
                    TodayScreen(
                        navController, themeRepository = themeRepository
                    )
                }
                composable(Navigation.THEMELIST) {
                    ThemeListScreen(
                        navController, themeRepository = themeRepository
                    )
                }
                composable(
                    route = Navigation.THEMEDETAIL,
                    arguments = listOf(navArgument(Navigation.THEMEID) {
                        type = NavType.StringType
                    })
                ) { backStackEntry ->
                    val themeId = backStackEntry.arguments?.getString(Navigation.THEMEID)
                    val theme = themesList.find { it.id == themeId }
                    if (theme != null) {
                        ThemeDetailScreen(
                            navController = navController,
                            themeID = themeId!!,
                            themeRepository = themeRepository
                        )
                    }
                }
                composable(
                    route = Navigation.TASKDETAIL,
                    arguments = listOf(navArgument(Navigation.THEMEID) {
                        type = NavType.StringType
                    }, navArgument(Navigation.TASKID) {
                        type = NavType.StringType
                    })
                ) { backStackEntry ->
                    val themeId = backStackEntry.arguments?.getString(Navigation.THEMEID)
                    val taskId = backStackEntry.arguments?.getString(Navigation.TASKID)
                    themesList.find { it.id == themeId }?.also { theme ->
                        theme.tasks.find { it.id == taskId }?.also { task ->
                            TaskDetailScreen(
                                theme = theme,
                                task = task,
                                themeRepository = themeRepository,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}
