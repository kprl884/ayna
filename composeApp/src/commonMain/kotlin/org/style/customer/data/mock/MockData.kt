package org.style.customer.data.mock

import org.style.customer.model.*

object MockData {
    val serviceCategories = listOf("Featured", "Shampoo", "Balayage-Hilights")
    val services = listOf(
        Service("1", "Hairstyling with shampoo", "45 mins", "€30", "Professional styling", true, "Featured"),
        Service("2", "Woman hair cut", "45 mins", "€55", "Expert cut", true, "Featured"),
        Service("3", "colour roots", "1 hr, 20 mins", "€43", "Root coloring", true, "Featured"),
        Service("4", "COMB OUT", "15 mins", "€20", "Comb out", true, "Featured"),
    )


    val mockSalons = listOf(
        Salon(
            id = "1",
            name = "Tania S Hair and Head Spa",
            photos = mockPhotos,
            rating = 5.0f,
            reviewCount = 6,
            location = "Kυψελαιτινου Σωκου 2, Εμπορικο Κεντρο, Λευκωσια",
            status = "Closed - opens on Tuesday at 9:00 AM",
            services = mockServices,
            team = mockTeam,
            category = "Berber",
            priceRange = "20 Tl",
            imageUrl = "https://picsum.photos/200",
            isFavorite = true,
            isNew = true,
            isTrending = true,
            description = "description",
            address = "bursa",
            phone = "053885743",
            workingHours = "10:00",
        ),
        Salon(
            id = "1",
            name = "Tania S Hair and Head Spa",
            photos = mockPhotos,
            rating = 5.0f,
            reviewCount = 6,
            location = "Kυψελαιτινου Σωκου 2, Εμπορικο Κεντρο, Λευκωσια",
            status = "Closed - opens on Tuesday at 9:00 AM",
            services = mockServices,
            team = mockTeam,
            category = "Berber",
            priceRange = "20 Tl",
            imageUrl = "https://picsum.photos/200",
            isFavorite = true,
            isNew = true,
            isTrending = true,
            description = "description",
            address = "bursa",
            phone = "053885743",
            workingHours = "10:00",
        )
    )
    val team = listOf(
        TeamMember("1", "Soultana", "MASTER HAIRD...", 5.0f, ""),
        TeamMember("2", "Agnis", "Senior Hairdress...", 5.0f, "")
    )
    val reviews = listOf(
        Review("1", "BRIGITTE P", "BP", "Fri, Jul 4, 2025 at 12:36 PM", 5.0f, "A pleasure as usual !!!!"),
        Review("2", "Toula B", "TB", "Fri, Jul 4, 2025 at 12:36 PM", 5.0f, "Εξαιρετική εξυπηρέτηση!")
    )
    val openingHours = listOf(
        OpeningHours("Monday", "9:00 AM - 6:00 PM", true),
        OpeningHours("Tuesday", "9:00 AM - 6:00 PM", true),
        OpeningHours("Wednesday", "10:00 AM - 7:00 PM", true),
        OpeningHours("Thursday", "Closed", false),
        OpeningHours("Friday", "9:00 AM - 6:00 PM", true),
        OpeningHours("Saturday", "9:00 AM - 5:00 PM", true),
        OpeningHours("Sunday", "Closed", false)
    )
    val nearbyVenues = listOf(
        NearbyVenue("1", "Beauty Bar", 4.8f, 120, "Main St 1", "Salon"),
        NearbyVenue("2", "Nail Studio", 4.7f, 80, "Second St 2", "Nail")
    )
    val photos = List(5) { "https://picsum.photos/600/400?random=$it" }
} 