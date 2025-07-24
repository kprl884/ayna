package com.techtactoe.ayna.data

import com.techtactoe.ayna.domain.model.Salon
import com.techtactoe.ayna.domain.repository.SalonRepository
import kotlinx.coroutines.delay

class MockSalonRepository : SalonRepository {
    override suspend fun getNearbySalons(): List<Salon> {
        delay(1000)
        
        return listOf(
            Salon(
                id = "1",
                name = "Elite Erkek Kuaförü",
                address = "Kadıköy, İstanbul",
                imageUrl = "https://images.unsplash.com/photo-1585747860715-2ba37e788b70?w=400",
                rating = 4.8,
                reviewCount = 127,
                tags = listOf("Barbershop", "Erkek Kuaförü", "Sakal Tıraşı")
            ),
            Salon(
                id = "2", 
                name = "Modern Hair Studio",
                address = "Beşiktaş, İstanbul",
                imageUrl = "https://images.unsplash.com/photo-1562322140-8baeececf3df?w=400",
                rating = 4.6,
                reviewCount = 89,
                tags = listOf("Hair Salon", "Kadın Kuaförü", "Saç Boyama")
            ),
            Salon(
                id = "3",
                name = "Gentleman's Barbershop",
                address = "Şişli, İstanbul", 
                imageUrl = "https://images.unsplash.com/photo-1503951914875-452162b0f3f1?w=400",
                rating = 4.9,
                reviewCount = 203,
                tags = listOf("Barbershop", "Vintage", "Sakal Şekillendirme")
            ),
            Salon(
                id = "4",
                name = "Beauty & Style",
                address = "Bakırköy, İstanbul",
                imageUrl = "https://images.unsplash.com/photo-1560869713-da86a9ec74c1?w=400", 
                rating = 4.5,
                reviewCount = 156,
                tags = listOf("Hair Salon", "Kadın Kuaförü", "Saç Kesimi", "Makyaj")
            )
        )
    }
} 