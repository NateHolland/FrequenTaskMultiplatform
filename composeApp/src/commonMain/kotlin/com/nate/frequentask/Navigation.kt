package com.nate.frequentask

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import frequentaskmulti.composeapp.generated.resources.Res
import frequentaskmulti.composeapp.generated.resources.back
import org.jetbrains.compose.resources.stringResource

object Navigation {
    const val TASKID = "taskId"
    const val TODAY = "todayScreen"
    const val THEMELIST = "themeList"
    const val THEMEID = "themeId"
    const val THEMEDETAIL = "themeDetail/{${THEMEID}}"
    const val TASKDETAIL = "taskDetail/{${THEMEID}}/{${TASKID}}"
    fun task(themeID: String, taskID: String): String {
        return "taskDetail/$themeID/$taskID"
    }

    fun theme(themeID: String): String {
        return "themeDetail/$themeID"
    }
}

val showNavigationIcon = getPlatform().os == OS.IOS

fun NavController.navigationIcon(): @Composable () -> Unit {
    val func = @androidx.compose.runtime.Composable {
        if (showNavigationIcon) {
            IconButton(
                onClick = {
                    navigateUp()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(
                        Res.string.back
                    )
                )
            }
        }
    }
    return func
}