package com.techtactoe.ayna.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.domain.model.Review
import com.techtactoe.ayna.presentation.theme.AynaColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ReviewsSection(
    reviews: List<Review>,
    overallRating: Double,
    reviewCount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Reviews",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AynaColors.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Overall rating
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 20.dp)
        ) {
            // Star rating
            repeat(5) {
                Text(
                    text = "⭐",
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                modifier = Modifier.clickable {
                    //todo navigate Reviews screen
                },
                text = "$overallRating ($reviewCount)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = AynaColors.Black
            )
        }

        // Individual reviews
        reviews.forEach { review ->
            ReviewCard(review = review)

            if (review != reviews.last()) {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun ReviewCard(
    review: Review,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // User avatar
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(AynaColors.Purple.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = review.userInitials,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AynaColors.Purple
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            // User name and date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = review.userName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = AynaColors.Black
                )

                Text(
                    text = review.date,
                    fontSize = 12.sp,
                    color = AynaColors.SecondaryText
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Star rating
            Row {
                repeat(review.rating) {
                    Text(
                        text = "⭐",
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Review comment
            Text(
                text = review.comment,
                fontSize = 14.sp,
                color = AynaColors.Black,
                lineHeight = 20.sp
            )

            // Read more link (if needed)
            if (review.comment.length > 100) {
                Text(
                    text = "Read more",
                    fontSize = 14.sp,
                    color = AynaColors.Purple,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ReviewsSectionPreview() {
    val mockReviews = listOf(
        Review(
            id = "1",
            userName = "Monica T",
            userInitials = "MT",
            date = "Sat, Jul 19, 2025 at 10:28 PM",
            rating = 5,
            comment = "Only the best! Much appreciated 😊"
        ),
        Review(
            id = "2",
            userName = "Froso P",
            userInitials = "FP",
            date = "Sat, Jul 19, 2025 at 12:52 PM",
            rating = 5,
            comment = "Absolutely excellent service! The environment is welcoming, stylish, and relaxing. Special thanks to the team for their professionalism."
        )
    )

    MaterialTheme {
        ReviewsSection(
            reviews = mockReviews,
            overallRating = 5.0,
            reviewCount = 3645
        )
    }
}
