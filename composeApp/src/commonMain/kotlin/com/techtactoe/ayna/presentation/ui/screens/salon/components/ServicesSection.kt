package com.techtactoe.ayna.presentation.ui.screens.salon.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.domain.model.SalonService
import com.techtactoe.ayna.domain.model.ServiceCategoryEnum
import com.techtactoe.ayna.presentation.theme.AynaColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ServicesSection(
    services: List<SalonService>,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(ServiceCategoryEnum.FEATURED) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Services",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AynaColors.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Filter tabs
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(bottom = 20.dp)
        ) {
            items(ServiceCategoryEnum.entries) { category ->
                FilterTab(
                    category = category,
                    isSelected = selectedCategory == category,
                    onClick = { selectedCategory = category }
                )
            }
        }

        // Service list - filtered based on selected category
        val filteredServices = services.filter { it.category == selectedCategory }

        if (filteredServices.isNotEmpty()) {
            filteredServices.forEachIndexed { index, service ->
                ServiceCard(
                    service = service,
                    onBookClick = { /* Handle booking */ }
                )

                if (index < filteredServices.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        thickness = 1.dp,
                        color = AynaColors.BorderGray
                    )
                }
            }
        } else {
            // Show empty state when no services in category
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No services available in this category",
                    fontSize = 16.sp,
                    color = AynaColors.SecondaryText
                )
            }
        }
    }
}

@Composable
private fun FilterTab(
    category: ServiceCategoryEnum,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryName = when (category) {
        ServiceCategoryEnum.FEATURED -> "Featured"
        ServiceCategoryEnum.CONSULTATION -> "CONSULTATION"
        ServiceCategoryEnum.MENS_CUT -> "MEN'S CUT"
        ServiceCategoryEnum.WOMENS_HAIRCUT -> "WOMEN'S HAIRCUT"
        ServiceCategoryEnum.STYLE -> "STYLE"
        ServiceCategoryEnum.COLOR_APPLICATION -> "COLOR APPLICATION"
        ServiceCategoryEnum.QIQI_STRAIGHTENING -> "QIQI | STRAIGHTENING"
        ServiceCategoryEnum.KIDS -> "KIDS"
    }

    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(50.dp), // Fully rounded
        color = if (isSelected) AynaColors.Black else Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .then(
                    if (!isSelected) {
                        Modifier.border(
                            1.dp,
                            AynaColors.BorderGray,
                            RoundedCornerShape(50.dp)
                        )
                    } else Modifier
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = categoryName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) AynaColors.White else AynaColors.Black
            )
        }
    }
}

@Composable
private fun ServiceCard(
    service: SalonService,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Service title
            Text(
                text = service.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AynaColors.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Details line: duration • serviceCount services • genderRestriction
            val detailsText = buildString {
                append(service.duration)
                if (service.serviceCount > 0) {
                    append(" • ${service.serviceCount} services")
                }
                service.genderRestriction?.let { restriction ->
                    append(" • $restriction")
                }
            }

            Text(
                text = detailsText,
                fontSize = 14.sp,
                color = AynaColors.SecondaryText
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Price
            Text(
                text = service.priceFrom,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AynaColors.Black
            )
        }

        // Book button with fully rounded shape
        Surface(
            modifier = Modifier.clickable { onBookClick() },
            shape = RoundedCornerShape(50.dp), // Fully rounded
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .border(1.dp, AynaColors.BorderGray, RoundedCornerShape(50.dp))
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Book",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = AynaColors.Black
                )
            }
        }
    }
}

@Preview
@Composable
fun ServicesSectionPreview() {
    val mockServices = listOf(
        SalonService(
            id = "1",
            name = "QIQI | STRAIGHTENING TREATMENT - MAN",
            duration = "2 hrs, 20 mins",
            serviceCount = 4,
            genderRestriction = "Male only",
            priceFrom = "€70",
            category = ServiceCategoryEnum.QIQI_STRAIGHTENING
        ),
        SalonService(
            id = "2",
            name = "CUT",
            duration = "55 mins – 1 hr, 10 mins",
            serviceCount = 2,
            genderRestriction = "Male only",
            priceFrom = "from €20",
            category = ServiceCategoryEnum.MENS_CUT
        ),
        SalonService(
            id = "3",
            name = "HAIRCUT & FINISH",
            duration = "1 hr, 15 mins – 1 hr, 45 mins",
            serviceCount = 3,
            genderRestriction = null,
            priceFrom = "from €40",
            category = ServiceCategoryEnum.WOMENS_HAIRCUT
        )
    )

    MaterialTheme {
        ServicesSection(services = mockServices)
    }
}
