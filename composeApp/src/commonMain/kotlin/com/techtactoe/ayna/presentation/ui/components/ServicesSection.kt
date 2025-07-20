package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.background
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
import com.techtactoe.ayna.domain.model.ServiceCategory
import com.techtactoe.ayna.theme.AynaColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ServicesSection(
    services: List<SalonService>,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(ServiceCategory.FEATURED) }
    
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
            items(ServiceCategory.entries) { category ->
                FilterTab(
                    category = category,
                    isSelected = selectedCategory == category,
                    onClick = { selectedCategory = category }
                )
            }
        }
        
        // Service list
        val filteredServices = services.filter { it.category == selectedCategory }
        
        filteredServices.forEach { service ->
            ServiceCard(
                service = service,
                onBookClick = { /* Handle booking */ }
            )
            
            if (service != filteredServices.last()) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = AynaColors.BorderGray
                )
            }
        }
    }
}

@Composable
private fun FilterTab(
    category: ServiceCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryName = when (category) {
        ServiceCategory.FEATURED -> "Featured"
        ServiceCategory.CONSULTATION -> "Consultation"
        ServiceCategory.MENS_CUT -> "Men's Cut"
        ServiceCategory.WOMENS_CUT -> "Women's Cut"
        ServiceCategory.STYLING -> "Styling"
        ServiceCategory.COLOR -> "Color"
    }
    
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) AynaColors.Black else Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .then(
                    if (!isSelected) {
                        Modifier.border(
                            1.dp,
                            AynaColors.BorderGray,
                            RoundedCornerShape(20.dp)
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
            Text(
                text = service.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AynaColors.Black
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "${service.duration} • ${service.serviceCount} services",
                fontSize = 14.sp,
                color = AynaColors.SecondaryText
            )
            
            service.genderRestriction?.let { restriction ->
                Text(
                    text = restriction,
                    fontSize = 14.sp,
                    color = AynaColors.SecondaryText
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = service.priceFrom,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AynaColors.Black
            )
        }
        
        Surface(
            modifier = Modifier.clickable { onBookClick() },
            shape = RoundedCornerShape(20.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .border(1.dp, AynaColors.BorderGray, RoundedCornerShape(20.dp))
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
            name = "CUT",
            duration = "55 mins – 1 hr, 10 mins",
            serviceCount = 2,
            genderRestriction = "Male only",
            priceFrom = "from €20",
            category = ServiceCategory.FEATURED
        ),
        SalonService(
            id = "2",
            name = "BLOW DRY | BRUSH STYLE",
            duration = "50 mins – 1 hr, 10 mins",
            serviceCount = 2,
            genderRestriction = "Female only",
            priceFrom = "from €20",
            category = ServiceCategory.FEATURED
        )
    )
    
    MaterialTheme {
        ServicesSection(services = mockServices)
    }
}
