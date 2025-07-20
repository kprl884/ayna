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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.domain.model.BuyOption
import com.techtactoe.ayna.domain.model.BuyOptionType
import com.techtactoe.ayna.theme.AynaColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BuySection(
    buyOptions: List<BuyOption>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Buy",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AynaColors.Black,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        
        buyOptions.forEach { option ->
            BuyOptionCard(
                option = option,
                onBuyClick = { /* Handle buy */ }
            )
            
            if (option != buyOptions.last()) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun BuyOptionCard(
    option: BuyOption,
    onBuyClick: () -> Unit,
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
                text = option.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AynaColors.Black
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = option.description,
                fontSize = 14.sp,
                color = AynaColors.SecondaryText,
                lineHeight = 20.sp
            )
        }
        
        Box(
            modifier = Modifier
                .clickable { onBuyClick() }
                .border(1.dp, AynaColors.BorderGray, RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Buy",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = AynaColors.Black
            )
        }
    }
}

@Preview
@Composable
fun BuySectionPreview() {
    val mockBuyOptions = listOf(
        BuyOption(
            id = "1",
            title = "Memberships",
            description = "Bundle your services in to a membership",
            type = BuyOptionType.MEMBERSHIP
        ),
        BuyOption(
            id = "2",
            title = "Gift card",
            description = "Treat yourself or a friend to future visits",
            type = BuyOptionType.GIFT_CARD
        )
    )
    
    MaterialTheme {
        BuySection(buyOptions = mockBuyOptions)
    }
}
