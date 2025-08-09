package com.techtactoe.ayna.presentation.ui.screens.salon.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayna.composeapp.generated.resources.Res
import ayna.composeapp.generated.resources.ic_heart
import com.techtactoe.ayna.common.designsystem.icon.IconInCircle
import com.techtactoe.ayna.common.designsystem.theme.AynaColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(
    images: List<String>,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { images.size })

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
    ) {
        // Image carousel
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AynaColors.LightGray),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder for actual image loading with Coil
                Text(
                    text = "🏢",
                    fontSize = 48.sp,
                    color = AynaColors.SecondaryText
                )
            }
        }

        // Top overlay controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Back button
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onBackClick() },
                shape = CircleShape,
                color = AynaColors.White,
                shadowElevation = 2.dp
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "←",
                        fontSize = 18.sp,
                        color = AynaColors.Black
                    )
                }
            }

            // Share and favorite buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onShareClick() },
                    shape = CircleShape,
                    color = AynaColors.White,
                    shadowElevation = 2.dp
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "↗",
                            fontSize = 18.sp,
                            color = AynaColors.Black
                        )
                    }
                }
                IconInCircle(
                    onClick = { onFavoriteClick() },
                    resource = Res.drawable.ic_heart
                )
            }
        }

        if (images.size > 1) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.Black.copy(alpha = 0.6f)
            ) {
                Text(
                    text = "${pagerState.currentPage + 1}/${images.size}",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ImageCarouselPreview() {
    MaterialTheme {
        ImageCarousel(
            images = listOf("1", "2", "3", "4"),
            onBackClick = {},
            onShareClick = {},
            onFavoriteClick = {}
        )
    }
}
