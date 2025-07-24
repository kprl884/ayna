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
            // Featured services
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
                duration = "1 hr, 40 mins – 1 hr, 50 mins",
                serviceCount = 3,
                genderRestriction = "Female only",
                priceFrom = "from €40",
                category = ServiceCategory.FEATURED
            ),
            
            // Consultation services
            SalonService(
                id = "5",
                name = "EXTENSION CONSULTATION",
                duration = "10 mins",
                serviceCount = 0,
                genderRestriction = null,
                priceFrom = "free",
                category = ServiceCategory.CONSULTATION
            ),
            SalonService(
                id = "6",
                name = "TREATMENTS CONSULTATION",
                duration = "15 mins",
                serviceCount = 0,
                genderRestriction = null,
                priceFrom = "€0",
                category = ServiceCategory.CONSULTATION
            ),
            SalonService(
                id = "7",
                name = "KERATIN CONSULTATION",
                duration = "15 mins",
                serviceCount = 0,
                genderRestriction = null,
                priceFrom = "free",
                category = ServiceCategory.CONSULTATION
            ),
            SalonService(
                id = "8",
                name = "BALAYAGE | HIGHLIGHTS CONSULTATION",
                duration = "15 mins",
                serviceCount = 0,
                genderRestriction = null,
                priceFrom = "free",
                category = ServiceCategory.CONSULTATION
            ),
            
            // Men's Cut services
            SalonService(
                id = "9",
                name = "CUT",
                duration = "55 mins – 1 hr, 10 mins",
                serviceCount = 2,
                genderRestriction = "Male only",
                priceFrom = "from €20",
                category = ServiceCategory.MENS_CUT
            ),
            SalonService(
                id = "10",
                name = "CUT & BEARD",
                duration = "1 hr, 5 mins – 1 hr, 35 mins",
                serviceCount = 3,
                genderRestriction = "Male only",
                priceFrom = "from €25",
                category = ServiceCategory.MENS_CUT
            ),
            SalonService(
                id = "11",
                name = "BUZZ",
                duration = "30 mins",
                serviceCount = 2,
                genderRestriction = "Male only",
                priceFrom = "from €15",
                category = ServiceCategory.MENS_CUT
            ),
            SalonService(
                id = "12",
                name = "BUZZ & BEARD",
                duration = "45 mins",
                serviceCount = 2,
                genderRestriction = "Male only",
                priceFrom = "from €20",
                category = ServiceCategory.MENS_CUT
            ),
            
            // Women's Haircut services
            SalonService(
                id = "13",
                name = "HAIRCUT & FINISH",
                duration = "1 hr, 15 mins – 1 hr, 45 mins",
                serviceCount = 3,
                genderRestriction = null,
                priceFrom = "from €40",
                category = ServiceCategory.WOMENS_HAIRCUT
            ),
            SalonService(
                id = "14",
                name = "HAIRCUT & FINISH | CHRIS LEROIS",
                duration = "1 hr, 15 mins – 1 hr, 45 mins",
                serviceCount = 3,
                genderRestriction = "Female only",
                priceFrom = "from €50",
                category = ServiceCategory.WOMENS_HAIRCUT
            ),
            SalonService(
                id = "15",
                name = "HAIRCUT & BLOW DRY",
                duration = "1 hr, 30 mins – 2 hrs",
                serviceCount = 3,
                genderRestriction = null,
                priceFrom = "from €50",
                category = ServiceCategory.WOMENS_HAIRCUT
            ),
            SalonService(
                id = "16",
                name = "HAIRCUT & CURLING TONGS | FLAT IRON",
                duration = "1 hr, 45 mins – 2 hrs, 15 mins",
                serviceCount = 3,
                genderRestriction = null,
                priceFrom = "from €55",
                category = ServiceCategory.WOMENS_HAIRCUT
            ),
            
            // Style services
            SalonService(
                id = "17",
                name = "FINISH | CHRIS LEROIS",
                duration = "15 mins",
                serviceCount = 0,
                genderRestriction = null,
                priceFrom = "€15",
                category = ServiceCategory.STYLE
            ),
            SalonService(
                id = "18",
                name = "BLOW DRY | BRUSH STYLE",
                duration = "50 mins – 1 hr, 10 mins",
                serviceCount = 2,
                genderRestriction = "Female only",
                priceFrom = "from €20",
                category = ServiceCategory.STYLE
            ),
            SalonService(
                id = "19",
                name = "FLAT IRON | CURLING TONGS",
                duration = "1 hr – 1 hr, 15 mins",
                serviceCount = 2,
                genderRestriction = "Female only",
                priceFrom = "from €25",
                category = ServiceCategory.STYLE
            ),
            SalonService(
                id = "20",
                name = "BOXER BRAIDS – Wet",
                duration = "45 mins – 1 hr",
                serviceCount = 1,
                genderRestriction = "Female only",
                priceFrom = "from €30",
                category = ServiceCategory.STYLE
            ),
            
            // Color Application services
            SalonService(
                id = "21",
                name = "COLOR | ROOTS & FINISH",
                duration = "1 hr, 40 mins – 1 hr, 50 mins",
                serviceCount = 3,
                genderRestriction = "Female only",
                priceFrom = "from €40",
                category = ServiceCategory.COLOR_APPLICATION
            ),
            SalonService(
                id = "22",
                name = "COLOR | ROOTS & BLOW DRY | BRUSHES STYLE",
                duration = "1 hr, 55 mins – 2 hrs, 5 mins",
                serviceCount = 3,
                genderRestriction = "Female only",
                priceFrom = "from €50",
                category = ServiceCategory.COLOR_APPLICATION
            ),
            SalonService(
                id = "23",
                name = "COLOR | ROOTS & CURLING TONGS | FLAT IRON",
                duration = "2 hrs, 10 mins – 2 hrs, 20 mins",
                serviceCount = 3,
                genderRestriction = "Female only",
                priceFrom = "from €55",
                category = ServiceCategory.COLOR_APPLICATION
            ),
            
            // QIQI Straightening services
            SalonService(
                id = "24",
                name = "QIQI | STRAIGHTENING TREATMENT - MAN",
                duration = "2 hrs, 20 mins",
                serviceCount = 4,
                genderRestriction = "Male only",
                priceFrom = "€70",
                category = ServiceCategory.QIQI_STRAIGHTENING
            ),
            SalonService(
                id = "25",
                name = "QIQI | STRAIGHTENING TREATMENT - SHORT | FINE HAIR",
                duration = "4 hrs, 30 mins",
                serviceCount = 6,
                genderRestriction = null,
                priceFrom = "from €211",
                category = ServiceCategory.QIQI_STRAIGHTENING
            ),
            SalonService(
                id = "26",
                name = "QIQI | STRAIGHTENING TREATMENT - MEDIUM HAIR",
                duration = "4 hrs, 45 mins",
                serviceCount = 6,
                genderRestriction = null,
                priceFrom = "from €241",
                category = ServiceCategory.QIQI_STRAIGHTENING
            ),
            
            // Kids services
            SalonService(
                id = "27",
                name = "GIRLS | 1 – 12 Years",
                duration = "1 hr – 1 hr, 20 mins",
                serviceCount = 3,
                genderRestriction = "Female only",
                priceFrom = "from €24",
                category = ServiceCategory.KIDS
            ),
            SalonService(
                id = "28",
                name = "BOYS | 1 – 12 Years",
                duration = "30 mins – 55 mins",
                serviceCount = 2,
                genderRestriction = "Male only",
                priceFrom = "from €14",
                category = ServiceCategory.KIDS
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
