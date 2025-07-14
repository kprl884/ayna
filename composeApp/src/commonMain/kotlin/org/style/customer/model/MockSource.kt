package org.style.customer.model

val mockPhotos = listOf(
    "https://picsum.photos/600/400?1",
    "https://picsum.photos/600/400?2",
    "https://picsum.photos/600/400?3"
)

val mockServices = listOf(
    Service("1", "Hairstyling with shampoo", "45 mins", "Female only", "€30", "Featured"),
    Service("2", "Woman hair cut", "45 mins", "Female only", "€55", "Featured"),
    Service("3", "Colour roots", "1 hr, 20 mins", "Female only", "€43", "Featured"),
    Service("4", "COMB OUT", "15 mins", "Female only", "€20", "Featured"),
    Service("5", "Shampoo", "30 mins", "Unisex", "€15", "Shampoo")
)

val mockTeam = listOf(
    TeamMember("1", "Jane Doe", "Stylist", "https://randomuser.me/api/portraits/women/1.jpg"),
    TeamMember("2", "John Smith", "Colorist", "https://randomuser.me/api/portraits/men/2.jpg")
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
    )
)