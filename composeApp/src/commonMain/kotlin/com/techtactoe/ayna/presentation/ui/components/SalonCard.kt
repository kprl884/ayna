package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.domain.model.Salon
import com.techtactoe.ayna.theme.AynaColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SalonCard(
    salon: Salon,
    onSalonClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.width(280.dp).clickable { onSalonClick(salon.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AynaColors.White)
    ) {
        Column {
            // Salon image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(AynaColors.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🪒",
                    fontSize = 32.sp
                )
            }
            
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // Salon name
                Text(
                    text = salon.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AynaColors.PrimaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Rating and review count
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${salon.rating} ⭐",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AynaColors.PrimaryText
                    )
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    Text(
                        text = "(${salon.reviewCount})",
                        fontSize = 14.sp,
                        color = AynaColors.SecondaryText
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Address
                Text(
                    text = salon.address,
                    fontSize = 12.sp,
                    color = AynaColors.SecondaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Category tag
                if (salon.tags.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = AynaColors.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = salon.tags.first(),
                            fontSize = 12.sp,
                            color = AynaColors.SecondaryText
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SalonCardPreview() {
    val mockSalon = Salon(
        id = "1",
        name = "David James San Francisco",
        address = "600 Fillmore Street, San Francisco",
        imageUrl = "",
        rating = 4.9,
        reviewCount = 236,
        tags = listOf("Hair Salon")
    )
    
    SalonCard(salon = mockSalon)
}
