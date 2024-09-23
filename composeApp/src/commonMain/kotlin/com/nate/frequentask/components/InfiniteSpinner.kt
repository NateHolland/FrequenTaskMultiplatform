package com.nate.frequentask.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import frequentaskmulti.composeapp.generated.resources.Res
import frequentaskmulti.composeapp.generated.resources.every_x_days
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun InfiniteSpinner(
    items: List<Int>, more: () -> Unit, selectedItem: Int, onValueChange: (Int) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val selectedItemIndex = items.indexOf(selectedItem)
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)
            .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header (Selected Item)
        Box(modifier = Modifier.clickable {
                isExpanded = !isExpanded
                if (isExpanded) coroutineScope.launch {
                    listState.scrollToItem(selectedItemIndex)
                }
            }.padding(8.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(Res.string.every_x_days, selectedItem),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

        }

        // Dropdown List
        if (isExpanded) {
            LazyColumn(
                state = listState,
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            ) {
                items.forEach { item ->
                    item {
                        Box(modifier = Modifier.fillMaxWidth().clickable {
                                onValueChange(item)
                                isExpanded = false
                            }.padding(8.dp).background(
                                if (item == items[selectedItemIndex]) MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.1f
                                )
                                else Color.Transparent
                            ), contentAlignment = Alignment.Center) {
                            Text(
                                text = item.toString(),
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
            listState.OnBottomReached {
                more()
            }
        }
    }
}

@Composable
fun LazyListState.OnBottomReached(
    loadMore: () -> Unit
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem =
                layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf true

            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    // Convert the state into a cold flow and collect
    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }.collect {
                // if should load more, then invoke loadMore
                if (it) loadMore()
            }
    }
}
