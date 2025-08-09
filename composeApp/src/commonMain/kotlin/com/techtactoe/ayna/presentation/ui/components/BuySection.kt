package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.techtactoe.ayna.domain.model.BuyOption
import com.techtactoe.ayna.domain.model.BuyOptionType
import com.techtactoe.ayna.common.designsystem.component.button.PrimaryButton
import com.techtactoe.ayna.common.designsystem.theme.AynaShapes
import com.techtactoe.ayna.common.designsystem.theme.Elevation
import com.techtactoe.ayna.common.designsystem.theme.Spacing
import com.techtactoe.ayna.common.designsystem.theme.StringResources
import com.techtactoe.ayna.common.designsystem.typography.AynaTypography
import org.jetbrains.compose.ui.tooling.preview.Preview

@Stable
data class BuySectionViewState(
    val buyOptions: List<BuyOption>
)

@Composable
fun BuySection(
    viewState: BuySectionViewState,
    onBuyClick: (String, BuyOptionType) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        SectionHeader(
            title = "Buy"
        )

        viewState.buyOptions.forEach { option ->
            BuyOptionCard(
                option = option,
                onBuyClick = { onBuyClick(option.id, option.type) }
            )
        }
    }
}

@Composable
private fun BuyOptionCard(
    option: BuyOption,
    onBuyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = AynaShapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.sm),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.extraSmall)
            ) {
                Text(
                    text = option.title,
                    style = AynaTypography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = option.description,
                    style = AynaTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            PrimaryButton(
                text = StringResources.book_now_button,
                onClick = onBuyClick
            )
        }
    }
}

@Preview
@Composable
fun BuySectionPreview() {
    val mockViewState = BuySectionViewState(
        buyOptions = listOf(
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
    )

    MaterialTheme {
        BuySection(viewState = mockViewState)
    }
}
