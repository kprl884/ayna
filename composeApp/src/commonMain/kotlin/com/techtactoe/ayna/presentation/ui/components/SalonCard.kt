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
import com.techtactoe.ayna.domain.model.DayOfWeek
import com.techtactoe.ayna.domain.model.Employee
import com.techtactoe.ayna.domain.model.Location
import com.techtactoe.ayna.domain.model.Salon
import com.techtactoe.ayna.domain.model.Service
import com.techtactoe.ayna.presentation.theme.AynaColors
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
        name = "Emre's Barbershop Dolapdere",
        location = Location(
            address = "Dolapdere Mahallesi, 34384 Istanbul",
            city = "Istanbul",
            latitude = 41.0082,
            longitude = 28.9784
        ),
        imageUrls = listOf(
            "https://images.unsplash.com/photo-1585747860715-2ba37e788b70?w=800",
            "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?w=800",
            "https://images.unsplash.com/photo-1503951914875-452162b0f3f1?w=800"
        ),
        rating = 5.0,
        reviewCount = 69,
        operatingHours = mapOf(
            DayOfWeek.MONDAY to "9:00 AM - 7:00 PM",
            DayOfWeek.TUESDAY to "9:00 AM - 7:00 PM",
            DayOfWeek.WEDNESDAY to "9:00 AM - 7:00 PM",
            DayOfWeek.THURSDAY to "9:00 AM - 7:00 PM",
            DayOfWeek.FRIDAY to "9:00 AM - 8:00 PM",
            DayOfWeek.SATURDAY to "8:00 AM - 8:00 PM",
            DayOfWeek.SUNDAY to "Closed"
        ),
        employees = listOf(
            Employee(
                "emp1",
                "Emre Demir",
                "Master Barber",
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400",
                4.9,
                45
            ),
            Employee(
                "emp2",
                "Ali Yılmaz",
                "Hair Stylist",
                "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400",
                4.8,
                32
            )
        ),
        services = listOf(
            Service(
                "svc1",
                "Haircut / Saç Kesimi",
                "Professional men's haircut with styling",
                700.0,
                60
            ),
            Service(
                "svc2",
                "Haircut & Shave / Saç Kesimi & Sakal Tıraşı",
                "Complete grooming package",
                1175.0,
                90
            ),
            Service(
                "svc3",
                "Full Service / Komple Bakım",
                "Premium grooming experience",
                1880.0,
                120
            )
        ),
        description = "Traditional Turkish barbershop offering premium men's grooming services in the heart of Dolapdere.",
        phoneNumber = "+90 212 555 0101",
        isOpen = true,
        address = "Dolapdere Mahallesi, 34384 Istanbul",
        imageUrl = "https://images.unsplash.com/photo-1585747860715-2ba37e788b70?w=800",
        tags = listOf("Barbershop", "Traditional")
    )

    SalonCard(salon = mockSalon)
}
