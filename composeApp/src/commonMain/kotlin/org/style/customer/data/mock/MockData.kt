package org.style.customer.data.mock

import org.style.customer.data.models.Salon

object MockData {
    val salons = listOf(
        Salon(
            id = "1",
            name = "Pink Ivy",
            category = "Saç & Makyaj",
            rating = 4.8f,
            priceRange = "150-300",
            isFavorite = true,
            isNew = true
        ),
        Salon(
            id = "2",
            name = "Tania S Hair",
            category = "Saç",
            rating = 4.6f,
            priceRange = "200-400",
            isTrending = true
        ),
        Salon(
            id = "3",
            name = "My Thai Massage",
            category = "Masaj",
            rating = 4.9f,
            priceRange = "300-600",
            isFavorite = true
        ),
        Salon(
            id = "4",
            name = "Beauty Studio",
            category = "Cilt Bakımı",
            rating = 4.7f,
            priceRange = "250-500",
            isNew = true
        ),
        Salon(
            id = "5",
            name = "Nail Art Studio",
            category = "Manikür & Pedikür",
            rating = 4.5f,
            priceRange = "100-200"
        ),
        Salon(
            id = "6",
            name = "Laser Clinic",
            category = "Epilasyon",
            rating = 4.4f,
            priceRange = "400-800"
        ),
        Salon(
            id = "7",
            name = "Eyebrow Studio",
            category = "Kaş & Kirpik",
            rating = 4.3f,
            priceRange = "80-150",
            isTrending = true
        ),
        Salon(
            id = "8",
            name = "Hair & Beauty",
            category = "Saç",
            rating = 4.2f,
            priceRange = "180-350"
        )
    )

    val favoriteSalons = salons.filter { it.isFavorite }
    
    val recentlyViewedSalons = salons.take(3)
} 