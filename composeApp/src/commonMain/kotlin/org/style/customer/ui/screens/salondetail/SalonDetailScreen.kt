package org.style.customer.ui.screens.salondetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import org.style.customer.model.Salon

class SalonDetailScreen(private val salon: Salon, private val onBack: () -> Unit) : Screen {

    @Composable
    override fun Content() {
        Column(Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxHeight(0.4f).fillMaxWidth()) {
                val pagerState = rememberPagerState(pageCount = { salon.photos.size })
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    CoilImage(
                        modifier = Modifier.fillMaxSize(),
                        imageModel = { salon.imageUrl },
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center
                        )
                    )
                }
                Row(
                    Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Text(
                        salon.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                    )
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Default.FavoriteBorder,
                            null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Default.Share,
                            null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}