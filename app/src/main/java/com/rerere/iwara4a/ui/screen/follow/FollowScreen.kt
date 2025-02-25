package com.rerere.iwara4a.ui.screen.follow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rerere.iwara4a.R
import com.rerere.iwara4a.model.follow.FollowUser
import com.rerere.iwara4a.ui.component.SimpleIwaraTopBar
import com.rerere.iwara4a.ui.local.LocalNavController

@Composable
fun FollowScreen(viewModel: FollowScreenViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            SimpleIwaraTopBar(stringResource(R.string.screen_follow_title))
        }
    ) {
        FollowingUserList(viewModel)
    }
}

@Composable
private fun FollowingUserList(viewModel: FollowScreenViewModel) {
    val allUsers by viewModel.allUsers.collectAsState(initial = emptyList())
    LazyVerticalGrid(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(allUsers) {
            FollowingUserCard(it) {
                viewModel.delete(it)
            }
        }
    }
}

@Composable
private fun FollowingUserCard(followUser: FollowUser, onDelete: () -> Unit) {
    val navController = LocalNavController.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("user/${followUser.id}")
            }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                model = followUser.profilePic,
                contentDescription = "avatar",
                contentScale = ContentScale.FillWidth
            )
            Row(
                modifier = Modifier.padding(start = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = followUser.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )

                var expand by remember { mutableStateOf(false) }
                IconButton(onClick = {
                    expand = !expand
                }) {
                    Icon(Icons.Rounded.MoreVert, null)
                }
                DropdownMenu(
                    expanded = expand,
                    onDismissRequest = {
                        expand = false
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "移除")
                        },
                        onClick = {
                            expand = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}