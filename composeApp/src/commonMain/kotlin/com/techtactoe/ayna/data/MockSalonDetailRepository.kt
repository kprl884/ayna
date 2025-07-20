package com.techtactoe.ayna.data

import com.techtactoe.ayna.domain.model.BuyOption
import com.techtactoe.ayna.domain.model.BuyOptionType
import com.techtactoe.ayna.domain.model.OpeningHour
import com.techtactoe.ayna.domain.model.Review
import com.techtactoe.ayna.domain.model.SalonAbout
import com.techtactoe.ayna.domain.model.SalonDetail
import com.techtactoe.ayna.domain.model.SalonService
import com.techtactoe.ayna.domain.model.SalonStatus
import com.techtactoe.ayna.domain.model.ServiceCategory
import com.techtactoe.ayna.domain.model.TeamMember

object MockSalonDetailRepository {
    
    fun getSalonDetail(salonId: String): SalonDetail {
        return SalonDetail(
            id = salonId,
            name = "Hair Etc. Studio",
            rating = 5.0,
            reviewCount = 3645,
            address = "17D, Themistokli Dervi Str., \"THE CITY HOUSE\" Bld., Nicosia",
            status = SalonStatus.OPENS_LATER,
            images = listOf(
                "image1.jpg",
                "image2.jpg", 
                "image3.jpg",
                "image4.jpg"
            ),
            services = getSampleServices(),
            team = getSampleTeam(),
            reviews = getSampleReviews(),
            buyOptions = getSampleBuyOptions(),
            about = SalonAbout(
                description = "Hair Etc. Studio offers the most unique hair experience in Cyprus. We are a team of creators working with people on a daily basis...",
                fullDescription = "Hair Etc. Studio offers the most unique hair experience in Cyprus. We are a team of creators working with people on a daily basis, understanding their needs and translating them into gorgeous hair looks. Our philosophy is based on creating personalized experiences for each client."
            ),
            openingHours = getSampleOpeningHours()
        )
    }
    
    private fun getSampleServices(): List<SalonService> {
        return listOf(
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
            ),
            SalonService(
                id = "3",
                name = "HAIRCUT & FINISH",
                duration = "1 hr, 15 mins – 1 hr, 45 mins",
                serviceCount = 3,
                genderRestriction = null,
                priceFrom = "from €40",
                category = ServiceCategory.FEATURED
            ),
            SalonService(
                id = "4",
                name = "COLOR | ROOTS & FINISH",
                duration = "2 hrs – 3 hrs",
                serviceCount = 5,
                genderRestriction = null,
                priceFrom = "from €60",
                category = ServiceCategory.COLOR
            ),
            SalonService(
                id = "5",
                name = "CONSULTATION",
                duration = "15 mins",
                serviceCount = 1,
                genderRestriction = null,
                priceFrom = "Free",
                category = ServiceCategory.CONSULTATION
            )
        )
    }
    
    private fun getSampleTeam(): List<TeamMember> {
        return listOf(
            TeamMember(
                id = "1",
                name = "Marios",
                role = "Creative Director",
                imageUrl = null,
                rating = 5.0
            ),
            TeamMember(
                id = "2",
                name = "Ankit",
                role = "Support Team",
                imageUrl = null,
                rating = 5.0
            ),
            TeamMember(
                id = "3",
                name = "Fanouria",
                role = "Stylist",
                imageUrl = null,
                rating = 5.0
            )
        )
    }
    
    private fun getSampleReviews(): List<Review> {
        return listOf(
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
    }
    
    private fun getSampleBuyOptions(): List<BuyOption> {
        return listOf(
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
    }
    
    private fun getSampleOpeningHours(): List<OpeningHour> {
        return listOf(
            OpeningHour("Monday", false),
            OpeningHour("Tuesday", true, "9:00 AM", "7:00 PM"),
            OpeningHour("Wednesday", true, "9:00 AM", "7:00 PM"),
            OpeningHour("Thursday", true, "9:30 AM", "5:30 PM"),
            OpeningHour("Friday", true, "9:00 AM", "7:00 PM"),
            OpeningHour("Saturday", true, "8:30 AM", "5:00 PM"),
            OpeningHour("Sunday", false)
        )
    }
}
