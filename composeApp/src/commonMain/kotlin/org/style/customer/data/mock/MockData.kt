package org.style.customer.data.mock

import org.style.customer.model.Salon

object MockData {
    val favoriteSalons = listOf(
        Salon(
            id = "1",
            name = "Tania S Hair and Head Spa",
            category = "Hair & Spa",
            rating = 4.9f,
            priceRange = "€30-€100",
            imageUrl = "https://picsum.photos/200/300?1",
            isFavorite = true,
            isNew = true,
            isTrending = false,
            description = "Best hair and spa in town.",
            address = "Kυψελαιτινου Σωκου 2, Lefkoşa",
            phone = "+90 555 123 45 67",
            workingHours = "09:00-19:00",
            photos = listOf(),
            reviewCount = 32,
            location = "lcoation",
            status = "Status",
            services = emptyList(),
            team = emptyList(),
        ),
        Salon(
            id = "2",
            name = "My Thai Massage",
            category = "Massage",
            rating = 4.8f,
            priceRange = "€40-€120",
            imageUrl = "https://picsum.photos/200/300?2",
            isFavorite = true,
            isNew = false,
            isTrending = true,
            description = "Relaxing Thai massage.",
            address = "Main Street 123, Lefkoşa",
            phone = "+90 555 987 65 43",
            workingHours = "10:00-22:00",
            photos = listOf(),
            reviewCount = 32,
            location = "lcoation",
            status = "Status",
            services = emptyList(),
            team = emptyList(),
        )
    )
    val recentlyViewedSalons = listOf(
        Salon(
            id = "3",
            name = "Pink Ivy",
            category = "Saç & Makyaj",
            rating = 4.8f,
            priceRange = "€25-€80",
            imageUrl = "https://picsum.photos/200/300?3",
            isFavorite = false,
            isNew = false,
            isTrending = true,
            description = "Modern saç ve makyaj.",
            address = "Atatürk Cad. 45, Lefkoşa",
            phone = "+90 555 111 22 33",
            photos = listOf(),
            reviewCount = 32,
            location = "lcoation",
            status = "Status",
            services = emptyList(),
            team = emptyList(),
            workingHours = "08:00-20:00"
        ),
        Salon(
            id = "4",
            name = "Tania S Hair and Head Spa",
            category = "Saç",
            rating = 4.6f,
            priceRange = "€30-€100",
            imageUrl = "https://picsum.photos/200/300?4",
            isFavorite = false,
            isNew = false,
            isTrending = false,
            description = "Profesyonel saç bakımı.",
            address = "Küçük Kaymaklı, Lefkoşa",
            phone = "+90 555 444 55 66",
            photos = listOf(),
            reviewCount = 32,
            location = "lcoation",
            status = "Status",
            services = emptyList(),
            team = emptyList(),
            workingHours = "09:00-19:00"
        )
    )
} 