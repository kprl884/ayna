package com.techtactoe.ayna.presentation.ui.components

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
<<<<<<< HEAD
import com.techtactoe.ayna.domain.model.ServiceCategory
=======
import com.techtactoe.ayna.domain.model.ServiceCategoryEnum
>>>>>>> c6f912b7061690bd37a0eb2667fb82cbe0eb4d29
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
<<<<<<< HEAD
        ServiceCategory.FEATURED -> "Featured"
        ServiceCategory.CONSULTATION -> "CONSULTATION"
        ServiceCategory.MENS_CUT -> "MEN'S CUT"
        ServiceCategory.WOMENS_HAIRCUT -> "WOMEN'S HAIRCUT"
        ServiceCategory.STYLE -> "STYLE"
        ServiceCategory.COLOR_APPLICATION -> "COLOR APPLICATION"
        ServiceCategory.QIQI_STRAIGHTENING -> "QIQI | STRAIGHTENING"
        ServiceCategory.KIDS -> "KIDS"
=======
        ServiceCategoryEnum.FEATURED -> "Featured"
        ServiceCategoryEnum.CONSULTATION -> "Consultation"
        ServiceCategoryEnum.MENS_CUT -> "Men's Cut"
        ServiceCategoryEnum.WOMENS_CUT -> "Women's Cut"
        ServiceCategoryEnum.STYLING -> "Styling"
        ServiceCategoryEnum.COLOR -> "Color"
>>>>>>> c6f912b7061690bd37a0eb2667fb82cbe0eb4d29
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
            category = ServiceCategory.QIQI_STRAIGHTENING
        ),
        SalonService(
            id = "2",
            name = "CUT",
            duration = "55 mins – 1 hr, 10 mins",
            serviceCount = 2,
            genderRestriction = "Male only",
            priceFrom = "from €20",
<<<<<<< HEAD
            category = ServiceCategory.MENS_CUT
        ),
        SalonService(
            id = "3",
            name = "HAIRCUT & FINISH",
            duration = "1 hr, 15 mins – 1 hr, 45 mins",
            serviceCount = 3,
            genderRestriction = null,
            priceFrom = "from €40",
            category = ServiceCategory.WOMENS_HAIRCUT
=======
            category = ServiceCategoryEnum.FEATURED
        ),
        SalonService(
            id = "2",
            name = "BLOW DRY | BRUSH STYLE",
            duration = "50 mins – 1 hr, 10 mins",
            serviceCount = 2,
            genderRestriction = "Female only",
            priceFrom = "from €20",
            category = ServiceCategoryEnum.FEATURED
>>>>>>> c6f912b7061690bd37a0eb2667fb82cbe0eb4d29
        )
    )
    
    MaterialTheme {
        ServicesSection(services = mockServices)
    }
}
