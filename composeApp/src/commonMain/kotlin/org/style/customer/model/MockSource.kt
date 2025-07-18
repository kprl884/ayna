package org.style.customer.model

val mockPhotos = listOf(
    "https://picsum.photos/600/400?1",
    "https://picsum.photos/600/400?2",
    "https://picsum.photos/600/400?3"
)

val mockServices = listOf(
    Service(
        "1",
        "Hairstyling with shampoo",
        "45 mins",
        "Female only",
        "€30",
        femaleOnly = false,
        category = "Berber"
    ),
    Service("2", "Woman hair cut", "45 mins", "Female only", "€55", femaleOnly = true, "Featured"),
    Service(
        "3",
        "Colour roots",
        "1 hr, 20 mins",
        "Female only",
        "€43",
        femaleOnly = false,
        category = "Berber"
    ),
    Service(
        "4",
        "COMB OUT",
        "15 mins",
        "Female only",
        "€20",
        femaleOnly = false,
        category = "Berber"
    ),
    Service("5", "Shampoo", "30 mins", "Unisex", "€15", femaleOnly = false, category = "Berber")
)

val mockTeam = listOf(
    TeamMember(
        "1",
        "Jane Doe",
        "Stylist",
        imageUrl = "https://randomuser.me/api/portraits/women/1.jpg",
        rating = 4.5f
    ),
    TeamMember(
        "2",
        "John Smith",
        "Colorist",
        imageUrl = "https://randomuser.me/api/portraits/men/2.jpg",
        rating = 4.8f
    )
)
