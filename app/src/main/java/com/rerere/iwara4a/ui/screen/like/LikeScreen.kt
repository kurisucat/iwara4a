package com.rerere.iwara4a.ui.screen.like

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.rerere.iwara4a.R
import com.rerere.iwara4a.ui.component.*
import com.rerere.iwara4a.ui.modifier.noRippleClickable

@ExperimentalFoundationApi
@Composable
fun LikeScreen(navController: NavController, likedViewModel: LikedViewModel = hiltViewModel()) {
    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        topBar = {
            Md3TopBar(
                title = {
                    Text(text = stringResource(id = R.string.screen_like_topbar_title))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) {
        val likeList = likedViewModel.pager.collectAsLazyPagingItems()
        when (likeList.loadState.refresh) {
            is LoadState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .noRippleClickable {
                            likeList.retry()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))
                        LottieAnimation(
                            modifier = Modifier.size(150.dp),
                            composition = composition,
                            iterations = LottieConstants.IterateForever
                        )
                        Text(text = stringResource(id = R.string.load_error), fontWeight = FontWeight.Bold)
                    }
                }
            }
            else -> {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = likeList.loadState.refresh == LoadState.Loading),
                    onRefresh = {
                        likeList.refresh()
                    },
                    indicator = { s, trigger ->
                        SwipeRefreshIndicator(
                            s,
                            trigger,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Fixed(2)
                    ) {
                        items(likeList) {
                            MediaPreviewCard(navController, it!!)
                        }

                        appendIndicator(likeList)
                    }
                }
            }
        }
    }
}